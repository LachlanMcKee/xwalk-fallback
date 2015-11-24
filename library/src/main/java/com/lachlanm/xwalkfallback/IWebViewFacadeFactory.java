package com.lachlanm.xwalkfallback;

public interface IWebViewFacadeFactory {
    WebViewFacade makeWebViewFacade();

    CookieManagerFacade makeCookieManagerFacade();
}
