package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hitasoft.apps.milymarket.DetailActivity.sendFB;
import com.hitasoft.apps.milymarket.DetailActivity.sendMail;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.hitasoft.apps.milymarket.util.TextViewEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Map extends Activity implements LocationListener {
	LocationManager lm;
	public static GoogleMap map;
	public static ArrayList<HashMap<String, String>> Informationpageitems;
	ArrayList<HashMap<String, String>> arraylist;
	public static ArrayList<HashMap<String, String>> photoslist;
	public static HashMap<String, ArrayList<String>> photosl;
	public static ArrayList<String> orgary;
	HashMap<String, String> userDatas;
	final Context context = this;
	public Marker markernew;
	public static LatLng lastLatLng;
	public static String gotcomment = "current location", key = "YourLocation";
	TextView uname, iaddr1, iaddr2, iweb1, iweb2, cmt1, countcmt, countph,
			photoph, untitle, addphotos, addcomments,phnum,iweb3;
	ImageView uimg, img1, img2;
	ImageLoader imagel;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	LinearLayout cmtlayout, phtlayout, maplayout;
	RelativeLayout profile;
	TextViewEx descp;
	int inc=0;
	String phone = "";
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		imagel = ImageLoader.getInstance();
		
		profile = (RelativeLayout) findViewById(R.id.reltlay);
		profile.setVisibility(View.INVISIBLE);
		uname = (TextView) findViewById(R.id.infousername);
		untitle = (TextView) findViewById(R.id.userNameTitle);
		iaddr1 = (TextView) findViewById(R.id.infoaddr1);
		iaddr2 = (TextView) findViewById(R.id.infoaddr2);
		iweb1 = (TextView) findViewById(R.id.infoweb1);
		iweb2 = (TextView) findViewById(R.id.infoweb2);
		iweb3 = (TextView) findViewById(R.id.infoweb3);
		phnum = (TextView) findViewById(R.id.infophno);
		countcmt = (TextView) findViewById(R.id.countcmt);
		countph = (TextView) findViewById(R.id.countph);
		photoph = (TextView) findViewById(R.id.photo12);
		cmt1 = (TextView) findViewById(R.id.comments1);
		addphotos = (TextView) findViewById(R.id.add_photo);
		addcomments = (TextView) findViewById(R.id.add_comment);
		uimg = (ImageView) findViewById(R.id.infouserimage);
		uimg.setScaleType(ScaleType.FIT_XY);
		img1 = (ImageView) findViewById(R.id.infoimage1);
		img2 = (ImageView) findViewById(R.id.infoimage2);
		cmtlayout = (LinearLayout) findViewById(R.id.comt);
		phtlayout = (LinearLayout) findViewById(R.id.phot);
		maplayout = (LinearLayout) findViewById(R.id.mapl);
		userDatas = new HashMap<String, String>();
		arraylist = new ArrayList<HashMap<String, String>>();
		photoslist = new ArrayList<HashMap<String, String>>();
		photosl = new HashMap<String, ArrayList<String>>();
		orgary = new ArrayList<String>();
		descp = (TextViewEx) findViewById(R.id.infodescription);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		ImageButton back = (ImageButton) findViewById(R.id.smenu);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		map.getUiSettings().setZoomControlsEnabled(false);
		getCurrentLocation();
	
		inc=Comments_list.inc;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				Map.this).build();
		imagel.init(config);
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().build();
		loadData();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FragmentChangeActivity.rsprofile = true;
				FragmentChangeActivity.menumap = true;
		    	FragmentChangeActivity.filter_icon=false;
		        invalidateOptionsMenu();
				startActivity(new Intent(Map.this,FragmentChangeActivity.class));
				//Intent i=new Intent(Map.this,ProfileFragment.class);
				//startActivity(i);
				//Map.this.finish();
			}
		});

		cmtlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(getApplicationContext(),
						Comments_list.class);
				i.putExtra("cmtsarray", arraylist);
				startActivityForResult(i, 500);

			}

		});

		phtlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String uname=userDatas.get(ConstantValues.TAG_NAME_INFO);
				Intent j = new Intent(getApplicationContext(), photosgrid.class);
				j.putExtra("photo", photoslist);
				j.putExtra("uname",uname);
				startActivityForResult(j, 500);

			}

		});
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				Intent i = new Intent(getApplicationContext(),
						Map_fullscreen.class);
				startActivity(i);

			}
		});

		addphotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String uname=userDatas.get(ConstantValues.TAG_NAME_INFO);
				Intent j = new Intent(getApplicationContext(), photosgrid.class);
				j.putExtra("photo", photoslist);
				j.putExtra("uname",uname);
				startActivityForResult(j, 500);

			}

		});
		addcomments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						Comments_list.class);
				i.putExtra("cmtsarray", arraylist);
				startActivityForResult(i, 500);

			}

		});
		phnum.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
			  phone=phnum.getText().toString();
			  call();
				
			}
			
		});
		iweb1.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				String web=iweb1.getText().toString();
				Uri uriUrl = Uri.parse(web);
			    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			    startActivity(launchBrowser);
				
			}
			
		});
		iweb2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				String web=iweb2.getText().toString();
				Uri uriUrl = Uri.parse(web);
			    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			    startActivity(launchBrowser);
				
			}
			
		});
		iweb3.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				String web=iweb3.getText().toString();
				Uri uriUrl = Uri.parse(web);
			    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			    startActivity(launchBrowser);
				
			}
			
		});

	}
	
	public void call() {   
        Intent callIntent = new Intent(Intent.ACTION_DIAL);          
        callIntent.setData(Uri.parse("tel:"+phone));          
        startActivity(callIntent);  
}

	private void loadData() {
		if (isNetworkAvailable(Map.this)) {
			try {
				String uId = ConstantValues.pref.getString("userprefid", "");
				new getInformation().execute(Integer.parseInt(uId));
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	class getInformation extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(Map.this);
		JSONObject jobj;
		JSONArray jary;

		@Override
		protected Void doInBackground(Integer... userid) {
			try {
				parsing(userid[0]);
			} catch (Exception e) {
				Log.v("error in information page", "" + e);
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Chargement...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void unused) {
			if (null != dialog && dialog.isShowing()) {
				untitle.setText(userDatas.get(ConstantValues.TAG_NAME_INFO));
				uname.setText(userDatas.get(ConstantValues.TAG_NAME_INFO));
				photoph.setText("par" + "\t"
						+ userDatas.get(ConstantValues.TAG_NAME_INFO));
				descp.setText(userDatas.get(ConstantValues.TAG_DESC_INFO), true);
				iaddr1.setText(userDatas.get(ConstantValues.TAG_ADDRESS_INFO)
						+ "\n");
				phnum.setText(userDatas.get(ConstantValues.TAG_PHONE_INFO)+"\n");
				iaddr2.setText(userDatas.get(ConstantValues.TAG_OTIME_INFO)+"\n");
			
				iweb1.setText(userDatas.get(ConstantValues.TAG_WEB_INFO) + "\n");
				iweb2.setText(userDatas.get(ConstantValues.TAG_FB_INFO) + "\n");
				iweb3.setText(userDatas.get(ConstantValues.TAG_TW_INFO)+"\n");
				imagel.displayImage(
						userDatas.get(ConstantValues.TAG_SIMG_INFO), uimg);
				Display display = getWindowManager().getDefaultDisplay();
				int swidth = display.getWidth(); 
				int width = Integer.parseInt(userDatas.get(ConstantValues.TAG_WIDTH));
				int height =Integer.parseInt(userDatas.get(ConstantValues.TAG_HEIGHT));
				Log.v("w&h",""+width+"\t"+height);
				if(height>=250&&height<300){
					Log.v("200","200");
					int screenh=display.getHeight()*60/100;
					LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(swidth,screenh);
					uimg.setLayoutParams(parms);
				}
				else if(height>=300){
					Log.v("300","300");
					if(width>=height){
					int screenh=display.getHeight()*60/100;
					LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(swidth,screenh);
					uimg.setLayoutParams(parms);
					}
					else{
						int screenh=display.getHeight()*80/100;
						LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(swidth,screenh);
						uimg.setLayoutParams(parms);
					}
				}
				else if(height<250){
					Log.v("200","200");
					int screenh=display.getHeight()*50/100;
					LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(swidth,screenh);
					uimg.setLayoutParams(parms);
				}
				
				
				cmt1.setText(userDatas.get(ConstantValues.TAG_CMT_INFO));
				imagel.displayImage(
						userDatas.get(ConstantValues.TAG_UIMG_INFO), img1);
				imagel.displayImage(
						userDatas.get(ConstantValues.TAG_UIMG_INFO), img2);
				countcmt.setText("Il y a " + "\t" + userDatas.get("count")
						+ "\t" + "autres message");
				countph.setText("Il y a  " + "\t" + userDatas.get("countp")
						+ "\t" + "autres photos");

				dialog.dismiss();
				profile.setVisibility(View.VISIBLE);
			}

		}
	}

	private void parsing(int userId) {

		String commt = null, countphoto = null, userimg = null;
		String usimg = null, cmts = null;
		JSONParser jParser = new JSONParser();
		String url = ConstantValues.informationpageurl + userId;
		
		JSONObject userData = jParser.getJSONFromUrl(url.trim());
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

					JSONArray shopcmt = jseller
							.getJSONArray(ConstantValues.TAG_SHOP_INFO);
					commt = Integer.toString(shopcmt.length()+inc);

					for (int j = 0; j < shopcmt.length(); j++) {
						JSONObject jshop = shopcmt.getJSONObject(j);
                        String unam=jshop.getString(ConstantValues.TAG_USER_NAME);
						usimg = jshop.getString(ConstantValues.TAG_UIMG_INFO);
						cmts = jshop.getString(ConstantValues.TAG_CMT_INFO);
						HashMap<String, String> hmap = new HashMap<String, String>();
						hmap.put(ConstantValues.TAG_NAME_INFO, unam);
						hmap.put(ConstantValues.TAG_CMT_INFO, cmts);
						hmap.put(ConstantValues.TAG_UIMG_INFO, usimg);

						arraylist.add(hmap);

					}
					JSONArray shopphoto = jseller
							.getJSONArray(ConstantValues.TAG_SPU_INFO);
					countphoto = Integer.toString(shopphoto.length());
					for (int k = 0; k < shopphoto.length(); k++) {
						JSONObject jphoto = shopphoto.getJSONObject(k);

						userimg = jphoto
								.getString(ConstantValues.TAG_USIMG_INFO);
						HashMap<String, String> pmap = new HashMap<String, String>();
						pmap.put(ConstantValues.TAG_USIMG_INFO, userimg);
						orgary.add(userimg);
						photosl.put("images", orgary);
						photoslist.add(pmap);

					}

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
					userDatas.put(ConstantValues.TAG_UIMG_INFO, usimg);
					userDatas.put(ConstantValues.TAG_CMT_INFO, cmts);
					userDatas.put("count", commt);
					userDatas.put("countp", countphoto);

				}

			}

		} catch (Exception e) {
			Log.e("error", "error in information page");

		}

	}
	

	public void getCurrentLocation() {

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, false);
		Location lc = lm.getLastKnownLocation(provider);
		if (lc != null) {
			onLocationChanged(lc);
			lastLatLng = new LatLng(ProfileFragment.lat, ProfileFragment.lon);
			markernew = map.addMarker(new MarkerOptions().position(lastLatLng)
					.title(ProfileFragment.currentshop));

		} else {
			Log.v("lc",""+"lc null");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(ProfileFragment.lat, ProfileFragment.lon))
				.zoom(10).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void onBackPressed() {
		Map.this.finish();
	}

}