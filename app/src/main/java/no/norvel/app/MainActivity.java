package no.norvel.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private static final String HOME_URL = "https://norvel.no/app/";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureSystemBars();

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(251, 252, 251));
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        setContentView(webView);
        applySafeInsets();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(false);
        settings.setTextZoom(100);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        CookieManager cookies = CookieManager.getInstance();
        cookies.setAcceptCookie(true);
        cookies.setAcceptThirdPartyCookies(webView, false);

        webView.setWebViewClient(new NorvelClient());

        Uri deepLink = getIntent() != null ? getIntent().getData() : null;
        if (deepLink != null && isNorvelUrl(deepLink)) {
            webView.loadUrl(deepLink.toString());
        } else {
            webView.loadUrl(HOME_URL + "?android=v33");
        }
    }

    private void applyAndroidScale(WebView view) {
        String js = "(function(){" +
            "var id='norvel-android-scale';" +
            "var old=document.getElementById(id);if(old)old.remove();" +
            "var s=document.createElement('style');s.id=id;" +
            "s.textContent='" +
              "html{zoom:0.88!important;width:113.636%!important;min-height:113.636%!important;}" +
              "body{width:100%!important;max-width:none!important;}" +
              ".simple-home{padding:24px 16px 108px!important;min-height:auto!important;}" +
              ".simple-home-copy{padding:2px 2px 12px!important;}" +
              ".simple-home-copy h1{font-size:34px!important;line-height:1!important;margin:4px 0 8px!important;}" +
              ".simple-home-copy p{font-size:15.5px!important;line-height:1.42!important;}" +
              ".simple-home-actions{gap:12px!important;margin-top:10px!important;}" +
              ".simple-focus-card{min-height:142px!important;padding:17px 18px!important;border-radius:22px!important;}" +
              ".simple-focus-icon{width:48px!important;height:48px!important;border-radius:14px!important;}" +
              ".simple-focus-text h2{font-size:25px!important;line-height:1.05!important;}" +
              ".simple-focus-text p{font-size:14px!important;line-height:1.4!important;}" +
              ".simple-home-help{font-size:13px!important;margin-top:16px!important;}" +
            "';" +
            "document.head.appendChild(s);" +
            "document.documentElement.setAttribute('data-norvel-android','v33');" +
        "})();";
        view.evaluateJavascript(js, null);
    }

    private void configureSystemBars() {
        getWindow().setNavigationBarColor(Color.rgb(251, 252, 251));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars());
                controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                );
            }
        }
    }

    private void applySafeInsets() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            webView.setOnApplyWindowInsetsListener((view, windowInsets) -> {
                Insets nav = windowInsets.getInsets(WindowInsets.Type.navigationBars());
                Insets cutout = windowInsets.getInsets(WindowInsets.Type.displayCutout());
                int left = Math.max(nav.left, cutout.left);
                int right = Math.max(nav.right, cutout.right);
                int top = cutout.top;
                int bottom = Math.max(nav.bottom, cutout.bottom);
                view.setPadding(left, top, right, bottom);
                return windowInsets;
            });
            webView.requestApplyInsets();
        }
    }

    private boolean isNorvelUrl(Uri uri) {
        if (uri == null) return false;
        String host = uri.getHost();
        return "https".equalsIgnoreCase(uri.getScheme())
                && host != null
                && (host.equalsIgnoreCase("norvel.no") || host.endsWith(".norvel.no"));
    }

    private void openExternal(Uri uri) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception ignored) { }
    }

    private final class NorvelClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if (isNorvelUrl(uri)) return false;
            openExternal(uri);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            applyAndroidScale(view);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) {
                view.loadDataWithBaseURL(
                    HOME_URL,
                    "<html><head><meta name='viewport' content='width=device-width,initial-scale=1'>" +
                    "<style>body{font-family:sans-serif;background:#f7f9f8;color:#173c2f;padding:32px}" +
                    "button{background:#173c2f;color:white;border:0;border-radius:14px;padding:14px 18px;font-weight:700}</style>" +
                    "</head><body><h2>Norvel er ikke tilgjengelig akkurat nå</h2>" +
                    "<p>Sjekk internettforbindelsen og prøv igjen.</p>" +
                    "<button onclick=\"location.href='https://norvel.no/app/'\">Prøv igjen</button></body></html>",
                    "text/html", "UTF-8", null
                );
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
