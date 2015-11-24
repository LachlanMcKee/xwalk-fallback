package com.lachlanm.xwalkfallback;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

class StandardCookieManager extends CookieManagerFacade {
    private CookieManager standardCookieManager;

    public StandardCookieManager() {
        super();
    }

    @Override
    public void init(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //noinspection deprecation
            CookieSyncManager.createInstance(context);
        }
        standardCookieManager = CookieManager.getInstance();
    }

    @Override
    public void setAcceptCookies(boolean accept) {
        standardCookieManager.setAcceptCookie(accept);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeAllCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            standardCookieManager.removeAllCookies(null);
            standardCookieManager.flush();
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.getInstance();
            cookieSyncManager.startSync();
            standardCookieManager.removeAllCookie();
            standardCookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }

    @Override
    protected void setCookie(String url, String headerValue) {
        standardCookieManager.setCookie(url, headerValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //noinspection deprecation
            CookieSyncManager.getInstance().sync();
        }
    }

    @Override
    protected String getCookie(String url) {
        return standardCookieManager.getCookie(url);
    }

}
