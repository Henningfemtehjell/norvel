package no.norvel.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

        getWindow().setStatusBarColor(Color.rgb(23, 60, 47));
        getWindow().setNavigationBarColor(Color.rgb(251, 252, 251));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        CookieManager cookies = CookieManager.getInstance();
        cookies.setAcceptCookie(true);
        cookies.setAcceptThirdPartyCookies(webView, false);

        webView.setWebViewClient(new NorvelClient());

        Uri deepLink = getIntent() != null ? getIntent().getData() : null;
        if (deepLink != null && isNorvelUrl(deepLink)) {
            webView.loadUrl(deepLink.toString());
        } else {
            webView.loadUrl(HOME_URL);
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
