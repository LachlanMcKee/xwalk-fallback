package com.lachlanm.xwalkfallback;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class StandardWebView extends AbstractWebViewFacade {
    private WebView webView;
    private ProgressListener progressListener;

    @Override
    public View getView(Context context) {
        if (webView == null) {
            webView = new WebView(context);

            WebSettings settings = webView.getSettings();
            settings.setSupportZoom(true);
            settings.setUseWideViewPort(true);
            settings.setJavaScriptEnabled(true);

            // Performance options
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
        return webView;
    }

    @Override
    public boolean isInitialised() {
        return webView != null;
    }

    @Override
    public boolean canGoBack() {
        ensureWebViewReady();
        return webView.canGoBack();
    }

    @Override
    public void goBack() {
        ensureWebViewReady();
        webView.goBack();
    }

    @Override
    public void load(String url) {
        ensureWebViewReady();
        load(url, null);
    }

    @Override
    public void load(String url, String content) {
        ensureWebViewReady();
        if (content != null) {
            webView.loadDataWithBaseURL(url, content, null, "UTF-8", url);
        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    public void saveState(Bundle outState) {
        ensureWebViewReady();
        webView.saveState(outState);
    }

    @Override
    public void restoreState(Bundle inState) {
        ensureWebViewReady();
        webView.restoreState(inState);
    }

    @Override
    public String getUserAgent() {
        ensureWebViewReady();
        return webView.getSettings().getUserAgentString();
    }

    @Override
    public void resume() {
        ensureWebViewReady();

        webView.resumeTimers();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onResume();
        }
    }

    @Override
    public void pause() {
        ensureWebViewReady();

        webView.pauseTimers();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause();
        }
    }

    @Override
    public void destroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        progressListener = null;
    }

    @Override
    public void clearCache(boolean includeDiskFiles) {
        ensureWebViewReady();
        webView.clearCache(includeDiskFiles);
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;

        if (webView != null) {
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    StandardWebView facade = StandardWebView.this;

                    // On destroy may have been called during webview load.
                    if (facade.progressListener != null) {
                        facade.progressListener.onProgressChanged(facade, newProgress);
                    }
                }
            });
        }
    }
}
