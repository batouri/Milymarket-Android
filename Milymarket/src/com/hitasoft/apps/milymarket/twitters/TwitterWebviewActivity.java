package com.hitasoft.apps.milymarket.twitters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hitasoft.apps.milymarket.R;

public class TwitterWebviewActivity extends Activity {
	private Intent mIntent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_webview);
		mIntent = getIntent();
		String url = (String) mIntent.getExtras().get("URL");
		String newurl=url.replace("http","https");
		Log.v("url",""+newurl);
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains(getResources().getString(
						R.string.twitter_callback))) {
					Uri uri = Uri.parse(url);
					String oauthVerifier = uri
							.getQueryParameter("qgQkj01kBdVBstEiugdnvwYs8kAzhKXIFaAFntAyuE4ia");
					mIntent.putExtra("oauth_verifier", oauthVerifier);
					setResult(RESULT_OK, mIntent);
					// Intent i =new Intent(TwitterWebviewActivity.this,
					// TwitterPost.class);
					// startActivity(i);
					finish();
					return true;
				}
				return false;
			}
		});
		webView.loadUrl(newurl);
	}
}
