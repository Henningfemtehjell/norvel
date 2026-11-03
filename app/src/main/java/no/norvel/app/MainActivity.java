package no.norvel.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
    private static final String NORVEL_LOGO = "https://norvel.no/app/norvel-logo.png";
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
        settings.setUseWideViewPort(false);
        settings.setLoadWithOverviewMode(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
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

        boolean restored = false;
        if (savedInstanceState != null) {
            restored = webView.restoreState(savedInstanceState) != null;
        }

        if (!restored) {
            Uri deepLink = getIntent() != null ? getIntent().getData() : null;
            if (deepLink != null && isNorvelUrl(deepLink)) {
                webView.loadUrl(deepLink.toString());
            } else {
                webView.loadUrl(HOME_URL + "?android=v36");
            }
        }
    }

    private void applyAndroidLayoutFix(WebView view) {
        String js = "(function(){" +
            "var id='norvel-android-v36';" +
            "var old=document.getElementById(id);if(old)old.remove();" +
            "var s=document.createElement('style');s.id=id;" +
            "s.textContent='" +
              "html{-webkit-text-size-adjust:100%!important;text-size-adjust:100%!important;zoom:.88!important;width:113.636%!important;min-height:113.636%!important;}" +
              "body{width:100%!important;max-width:none!important;overflow-x:hidden!important;}" +
              "#appShell{max-width:590px!important;width:100%!important;}" +
              ".topbar{padding:9px 14px!important;gap:10px!important;}" +
              ".brand{min-width:0!important;gap:10px!important;align-items:center!important;}" +
              ".brand img{width:105px!important;height:auto!important;max-height:66px!important;object-fit:contain!important;flex:0 0 auto!important;border-radius:0!important;}" +
              ".brand>span,.brand>div{min-width:0!important;}" +
              ".brand strong{display:none!important;}" +
              ".brand small{display:block!important;font-size:10.5px!important;line-height:1.2!important;white-space:normal!important;color:#68756f!important;max-width:210px!important;}" +
              ".text-btn{font-size:16px!important;padding:9px 11px!important;flex:0 0 auto!important;}" +
              ".page-head{padding:19px 16px 11px!important;}" +
              ".page-head h1{font-size:28px!important;line-height:1.08!important;overflow-wrap:anywhere!important;}" +
              ".page-head p{font-size:14px!important;line-height:1.4!important;}" +
              ".category-card{min-height:74px!important;height:auto!important;padding:14px!important;gap:11px!important;}" +
              ".category-card .cat-icon{width:39px!important;height:39px!important;flex-basis:39px!important;}" +
              ".category-card strong{font-size:15.5px!important;line-height:1.22!important;overflow-wrap:anywhere!important;}" +
              ".category-card small{font-size:11.5px!important;line-height:1.3!important;}" +
              ".simple-home{padding:22px 15px 106px!important;min-height:auto!important;}" +
              ".simple-home-copy{padding:2px 2px 11px!important;}" +
              ".simple-home-copy h1{font-size:32px!important;line-height:1!important;margin:4px 0 8px!important;overflow-wrap:anywhere!important;}" +
              ".simple-home-copy p{font-size:15px!important;line-height:1.42!important;}" +
              ".simple-home-actions{gap:11px!important;margin-top:9px!important;}" +
              ".simple-focus-card{min-height:136px!important;height:auto!important;padding:16px 17px!important;border-radius:21px!important;}" +
              ".simple-focus-icon{width:46px!important;height:46px!important;border-radius:13px!important;flex:0 0 46px!important;}" +
              ".simple-focus-text{min-width:0!important;}" +
              ".simple-focus-text h2{font-size:23px!important;line-height:1.08!important;overflow-wrap:anywhere!important;}" +
              ".simple-focus-text p{font-size:13.5px!important;line-height:1.38!important;}" +
              ".simple-home-help{font-size:12.5px!important;line-height:1.35!important;margin-top:15px!important;}" +
              ".item-card h3{font-size:15px!important;line-height:1.25!important;overflow-wrap:anywhere!important;}" +
              ".item-card p{font-size:12.5px!important;line-height:1.4!important;}" +
              ".detail-section h2{font-size:17px!important;line-height:1.2!important;}" +
              ".detail-section p{font-size:13.5px!important;line-height:1.5!important;}" +
              ".nav{font-size:9px!important;line-height:1.15!important;min-width:0!important;}" +
              ".nav b{display:block!important;white-space:normal!important;overflow-wrap:anywhere!important;}" +
              ".norvel-intro-overlay{padding:8px!important;}" +
              ".norvel-intro-screen.v27{min-height:calc(100dvh - 16px)!important;max-height:calc(100dvh - 16px)!important;overflow:auto!important;}" +
              ".v27-brandrow{padding:10px 12px 0!important;gap:8px!important;}" +
              ".v27-logo{width:112px!important;height:auto!important;max-width:49%!important;object-fit:contain!important;}" +
              ".v27-brandrow span{font-size:10.5px!important;line-height:1.2!important;max-width:145px!important;}" +
              ".v27-hero{padding:0 12px 12px!important;gap:0!important;}" +
              ".v27-ella-stage{min-height:220px!important;}" +
              ".v27-ella{width:min(218px,64vw)!important;}" +
              ".v27-ella-halo{width:205px!important;height:205px!important;}" +
              ".v27-copy{padding:0 2px 6px!important;}" +
              ".v27-kicker{font-size:9.5px!important;margin-bottom:5px!important;}" +
              ".v27-copy h1{font-size:31px!important;line-height:.98!important;margin-bottom:7px!important;}" +
              ".v27-copy p{font-size:14px!important;line-height:1.42!important;margin-bottom:10px!important;}" +
              ".v27-proof{font-size:10.5px!important;line-height:1.3!important;margin-bottom:11px!important;}" +
              ".v27-open{font-size:14px!important;padding:12px 15px!important;}" +
              ".v27-ella-label{padding:7px 9px!important;}" +
              ".v27-ella-label strong{font-size:13px!important;}" +
              ".v27-ella-label span{font-size:10px!important;}" +
              "body.large-text .brand img{width:112px!important;}" +
              "body.large-text .brand small{font-size:12px!important;line-height:1.25!important;}" +
              "body.large-text .text-btn{font-size:17px!important;background:#173c2f!important;color:#fff!important;}" +
              "body.large-text .page-head h1{font-size:32px!important;}" +
              "body.large-text .page-head p{font-size:16px!important;}" +
              "body.large-text .category-card{min-height:82px!important;}" +
              "body.large-text .category-card strong{font-size:18px!important;line-height:1.25!important;}" +
              "body.large-text .category-card small{font-size:13.5px!important;line-height:1.35!important;}" +
              "body.large-text .simple-home-copy h1{font-size:36px!important;}" +
              "body.large-text .simple-home-copy p{font-size:17px!important;line-height:1.48!important;}" +
              "body.large-text .simple-focus-card{min-height:148px!important;}" +
              "body.large-text .simple-focus-text h2{font-size:26px!important;line-height:1.1!important;}" +
              "body.large-text .simple-focus-text p{font-size:15.5px!important;line-height:1.45!important;}" +
              "body.large-text .simple-home-help{font-size:14.5px!important;}" +
              "body.large-text .item-card h3{font-size:17.5px!important;}" +
              "body.large-text .item-card p{font-size:14.5px!important;}" +
              "body.large-text .detail-section h2{font-size:19px!important;}" +
              "body.large-text .detail-section p{font-size:15.5px!important;}" +
              "body.large-text .nav{font-size:10.5px!important;}" +
              "body.large-text .v27-brandrow span{font-size:11.5px!important;}" +
              "body.large-text .v27-copy h1{font-size:34px!important;}" +
              "body.large-text .v27-copy p{font-size:15.5px!important;}" +
              "body.large-text .v27-open{font-size:15px!important;}" +
              "@media(max-width:390px){" +
                ".brand img{width:96px!important;}" +
                ".brand small{font-size:9.5px!important;max-width:165px!important;}" +
                ".simple-home-copy h1{font-size:29px!important;}" +
                ".simple-focus-text h2{font-size:21px!important;}" +
                ".page-head h1{font-size:26px!important;}" +
                "body.large-text .brand img{width:102px!important;}" +
                "body.large-text .brand small{font-size:11px!important;}" +
                "body.large-text .simple-home-copy h1{font-size:33px!important;}" +
                "body.large-text .simple-focus-text h2{font-size:24px!important;}" +
                "body.large-text .page-head h1{font-size:30px!important;}" +
              "}" +
            "';" +
            "document.head.appendChild(s);" +
            "var brand=document.querySelector('.brand');" +
            "if(brand){var bi=brand.querySelector('img');if(bi){bi.src='" + NORVEL_LOGO + "';bi.alt='Norvel';}var bs=brand.querySelector('strong');if(bs){bs.style.setProperty('display','none','important');}}" +
            "var intro=document.querySelector('.v27-logo');if(intro){intro.src='" + NORVEL_LOGO + "';intro.alt='Norvel';}" +
            "document.documentElement.setAttribute('data-norvel-android','v36');" +
        "})();";
        view.evaluateJavascript(js, null);
    }

    private void configureSystemBars() {
        getWindow().setNavigationBarColor(Color.rgb(251, 252, 251));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
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
        return "https".equalsIgnoreCase(uri.getScheme()) && host != null && (host.equalsIgnoreCase("norvel.no") || host.endsWith(".norvel.no"));
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
            applyAndroidLayoutFix(view);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) {
                view.loadDataWithBaseURL(
                    HOME_URL,
                    "<html><head><meta name='viewport' content='width=device-width,initial-scale=1'><style>body{font-family:sans-serif;background:#f7f9f8;color:#173c2f;padding:32px}button{background:#173c2f;color:white;border:0;border-radius:14px;padding:14px 18px;font-weight:700}</style></head><body><h2>Norvel er ikke tilgjengelig akkurat nå</h2><p>Sjekk internettforbindelsen og prøv igjen.</p><button onclick=\"location.href='https://norvel.no/app/'\">Prøv igjen</button></body></html>",
                    "text/html", "UTF-8", null
                );
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configureSystemBars();
        if (webView != null) {
            webView.requestLayout();
            applyAndroidLayoutFix(webView);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (webView != null) webView.saveState(outState);
        super.onSaveInstanceState(outState);
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
