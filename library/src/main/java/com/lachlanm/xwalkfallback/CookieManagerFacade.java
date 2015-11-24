package com.lachlanm.xwalkfallback;

import android.content.Context;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class CookieManagerFacade extends CookieManager {

    protected CookieManagerFacade() {
        super(null, java.net.CookiePolicy.ACCEPT_ALL);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        // Make sure our args are valid.
        if (uri == null || responseHeaders == null) {
            return;
        }

        String url = uri.toString();

        // go over the headers
        for (String headerKey : responseHeaders.keySet()) {
            // ignore headers which aren't cookie related
            if (headerKey == null || !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie"))) {
                continue;
            }

            // process each of the headers
            for (String headerValue : responseHeaders.get(headerKey)) {
                setCookie(url, headerValue);
            }
        }
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        // Make sure our args are valid.
        if (uri == null || requestHeaders == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        String url = uri.toString();
        Map<String, List<String>> res = new java.util.HashMap<>();

        // get the cookie
        String cookie = getCookie(url);

        // return it
        if (cookie != null) {
            res.put("Cookie", Collections.singletonList(cookie));
        }
        return res;
    }

    @Override
    public CookieStore getCookieStore() {
        // we don't want anyone to work with this cookie store directly
        throw new UnsupportedOperationException();
    }

    public abstract void init(Context context);

    /**
     * Sets whether the application's {@link WebViewFacade} instances should send and
     * accept cookies.
     * <p/>
     * When this is true
     * {@link CookieManager#setAcceptThirdPartyCookies setAcceptThirdPartyCookies} and
     * {@link CookieManager#setAcceptFileSchemeCookies setAcceptFileSchemeCookies}
     * can be used to control the policy for those specific types of cookie.
     *
     * @param accept whether {@link WebViewFacade} instances should send and accept
     *               cookies
     */
    public abstract void setAcceptCookies(boolean accept);

    public abstract void removeAllCookies();

    protected abstract void setCookie(String url, String headerValue);

    protected abstract String getCookie(String url);

}
