package com.hitasoft.apps.milymarket;

import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Map;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.paypal.android.MEP.PayPal;

@ReportsCrashes(mailTo = "info@hitasoft.com", formKey = "")
public class FantacyApplication extends Application {

	ImageLoader imageLoader;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;

	
	    private  final int MAX_ATTEMPTS = 5;
	    private  final int BACKOFF_MILLI_SECONDS = 2000;
	    private  final Random random = new Random();
	    
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);

		imageLoader = ImageLoader.getInstance();
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "Milymarket");
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.showImageOnFail(R.drawable.menu_user)
				.showImageForEmptyUri(R.drawable.menu_user)
				.showStubImage(R.drawable.menu_user).resetViewBeforeLoading()
				.cacheOnDisc().build();

		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.memoryCacheSize(41943040).discCacheSize(104857600)
				.threadPoolSize(10)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.build();
		imageLoader.init(config);
		new LoadLib().execute();

	}

	public void initLibrary() {
		PayPal pp = PayPal.getInstance();
		// If the library is already initialized, then we don't need to
		// initialize it again.
		if (pp == null) {
			// This is the main initialization call that takes in your Context,
			// the Application ID, and the server you would like to connect to.
			pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
					PayPal.ENV_SANDBOX);

			// -- These are required settings.
			pp.setLanguage("en_US"); // Sets the language for the library.
			// --

			// -- These are a few of the optional settings.
			// Sets the fees payer. If there are fees for the transaction, this
			// person will pay for them. Possible values are FEEPAYER_SENDER,
			// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and
			// FEEPAYER_SECONDARYONLY.
			pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
			// Set to true if the transaction will require shipping.
			pp.setShippingEnabled(true);
			// Dynamic Amount Calculation allows you to set tax and shipping
			// amounts based on the user's shipping address. Shipping must be
			// enabled for Dynamic Amount Calculation. This also requires you to
			// create a class that implements PaymentAdjuster and Serializable.
			pp.setDynamicAmountCalculationEnabled(false);
			// --
		}
	}

	class LoadLib extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			initLibrary();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.v("Finished", "FInished");
		}

	}
	// Register this account with the server.
    void register(final Context context, String name, String email, final String regId) {
          
        Log.i("Push-Notification", "registering device (regId = " + regId + ")");
         
        String serverUrl = ConstantValues.url+ConstantValues.Push_Notification;
         
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("name", name);
        params.put("email", email);
         
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
         
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
             
            Log.d("Push-Notification", "Attempt #" + i + " to register");
             
       //     try {
                //Send Broadcast to Show message on screen
                /*displayMessageOnScreen(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));*/
                 
                // Post registration values to web server
             //   post(serverUrl, params);
            	post();
                 
                GCMRegistrar.setRegisteredOnServer(context, true);
                 
                //Send Broadcast to Show message on screen
               /* String message = context.getString(R.string.server_registered);
                displayMessageOnScreen(context, message);*/
                 
                return;
            /*} catch (IOException e) {
                 
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                 
                Log.e("Push-Notification", "Failed to register on attempt " + i + ":" + e);
                 
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                     
                    Log.d("Push-Notification", "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                     
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d("Push-Notification", "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                 
                // increase backoff exponentially
                backoff *= 2;
            }*/
        }
         
        /*String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
         
        //Send Broadcast to Show message on screen
        displayMessageOnScreen(context, message);*/
    }
 
	
	   // Unregister this account/device pair within the server.
    void unregister(final Context context, final String regId) {
         
       Log.i("Push-Notification", "unregistering device (regId = " + regId + ")");
        
       String serverUrl = ConstantValues.Push_Notification + "/unregister";
       Map<String, String> params = new HashMap<String, String>();
       params.put("regId", regId);
        
    //   try {
          // post(serverUrl, params);
    	   unpost();
           GCMRegistrar.setRegisteredOnServer(context, false);
          /* String message = context.getString(R.string.server_unregistered);
           displayMessageOnScreen(context, message);*/
     /*  } catch (IOException e) {
            
           // At this point the device is unregistered from GCM, but still
           // registered in the our server.
           // We could try to unregister again, but it is not necessary:
           // if the server tries to send a message to the device, it will get
           // a "NotRegistered" error message and should unregister the device.
            
           String message = context.getString(R.string.server_unregister_error,
                   e.getMessage());
           displayMessageOnScreen(context, message);
       }*/
   }
    
    //duplicate Post method
    private static void post(){
    	
    	
    	
    	String result = null;
    //	String Url=ConstantValues.url+ConstantValues.Push_Notification;
    	String Url="http://milymarket.com/"+ConstantValues.Push_Notification;
    	Log.v("the complete url","the complete url="+Url);
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Url);//(ConstantValues.Push_Notification);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userid", GetSet.getUserId()));
			nameValuePairs.add(new BasicNameValuePair("deviceToken", AppConstant.Register_Id));
			nameValuePairs.add(new BasicNameValuePair("devicetype","1"));
			
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
		
    }
    
    
    //Un POst PUshNotification
 private static void unpost(){
    	
    	
    	
    	String result = null;
    //	String Url=ConstantValues.url+ConstantValues.Push_Notification;
    	String Url="http://milymarket.com/"+ConstantValues.Push_Notification_stop;
    	Log.v("the complete url","the complete url="+Url);
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Url);//(ConstantValues.Push_Notification);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("deviceToken", AppConstant.Register_Id));
			
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
		
    }
    
    
    
	  // Issue a POST request to the server.
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {    
         
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
         
        Log.v("Push-Notification", "Posting '" + body + "' to " + url);
         
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
