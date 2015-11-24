package com.lachlanm.xwalkfallback;

import android.content.Context;

class XWalkCookieManager extends CookieManagerFacade {
    private org.xwalk.core.XWalkCookieManager xWalkCookieManager;

    public XWalkCookieManager() {
        super();
    }

    @Override
    public void init(Context context) {
        xWalkCookieManager = new org.xwalk.core.XWalkCookieManager();
    }

    @Override
    public void setAcceptCookies(boolean accept) {
        xWalkCookieManager.setAcceptCookie(accept);
    }

    @Override
    public void removeAllCookies() {
        xWalkCookieManager.removeAllCookie();
    }

    @Override
    protected void setCookie(String url, String headerValue) {
        xWalkCookieManager.setCookie(url, headerValue);
    }

    @Override
    protected String getCookie(String url) {
        return xWalkCookieManager.getCookie(url);
    }

}
