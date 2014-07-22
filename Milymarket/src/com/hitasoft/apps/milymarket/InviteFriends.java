package com.hitasoft.apps.milymarket;

import java.util.ArrayList;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPersonLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.hitasoft.apps.milymarket.twitters.Constants;
import com.hitasoft.apps.milymarket.twitters.TwitterPostLogin;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;

public class InviteFriends extends SherlockFragment implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener, OnPersonLoadedListener {
	private ImageButton home, near, cart, alert, menu;
	LinearLayout fb, twit, gPlus, contact;
	WebDialog dialog;
	private LoginButton mLoginButton;
	private SharedPreferences mPrefs;
	private static String APP_ID = AppConstant.App_ID;
	@SuppressWarnings("deprecation")
	private Facebook facebook = new Facebook(APP_ID);
	@SuppressWarnings("deprecation")
	private AsyncFacebookRunner mAsyncRunner;

	private int TWITTER_AUTH;
	public static Twitter mTwitter;
	public static RequestToken mRequestToken;

	private String accessToken;
	private String accessTokenSecret;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	private BroadcastReceiver networkStateReceiver;

	//static String TWITTER_CONSUMER_KEY = "ZGAAloJmxoMdkhhAwA35iA"; // place your
																	// cosumer
																	// key here
	static String TWITTER_CONSUMER_KEY = "JumuzoiNDwGhqnfs3ozy12RBV";
	
