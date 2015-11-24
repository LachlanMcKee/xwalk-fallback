package com.lachlanm.xwalkfallback;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public interface WebViewFacade {

    /**
     * Obtains the underlying view and initialises it if it doesn't already exist.
     *
     * @param context used to create the view.
     * @return the underlying WebView.
     */
    View getView(Context context);

    /**
     * Determines whether the WebView has been created.
     *
     * @return if the WebView exists.
     */
    boolean isInitialised();

    /**
     * Gets whether this WebView has a back history item.
     *
     * @return true if this WebView has a back history item
     */
    boolean canGoBack();

    /**
     * Goes back in the history of this WebView.
     */
    void goBack();

    /**
     * Loads the given URL.
     *
     * @param url the URL of the resource to load
     */
    void load(String url);

    /**
     * Loads the given URL.
     *
     * @param url     the URL of the resource to load
     * @param content content which should be loaded instead executing the http request.
     */
    void load(String url, String content);

    /**
     * Saves the state of this WebView used in
     * {@link android.app.Activity#onSaveInstanceState}. Please note that this
     * method no longer stores the display data for this WebView. The previous
     * behavior could potentially leak files if {@link #restoreState} was never
     * called.
     *
     * @param outState the Bundle to store this WebView's state
     */
    void saveState(Bundle outState);

    /**
     * Restores the state of this WebView from the given Bundle. This method is
     * intended for use in {@link android.app.Activity#onRestoreInstanceState}
     * and should be called to restore the state of this WebView. If
     * it is called after this WebView has had a chance to build state (load
     * pages, create a back/forward list, etc.) there may be undesirable
     * side-effects. Please note that this method no longer restores the
     * display data for this WebView.
     *
     * @param inState the incoming Bundle of state
     */
    void restoreState(Bundle inState);

    /**
     * Gets the WebView's user-agent string.
     *
     * @return the WebView's user-agent string
     */
    String getUserAgent();

    /**
     * Resumes a WebView after a previous call to onPause().
     */
    void resume();

    /**
     * Pauses any extra processing associated with this WebView and its
     * associated DOM, plugins, JavaScript etc.
     */
    void pause();

    /**
     * Destroys the internal state of this WebView. This method should be called
     * after this WebView has been removed from the view system. No other
     * methods may be called on this WebView after destroy.
     */
    void destroy();

    /**
     * Clears the resource cache. Note that the cache is per-application, so
     * this will clear the cache for all WebViews used.
     *
     * @param includeDiskFiles if false, only the RAM cache is cleared
     */
    void clearCache(boolean includeDiskFiles);

    /**
     * Assigns a listener which keeps track of the loading progress of a WebView request.
     *
     * @param progressListener the listener to assign.
     */
    void setProgressListener(ProgressListener progressListener);

    /**
     * Keeps track of any WebView progress related events.
     */
    interface ProgressListener {
        /**
         * Fired when any changes in progress occur.
         *
         * @param facade            the WebViewFacade itself.
         * @param progressInPercent the percentage through the request.
         */
        void onProgressChanged(WebViewFacade facade, int progressInPercent);
    }
}
