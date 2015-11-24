package com.lachlanm.xwalkfallback;

public class WebViewFacadeFactory implements IWebViewFacadeFactory {
    @Override
    public WebViewFacade makeWebViewFacade() {
        return new StandardWebView();
    }

    @Override
    public CookieManagerFacade makeCookieManagerFacade() {
        return new StandardCookieManager();
    }
}