	static String TWITTER_CONSUMER_SECRET ="62BcUKYF3kAPsq0TwYqxjElnGRbAOWrZy4U5CV5Mu0ExNpHjiv";
	//static String TWITTER_CONSUMER_SECRET = "7f8oBM9fcdr7FAxdmzTQODBn6yof59sVt6HWOhmjcwY"; // place
																							// your
																							// consumer
																							// secret
																							// here

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 8) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.invite_friends, container,
				false);
		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		cart = (ImageButton) getView().findViewById(R.id.btn_cart);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		fb = (LinearLayout) getView().findViewById(R.id.fb);
		twit = (LinearLayout) getView().findViewById(R.id.twit);
		gPlus = (LinearLayout) getView().findViewById(R.id.gogle);
		contact = (LinearLayout) getView().findViewById(R.id.cntct);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		fb.setOnClickListener(this);
		twit.setOnClickListener(this);
		gPlus.setOnClickListener(this);
		contact.setOnClickListener(this);

		int colorBlack = getResources().getColor(R.color.black);
		String text = "Inviter des amis";
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

		mPlusClient = new PlusClient.Builder(getActivity(), this, this)
				.setVisibleActivities("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity").build();
		// Progress bar to be displayed if the connection failure is not
		// resolved.
		mConnectionProgressDialog = new ProgressDialog(getActivity());
		mConnectionProgressDialog.setMessage("Signing in...");

	}

	private void loginToTwitter() {
		if ((accessToken == null) || (accessTokenSecret == null)) {
			mTwitter = new TwitterFactory().getInstance();
			mRequestToken = null;
			mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY,
					Constants.CONSUMER_SECRET);
			//String callbackURL = "https://www.google.co.in/?gws_rd=cr&ei=TOW7UvWjLojhkAXF7IGwCg";
			String callbackURL="https://www.google.co.in/";
			// getResources().getString(
			// R.string.twitter_callback);
			try {
				mRequestToken = mTwitter.getOAuthRequestToken(callbackURL);
				Log.v("mRequestToken",""+mRequestToken);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			Intent i = new Intent(InviteFriends.this.getActivity(),
					TwitterPostLogin.class);
			i.putExtra("URL", mRequestToken.getAuthenticationURL());
			startActivityForResult(i, TWITTER_AUTH);
		}
	}

	@SuppressWarnings("deprecation")
	private void inviteFriends() {
		try {
			Bundle params = new Bundle();
			params.putString("title", "invite friends");
			params.putString("message", "come join us!");
			facebook.dialog(InviteFriends.this.getActivity(), "apprequests",
					params, new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
						}

						@Override
						public void onFacebookError(FacebookError error) {
						}

						@Override
						public void onError(DialogError e) {
						}

						@Override
						public void onCancel() {
						}

						@SuppressWarnings("unused")
						public void onComplete(String response, Object state) {
							try {
								JSONObject eventResponse = new JSONObject(
										response);
								// event_id = event.getString("id");
								Log.i("event respone", "Event Response => "
										+ eventResponse);
							} catch (Exception e) {

							}
						}
					});
		} catch (Exception e) {

		}
	}

	private void loginToGoogle() {
		if (!mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
				mPlusClient.connect();
			} else {
				try {
					mConnectionResult.startResolutionForResult(getActivity(),
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_RESOLVE_ERR) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

	private void performLocalSearch() {
		final Dialog dialog = new Dialog(InviteFriends.this.getActivity(),
				android.R.style.Theme_Translucent);
		dialog.setContentView(R.layout.contact_friends_finder);
		dialog.setTitle("Recherche...");

		final EditText name = (EditText) dialog.findViewById(R.id.name);
		Button searchButton = (Button) dialog.findViewById(R.id.search);
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (name.getText().toString().trim().length() == 0) {
					name.setError("Please enter a query");
				} else {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					new SearchContact().execute(name.getText().toString()
							.trim());
				}
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}

	class SearchContact extends AsyncTask<String, Void, JSONObject> {

		String url = ConstantValues.findFriendsSearch;
		ArrayList<HashMap<String, String>> userDatas = new ArrayList<HashMap<String, String>>();
		ProgressDialog dialog = new ProgressDialog(
				InviteFriends.this.getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);

		@Override
		protected JSONObject doInBackground(String... params) {
			url = url + params[0];
			JSONParser parser = new JSONParser();
			return parser.getJSONFromUrl(url);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Un instant s'il vous plait");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			String status;
			try {
				status = result.getString("status");
				if (status.equalsIgnoreCase("true")) {
					JSONArray array = result.getJSONArray("result");
					for (int i = 0; i < array.length(); i++) {
						HashMap<String, String> tmpMap = new HashMap<String, String>();
						JSONObject obj = array.getJSONObject(i);
						String userId = obj.getString("UserId");
						String userName = obj.getString("userName");
						String imageName = obj.getString("imageName");

						tmpMap.put("userId", userId);
						tmpMap.put("userName", userName);
						tmpMap.put("imageName", imageName);
						userDatas.add(tmpMap);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (userDatas.size() > 0) {
				Intent i = new Intent(InviteFriends.this.getActivity(),
						UserListActivity.class);
				i.putExtra("data", userDatas);
				getActivity().startActivity(i);

			}
		}
	}

	@Override
	public void onClick(View arg0) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (arg0.getId()) {
		case R.id.fb:
			inviteFriends();
			break;
		case R.id.twit:
			loginToTwitter();
			break;
		case R.id.gogle:
			loginToGoogle();
			break;
		case R.id.cntct:
			performLocalSearch();
			break;
		case R.id.btn_home:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new HomeFragment());
			break;
		case R.id.btn_near:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = true;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new LocationFragment());
			break;
		case R.id.btn_cart:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new CartFragment());
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_menu:
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			break;

		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(getActivity(),
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}

		mConnectionResult = result;

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mConnectionProgressDialog.dismiss();
		mPlusClient.loadPerson(this, "me");
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onStart() {
		super.onStart();
		// mPlusClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mPlusClient.isConnected()) {
			mPlusClient.clearDefaultAccount();
			mPlusClient.disconnect();
			mPlusClient.connect();
		}
	}

	@Override
	public void onPersonLoaded(ConnectionResult status, Person person) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
			Log.d("google plus",
					"Display Name: " + person.getDisplayName()
							+ person.getImage() + person.getId()
							+ mPlusClient.getAccountName());
			final HashMap<String, String> googleMap = new HashMap<String, String>();
			googleMap.put("id", person.getId());
			JSONObject obj = null;
			try {
				obj = new JSONObject(person.getName().toString());
				googleMap.put("firstName", obj.getString("givenName"));
				googleMap.put("lastName", obj.getString("familyName"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			googleMap.put("email", mPlusClient.getAccountName());
			googleMap.put("userName", person.getDisplayName());
			googleMap.put("type", "google");
		}

	}

}
