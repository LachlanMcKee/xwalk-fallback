package com.lachlanm.xwalkfallback;

public abstract class AbstractWebViewFacade implements WebViewFacade {

    public void ensureWebViewReady() {
        if (!isInitialised()) {
            throw new IllegalStateException("You must call getView before executing any calls");
        }
    }

}
