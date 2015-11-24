package com.lachlanm.xwalkfallback;

import android.app.Activity;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkSettings;

class XWalkWebView extends AbstractWebViewFacade {
    private XWalkView webView;
    private ProgressListener progressListener;

    @Override
    public View getView(Context context) {
        if (webView == null) {
            webView = new XWalkView(context, (Activity) null);

            webView.setResourceClient(new XWalkResourceClient(webView) {
                @Override
                public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {

                }

                @Override
                public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {

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

        XWalkNavigationHistory navigationHistory = webView.getNavigationHistory();
        return navigationHistory != null && navigationHistory.canGoBack();
    }

    @Override
    public void goBack() {
        ensureWebViewReady();

        XWalkNavigationHistory navigationHistory = webView.getNavigationHistory();
        if (navigationHistory != null) {
            navigationHistory.navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
        }
    }

    @Override
    public void load(String url) {
        load(url, null);
    }

    @Override
    public void load(String url, String content) {
        ensureWebViewReady();
        webView.load(url, content);
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
        return XWalkSettings.getDefaultUserAgent();
    }

    @Override
    public void resume() {
        ensureWebViewReady();

        webView.resumeTimers();
        webView.onShow();
    }

    @Override
    public void pause() {
        ensureWebViewReady();

        webView.pauseTimers();
        webView.onHide();
    }

    @Override
    public void destroy() {
        if (webView != null) {
            webView.onDestroy();
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
            webView.setResourceClient(new XWalkResourceClient(webView) {
                @Override
                public void onProgressChanged(XWalkView view, int progressInPercent) {
                    XWalkWebView facade = XWalkWebView.this;
                    facade.progressListener.onProgressChanged(facade, progressInPercent);
                }
            });
        }
    }
}
