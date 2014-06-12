package com.hitasoft.apps.milymarket;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TwitterPost extends Activity {

	Button btnUpdateStatus;
	Button btnLogoutTwitter;
	EditText txtUpdate;
	TextView lblUpdate;
	TextView lblUserName;

	private static SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_post);

		btnUpdateStatus = (Button) findViewById(R.id.btnUpdateStatus);
		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
		txtUpdate = (EditText) findViewById(R.id.txtUpdateStatus);
		lblUpdate = (TextView) findViewById(R.id.lblUpdate);
		lblUserName = (TextView) findViewById(R.id.lblUserName);
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		btnUpdateStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String status = txtUpdate.getText().toString();

				if (status.trim().length() > 0) {
					new updateTwitterStatus().execute(status);
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter status message", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				logoutFromTwitter();
			}
		});

		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null
					&& uri.toString().startsWith(
							InviteFriends.TWITTER_CALLBACK_URL)) {
				// oAuth verifier
				String verifier = uri
						.getQueryParameter(InviteFriends.URL_TWITTER_OAUTH_VERIFIER);

				try {
					// Get the access token
					AccessToken accessToken = InviteFriends.mTwitter
							.getOAuthAccessToken(InviteFriends.mRequestToken,
									verifier);

					// Shared Preferences
					Editor e = mSharedPreferences.edit();

					// After getting access token, access token secret
					// store them in application preferences
					e.putString(InviteFriends.PREF_KEY_OAUTH_TOKEN,
							accessToken.getToken());
					e.putString(InviteFriends.PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
					// Store login status - true
					e.putBoolean(InviteFriends.PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

					// Show Update Twitter
					lblUpdate.setVisibility(View.VISIBLE);
					txtUpdate.setVisibility(View.VISIBLE);
					btnUpdateStatus.setVisibility(View.VISIBLE);
					btnLogoutTwitter.setVisibility(View.VISIBLE);

					// Getting user details from twitter
					// For now i am getting his name only
					long userID = accessToken.getUserId();
					User user = InviteFriends.mTwitter.showUser(userID);
					String username = user.getName();

					// Displaying in xml ui
					lblUserName.setText(Html.fromHtml("<b>Welcome " + username
							+ "</b>"));
				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}

	}

	private boolean isTwitterLoggedInAlready() {
		return mSharedPreferences.getBoolean(
				InviteFriends.PREF_KEY_TWITTER_LOGIN, false);
	}

	class updateTwitterStatus extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(InviteFriends.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(InviteFriends.TWITTER_CONSUMER_SECRET);

				String access_token = mSharedPreferences.getString(
						InviteFriends.PREF_KEY_OAUTH_TOKEN, "");
				String access_token_secret = mSharedPreferences.getString(
						InviteFriends.PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				twitter4j.Status response = twitter.updateStatus(status);

				Log.v("Status", "> " + response.getText());
			} catch (TwitterException e) {
				Log.v("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
					txtUpdate.setText("");
				}
			});
		}

	}

	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = mSharedPreferences.edit();
		e.remove(InviteFriends.PREF_KEY_OAUTH_TOKEN);
		e.remove(InviteFriends.PREF_KEY_OAUTH_SECRET);
		e.remove(InviteFriends.PREF_KEY_TWITTER_LOGIN);
		e.commit();
		TwitterPost.this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter_post, menu);
		return true;
	}

}
