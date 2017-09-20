package com.grupocisc.healthmonitor.Advertising;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;


import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;

import java.util.HashMap;

public class PublicidadWebViewActivity extends AppCompatActivity {

	private String TAG ="WebViewPublicidad";
	private WebView webView;
	private String url = "http://181.39.136.237/app.php/Publicidad";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publicidad_webview);

		Utils.SetStyleToolbarLogo(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("url");
		}

		webView = (WebView) findViewById(R.id.webView);

		if(url != null) {
			try {

				webView.getSettings().setJavaScriptEnabled(true);
				webView.clearHistory();
				webView.clearCache(true);
				webView.getSettings().setPluginState(WebSettings.PluginState.ON);
				webView.getSettings().setAllowFileAccess(true);
				webView.loadUrl(url);
				webView.setWebChromeClient(new WebChromeClient());

				webView.setWebViewClient(new WebViewClient() {

					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						view.loadUrl(url);
						return true;
					}
				});

			}catch (Exception e){
				e.printStackTrace();
			}
		}


	}


	@Override
	public void onPause() {
		if(webView != null)
			webView.onPause();

		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		super.onResume();

		if(webView != null) {
			webView.onResume();
		}
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if(webView != null){
			webView.onPause();
			webView.destroy();
		}
		webView=null;
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
