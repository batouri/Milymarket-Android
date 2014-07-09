package com.hitasoft.apps.milymarket;

/*************************************************************\
 * 
 * @author 'Hitasoft Technologies'
 * 
 * Description:
 * This Activity is used for Login
 * 
 * Revision History:
 * Version 1.0 - Initial Version
 *   
 \************************************************************/

import java.io.BufferedReader;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPersonLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.hitasoft.apps.milymarket.twitters.Constants;
import com.hitasoft.apps.milymarket.twitters.TwitterWebviewActivity;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener, OnPersonLoadedListener {

	private static boolean uNameCheck = false;
	private ImageButton twr, fb, googlePlus, signIn;
	private TextView signUp;
	private EditText uName, pWord;
	private static Toast toast;
	private static String userName = null, Password = null;
	JSONParser jParser;
	private RelativeLayout parent;
	CheckBox primary;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * For facebook login
	 */
	private static String APP_ID = AppConstant.App_ID;
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * For Google Plus
	 */
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * Fot Twitter
	 */
	private int TWITTER_AUTH;
	private Twitter mTwitter;
	private RequestToken mRequestToken;
	private static final String CALLBACK_URL = "tweet-to-twitter-blundell-01-android:///";
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	private String accessToken;
	private String accessTokenSecret;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	private BroadcastReceiver networkStateReceiver;

	SharedPreferences pref;
	Editor editor;
	private static boolean setPref = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see for Background scroll
	 */
	JSONObject jsonObject;
	private LinearLayout horizontalOuterLayout;
	private HorizontalScrollView horizontalScrollview;
	private TextView horizontalTextView;
	private int scrollMax;
	private int scrollPos = 0;
	private int scrollMax1;
	private int scrollPos1 = 0;
	private TimerTask clickSchedule;
	private TimerTask scrollerSchedule;
	private TimerTask faceAnimationSchedule;
	private Button clickedButton = null;
	private Timer scrollTimer = null;
	private Timer clickTimer = null;
	private Timer faceTimer = null;
	private Boolean isFaceDown = true;
	private TimerTask clickSchedule1;
	private TimerTask scrollerSchedule1;
	private TimerTask faceAnimationSchedule1;
	private Button clickedButton1 = null;
	private Timer scrollTimer1 = null;
	private Timer clickTimer1 = null;
	private Timer faceTimer1 = null;
	private Boolean isFaceDown1 = true;
	int t = 1;
	int g = 0;
	JSONObject jsonobj;
	private LinearLayout horizontalOuterLayout1;
	private HorizontalScrollView horizontalScrollview1;
	private String[] imageNameArray;
	private ImageButton home, near, shop, alert, menu;
	
	//GCM variables
	 AsyncTask<Void, Void, Void> mRegisterTask;
	 private FantacyApplication aController;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		internetCheck();
		pref = getApplicationContext().getSharedPreferences("FantacyPref",
				MODE_PRIVATE);
		editor = pref.edit();
		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		shop = (ImageButton) findViewById(R.id.btn_shop);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);
		

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		
	//	new myAsyncTask().execute();
		
		signIn = (ImageButton) findViewById(R.id.signin);
		signIn.setOnClickListener(this);
		parent = (RelativeLayout) findViewById(R.id.parent);
		setupUI(parent);
		twr = (ImageButton) findViewById(R.id.twittr);
		fb = (ImageButton) findViewById(R.id.fbook);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		googlePlus = (ImageButton) findViewById(R.id.googlePlus);
		signUp = (TextView) findViewById(R.id.signUp);
		jParser = new JSONParser();
		twr.setOnClickListener(this);
		fb.setOnClickListener(this);
		googlePlus.setOnClickListener(this);
		signUp.setOnClickListener(this);
		uName = (EditText) findViewById(R.id.userName);
		
		//Register the app with gcm
		
		  // Make sure the device has the proper dependencies.
	    GCMRegistrar.checkDevice(this);

	    // Make sure the manifest permissions was properly set 
	    GCMRegistrar.checkManifest(this);
	    // Register custom Broadcast receiver to show messages on activity
        registerReceiver(mHandleMessageReceiver, "Com.MilyMarket.APPS");
	    

		uName.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				if (uName.getText().toString().matches(emailPattern)
						&& s.length() > 0) {
					uNameCheck = true;

				} else {
					uNameCheck = false;
				}
			}
		

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		primary = (CheckBox) findViewById(R.id.primaryLogin);
		primary.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					setPref = true;
				} else {
					editor.clear();
					editor.commit();
				}
			}
		});
		pWord = (EditText) findViewById(R.id.passWord);

		/*
		 * For Google Plus
		 */
		mPlusClient = new PlusClient.Builder(this, this, this)
				.setVisibleActivities("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity").build();
		// Progress bar to be displayed if the connection failure is not
		// resolved.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.twittr:
			Thread th = new Thread(new Runnable() {
				public void run() {
					loginToTwitter();
					//loginNewUser();
					//loginAuthorisedUser();
				}
			});

			th.start();
			break;
		case R.id.fbook:
			loginToFacebook();
			break;
		case R.id.googlePlus:
			loginToGoogle();
			break;
		case R.id.signin:
			if (!uNameCheck) {
				uName.setError("Please Verify Your UserName");
			} else if (pWord.getText().toString().length() == 0) {
				pWord.setError("Please Type your Password");
			} else {
				userName = uName.getText().toString().trim();
				Password = pWord.getText().toString().trim();
				new loginAsync().execute();

			}
			break;
		case R.id.signUp:
			Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(i);
			break;
		

		case R.id.btn_home:
			FragmentChangeActivity.rshome = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_near:
			FragmentChangeActivity.rsnear = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_shop:
			FragmentChangeActivity.rsshop = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_alert:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent j=new Intent(LoginActivity.this,LoginActivity.class);
				startActivity(j);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsprofile = true;
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon=false;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent j=new Intent(LoginActivity.this,LoginActivity.class);
				startActivity(j);
			}
			break;

		}
	}

	private void loginToTwitter() {
		if ((accessToken == null) || (accessTokenSecret == null)) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
            builder.setUseSSL(true);
            Configuration configuration = builder.build();
			mTwitter = new TwitterFactory(configuration).getInstance();
			mRequestToken = null;
			//mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY,
			//		Constants.CONSUMER_SECRET);
			String callbackURL = getResources().getString(
					R.string.twitter_callback);
			try {
				mRequestToken = mTwitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				Log.v("mRequestToken",""+mRequestToken);
				Intent i = new Intent(this, TwitterWebviewActivity.class);
				i.putExtra("URL", mRequestToken.getAuthenticationURL());
				startActivityForResult(i, TWITTER_AUTH);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void loginToGoogle() {
		if (!mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
				mPlusClient.connect();
			} else {
				try {
					mConnectionResult.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}
	
	/** private void loginNewUser() {
         try {
        	 
        	     mTwitter = new TwitterFactory().getInstance();
                 Log.i("TAG", "Request App Authentication");
                 mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY,
     					Constants.CONSUMER_SECRET);
                 mRequestToken= mTwitter.getOAuthRequestToken(CALLBACK_URL);

                 Log.i("TAG", "Starting Webview to login to twitter");
                 WebView twitterSite = new WebView(this);
                 twitterSite.loadUrl(mRequestToken.getAuthenticationURL());
                 setContentView(twitterSite);

         } catch (TwitterException e) {
        	 Log.v("error",e.getErrorMessage());
               //  Toast.makeText(this, "Twitter Login error, try again later", Toast.LENGTH_SHORT).show();
         }
 }
	 private void loginAuthorisedUser() {
		 mTwitter = new TwitterFactory().getInstance();
		 mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY,
					Constants.CONSUMER_SECRET);
         String token ="247841866-1ghbKCxfaqpTAgMiPzeJUppWV5YpTxVRNvPIAn4R";
         String secret ="qgQkj01kBdVBstEiugdnvwYs8kAzhKXIFaAFntAyuE4ia";

         // Create the twitter access token from the credentials we got previously
         AccessToken at = new AccessToken(token, secret);

         mTwitter.setOAuthAccessToken(at);
         
        // Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
         
        // enableTweetButton();
 }
**/
	class loginAsync extends AsyncTask<Void, Void, String> {
		private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

		@Override
		protected String doInBackground(Void... arg0) {
			String result = postData();
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("S'il vous plaît attendre...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			this.dialog.show();

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			try {
				JSONObject jonj = new JSONObject(result);
				if (jonj.getString(ConstantValues.status).equalsIgnoreCase(
						"true")) {
					GetSet.setLogged(true);
					GetSet.setEmail(userName);
					GetSet.setPassword(Password);
					GetSet.setUserId(jonj.getString("userid"));
					GetSet.setUserName(jonj.getString("username"));
					GetSet.setFullName(jonj.getString("fullname"));
					GetSet.setProfileUserId(jonj.getString("userid"));
					GetSet.setImageUrl(jonj.getString("photo"));
					if (setPref) {
						editor.putBoolean("isLogged", true);
						editor.putString("userId", GetSet.getUserId());
						editor.putString("userName", GetSet.getUserName());
						editor.putString("Email", GetSet.getEmail());
						editor.putString("Password", GetSet.getPassword());
						editor.putString("photo", GetSet.getImageUrl());
						editor.putString("fullname", GetSet.getFullName());
						editor.commit();
					}

					Intent datas = new Intent();
					datas.putExtra("returnKey1", "Swinging on a star. ");
					setResult(RESULT_OK, datas);
					
					Registernotifi();
					
					LoginActivity.this.finish();

				} else {
					showAToast(jonj.getString(ConstantValues.msg));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void registerReceiver(BroadcastReceiver mHandleMessageReceiver2,
			String string) {
		// TODO Auto-generated method stub
		
	}
	
	public void  Registernotifi(){
		Log.v("enetered push","entered push");
		
		
		AppConstant. Register_Id = GCMRegistrar.getRegistrationId(getApplicationContext());
		
		Log.v("enetered push","registerid="+AppConstant. Register_Id);
		
		String Url=ConstantValues.url+ConstantValues.Push_Notification;
    	Log.v("the complete url","the complete url="+Url);
    	
		if(AppConstant. Register_Id==""){
			Log.v("enetered push","registerid_if");
			GCMRegistrar.register(this, AppConstant.sender_Id);
			//Registernotifi();
			
		}else{
			Log.v("enetered push","registerid_else");
	            
	            // Device is already registered on GCM Server
	            if (GCMRegistrar.isRegisteredOnServer(this)) {
	                 
	                // Skips registration.              
	                Toast.makeText(getApplicationContext(), "Already registered with GCM Server", 
	                              Toast.LENGTH_LONG).
	                              show();
	             
	            } else {
	            	final Context context= this;
	            	  Map<String, String> params = new HashMap<String, String>();
	                  params.put("regId", AppConstant. Register_Id);
	                  params.put("name", GetSet.getUserName());
	                  params.put("email", GetSet.getEmail());
	                  
	                 mRegisterTask = new AsyncTask<Void, Void, Void>() {
	                	  
	                      @Override
	                      protected Void doInBackground(Void... params) {
	                           
	                          // Register on our server
	                          // On server creates a new user
	                         aController.register(context,  GetSet.getUserName(), GetSet.getEmail(), AppConstant. Register_Id);
	                           
	                          return null;
	                      }
	   
	                      @Override
	                      protected void onPostExecute(Void result) {
	                          mRegisterTask = null;
	                      }
	   
	                  };
	                   
	                  // execute AsyncTask
	                  mRegisterTask.execute(null, null, null);
	              
			/*try {
				//aController.register(getApplicationContext(),  GetSet.getUserName(), GetSet.getEmail(), Register_Id);
				post(ConstantValues.Push_Notification,params);
				
			    GCMRegistrar.setRegisteredOnServer(getApplicationContext(), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	            }
			
		}
		
		
	}
	
	
	@SuppressWarnings("unused")
	private  void post(String endpoint, Map<String, String> params)
	        throws IOException {  
		
		Log.v("enetered push","entered post method_uri="+endpoint);
		
	    URL url;
	    try {
	         
	        url = new URL(endpoint);
	         
	    } catch (MalformedURLException e) {
	        throw new IllegalArgumentException("invalid url: " + endpoint);
	    }
	     
	    StringBuilder bodyBuilder = new StringBuilder();
	    Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	     
	    // constructs the POST body using the parameters
	    while (iterator.hasNext()) {
	        Entry<String, String> param = iterator.next();
	        bodyBuilder.append(param.getKey()).append('=')
	                .append(param.getValue());
	        if (iterator.hasNext()) {
	            bodyBuilder.append('&');
	        }
	    }
	     
	    String body = bodyBuilder.toString();
	     
	    Log.v("PushNotification", "Posting '" + body + "' to " + url);
	     
	    byte[] bytes = body.getBytes();
	     
	    HttpURLConnection conn = null;
	    try {
	         
	        Log.e("URL", "> " + url);
	         
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);
	        conn.setFixedLengthStreamingMode(bytes.length);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded;charset=UTF-8");
	        // post the request
	        OutputStream out = conn.getOutputStream();
	        out.write(bytes);
	        out.close();
	         
	        // handle the response
	        int status = conn.getResponseCode();
	         
	        // If response is not success
	        if (status != 200) {
	             
	          throw new IOException("Post failed with error code " + status);
	        }
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	  }
	 
	
	public String postData() {
		String result = null;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ConstantValues.signIn);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", userName));
			nameValuePairs.add(new BasicNameValuePair("password", Password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.v("response", result.toString());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {

		}
		return result;
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					hideSoftKeyboard(LoginActivity.this);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		} catch (NullPointerException npe) {

		} catch (Exception e) {

		}
	}

	public void showAToast(String st) {
		try {
			toast.getView().isShown(); // true if visible
			toast.setText(st);
		} catch (Exception e) { // invisible if exception
			toast = Toast.makeText(LoginActivity.this, st, Toast.LENGTH_SHORT);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
		if (requestCode == TWITTER_AUTH) {

			if (resultCode == Activity.RESULT_OK) {
				final String oauthVerifier = (String) data.getExtras().get(
						"qgQkj01kBdVBstEiugdnvwYs8kAzhKXIFaAFntAyuE4ia");

				Thread th = new Thread(new Runnable() {
					public void run() {
						try {
							AccessToken at = mTwitter
									.getOAuthAccessToken(oauthVerifier);
							String theToken = at.getToken();
							Log.v("stre", theToken);
							long userID = at.getUserId();
							User user = mTwitter.showUser(userID);
							HashMap<String, String> twitterData = new HashMap<String, String>();
							twitterData.put("id", Long.toString(at.getUserId()));
							twitterData.put("firstName", user.getScreenName());
							twitterData.put("userName", user.getName());
							twitterData.put("image", user.getProfileImageURL());
							twitterData.put("url", user.getURL());
							Log.v("stre", user.getProfileImageURL());
							startTwitterLogin(twitterData);
						} catch (TwitterException e) {
							e.printStackTrace();
						}
					}
				});

				th.start();
			}
		}
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	protected void startTwitterLogin(final HashMap<String, String> twitterData) {

		LoginActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setContentView(R.layout.twitter_login);
				setupUI(parent);
				final EditText userName = (EditText) findViewById(R.id.twitter_userName);
				final EditText firstName = (EditText) findViewById(R.id.twitter_firstName);
				final EditText email = (EditText) findViewById(R.id.twitter_email);
				TextView url = (TextView) findViewById(R.id.twitter_url);
				ImageButton ibutton = (ImageButton) findViewById(R.id.twittr_register);
				userName.setText(twitterData.get("userName"));
				firstName.setText(twitterData.get("firstName"));
				url.setText("Your url : " + ConstantValues.url + "/people/"
						+ userName);
				ibutton.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View arg0) {
						if (userName.getText().toString().length() == 0) {
							userName.setError("Please provide userName");
						} else if (!email.getText().toString()
								.matches(emailPattern)) {
							email.setError("Please verify your mail id");
						} else {
							HashMap<String, String> twitterData = new HashMap<String, String>();
							twitterData.put("type", "twitter");
							twitterData
									.put("email", email.getText().toString());
							twitterData.put("id", twitterData.get("id"));
							twitterData.put("firstName", userName.getText()
									.toString());
							twitterData.put("lastName", firstName.getText()
									.toString());
							new sendTwitterData().execute(twitterData);
						}
					}
				});

			}
		});

	}

	/**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {

		if (facebook.isSessionValid()) {
			getProfileInformation();
		} else {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						@Override
						public void onCancel() {
						}

						@Override
						public void onComplete(Bundle values) {
							getProfileInformation();
						}

						@Override
						public void onError(DialogError error) {

						}

						@Override
						public void onFacebookError(FacebookError fberror) {

						}

					});
		}
	}

	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileInformation() {
		final HashMap<String, String> fbdata = new HashMap<String, String>();
		final ProgressDialog dialog = new ProgressDialog(getBaseContext());
		dialog.setMessage("Getting datas From Fb");
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.dismiss();

		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);

					// getting name of the user
					final String name = profile.getString("name");

					// getting email of the user
					final String email = profile.getString("email");

					// getting userId of the user
					final String userId = profile.getString("id");

					// getting firstName of the user
					final String firstName = profile.getString("first_name");

					// getting lastName of the user
					final String lastName = profile.getString("last_name");

					fbdata.put("type", "facebook");
					fbdata.put("email", email);
					fbdata.put("id", userId);
					fbdata.put("firstName", firstName);
					fbdata.put("lastName", lastName);
					LoginActivity.this.runOnUiThread(new Runnable() {

						@SuppressWarnings("unchecked")
						@Override
						public void run() {
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
							}
							new sendFbData().execute(fbdata);

						}
					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}

		// Save the intent so that we can start an activity when the user clicks
		// the sign-in button.
		mConnectionResult = result;

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mConnectionProgressDialog.dismiss();
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		mPlusClient.loadPerson(this, "me");
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	protected void onStart() {
		super.onStart();
		// mPlusClient.connect();
	}

	@Override
	protected void onStop() {
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
			LoginActivity.this.runOnUiThread(new Runnable() {

				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					new sendGoogleData().execute(googleMap);
				}
			});
		}

	}

	class sendFbData extends
			AsyncTask<HashMap<String, String>, Void, JSONObject> {

		String url = ConstantValues.socialLoginUrl;
		JSONParser parser = new JSONParser();
		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

		@Override
		protected JSONObject doInBackground(HashMap<String, String>... params) {
			Log.v("goind", "dsfsdf");
			HashMap<String, String> datas = params[0];
			url = url + "?type=" + datas.get("type") + "&id=" + datas.get("id")
					+ "&firstName=" + datas.get("firstName") + "&lastName="
					+ datas.get("lastName") + "&email=" + datas.get("email");
			Log.v("url", url);
			JSONObject response = parser.getJSONFromUrl(url);
			Log.v("response", response.toString());
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				String status = result.getString(ConstantValues.status);
				if (status.equalsIgnoreCase("true")) {
					JSONObject values = result.getJSONObject("result");
					GetSet.setLogged(true);
					GetSet.setUserId(values.getString("userId"));
					GetSet.setUserName(values.getString("userName"));
					GetSet.setFullName(values.getString("fullName"));
					GetSet.setProfileUserId(values.getString("userId"));
					GetSet.setImageUrl(values.getString("photo"));
					if (setPref) {
						editor.putBoolean("isLogged", true);
						editor.putString("userId", GetSet.getUserId());
						editor.putString("userName", GetSet.getUserName());
						editor.putString("Email", GetSet.getEmail());
						editor.putString("Password", GetSet.getPassword());
						editor.putString("photo", GetSet.getImageUrl());
						editor.putString("fullname", GetSet.getFullName());
						editor.commit();
					}
					Intent datas = new Intent();
					datas.putExtra("returnKey1", "Swinging on a star. ");
					setResult(RESULT_OK, datas);
					LoginActivity.this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	class sendTwitterData extends
			AsyncTask<HashMap<String, String>, Void, JSONObject> {

		String url = ConstantValues.socialLoginUrl;
		JSONParser parser = new JSONParser();
		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

		@Override
		protected JSONObject doInBackground(HashMap<String, String>... params) {
			Log.v("goind", "dsfsdf");
			HashMap<String, String> datas = params[0];
			url = url + "?type=" + datas.get("type") + "&id=" + datas.get("id")
					+ "&firstName=" + datas.get("firstName") + "&lastName="
					+ datas.get("lastName") + "&email=" + datas.get("email")
					+ "&imageUrl=" + datas.get("image");
			Log.v("url", url);
			JSONObject response = parser.getJSONFromUrl(url);
			Log.v("response", response.toString());
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				String status = result.getString(ConstantValues.status);
				if (status.equalsIgnoreCase("true")) {
					JSONObject values = result.getJSONObject("result");
					Intent datas = new Intent();
					GetSet.setLogged(true);
					GetSet.setEmail(userName);
					GetSet.setPassword(Password);
					GetSet.setUserId(values.getString("userId"));
					GetSet.setUserName(values.getString("userName"));
					GetSet.setFullName(values.getString("fullName"));
					GetSet.setProfileUserId(values.getString("userId"));
					GetSet.setImageUrl(values.getString("photo"));
					if (setPref) {
						editor.putBoolean("isLogged", true);
						editor.putString("userId", GetSet.getUserId());
						editor.putString("userName", GetSet.getUserName());
						editor.putString("Email", GetSet.getEmail());
						editor.putString("Password", GetSet.getPassword());
						editor.putString("photo", GetSet.getImageUrl());
						editor.putString("fullname", GetSet.getFullName());
						editor.commit();
					}
					datas.putExtra("returnKey1", "Swinging on a star. ");
					setResult(RESULT_OK, datas);
					LoginActivity.this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	class sendGoogleData extends
			AsyncTask<HashMap<String, String>, Void, JSONObject> {

		String url = ConstantValues.socialLoginUrl;
		JSONParser parser = new JSONParser();

		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

		@Override
		protected JSONObject doInBackground(HashMap<String, String>... params) {
			Log.v("goind", "dsfsdf");
			HashMap<String, String> datas = params[0];
			url = url + "?type=" + datas.get("type") + "&id=" + datas.get("id")
					+ "&email=" + datas.get("email") + "&firstName="
					+ datas.get("firstName") + "&lastName="
					+ datas.get("lastName");
			Log.v("url", url);
			JSONObject response = parser.getJSONFromUrl(url);
			Log.v("response", response.toString());
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				String status = result.getString(ConstantValues.status);
				if (status.equalsIgnoreCase("true")) {
					JSONObject values = result.getJSONObject("result");
					GetSet.setLogged(true);
					GetSet.setUserId(values.getString("userId"));
					GetSet.setUserName(values.getString("userName"));
					GetSet.setFullName(values.getString("fullName"));
					GetSet.setProfileUserId(values.getString("userId"));
					GetSet.setImageUrl(values.getString("photo"));
					if (setPref) {
						editor.putBoolean("isLogged", true);
						editor.putString("userId", GetSet.getUserId());
						editor.putString("userName", GetSet.getUserName());
						editor.putString("Email", GetSet.getEmail());
						editor.putString("Password", GetSet.getPassword());
						editor.putString("photo", GetSet.getImageUrl());
						editor.putString("fullname", GetSet.getFullName());
						editor.commit();
					}
					Intent datas = new Intent();
					datas.putExtra("returnKey1", "Swinging on a star. ");
					setResult(RESULT_OK, datas);
					LoginActivity.this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public void internetCheck() {
		networkStateReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				NetworkInfo networkinfo = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (networkinfo != null
						&& networkinfo.getState() == NetworkInfo.State.CONNECTED) {
					Log.v("we are connected", "we are connected");
				} else {
					exit();
				}
			}
		};
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(this.networkStateReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
	}

	public void exit() {
		final Dialog settingsDialog = new Dialog(LoginActivity.this);
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		settingsDialog.setContentView(getLayoutInflater().inflate(
				R.layout.alert, null));
		settingsDialog.setCancelable(false);
		settingsDialog.setCanceledOnTouchOutside(false);
		settingsDialog.setTitle("Network Problem");
		Button retry = (Button) settingsDialog.findViewById(R.id.alertRetry);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}

			}
		});
		settingsDialog.show();
	}

	public void dismiss(View v) {
		switch (v.getId()) {
		case R.id.alertClose:
			LoginActivity.this.finish();
			break;
		}
	}

	public void right() {
		horizontalScrollview = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id);
		horizontalOuterLayout = (LinearLayout) findViewById(R.id.horiztonal_outer_layout_id);
		horizontalScrollview.setHorizontalScrollBarEnabled(false);
		addImagesToView();
		ViewTreeObserver vto = horizontalOuterLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				horizontalOuterLayout.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				getScrollMaxAmount();
				startAutoScrolling();

			}
		});
	}

	public void left() {
		horizontalScrollview1 = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id1);
		horizontalOuterLayout1 = (LinearLayout) findViewById(R.id.horiztonal_outer_layout_id1);
		horizontalScrollview1.setHorizontalScrollBarEnabled(false);
		addImagesToView1();
		ViewTreeObserver vto = horizontalOuterLayout1.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				horizontalOuterLayout1.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				getScrollMaxAmount1();
				startAutoScrolling1();
			}
		});
	}

	public void getScrollMaxAmount() {
		int actualWidth = (horizontalOuterLayout.getMeasuredWidth() - 512);
		scrollMax = actualWidth;
	}

	public void getScrollMaxAmount1() {
		int actualWidth = (horizontalOuterLayout1.getMeasuredWidth() - 512);
		scrollMax1 = actualWidth;
	}

	public void startAutoScrolling() {
		if (scrollTimer == null) {
			scrollTimer = new Timer();
			final Runnable Timer_Tick = new Runnable() {
				public void run() {
					lefttoright();
				}
			};

			if (scrollerSchedule != null) {
				scrollerSchedule.cancel();
				scrollerSchedule = null;
			}
			scrollerSchedule = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(Timer_Tick);
				}
			};

			scrollTimer.schedule(scrollerSchedule, 30, 30);
		}
	}

	public void startAutoScrolling1() {
		if (scrollTimer1 == null) {
			scrollTimer1 = new Timer();
			final Runnable Timer_Tick = new Runnable() {
				public void run() {
					moveScrollView();
				}
			};

			if (scrollerSchedule1 != null) {
				scrollerSchedule1.cancel();
				scrollerSchedule1 = null;
			}
			scrollerSchedule1 = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(Timer_Tick);
				}
			};

			scrollTimer1.schedule(scrollerSchedule1, 30, 30);
		}
	}

	public void moveScrollView() {
		scrollPos1 = (int) (horizontalScrollview1.getScrollX() + 1.0);
		if (scrollPos1 >= scrollMax1) {
			scrollPos1 = 0;
		}
		horizontalScrollview1.scrollTo(scrollPos1, 0);
	}

	/** Adds the images to view. */
	@SuppressLint("NewApi")
	public void addImagesToView() {
		for (int i = 0; i < imageNameArray.length; i++) {
			final int s = i;
			final ImageView imageButton = new ImageView(this);
			new Thread(new Runnable() {
				public void run() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// System.out.println("Downloading started");
						}
					});
					try {
						URL url1 = new URL(imageNameArray[s]);
						InputStream is = (InputStream) url1.getContent();
						Drawable drawable = Drawable.createFromStream(is,
								"src name");
						imageButton.setBackgroundDrawable(drawable);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
			}).start();
			imageButton.setTag(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					100, 100);
			params.setMargins(0, 5, 0, 5);
		    imageButton.setAlpha(0.35f);
			imageButton.setLayoutParams(params);
			horizontalOuterLayout.addView(imageButton);
		}
	}

	public void addImagesToView1() {
		for (int i = 0; i < imageNameArray.length; i++) {
			final int s = i;
			final ImageView imageButton = new ImageView(this);

			new Thread(new Runnable() {
				public void run() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// System.out.println("Downloading started");
						}
					});
					try {
						URL url1 = new URL(imageNameArray[s]);
						InputStream is = (InputStream) url1.getContent();
						Drawable drawable = Drawable.createFromStream(is,
								"src name");
						imageButton.setBackgroundDrawable(drawable);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
			}).start();
			imageButton.setTag(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					100, 100);
			params.setMargins(0, 5, 0, 5);
			imageButton.setLayoutParams(params);
			horizontalOuterLayout1.addView(imageButton);
		}
	}

	public Animation scaleFaceUpAnimation() {
		Animation scaleFace = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(500);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				isFaceDown = false;
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (faceTimer != null) {
					faceTimer.cancel();
					faceTimer = null;
				}

				faceTimer = new Timer();
				if (faceAnimationSchedule != null) {
					faceAnimationSchedule.cancel();
					faceAnimationSchedule = null;
				}
				faceAnimationSchedule = new TimerTask() {
					@Override
					public void run() {
						faceScaleHandler.sendEmptyMessage(0);
					}
				};

				faceTimer.schedule(faceAnimationSchedule, 750);
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	public Animation scaleFaceUpAnimation1() {
		Animation scaleFace = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(500);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				isFaceDown1 = false;
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (faceTimer1 != null) {
					faceTimer1.cancel();
					faceTimer1 = null;
				}

				faceTimer1 = new Timer();
				if (faceAnimationSchedule1 != null) {
					faceAnimationSchedule1.cancel();
					faceAnimationSchedule1 = null;
				}
				faceAnimationSchedule1 = new TimerTask() {
					@Override
					public void run() {
						faceScaleHandler1.sendEmptyMessage(0);
					}
				};

				faceTimer1.schedule(faceAnimationSchedule1, 750);
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	@SuppressLint("HandlerLeak")
	private Handler faceScaleHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (clickedButton.isSelected() == true)
				clickedButton.startAnimation(scaleFaceDownAnimation(500));
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler faceScaleHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (clickedButton1.isSelected() == true)
				clickedButton1.startAnimation(scaleFaceDownAnimation1(500));
		}
	};

	public Animation scaleFaceDownAnimation(int duration) {
		Animation scaleFace = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(duration);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				horizontalTextView.setText("");
				isFaceDown = true;
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	public Animation scaleFaceDownAnimation1(int duration) {
		Animation scaleFace = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(duration);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				isFaceDown1 = true;
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	public void stopAutoScrolling() {
		if (scrollTimer1 != null) {
			scrollTimer1.cancel();
			scrollTimer1 = null;
		}
	}

	public void stopAutoScrolling1() {
		if (scrollTimer != null) {
			scrollTimer.cancel();
			scrollTimer = null;
		}
	}

	public void onDestroy() {
		clearTimerTaks(clickSchedule);
		clearTimerTaks(scrollerSchedule);
		clearTimerTaks(faceAnimationSchedule);
		clearTimers(scrollTimer);
		clearTimers(clickTimer);
		clearTimers(faceTimer);

		clickSchedule = null;
		scrollerSchedule = null;
		faceAnimationSchedule = null;
		scrollTimer = null;
		clickTimer = null;
		faceTimer = null;
		clearTimerTaks(clickSchedule1);
		clearTimerTaks(scrollerSchedule1);
		clearTimerTaks(faceAnimationSchedule1);
		clearTimers(scrollTimer1);
		clearTimers(clickTimer1);
		clearTimers(faceTimer1);

		clickSchedule1 = null;
		scrollerSchedule1 = null;
		faceAnimationSchedule1 = null;
		scrollTimer1 = null;
		clickTimer1 = null;
		super.onDestroy();
	}

	private void clearTimers(Timer timer) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void clearTimerTaks(TimerTask timerTask) {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	public void lefttoright() {
		if (t == 1) {
			scrollPos = scrollMax;
			t++;
		}

		scrollPos = scrollPos - 1;
		if (scrollPos <= 0) {
			scrollPos = scrollMax;
		}
		horizontalScrollview.scrollTo(scrollPos, 0);
	}

	/** Horizontal Button Handler */
	// public void horizontalButtonHandler(View target){
	// Intent horizontalSlideshowIntent = new
	// Intent(MainActivity.this,HorizonalSlideshow.class);
	// startActivity(horizontalSlideshowIntent);
	// }

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	public class myAsyncTask extends AsyncTask<Void, Void, Void> {

		myAsyncTask() {
			System.out.println("hereasyntask");
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("Onpostexecute");
			right();
			//left();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("Onpreexecute");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			System.out.println("doinbackground");
			try {
				StringBuilder sb = new StringBuilder();
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(
						"http://fancyclone.net/dev/api/slideshow");
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null
						&& response.getStatusLine().getStatusCode() == 200) {
					InputStream is = entity.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String str;
					while ((str = br.readLine()) != null) {
						sb.append(str);
					}
					String result = sb.toString();
					jsonObject = new JSONObject(result);
					is.close();
				}
				System.out.println(jsonObject);
				JSONArray arr = jsonObject.getJSONArray("result");
				int n = arr.length();
				imageNameArray = new String[n];
				for (int i = 0; i < n; i++) {
					JSONObject obj = arr.getJSONObject(i);
					System.out.println(obj);
					String s = obj.getString("item_url_main_150");
					System.out.println(s);
					imageNameArray[i] = s;
					System.out.println(s);
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
	}

	public void geturl() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

					}
				});
				try {
					StringBuilder sb = new StringBuilder();
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(
							"http://fancyclone.net/dev/api/slideshow");
					HttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null
							&& response.getStatusLine().getStatusCode() == 200) {
						InputStream is = entity.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						String str;
						while ((str = br.readLine()) != null) {
							sb.append(str);
						}
						String result = sb.toString();
						jsonObject = new JSONObject(result);
						is.close();
					}
					System.out.println(jsonObject);
					JSONArray arr = jsonObject.getJSONArray("result");
					int n = arr.length();
					imageNameArray = new String[n];
					for (int i = 0; i < n; i++) {
						JSONObject obj = arr.getJSONObject(i);
						System.out.println(obj);
						String s = obj.getString("item_url_main_150");
						System.out.println(s);
						imageNameArray[i] = s;
						System.out.println(s);
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
		}).start();
	}
	
	 // Create a broadcast receiver to get message and show on screen 
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
         
        @Override
        public void onReceive(Context context, Intent intent) {
             
            String newMessage = intent.getExtras().getString(AppConstant.EXTRA_MESSAGE);
             
            // Waking up mobile if it is sleeping
            acquireWakeLock(getApplicationContext());
             
            // Display message on the screen
           /* lblMessage.append(newMessage + "");         */
             
            Toast.makeText(getApplicationContext(), 
                           "Got Message: " + newMessage, 
                           Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            releaseWakeLock();
        }
    };
    
    private PowerManager.WakeLock wakeLock;
    
    
    public  void acquireWakeLock(Context context) {
        if (wakeLock != null) wakeLock.release();
 
        PowerManager pm = (PowerManager) 
                          context.getSystemService(Context.POWER_SERVICE);
         
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
         
        wakeLock.acquire();
    }
 
    public  void releaseWakeLock() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
    
	
	
}
