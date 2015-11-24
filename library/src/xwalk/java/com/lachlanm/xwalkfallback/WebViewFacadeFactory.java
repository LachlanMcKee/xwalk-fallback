package com.lachlanm.xwalkfallback;

public class WebViewFacadeFactory implements IWebViewFacadeFactory {
    @Override
    public WebViewFacade makeWebViewFacade() {
        return new XWalkWebView();
    }

    @Override
    public CookieManagerFacade makeCookieManagerFacade() {
        return new XWalkCookieManager();
    }
}
