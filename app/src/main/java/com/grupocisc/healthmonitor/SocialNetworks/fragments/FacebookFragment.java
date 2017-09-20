package com.grupocisc.healthmonitor.SocialNetworks.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.SocialNetworks.activities.SocialActivity;

public class FacebookFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Button btnnext, btnpre;
    private View contenView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView mWebView;
    private CoordinatorLayout coordinatorLayout;
    private String URL = "https://www.facebook.com/Health-Monitor-UG-1852824945045596/posts/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contenView = inflater.inflate(R.layout.social_facebook_fragment, container, false);


        mWebView = (WebView) contenView.findViewById(R.id.activity_main_webview);
        swipeRefreshLayout = (SwipeRefreshLayout) contenView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener( this );
        coordinatorLayout = (CoordinatorLayout) contenView.findViewById(R.id.container);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                swipeRefreshLayout.setRefreshing(true);
            }

            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);

                mWebView.scrollTo(0, 2100);
            }

        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        loadWebsite();


        btnnext = (Button) contenView.findViewById(R.id.btnnext);
        btnpre = (Button) contenView.findViewById(R.id.btnprev);
        btnpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mWebView.canGoForward()){
                    mWebView.goForward();
                }
            }
        });

        return contenView;
    }
    private void loadWebsite() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            mWebView.loadUrl(URL);
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please check your internet connection.", Snackbar.LENGTH_LONG);
            snackbar.show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        mWebView.reload();
    }


}
