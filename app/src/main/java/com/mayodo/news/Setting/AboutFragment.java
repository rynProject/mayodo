package com.mayodo.news.Setting;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.mayodo.news.Home.HomePageActivity;
import com.mayodo.news.R;

public class AboutFragment extends Fragment {
    private static final String TAG = "tag";
    private WebView mWebView;
    ProgressDialog progressDialog;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        Initialization(v);
        return v;
    }
    private void Initialization(View v){
        HomePageActivity.tvToolbarTitle.setText("About Us");
        setHasOptionsMenu(true);
        ProgressDialogSetup();
        progressDialog.show();
        mWebView = (WebView) v.findViewById(R.id.webView);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath("data/data/com.exempt/databases");
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " (XY ClientApp)");
        webSettings.setSavePassword(false);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath("");
        webSettings.setAppCacheMaxSize(5 * 1024 * 1024);
        String summary = "<html><head><title>Title of the document</title></head><body><h1><a href=\"hrupin://second_activity\">LINK to second activity</a></h1><h1><a href=\"http://www.google.com/\">Link to GOOGLE.COM</a></h1></body></html>";
        mWebView.loadData(summary, "text/html", null);
        mWebView.loadUrl("http://itechnotion.in/wp-news/about-us/");
        progressDialog.dismiss();
    }
    private void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void startWebView(String url) {
        progressDialog.show();
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            public void onLoadResource(WebView view, String url) {

                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        progressDialog.dismiss();
    }

    private boolean handleUri(final Uri uri) {
        Log.i(TAG, "Uri =" + uri);
        final String host = uri.getHost();
        final String scheme = uri.getScheme();
        if (host == scheme) {
            return false;
        } else {
            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
    }
}
