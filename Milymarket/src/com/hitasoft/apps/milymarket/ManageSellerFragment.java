package com.hitasoft.apps.milymarket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hitasoft.apps.milymarket.AddProduct.SendProducts;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;

public class ManageSellerFragment extends Activity implements
		OnItemClickListener, OnClickListener, LocationListener {
	LocationManager lm;
	private ImageButton home, near, cart, alert, menu;
	public static HashMap<String, ArrayList<String>> urls = null;
	HashMap<String, String> userDatas;
	ArrayList<HashMap<String, String>> arraylist;
	TextView addr, phnum;
	EditText descp;
	private Spinner sellerType;
	private ToggleButton enablePaiment;
	public static LatLng lastLatLng;
	public Marker markernew;
	private Button  saveButton;
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userDatas = new HashMap<String, String>();
		arraylist = new ArrayList<HashMap<String, String>>();
		setContentView(R.layout.manage_seller);

		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		cart = (ImageButton) findViewById(R.id.btn_cart);
		alert = (ImageButton)findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);
		saveButton = (Button) findViewById(R.id.btn_save);
		home.setImageResource(R.drawable.tab_bar_product_selected);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		
		descp = (EditText) findViewById(R.id.description);
				phnum = (TextView) findViewById(R.id.telephone);
				addr = (TextView) findViewById(R.id.adress);
				sellerType = (Spinner) findViewById(R.id.sellerType);
				enablePaiment = (ToggleButton) findViewById(R.id.enablePaiment); 
				
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		urls = new HashMap<String, ArrayList<String>>();
		loadData();
		lm = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		Location lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//getAdressFromLocatio(lastLocation);
		try{
		getStringFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		LocationManager locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(
		LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


	}
	
	
	private void loadData() {
		if (isNetworkAvailable(ManageSellerFragment.this)) {
			try {
				
				new managerPageLoadImages().execute(0);
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
	}
	
	
	class managerPageLoadImages extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				ManageSellerFragment.this);

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				
				parsing(params[0]);
			} catch (Exception e) {
				Log.v("error", e.toString());
			}
			// Log.v("page", Integer.toString(params[0]));
			return null;
		}
		
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void unused) {
			if (null != dialog && dialog.isShowing()) {
				if(userDatas.get(ConstantValues.TAG_ADDRESS_INFO) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_INFO).isEmpty())
			addr.setText(userDatas.get(ConstantValues.TAG_ADDRESS_INFO));
				if(userDatas.get(ConstantValues.TAG_DESC_INFO) != "null" && !userDatas.get(ConstantValues.TAG_DESC_INFO).isEmpty())
			descp.setText(userDatas.get(ConstantValues.TAG_DESC_INFO));
			phnum.setText(userDatas.get(ConstantValues.TAG_PHONE_INFO));
			sellerType.setSelection(Integer.parseInt(userDatas.get(ConstantValues.TAG_SELLER_TYPE)));
			enablePaiment.setSelected(Boolean.parseBoolean(userDatas.get(ConstantValues.TAG_ENABLE_PAIEMENT)));
			dialog.dismiss();
			}
			
		}
		
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Chargement...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}
	}
	
	public void getUserData(String userId2) {
		// GetSet.setProfileUserId(Integer.toString(Id));

		JSONParser jParser = new JSONParser();
		String url = ConstantValues.userProfile + userId2;
		Log.v("loadingurl", url);
		JSONObject userData = jParser.getJSONFromUrl(url);
		try {
			String response = userData.getString(ConstantValues.status);
			if (response.equalsIgnoreCase("true")) {
				JSONObject results = userData
						.getJSONObject(ConstantValues.TAG_RESULT);
				//Log.v("results", "" + results);
				String userId = results
						.getString(ConstantValues.TAG_PROFILE_USERID);
				//Log.v("userId", userId);
				String userName = results
						.getString(ConstantValues.TAG_PROFILE_USERNAME);
				//Log.v("userName", userName);
				String fullName = results
						.getString(ConstantValues.TAG_PROFILE_FULLNAME);
				//Log.v("fullName", fullName);
				String about = results
						.getString(ConstantValues.TAG_PROFILE_ABOUT);
				userDatas.put(ConstantValues.TAG_PROFILE_USERID, userId);
				userDatas.put(ConstantValues.TAG_PROFILE_USERNAME, userName);
				//Log.v("frommap",
				//		"" + userDatas.get(ConstantValues.TAG_PROFILE_USERNAME));
				userDatas.put(ConstantValues.TAG_PROFILE_FULLNAME, fullName);
				userDatas.put(ConstantValues.TAG_PROFILE_ABOUT, about);

			} else {
				String message = userData.getString(ConstantValues.msg);
				Toast.makeText(ManageSellerFragment.this, message, Toast.LENGTH_LONG)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	private void parsing(Integer page) {

		String userId = GetSet.getUserId();
		String urlAddr = ConstantValues.informationpageurl + userId;
		JSONParser jParser = new JSONParser();
		JSONObject userData = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = userData
					.getString(ConstantValues.TAG_STATUS_INFO);
			if (response.equalsIgnoreCase("true")) {
				JSONObject results = userData
						.getJSONObject(ConstantValues.TAG_RESULT_INFO);
				JSONArray seller = results.getJSONArray("sellers");

				for (int i = 0; i < seller.length(); i++) {
					JSONObject jseller = seller.getJSONObject(i);
					String nam = jseller
							.getString(ConstantValues.TAG_NAME_INFO);
					String des = jseller
							.getString(ConstantValues.TAG_DESC_INFO);
					String adr = jseller
							.getString(ConstantValues.TAG_ADDRESS_INFO);
					String phn = jseller
							.getString(ConstantValues.TAG_PHONE_INFO);
					String otime = jseller
							.getString(ConstantValues.TAG_OTIME_INFO);
					String fbac = jseller.getString(ConstantValues.TAG_FB_INFO);
					String twac = jseller.getString(ConstantValues.TAG_TW_INFO);
					String web = jseller.getString(ConstantValues.TAG_WEB_INFO);
					String simg = jseller
							.getString(ConstantValues.TAG_SIMG_INFO);
					String height=jseller.getString(ConstantValues.TAG_HEIGHT);
					String width=jseller.getString(ConstantValues.TAG_WIDTH);
					String lat = jseller.getString(ConstantValues.TAG_LAT_INFO);
					String lon = jseller.getString(ConstantValues.TAG_LON_INFO);
					String sellerType = jseller.getString(ConstantValues.TAG_SELLER_TYPE);
					String enablePaiment = jseller.getString(ConstantValues.TAG_ENABLE_PAIEMENT);
					

					userDatas.put(ConstantValues.TAG_NAME_INFO, nam);
					userDatas.put(ConstantValues.TAG_DESC_INFO, des);
					userDatas.put(ConstantValues.TAG_ADDRESS_INFO, adr);
					userDatas.put(ConstantValues.TAG_PHONE_INFO, phn);
					userDatas.put(ConstantValues.TAG_OTIME_INFO, otime);
					userDatas.put(ConstantValues.TAG_FB_INFO, fbac);
					userDatas.put(ConstantValues.TAG_TW_INFO, twac);
					userDatas.put(ConstantValues.TAG_WEB_INFO, web);
					userDatas.put(ConstantValues.TAG_SIMG_INFO, simg);
					userDatas.put(ConstantValues.TAG_HEIGHT, height);
					userDatas.put(ConstantValues.TAG_WIDTH, width);
					userDatas.put(ConstantValues.TAG_SELLER_TYPE, sellerType);
					userDatas.put(ConstantValues.TAG_ENABLE_PAIEMENT, enablePaiment);
					userDatas.put(ConstantValues.TAG_LAT_INFO, lat);
					userDatas.put(ConstantValues.TAG_LON_INFO, lon);
				}
				
				getUserData(userId);

			}

		} catch (Exception e) {
			Log.e("error", "error in information page");

		}
	}
	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					Log.i("Class", info[i].getState().toString());
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_save:
				SendPorValues();
			break;
		case R.id.btn_home:
			FragmentChangeActivity.rshome = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_near:
			FragmentChangeActivity.rsnear = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_cart:
			FragmentChangeActivity.rscart = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_menu:
			FragmentChangeActivity.rsmenu = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			/*
			 * Intent i = new Intent(DetailActivity.this,
			 * ProfileTabHolder.class); i.putExtra("userId",
			 * GetSet.getUserId()); startActivity(i);
			 */

			// fca.switchContent(new ProfileFragment());
			break;

		}
	}
	
	
	
	
	
	/*---------- Listener class to get coordinates ------------- */
	private class MyLocationListener implements LocationListener {

	    @Override
	    public void onLocationChanged(Location loc) {
	        addr.setText("");
	        //pb.setVisibility(View.INVISIBLE);
	        Toast.makeText(
	                getBaseContext(),
	                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
	                    + loc.getLongitude(), Toast.LENGTH_SHORT).show();
	        String longitude = "Longitude: " + loc.getLongitude();
	        //Log.v(TAG, longitude);
	        String latitude = "Latitude: " + loc.getLatitude();
	        //Log.v(TAG, latitude);

	        /*------- To get city name from coordinates -------- */
	        String cityName = null;
	        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
	        List<Address> addresses;
	        try {
	            addresses = gcd.getFromLocation(loc.getLatitude(),
	                    loc.getLongitude(), 1);
	            if (addresses.size() > 0)
	                System.out.println(addresses.get(0).getLocality());
	            cityName = addresses.get(0).getLocality();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
	            + cityName;
	        addr.setText(s);
	    }

	    public void onProviderDisabled(String provider) {}

	    public void onProviderEnabled(String provider) {}

	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	}

	
	
	private void getAdressFromLocatio(Location loc){
		try {

	        Geocoder geo = new Geocoder(ManageSellerFragment.this.getApplicationContext(), Locale.getDefault());
	        List<Address> addresses = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
	        if (addresses.isEmpty()) {
	        	Log.w("Wait","Waiting for Location");
	        }
	        else {
	            if (addresses.size() > 0) {
	            	if(addr !=null)
	                addr.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
	                //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
	            }
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace(); // getFromLocation() may sometimes fail
	    }
	}
	
	
	
	
	
	public List<Address> getStringFromLocation(double lat, double lng)
	        throws ClientProtocolException, IOException, JSONException {

	    String address = String
	            .format(Locale.ENGLISH,                                 "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
	                            + Locale.getDefault().getCountry(), lat, lng);
	    HttpGet httpGet = new HttpGet(address);
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    List<Address> retList = null;

	    response = client.execute(httpGet);
	    HttpEntity entity = response.getEntity();
	    InputStream stream = entity.getContent();
	    int b;
	    while ((b = stream.read()) != -1) {
	        stringBuilder.append((char) b);
	    }

	    JSONObject jsonObject = new JSONObject();
	    jsonObject = new JSONObject(stringBuilder.toString());

	    retList = new ArrayList<Address>();

	    if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
	        JSONArray results = jsonObject.getJSONArray("results");
	        for (int i = 0; i < results.length(); i++) {
	            JSONObject result = results.getJSONObject(i);
	            String form = result.getString("types");
	            String temp = "[\"neighborhood\",\"political\"]";
	            if(form.equals(temp)){
	            String indiStr = result.getString("formatted_address");
	            Address adr = new Address(Locale.getDefault());
	            adr.setAddressLine(0, indiStr);
	            retList.add(adr);
	            addr.setText(indiStr);
	            }
	        }
	    }

	    return retList;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/*public void getCurrentLocation() {

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, false);
		Location lc = lm.getLastKnownLocation(provider);
		
		if (lc != null) {
			onLocationChanged(lc);
			lastLatLng = new LatLng(ProfileFragment.lat, ProfileFragment.lon);

			try {

		        Geocoder geo = new Geocoder(ManageSellerFragment.this.getApplicationContext(), Locale.getDefault());
		        List<Address> addresses = geo.getFromLocation(lastLatLng.latitude, lastLatLng.longitude, 1);
		        if (addresses.isEmpty()) {
		        	Log.w("Wait","Waiting for Location");
		        }
		        else {
		            if (addresses.size() > 0) {
		            	if(addr !=null)
		                addr.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
		                //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
		            }
		        }
		    }
		    catch (Exception e) {
		        e.printStackTrace(); // getFromLocation() may sometimes fail
		    }

			
		} else {
			Log.v("lc",""+"lc null");
		}
	}*/


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void SendPorValues() {
		
			try {
				new SellerSignUp().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	class SellerSignUp extends AsyncTask<Void, Void, String> {

		ProgressDialog dialog = new ProgressDialog(
				ManageSellerFragment.this);

		@Override
		protected String doInBackground(Void... arg0) {
			String result = postData();
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("Un instant s'il vous plait...");
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
					Toast.makeText(ManageSellerFragment.this, "Félicitations!!! votre compte vendeur a été mis à jour",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ManageSellerFragment.this, "Désolé nous n'avons pas pu mettre à jour votre compte vendeur. Veuillez ré essayer ",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public String postData() {
		String result = null;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ConstantValues.sellerSignup);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					12);
			nameValuePairs.add(new BasicNameValuePair("userid", GetSet
					.getUserId()));
			nameValuePairs.add(new BasicNameValuePair("merchant_name", userDatas.get(ConstantValues.TAG_NAME_INFO)));
			nameValuePairs.add(new BasicNameValuePair("brand_name", userDatas.get(ConstantValues.TAG_PROFILE_USERNAME)));
			nameValuePairs.add(new BasicNameValuePair("person_phone_number", phnum.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("address", addr.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("seller_desc", descp.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("paypalId", "service@milymarket.com"));
			nameValuePairs.add(new BasicNameValuePair("seller_website", "http://milymarket.com"));
			nameValuePairs.add(new BasicNameValuePair("latitude",userDatas.get(ConstantValues.TAG_LAT_INFO)));
			nameValuePairs.add(new BasicNameValuePair("longitude", userDatas.get(ConstantValues.TAG_LON_INFO)));
			nameValuePairs.add(new BasicNameValuePair("sellertype", ""+sellerType.getSelectedItemPosition()));
			int isenablepaiment = 0;
			if(enablePaiment.isChecked())
				isenablepaiment = 0;
			else
				isenablepaiment = 1;
			nameValuePairs.add(new BasicNameValuePair("enablepaiement", ""+isenablepaiment));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.v("result",""+EntityUtils.toString(response.getEntity()));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
