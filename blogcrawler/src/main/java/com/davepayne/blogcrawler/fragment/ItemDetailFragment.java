package com.davepayne.blogcrawler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.davepayne.blogcrawler.R;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link com.davepayne.blogcrawler.activity.ItemListActivity}
 * in two-pane mode (on tablets) or a {@link com.davepayne.blogcrawler.activity.ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    private final static String SPIDEY_GIF = "file:///android_asset/spidey.gif";
    private WebView mWebView;
    private String urlString = null;
    private boolean isLoaded = false;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Let's check our bundled arguments for what will be the URL.
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            urlString = getArguments().getString(ARG_ITEM_ID);
        } else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Inflate our root view from XML.
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Assign our web view reference.
        mWebView = (WebView) rootView.findViewById(R.id.item_detail);

        // Enable Javascript and caching..
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.getSettings().setNeedInitialFocus(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

        // Set up our webview web client and settings.
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), description, Toast.LENGTH_LONG).show();
            }

            public void onPageFinished(WebView view, String url) {
                // If we are in a !isLoaded state, but we've obviously just finished loading
                // something, this means our animated gif load animation has just finished.
                // Proceed to loading urlString.
                if (!isLoaded) {
                    loadWebView();
                }
            }
        });

        // Load our webslinging animated hero.
        mWebView.loadUrl(SPIDEY_GIF);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Assumes urlString field is non-null, otherwise does nothing. Attempts to load urlString
     * into web view.
     */
    private void loadWebView() {
        if (urlString != null) {
            // Load our webview.
            mWebView.loadUrl(urlString);

            // Update our control logic.
            isLoaded = true;
        }
    }
}
