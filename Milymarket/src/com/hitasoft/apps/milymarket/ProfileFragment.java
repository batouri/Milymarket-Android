package com.hitasoft.apps.milymarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.etsy.android.grid.StaggeredGridView;
import com.hitasoft.apps.milymarket.R;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.DBController;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.ImageData;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("UseSparseArrays")
public class ProfileFragment extends SherlockFragment implements
		OnClickListener, OnItemClickListener {

	// ArrayList<HashMap<String, String>> SimilarUsers = new
	// ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> HomePageItems;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	public static boolean menumap = false;
	public static boolean filter_icon = false;
	Boolean cond;
	// private Fragment mContent
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
	HashMap<String, String> userDatas;
	public static double lat, lon;
	private static int layout = 0;
	RelativeLayout LoagingLayout;
	String u1name, u1add, u1img;
	TextView centerHome;
	LinearLayout bottomLoading,followlay;
	AlertDialog adialog;
	private int currentPage = 0;
	static AdapterForHdpi hdpiAdapter;
	ImageView userImage;
	ImageButton option;
	String name,latitude,longitude;
	public static boolean sellerfollow, sellerunfollow;
	ArrayList<String> map = null;
	int i, j, v = 0, length = 0;
	public static String currentshop;
	TextView userName, userId, followingCount, followersCount, 
			addedcount, about,followtxt;
	private StaggeredGridView gridView;
	RelativeLayout profile;
	LinearLayout followers, following, added, mainLayout;
	private static ImageLoader profileLoader;
	
	// ListView lv;
	// int value = getIntent().getExtras().getInt("bucketno");
	// CustomListViewAdapter adapter;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	private ImageButton home, near, shop, alert, menu;
	ArrayList<HashMap<String, String>> datas = null;
	ArrayList<HashMap<String, String>> datas2 = null;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;
	String sellerid;


	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		profileLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).build();
		profileLoader.init(config);
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
				.build();
         //setHasOptionsMenu(false);
		//setHasOptionsMenu(true);
		
		//menumap = true;
		
		// mContent = new HomeFragment();
		// setContentView(R.layout.content_frame);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profile_view_main_page,
				container, false);
		return v;
	}

	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	

		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);
		//menu.setImageResource(R.drawable.tab_bar_profile_selected);
	

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		
	//	FragmentChangeActivity.menumap = true;
	//	FragmentChangeActivity.filter_icon = false;
	//	getActivity().supportInvalidateOptionsMenu();
		// RelativeLayout tabrelative = (RelativeLayout) getView().findViewById(
		// R.id.tabrelative);
		// mcontext=(Fragment)getView().findViewById(R.id.profile_gridfragment);
		bottomLoading = (LinearLayout) getView().findViewById(R.id.bottomhome);
		bottomLoading.setVisibility(View.VISIBLE);
		mainLayout = (LinearLayout) getView().findViewById(R.id.profile_view);
		mainLayout.setVisibility(View.INVISIBLE);
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		followlay = (LinearLayout) getView().findViewById(R.id.followlay);
		followtxt = (TextView) getView().findViewById(R.id.followtxt);
		LoagingLayout = (RelativeLayout) (getView()
				.findViewById(R.id.loadImageLayout));
		centerHome = (TextView) getView().findViewById(R.id.homenulltext2);
		centerHome.setVisibility(View.INVISIBLE);
		
		/*
		 * ConstantValues.pref
		 * =getActivity().getSharedPreferences("fantacyid",0);
		 * //ConstantValues.pref =
		 * getActivity().getSharedPreferences("BlueDot",); ConstantValues.editor
		 * =ConstantValues.pref.edit();
		 */
		adialog=new AlertDialog.Builder(ProfileFragment.this.getActivity()).create();
		adialog.setTitle("Message");
		adialog.setMessage("Connectez vous pour continuer");
		adialog.setButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("alert","alert");
				Intent i = new Intent(ProfileFragment.this.getActivity(),
						LoginActivity.class);
				startActivityForResult(i, 1);
				
			}
		});
		
		
		
		gridView = (StaggeredGridView) (getView()
				.findViewById(R.id.homePage_gallery));
		gridView.setSmoothScrollbarEnabled(true);
	//	gridView.setExpanded(true);
		gridView.setOnItemClickListener(this);
		((FragmentChangeActivity) getActivity())
				.getSupportActionBar()
				.setTitle(getActivity().getResources().getString(R.string.home));
		setColumns();
		
		
		
		//profile = (RelativeLayout) getView().findViewById(R.id.profilePage);
		//profile.setVisibility(View.INVISIBLE);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		// lv = (ListView) getView().findViewById(R.id.similar_user_list);
		followers = (LinearLayout) getView().findViewById(R.id.followersLayout);
		following = (LinearLayout) getView().findViewById(R.id.followingLayout);
		//favourated = (LinearLayout) getView().findViewById(R.id.itemfavourated);
		added = (LinearLayout) getView().findViewById(R.id.itemadded);
		followers.setOnClickListener(this);
		//favourated.setOnClickListener(this);
		following.setOnClickListener(this);
		added.setOnClickListener(this);
		userImage = (ImageView) getView().findViewById(R.id.profilePic);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				screenWidth / 4, screenWidth / 4);
		lp.setMargins(0, 20, 0, 0);
		userImage.setLayoutParams(lp);
		userName = (TextView) getView().findViewById(R.id.profile_userName);
		userId = (TextView) getView().findViewById(R.id.profile_userId);
		followingCount = (TextView) getView().findViewById(
				R.id.prifilePage_following_count);
		followersCount = (TextView) getView().findViewById(
				R.id.prifilePage_followers_count);
		//favouratedcount = (TextView) getView().findViewById(
				//R.id.prifilePage_favourated_count);
		about = (TextView) getView().findViewById(R.id.about);
		addedcount = (TextView) getView().findViewById(
				R.id.prifilePage_added_count);
		followingCount.setOnClickListener(this);
		followersCount.setOnClickListener(this);
		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.Profile);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);
		sellerid=ConstantValues.pref.getString("userprefid", "");
		if(GetSet.isLogged()==true){
			if (GetSet.getUserId().equals(sellerid)) {
				followlay.setVisibility(View.INVISIBLE);
			}
			else{
			new GetFollowUserID().execute();
			}
		}
		else{
			
			followlay.setVisibility(View.INVISIBLE);
			followtxt.setText("S'abonner");
		}


		//menumap = true;
		// setColumns();
		// if (isNetworkAvailable(getActivity())) {

		/*
		 * option = (ImageButton) getView().findViewById(R.id.profileOption);
		 * option.setOnClickListener(this);
		 */
	
			// FragmentChangeActivity fca = (FragmentChangeActivity)
			// getActivity();
			// getActivity().supportInvalidateOptionsMenu();
			// fca.switchContent(new HomeFragment());
			/*
			 * userName.setText("Login");
			 * Log.v("setname",""+userDatas.get("Login"));
			 * userId.setText(userDatas.get("Login")); //
			 * userDatas.get(ConstantValues.TAG_PROFILE_USERNAME));
			 * followersCount.setText("0"); followingCount.setText("0");
			 * favouratedcount.setText("0"); addedcount.setText("0");
			 * about.setText("login"); Log.v("setabout", "login");
			 */
			// getActivity().finish();
	
			try {
				// if (SimilarUsers.size() == 0) {

				//profile.setVisibility(View.INVISIBLE);
				String uId = ConstantValues.pref.getString("userprefid", "");
				Log.v("uId", "uuuuuuu" + uId);

				//new UpdateUserData().execute(Integer.parseInt(uId));
				setColumns();
				loadData();

			} catch (Exception e) {

			}

			
			followlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(GetSet.isLogged()==true){
					if (followtxt.getText().toString().equals("S'abonner")
							&& sellerfollow == true) {
						sellerfollow = false;
						follow(sellerid);
						followlay.setBackgroundColor(Color.GRAY);
						followtxt.setText("Se désabonner");
					} else if (followtxt.getText().toString().equals("Se désabonner")
							&& sellerunfollow == true) {
						sellerunfollow = false;
						unfollow(sellerid);
						followlay.setBackgroundColor(Color.RED);
						followtxt.setText("S'abonner");
					} else {
						Log.v("error at follow", "");
					}
					}
					else{
						adialog.show();
					}


				}
			});
		

			
		
		/*
		 * adapter = new
		 * CustomListViewAdapter(ProfileFragment.this.getActivity(),
		 * R.layout.similar_user_list_item, SimilarUsers);
		 * lv.setAdapter(adapter); lv.setOnItemClickListener(this);
		 */

	}

	private void setColumns() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		if (screenWidth < 280
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			// gridView.setNumColumns(1);//1
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			// gridView.setNumColumns(1);//1
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			// gridView.setNumColumns(1);//1
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 900
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 700
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 1000
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 800
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
			if (!tabletSize) {
				// gridView.setNumColumns(1);//1
			}
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 1400
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else {
			// gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		}
		// setGridViewItems();
	}

	private void loadData() {
		HomePageItems = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(getActivity())) {
			try {
				String uId = ConstantValues.pref.getString("userprefid", "");
				new homePageLoadImages().execute(Integer.parseInt(uId));
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
		setAdapter();

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

	@SuppressWarnings("deprecation")
	private void setAdapter() {

		try {

			hdpiAdapter = new AdapterForHdpi(getActivity());

			int i=HomePageItems.size();
			Log.v("i",""+i);
			Display display1 = getActivity().getWindowManager().getDefaultDisplay();
			//Point size= new Point();
			//display1.getRealSize(size);
			//int xx=size.x;
			///int yy=size.y;
			//Log.v("xx & YY",""+xx+"\t"+yy);
			int width = display1.getWidth()*97/100; 
			int height=display1.getHeight();
			Log.v("weight",""+width);
			Log.v("height",""+height);
			//Toast.makeText(getActivity(), height, Toast.LENGTH_SHORT).show();
			int height2=0;
			if(height>=1900){
				Log.v("1900","1900");
				height2=i*350;
				LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height2);
			    gridView.setLayoutParams(parms); 
			}
			else if(height>=1500&&height<1900){
				Log.v("1500","1500");
				height2=i*330;
				LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height2);
			    gridView.setLayoutParams(parms);
			}
			else if(height>=1000&&height<1500){
				Log.v("1000","1000");
				height2=i*300;
				LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height2);
			    gridView.setLayoutParams(parms);
			}
			else if(height>=900&&height<1000){
			Log.v("900","900");
			height2=i*220;
			LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height2);
		    gridView.setLayoutParams(parms);
			}
			else if(height<900){
				Log.v("800","800");
				height2=i*200;
				LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height2);
			    gridView.setLayoutParams(parms);
			}
			gridView.setAdapter(hdpiAdapter);
		} catch (Exception e) {

		}

	}
	class GetFollowUserID extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			JSONArray items;

			String urlAddr = ConstantValues.FollowUserIDs;
			if (GetSet.getUserId() != null) {
				urlAddr = urlAddr + "?userId=" + GetSet.getUserId();
			}
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
			try {
				map = new ArrayList<String>();
				String response = json.getString("status");
				if (response.equalsIgnoreCase("true")) {
					JSONObject result = json.getJSONObject("result");
					items = result.getJSONArray("following");
					for (int i = 0; i < items.length(); i++) {

						JSONObject temp = items.getJSONObject(i);
						String userid = temp.getString("userId");
						map.add(userid);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return map;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			try {
				if (result.size() != 0) {
					outerloop: for (int i = 0; i < result.size(); i++) {
						String fuserid = result.get(i);
						if (fuserid.equals(sellerid)) {
							followtxt.setText("Se désabonner");
							followlay.setBackgroundColor(Color.GRAY);
							sellerunfollow = true;
							break outerloop;

						} else {
							followtxt.setText("S'abonner");
							followlay.setBackgroundColor(Color.RED);
							sellerfollow = true;

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	class UpdateUserData extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				ProfileFragment.this.getActivity());

		@Override
		protected Void doInBackground(Integer... arg0) {
			try {
				Log.v("arg0", "" + arg0[0]);
				parsing(arg0[0]);
				getUserData(arg0[0]);
				

				// LinearLayout profilegrid=
				// (LinearLayout)getView().findViewById(R.id.profilehome);

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			LoagingLayout.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				bottomLoading.setVisibility(View.VISIBLE);
				mainLayout.setVisibility(View.INVISIBLE);
			}

			/*dialog.setMessage("Comparaison données...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setIndeterminate(true);
			dialog.show();*/
		}

		@Override
		protected void onPostExecute(Void result) {

			if (dialog != null && dialog.isShowing()) {
				try {
					setAdapter();
					setUserValues();
					/*if(latitude.equalsIgnoreCase("null")&&longitude.equalsIgnoreCase("null")){
						Log.v("in if",""+"true");
						FragmentChangeActivity.menumap = false;
						FragmentChangeActivity.filter_icon = false;
						getActivity().supportInvalidateOptionsMenu();
						
						//setHasOptionsMenu(false);
						
					}
					
					else{
						Log.v("in if",""+"false");
						FragmentChangeActivity.menumap = true;
						FragmentChangeActivity.filter_icon = false;
						getActivity().supportInvalidateOptionsMenu();
					}*/
					
					
					
					dialog.dismiss();
					//profile.setVisibility(View.VISIBLE);
					bottomLoading.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
					hdpiAdapter.notifyDataSetChanged();
					LoagingLayout.setVisibility(View.GONE);
				} catch (Exception e) {
					Log.v("error",""+e.getMessage());
					dialog.dismiss();
				}
			}
			Log.v("reached", "reacjed");
		/*	if(GetSet.isLogged()==true){
				
				if (GetSet.getUserId().equals(sellerid)) {
					followlay.setVisibility(View.INVISIBLE);
				}
				}
				else{
					
					followlay.setVisibility(View.VISIBLE);
					followtxt.setText("Follow");
				}
			*/
				
			

			// adapter.notifyDataSetChanged();
			// ListUtility.setListViewHeightBasedOnChildren(lv);
			// LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
			// adapter.getCount() * lv.getMeasuredHeight());
			// lv.setLayoutParams(lp);
			int colorBlack = getResources().getColor(R.color.black);
			String text = name;
			SpannableString spannable = new SpannableString(text);
			spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
					text.length(), 0);

			((FragmentChangeActivity) getActivity()).getSupportActionBar()
					.setTitle(spannable);
		}
	}

	public void getUserData(int Id) {
		// GetSet.setProfileUserId(Integer.toString(Id));

		userDatas = new HashMap<String, String>();
		JSONParser jParser = new JSONParser();
		String url = ConstantValues.userProfile + Id;
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
				sellerid=userId;
				//Log.v("userId", userId);
				String userName = results
						.getString(ConstantValues.TAG_PROFILE_USERNAME);
				//Log.v("userName", userName);
				String fullName = results
						.getString(ConstantValues.TAG_PROFILE_FULLNAME);
				//Log.v("fullName", fullName);
				String about = results
						.getString(ConstantValues.TAG_PROFILE_ABOUT);
				//Log.v("about", about);
				latitude = results
						.getString(ConstantValues.TAG_PROFILE_LAT);
				//Log.v("latitude", latitude);
				longitude = results
						.getString(ConstantValues.TAG_PROFILE_LON);
				name = userName;
				//Log.v("longitude", longitude);
				/*
				 * if(latitude != null && longitude !=null) { lat=
				 * Double.parseDouble(latitude); lon=
				 * Double.parseDouble(longitude); Log.v("lat",""+lat ); }
				 */
				userDatas.put(ConstantValues.TAG_PROFILE_USERID, userId);
				userDatas.put(ConstantValues.TAG_PROFILE_USERNAME, userName);
				//Log.v("frommap",
				//		"" + userDatas.get(ConstantValues.TAG_PROFILE_USERNAME));
				userDatas.put(ConstantValues.TAG_PROFILE_FULLNAME, fullName);
				userDatas.put(ConstantValues.TAG_PROFILE_ABOUT, about);

				JSONObject images = results.getJSONObject("imageName");
				String imageName70 = images.getString("thumb70");
				String imageName150 = images.getString("thumb150");
				String following = results
						.getString(ConstantValues.TAG_PROFILE_FOLLOWING);
				String followers = results
						.getString(ConstantValues.TAG_PROFILE_FOLLOWERS);
				//String favourates = results
				//		.getString(ConstantValues.TAG_PROFILE_FAVOURATED);
				String added = results
						.getString(ConstantValues.TAG_PROFILE_ADDED);
				String shop_name = results.getString("shop_address");
				//Log.v("shop_name", shop_name);
				userDatas.put("SHOPADDRESS", shop_name);

				userDatas.put("imageName70", imageName70);
				userDatas.put("imageName150", imageName150);
				userDatas.put(ConstantValues.TAG_PROFILE_FOLLOWING, following);
				//userDatas
				//		.put(ConstantValues.TAG_PROFILE_FAVOURATED, favourates);
				userDatas.put(ConstantValues.TAG_PROFILE_ADDED, added);
				userDatas.put(ConstantValues.TAG_PROFILE_FOLLOWERS, followers);

				/*
				 * JSONArray similarUsers = results
				 * .getJSONArray(ConstantValues.TAG_PROFILE_SIMILARUSER);
				 */
				/*
				 * for (int i = 0; i < items.length(); i++) { map = new
				 * HashMap<String, String>(); String item_url_main_70 = null,
				 * user_url_main_350=null,item_url_main_350 = null,
				 * item_url_main_original = null; String comment_id = null,
				 * comment = null, user_id = null, user_img = null, username =
				 * null; JSONObject temp = items.getJSONObject(i); String id =
				 * temp.getString(ConstantValues.TAG_ID); String item_title =
				 * temp .getString(ConstantValues.TAG_TITLE); String
				 * item_description = temp .getString(ConstantValues.TAG_DESC);
				 * 
				 * String price = temp.getString(ConstantValues.TAG_PRICE);
				 * String quantity = temp
				 * .getString(ConstantValues.TAG_QUANTITY); String favorites =
				 * temp .getString(ConstantValues.TAG_FAVORITES); String liked =
				 * temp.getString(ConstantValues.TAG_LIKED); String seller =
				 * temp.getString(ConstantValues.TAG_SELLER); String shopname =
				 * temp.getString(ConstantValues.TAG_SHOP); //u1name = seller;
				 * //u1add = shopname;
				 * 
				 * Log.v("u1img",""+u1img); Log.v("uname",""+u1name); JSONArray
				 * photos = temp .getJSONArray(ConstantValues.TAG_PHOTOS); for
				 * (int j = 0; j < 1; j++) { JSONObject photosTemp =
				 * photos.getJSONObject(j); item_url_main_70 = photosTemp
				 * .getString(ConstantValues.TAG_ITEM_URL_MAIN_70);
				 * item_url_main_350 = photosTemp
				 * .getString(ConstantValues.TAG_ITEM_URL_MAIN_350);
				 * user_url_main_350 = photosTemp
				 * .getString(ConstantValues.TAG_USER_URL_MAIN_350);
				 * item_url_main_original = photosTemp
				 * .getString(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL); }
				 */
				/*
				 * for (int i = 0; i < similarUsers.length(); i++) {
				 * HashMap<String, String> similarUser = new HashMap<String,
				 * String>(); JSONObject similarUserDetails = similarUsers
				 * .getJSONObject(i); String similarUserId = similarUserDetails
				 * .getString(ConstantValues.TAG_PROFILE_USERID); String
				 * similaruserName = similarUserDetails
				 * .getString(ConstantValues.TAG_PROFILE_USERNAME); String
				 * similarfirstName = similarUserDetails
				 * .getString(ConstantValues.TAG_PROFILE_FIRSTNAME); String
				 * similarprofileImage = similarUserDetails
				 * .getString(ConstantValues.TAG_PROFILE_PROFILEIMAGE); String
				 * similarabout = similarUserDetails
				 * .getString(ConstantValues.TAG_PROFILE_ABOUT);
				 * similarUser.put(ConstantValues.TAG_PROFILE_USERID,
				 * similarUserId);
				 * similarUser.put(ConstantValues.TAG_PROFILE_USERNAME,
				 * similaruserName);
				 * similarUser.put(ConstantValues.TAG_PROFILE_FIRSTNAME,
				 * similarfirstName);
				 * similarUser.put(ConstantValues.TAG_PROFILE_PROFILEIMAGE,
				 * similarprofileImage);
				 * similarUser.put(ConstantValues.TAG_PROFILE_ABOUT,
				 * similarabout); SimilarUsers.add(similarUser);
				 * Log.v("similar user", similarUser.toString());
				 */
				// }
				currentshop = shop_name;
				lat = Double.parseDouble(latitude);
				lon = Double.parseDouble(longitude);
				Log.v("lat", "" + lat);
				

			} else {
				String message = userData.getString(ConstantValues.msg);
				Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void setUserValues() {
		profileLoader.displayImage(userDatas.get("imageName150"), userImage,
				defaultOptions);
		//Log.v("frommapin method",
		//		"" + userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));

		userName.setText(userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));
		//Log.v("setname",
		//		"" + userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));
		userId.setText(userDatas.get("SHOPADDRESS"));
		// userDatas.get(ConstantValues.TAG_PROFILE_USERNAME));
		followersCount.setText(userDatas
				.get(ConstantValues.TAG_PROFILE_FOLLOWERS));
		followingCount.setText(userDatas
				.get(ConstantValues.TAG_PROFILE_FOLLOWING));
		//favouratedcount.setText(userDatas
		//		.get(ConstantValues.TAG_PROFILE_FAVOURATED));
		addedcount.setText(userDatas.get(ConstantValues.TAG_PROFILE_ADDED));
		about.setText(userDatas.get(ConstantValues.TAG_PROFILE_ABOUT));
		//Log.v("setabout", "" + userDatas.get(ConstantValues.TAG_PROFILE_ABOUT));

	}

	class homePageLoadImages extends AsyncTask<Integer, Void, Void> {

		ProgressDialog dialog = new ProgressDialog(
				ProfileFragment.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				// parsing(params[0]);

				parsing(params[0]);
				getUserData(params[0]);
			} catch (Exception e) {
				Log.v("error", e.toString());
			}
			Log.v("page", Integer.toString(params[0]));
			return null;
		}

		@Override
		protected void onPreExecute() {
			LoagingLayout.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				bottomLoading.setVisibility(View.VISIBLE);
				mainLayout.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (currentPage == 0) {
					bottomLoading.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
				}
				if (HomePageItems.size() == 0) {
					centerHome.setVisibility(View.VISIBLE);
					// exit();
				}
				setUserValues();
				setAdapter();
				if(latitude.equalsIgnoreCase("null")&&longitude.equalsIgnoreCase("null")){
					Log.v("in if",""+"true");
					FragmentChangeActivity.menumap = false;
					FragmentChangeActivity.filter_icon = false;
					getActivity().supportInvalidateOptionsMenu();
					
					//setHasOptionsMenu(false);
					
				}
				
				else{
					Log.v("in if",""+"false");
					FragmentChangeActivity.menumap = false;
					FragmentChangeActivity.filter_icon = false;
					getActivity().supportInvalidateOptionsMenu();
				}
				
				
				
				dialog.dismiss();
				//profile.setVisibility(View.VISIBLE);
				bottomLoading.setVisibility(View.GONE);
				mainLayout.setVisibility(View.VISIBLE);
				hdpiAdapter.notifyDataSetChanged();
				LoagingLayout.setVisibility(View.GONE);
			
		         Log.v("reached", "reacjed");
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
				
				int colorBlack = getResources().getColor(R.color.black);
				String text = name;
				SpannableString spannable = new SpannableString(text);
				spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
						text.length(), 0);

				((FragmentChangeActivity) getActivity()).getSupportActionBar()
						.setTitle(spannable);
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}
			// updateDb(HomePageItems);

		}
	}

	private void parsing(int page) {
		Log.v("1st",""+"1st");

		JSONArray items;
		HashMap<String, String> map;
		//String url = ConstantValues.profile_imageloader;
		// urlAddr=ConstantValues.home;
		String urlAddr = ConstantValues.home + "?userAdded=" + page;
		
			//String urlAddr = url + "?userId=" + page;
			Log.v("user_userid", "" + page);
		
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		Log.v("given url", "" + urlAddr);
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				/*
				 * String type = result.getString("type"); ProfileFragment.type
				 * = type;
				 */
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null,user_url_main_70 = null, user_url_main_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, fid = null, fimg = null,height=null,width=null;
					JSONObject temp = items.getJSONObject(i);
					String id = temp.getString(ConstantValues.TAG_ID);
					String item_title = temp
							.getString(ConstantValues.TAG_TITLE);
					String item_description = temp
							.getString(ConstantValues.TAG_DESC);

					String price = temp.getString(ConstantValues.TAG_PRICE);
					String quantity = temp
							.getString(ConstantValues.TAG_QUANTITY);
					String sellerid = temp
							.getString(ConstantValues.TAG_PROFILE_USERID);

					String favorites = temp
							.getString(ConstantValues.TAG_FAVORITES);
					String fash_count = temp
							.getString(ConstantValues.TAG_FASHIONCOUNT);
					String liked = temp.getString(ConstantValues.TAG_LIKED);
					String seller = temp.getString(ConstantValues.TAG_SELLER);
					String shopname = temp.getString(ConstantValues.TAG_SHOP);
					// u1name = seller;
					// u1add = shopname;

					Log.v("shopname", "" + shopname);
					Log.v("uname", "" + seller);
					ArrayList<String> originalarray = new ArrayList<String>();
					JSONArray photos = temp
							.getJSONArray(ConstantValues.TAG_PHOTOS);
					for (int j = 0; j < photos.length(); j++) {
						JSONObject photosTemp = photos.getJSONObject(j);
						if (j == 0) {
							item_url_main_70 = photosTemp
									.getString(ConstantValues.TAG_ITEM_URL_MAIN_70);
							user_url_main_70 = photosTemp
									.getString(ConstantValues.TAG_USER_URL_MAIN_70);
							item_url_main_350 = photosTemp
									.getString(ConstantValues.TAG_ITEM_URL_MAIN_350);
							user_url_main_350 = photosTemp
									.getString(ConstantValues.TAG_USER_URL_MAIN_350);
							height=photosTemp.getString(ConstantValues.TAG_HEIGHT);
							width=photosTemp.getString(ConstantValues.TAG_WIDTH);

						}
						item_url_main_original = photosTemp
								.getString(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL);
						originalarray.add(item_url_main_original);
						HomeFragment.urls.put("detialimage" + i, originalarray);
						v = i + 1;
					}
					u1img = user_url_main_350;

					JSONArray comments = temp
							.getJSONArray(ConstantValues.TAG_COMMENTS);
					ArrayList<HashMap<String, String>> tmp = new ArrayList<HashMap<String, String>>();
					for (int k = 0; k < comments.length(); k++) {
						JSONObject commentsTemp = comments.getJSONObject(k);
						HashMap<String, String> tmpMap = new HashMap<String, String>();
						comment_id = commentsTemp
								.getString(ConstantValues.TAG_COMMENT_ID);
						comment = commentsTemp
								.getString(ConstantValues.TAG_COMMENT);
						user_id = commentsTemp
								.getString(ConstantValues.TAG_USER_ID);
						user_img = commentsTemp
								.getString(ConstantValues.TAG_USER_IMG);
						commentsTemp
								.getString(ConstantValues.TAG_USERNAME);

						tmpMap.put(ConstantValues.TAG_COMMENT_ID, comment_id);
						tmpMap.put(ConstantValues.TAG_COMMENT, comment);
						tmpMap.put(ConstantValues.TAG_USER_ID, user_id);
						tmpMap.put(ConstantValues.TAG_USER_IMG, user_img);
						/*
						 * tmpMap.put(ConstantValues.TAG_USERNAME, username);
						 * tmpMap.put(ConstantValues.TAG_SELLER, seller);
						 * tmpMap.put(ConstantValues.TAG_SHOP, shopname);
						 * tmpMap.put(ConstantValues.TAG_USER_URL_MAIN_350,
						 * user_url_main_350);
						 */
						tmp.add(tmpMap);
					}
					JSONArray fashioncounts = temp
							.getJSONArray(ConstantValues.TAG_FASHIONUSER);
					tmp2 = new ArrayList<HashMap<String, String>>();

					for (int m = 0; m < fashioncounts.length(); m++) {
						JSONObject values = fashioncounts.getJSONObject(m);
						tmpMap2 = new HashMap<String, String>();
						fid = values.getString(ConstantValues.TAG_FID);
						fimg = values.getString(ConstantValues.TAG_FIMG);

						tmpMap2.put(ConstantValues.TAG_FID, fid);
						tmpMap2.put(ConstantValues.TAG_FIMG, fimg);

						tmp2.add(tmpMap2);

						// orgary.add(fimg);
						// resultp.put("pictures", orgary);

					}
					JSONArray likescounts = temp
							.getJSONArray(ConstantValues.TAG_LIKED_USERS);
					tmp3 = new ArrayList<HashMap<String, String>>();
					for (int m = 0; m <likescounts.length(); m++){
						JSONObject values = likescounts.getJSONObject(m);
						tmpMap3 = new HashMap<String, String>();
						String itemid =values.getString(ConstantValues.TAG_ITEM_ID);
						String userid=values.getString(ConstantValues.TAG_USER_ID);
					//	String status=values.getString(ConstantValues.TAG_STATUS);
						String userimg=values.getString(ConstantValues.TAG_USER_IMG);
						String uname=values.getString(ConstantValues.TAG_USER_NAME);
					//	String fname=values.getString(ConstantValues.TAG_FULLNAME_NEARSHOP);
						
						tmpMap3.put(ConstantValues.TAG_ITEM_ID, itemid);
						tmpMap3.put(ConstantValues.TAG_USER_ID, userid);
					//	tmpMap3.put(ConstantValues.TAG_STATUS, status);
						tmpMap3.put(ConstantValues.TAG_USER_IMG, userimg);
						tmpMap3.put(ConstantValues.TAG_USER_NAME, uname);
					//	tmpMap3.put(ConstantValues.TAG_FULLNAME_NEARSHOP, fname);
						
						tmp3.add(tmpMap3);
					}
					

					likesMap.put(Integer.parseInt(temp
							.getString(ConstantValues.TAG_ID)), tmp3);
					photosMap.put(Integer.parseInt(temp
							.getString(ConstantValues.TAG_ID)), tmp2);
					commentsMap.put(Integer.parseInt(temp
							.getString(ConstantValues.TAG_ID)), tmp);

					map.put(ConstantValues.TAG_ID, id);
					map.put(ConstantValues.TAG_TITLE, item_title);
					map.put(ConstantValues.TAG_DESC, item_description);
					map.put(ConstantValues.TAG_PRICE, price);
					map.put(ConstantValues.TAG_QUANTITY, quantity);
					map.put(ConstantValues.TAG_SELLER, seller);
					map.put(ConstantValues.TAG_PROFILE_USERID, sellerid);
					Log.v("mappppppppppppid",
							"" + map.get(ConstantValues.TAG_SELLERID));
					map.put(ConstantValues.TAG_SHOP, shopname);
					map.put(ConstantValues.TAG_FAVORITES, favorites);
					map.put(ConstantValues.TAG_FASHIONCOUNT, fash_count);
					map.put(ConstantValues.TAG_LIKED, liked);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_70,
							item_url_main_70);
					map.put(ConstantValues.TAG_USER_URL_MAIN_70,
							user_url_main_70);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
							item_url_main_350);
					// map.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
					// item_url_main_original);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					Log.v("userthumb", "" + user_url_main_350);
					map.put(ConstantValues.TAG_HEIGHT, height);
					map.put(ConstantValues.TAG_WIDTH, width);
					// HomePageI.add(map);
					HomePageItems.add(map);

				}
			} else {

			}
			Log.v("HomePageItems",""+HomePageItems);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateDb(ArrayList<HashMap<String, String>> homePageItems2) {
		DBController controller = new DBController(getActivity());
		for (int i = 0; i < homePageItems2.size(); i++) {
			HashMap<String, String> map = homePageItems2.get(i);
			int id = Integer.parseInt(map.get(ConstantValues.TAG_ID));
			String data = map.toString();
			byte[] image = null;
			ImageData imageData = new ImageData(id, data, image);
			controller.insertItems(imageData);
		}
	}

	public class AdapterForHdpi extends BaseAdapter {

		private Context mContext;

		public AdapterForHdpi(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			return HomePageItems.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			/*
			 * if (convertView == null) { view = new View(mContext);
			 * LayoutInflater inflater = (LayoutInflater) mContext
			 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); view =
			 * inflater.inflate(layout,parent,false); } else { view = (View)
			 * convertView; }
			 */
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(layout, parent, false);
				// view.setPadding(100,100,100,100);

				// view.setLayoutParams();
			} else {
				view = convertView;
				view.forceLayout();
			}
			tempMap = HomePageItems.get(position);
			final ImageView image = (ImageView) view
					.findViewById(R.id.singleImage);
			final ProgressBar loader = (ProgressBar) view
					.findViewById(R.id.singleImageLoader);
			/*
			 * final TextView seller = (TextView) view.findViewById(R.id.uname);
			 * final TextView address = (TextView) view.findViewById(R.id.uadd);
			 * final ImageView sellerimage = (ImageView)
			 * view.findViewById(R.id.uimage);
			 * 
			 * Log.v("username","username"+u1img);
			 */
			/*
			 * final TextView uname = (TextView) view.findViewById(R.id.uname);
			 * final TextView uadd = (TextView) view.findViewById(R.id.uadd);
			 * final ImageView uimg = (ImageView)
			 * view.findViewById(R.id.uimage);
			 * 
			 * Log.v("username","username"+u1img);
			 * 
			 * //uname.setText(u1name); //uadd.setText(u1add);
			 * homeImageLoader.loadImage(u1img, new ImageLoadingListener() {
			 * 
			 * @Override public void onLoadingStarted(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingFailed(String imageUri, View view,
			 * FailReason failReason) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingComplete(String imageUri, View
			 * view, Bitmap loadedImage) { uimg.setImageBitmap(loadedImage);
			 * 
			 * }
			 * 
			 * @Override public void onLoadingCancelled(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * } });
			 */
			ArrayList<HashMap<String, String>> commentTemp = commentsMap
					.get(Integer.parseInt(tempMap.get(ConstantValues.TAG_ID)));
			final LinearLayout commentsLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_comments);
			TextView title = (TextView) view
					.findViewById(R.id.single_item_bottom_title);
			TextView cost = (TextView) view
					.findViewById(R.id.single_item_bottom_cost);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final LinearLayout likesLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_fantacy);
			final LinearLayout fashionLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_upload);
			final TextView photosCount = (TextView) view
					.findViewById(R.id.photocount);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);
			final TextView seller = (TextView) view.findViewById(R.id.uname);
			final TextView address = (TextView) view.findViewById(R.id.uadd);
			final ImageView sellerimage = (ImageView) view
					.findViewById(R.id.uimage);

			Log.v("username", "username" + u1img);

			/*
			 * final TextView seller = (TextView) view.findViewById(R.id.uname);
			 * final TextView address = (TextView) view.findViewById(R.id.uadd);
			 * final ImageView sellerimage = (ImageView)
			 * view.findViewById(R.id.uimage);
			 * 
			 * Log.v("username","username"+u1img);
			 * 
			 * //uname.setText(u1name); //uadd.setText(u1add);
			 * homeImageLoader.loadImage(u1img, new ImageLoadingListener() {
			 * 
			 * @Override public void onLoadingStarted(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingFailed(String imageUri, View view,
			 * FailReason failReason) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingComplete(String imageUri, View
			 * view, Bitmap loadedImage) { uimg.setImageBitmap(loadedImage);
			 * 
			 * }
			 * 
			 * @Override public void onLoadingCancelled(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * } });
			 * 
			 * // fancy.setText(ConstantValues.TAG_FAVORITES); // final TextView
			 * addtoList = (TextView) view // .findViewById(R.id.addtolistText);
			 * 
			 * // if (GetSet.isLogged()) { // String liked =
			 * tempMap.get(ConstantValues.TAG_LIKED); // if
			 * (liked.equalsIgnoreCase("yes")) { //
			 * fancy.setText(getResources().getString(R.string.unlike)); ////
			 * addtoList.setText(getResources().getString( ////
			 * R.string.editList)); // } else { //
			 * fancy.setText(getResources().getString(R.string.like)); ////
			 * addtoList.setText(getResources().getString( ////
			 * R.string.addToList)); // } // } // final LinearLayout fantacy =
			 * (LinearLayout) view //
			 * .findViewById(R.id.single_item_bottom_fantacy);
			 */
			image.setTag(position);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);
			/*
			 * // fantacy.setTag(position); // fantacy.setOnClickListener(new
			 * OnClickListener() { // // @Override // public void onClick(View
			 * arg0) { // if (GetSet.isLogged()) { // int point = (Integer)
			 * fantacy.getTag(); // HashMap<String, String> map =
			 * HomePageItems.get(point); // String liked =
			 * map.get(ConstantValues.TAG_LIKED); // // if
			 * (liked.equalsIgnoreCase("yes")) { // // new SendFancy().execute()
			 * // // } else { // // fancy.setTextColor(Color.BLUE); // // } //
			 * new SendFancy().execute(point); // } else { // Toast.makeText( //
			 * getActivity(), // getResources().getString( //
			 * R.string.PleaseLoginToContinue), // Toast.LENGTH_LONG).show(); //
			 * } // } // });
			 */
			if (commentTemp != null && comments != null) {
				comments.setText(Integer.toString(commentTemp.size()));
			}
			title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			cost.setText(" € " + tempMap.get(ConstantValues.TAG_PRICE));
			fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			photosCount.setText(tempMap.get(ConstantValues.TAG_FASHIONCOUNT));
			seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			String sel = tempMap.get(ConstantValues.TAG_SELLER);
			Log.v("sellername", sel);
			address.setText(tempMap.get(ConstantValues.TAG_SHOP));
			String sho = tempMap.get(ConstantValues.TAG_SHOP);
			Log.v("shopname", sho);

			sellerimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// FragmentChangeActivity fc = new FragmentChangeActivity();
					// fc.switchContent(new ProfileFragment());
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					ConstantValues.editor.clear();
					ConstantValues.editor.putString("userprefid",
							tempMap.get(ConstantValues.TAG_PROFILE_USERID));
					Log.v("present userid",
							"" + tempMap.get(ConstantValues.TAG_PROFILE_USERID));
					ConstantValues.editor.commit();
					fca.switchContent(new ProfileFragment());

				}
			});
		/*	likesLayout.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					Intent i=new Intent(getActivity(),LikedUsers.class);
					i.putExtra("likeUsers",likesMap.get(Integer.parseInt((String) likesLayout.getTag())));
					i.putExtra("position", (String) likesLayout.getTag());
	
					startActivity(i);
				}
				
			});

			fashionLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent j = new Intent(getActivity(), Fashion_photos.class);
					int id = (Integer) image.getTag();
					Log.v("id", "" + id);
					datas2 = HomePageItems;
					j.putExtra("fashionPhoto", photosMap.get(Integer
							.parseInt((String) fashionLayout.getTag())));
					j.putExtra("position", (String) fashionLayout.getTag());
					startActivity(j);
				}

			});

			commentsLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), CommentsActivity.class);
					int id = (Integer) image.getTag();
					Log.v("imgid", ":" + id);
					datas = HomePageItems;
					HashMap<String, String> dataMap = datas.get(id);
					Log.v("datagetid", "" + dataMap);
					i.putExtra("title", dataMap.get(ConstantValues.TAG_TITLE));
					i.putExtra("CommentsData", commentsMap.get(Integer
							.parseInt((String) commentsLayout.getTag())));
					i.putExtra("position", (String) commentsLayout.getTag());
					startActivity(i);

				}
			});
	*/		
			likesLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			commentsLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			fashionLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			/*
			 * seller.setText(map.get(ConstantValues.TAG_SELLER));
			 * address.setText(tempMap.get(ConstantValues.TAG_SHOP));
			 */

			profileLoader.displayImage(
					tempMap.get(ConstantValues.TAG_ITEM_URL_MAIN_350),image,defaultOptions,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							loader.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							loader.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							//Bitmap rdbitmap = RoundedCornerBitmap
							//		.getRoundedCornerBitmap(loadedImage, 10);
							image.setImageBitmap(loadedImage);
							HomePageItems
									.get(Integer.parseInt(image.getTag()
											.toString()));
							loader.setVisibility(View.INVISIBLE);
							/*
							 * final int id = Integer.parseInt(map
							 * .get(ConstantValues.TAG_ID)); Handler handler =
							 * new Handler();
							 */
							// handler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// DBController controller = new DBController(
							// mContext);
							// controller.insertImage(id,
							// getBitmapAsByteArray(bm));
							//
							// }
							// });
							// loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							loader.setVisibility(View.VISIBLE);
							// TODO Auto-generated method stub

						}
					});
			profileLoader.displayImage(tempMap.get(ConstantValues.TAG_USER_URL_MAIN_70),sellerimage,defaultOptions,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							loader.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							//Bitmap rdbitmap = RoundedCornerBitmap
							//		.getRoundedCornerBitmap(loadedImage, 10);
							sellerimage.setImageBitmap(loadedImage);
							HomePageItems
									.get(Integer.parseInt(sellerimage.getTag()
											.toString()));
							// final int id = Integer.parseInt(map
							// .get(ConstantValues.TAG_ID));
							// Handler handler = new Handler();
							// handler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// DBController controller = new DBController(
							// mContext);
							// controller.insertImage(id,
							// getBitmapAsByteArray(bm));
							//
							// }
							// });
							loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
			/*
			 * homeImageLoader.loadImage(ConstantValues.TAG_USER_URL_MAIN_350,
			 * new ImageLoadingListener() {
			 * 
			 * @Override public void onLoadingStarted(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingFailed(String imageUri, View view,
			 * FailReason failReason) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingComplete(String imageUri, View
			 * view, Bitmap loadedImage) {
			 * sellerimage.setImageBitmap(loadedImage);
			 * 
			 * }
			 * 
			 * @Override public void onLoadingCancelled(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * } });
			 */
			return view;
		}
	}

	/*
	 * class CustomListViewAdapter extends ArrayAdapter<HashMap<String, String>>
	 * {
	 * 
	 * Context context; ViewHolder holder = null;
	 * 
	 * public CustomListViewAdapter(Context context, int resourceId,
	 * ArrayList<HashMap<String, String>> items) { super(context, resourceId,
	 * items); this.context = context; }
	 * 
	 * private view holder class private class ViewHolder { ImageView imageView;
	 * TextView txtTitle; TextView txtDesc; }
	 * 
	 * public View getView(int position, View convertView, ViewGroup parent) {
	 * HashMap<String, String> tempMap = new HashMap<String, String>(); tempMap
	 * = SimilarUsers.get(position); LayoutInflater mInflater = (LayoutInflater)
	 * context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE); if
	 * (convertView == null) { convertView = mInflater.inflate(
	 * R.layout.similar_user_list_item, null); holder = new ViewHolder();
	 * holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
	 * holder.txtTitle = (TextView) convertView .findViewById(R.id.title);
	 * holder.imageView = (ImageView) convertView .findViewById(R.id.icon);
	 * convertView.setTag(holder); } else holder = (ViewHolder)
	 * convertView.getTag();
	 * 
	 * holder.txtTitle.setText(tempMap
	 * .get(ConstantValues.TAG_PROFILE_FIRSTNAME)); holder.txtDesc.setText("@" +
	 * tempMap.get(ConstantValues.TAG_PROFILE_USERNAME));
	 * profileLoader.displayImage(
	 * tempMap.get(ConstantValues.TAG_PROFILE_PROFILEIMAGE), holder.imageView);
	 * holder.imageView.setTag(tempMap); holder.imageView.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * @SuppressWarnings("unchecked") final HashMap<String, String> tempMap =
	 * (HashMap<String, String>) holder.imageView .getTag();
	 * 
	 * } }); return convertView; } }
	 */

	// public class LazyAdapter extends BaseAdapter {
	//
	// private Activity activity;
	// private ArrayList<HashMap<String, String>> data;
	// private LayoutInflater inflater = null;
	//
	// public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
	// activity = a;
	// data = d;
	// inflater = (LayoutInflater) activity
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//
	// }
	//
	// public int getCount() {
	// return data.size();
	// }
	//
	// public Object getItem(int position) {
	// return position;
	// }
	//
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// View vi = convertView;
	// if (convertView == null)
	// vi = inflater.inflate(R.layout.comment_item, null);
	//
	// TextView title = (TextView) vi.findViewById(R.id.title); // title
	// TextView artist = (TextView) vi.findViewById(R.id.artist); // artist
	// // name
	// final ImageView thumb_image = (ImageView) vi
	// .findViewById(R.id.list_image); // thumb image
	//
	// HashMap<String, String> tempMap = new HashMap<String, String>();
	// tempMap = SimilarUsers.get(position);
	//
	// // Setting all values in listview
	// title.setText(tempMap.get(ConstantValues.TAG_USERNAME));
	// artist.setText(tempMap.get(ConstantValues.TAG_COMMENT));
	// String url = tempMap.get(ConstantValues.TAG_USER_IMG);
	// String[] x = url.split("/");
	// String last = x[x.length - 1];
	// if (last.equalsIgnoreCase("usr_img.jpg")) {
	// thumb_image.setImageResource(R.drawable.tmp);
	// } else {
	// profileLoader.loadImage(
	// tempMap.get(ConstantValues.TAG_USER_IMG),
	// new ImageLoadingListener() {
	//
	// @Override
	// public void onLoadingStarted(String imageUri,
	// View view) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onLoadingFailed(String imageUri,
	// View view, FailReason failReason) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onLoadingComplete(String imageUri,
	// View view, Bitmap loadedImage) {
	// thumb_image.setImageBitmap(loadedImage);
	//
	// }
	//
	// @Override
	// public void onLoadingCancelled(String imageUri,
	// View view) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// return vi;
	// }
	// }

	/*
	 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) { String uid=
	 * SimilarUsers.get(arg2).get(ConstantValues.TAG_PROFILE_USERID);
	 * ConstantValues.editor.putString("userprefid", uid);
	 * ConstantValues.editor.commit(); //
	 * Toast.makeText(getActivity()," userprefid"+uid, 1000).show(); //final
	 * HashMap<String, String> tempMap = SimilarUsers.get(arg2); // String
	 * userId = tempMap.get(ConstantValues.TAG_PROFILE_USERID); // try { //
	 * adapter.clear(); // adapter.notifyDataSetChanged(); //
	 * profile.setVisibility(View.INVISIBLE); Intent i = new
	 * Intent(ProfileFragment.this.getActivity(), ProfileTabHolder.class); //
	 * i.putExtra("userId", userId); //
	 * ProfileFragment.this.getActivity().finish(); startActivity(i); // } catch
	 * (Exception e) { // } // final Dialog dialog = new Dialog(getActivity());
	 * // dialog.setTitle("Options"); //
	 * dialog.setContentView(R.layout.profile_options_view); // TextView
	 * viewprof = (TextView) dialog.findViewById(R.id.viewProfile); // TextView
	 * follow = (TextView) dialog.findViewById(R.id.follow); //
	 * viewprof.setOnClickListener(new OnClickListener() { // // @Override //
	 * public void onClick(View arg0) { // if (dialog.isShowing()) { //
	 * dialog.dismiss(); // } // String userId =
	 * tempMap.get(ConstantValues.TAG_PROFILE_USERID); // try {
	 * //adapter.clear(); //adapter.notifyDataSetChanged();
	 * //profile.setVisibility(View.INVISIBLE);
	 * 
	 * // ConstantValues.notifyFlag = true; //String uid=
	 * SimilarUsers.get(arg2).get(ConstantValues.TAG_PROFILE_USERID);
	 * //GetSet.setProfileUserId( uid); //Intent i = new
	 * Intent(ProfileFragment.this.getActivity(), //
	 * ProfileTabholderfragment.class); // i.putExtra("userId", userId);
	 * //ProfileFragment.this.getActivity().finish(); //startActivity(i);
	 * 
	 * //} catch (Exception e) { //} // } // }); // dialog.show(); }
	 */

	@Override
	public void onClick(View arg0) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (arg0.getId()) {
		case R.id.followersLayout:
		case R.id.prifilePage_followers_count:

			Bundle bundle = new Bundle();
			bundle.putString("userId", GetSet.getProfileUserId());
			bundle.putString("userName",
					userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));
			Intent followerIntent = new Intent(
					ProfileFragment.this.getActivity(), FollowersActivity.class);
			followerIntent.putExtra("userId", GetSet.getProfileUserId());
			followerIntent.putExtra("userName",
					userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));
			startActivity(followerIntent);
			break;
		case R.id.followingLayout:
		case R.id.prifilePage_following_count:
			Intent followingIntent = new Intent(
					ProfileFragment.this.getActivity(), FollowingActivity.class);
			followingIntent.putExtra("userId", GetSet.getProfileUserId());
			followingIntent.putExtra("userName",
					userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));
			startActivity(followingIntent);
			break;
		/*case R.id.itemadded:
			Intent addedIntent = new Intent(ProfileFragment.this.getActivity(),
					UserAddedItem.class);
			startActivity(addedIntent);

			break;*/
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
		case R.id.btn_shop:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ShopFragment());
			break;
		case R.id.btn_alert:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			}
			else{
				Intent i=new Intent(ProfileFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
		    ConstantValues.editor.clear();
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			//FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			}
			else{
				Intent i=new Intent(ProfileFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		/*
		 * if (v.getId() == R.id.similar_user_list) {
		 * getActivity().getMenuInflater().inflate(R.menu.profile_menu, menu);
		 * menu.setHeaderTitle("Options..."); }
		 */

	}

	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// inflater.inflate(R.menu.main, menu);
	// getSherlockActivity().getSupportMenuInflater().inflate(R.menu.main,
	// menu);
	//
	// // SearchManager searchManager = (SearchManager)
	// // getActivity().getSystemService(Context.SEARCH_SERVICE);
	// // SearchView searchView = (SearchView) menu.findItem(R.id.map)
	// // .getActionView();
	// super.onCreateOptionsMenu(menu, inflater);
	// }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	
		
		inflater.inflate(R.menu.main, menu);
	/*	Log.v("incond false",""+"false");
	//    getSherlockActivity().getSupportMenuInflater().inflate(R.menu.main, menu);
		if(menu!=null){
		MenuItem fil=menu.findItem(R.id.filter);
		fil.setVisible(false);
		}
		MenuItem map=menu.findItem(R.id.map);
		map.setVisible(true);
	*/	
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.main, menu);
		// getSherlockActivity().getSupportMenuInflater().inflate(R.menu.main,
		// menu);
		// msearch = menu.findItem(R.id.map);
		// msearch.setVisible(true);

		
		
		// msearch = menu.findItem(R.id.map);
		// msearch.setVisible(true);
		// info=menu.findItem(R.id.information);
		// info.setVisible(true);
		
	}
	
/*	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	//	menu.findItem(R.id.filter).setVisible(false);
		if (menumap == true) {
			menu.findItem(R.id.map).setVisible(true);
			menumap = false;
		} else {
			menu.findItem(R.id.map).setVisible(false);
			menumap = true;
		}
		if (filter_icon == true) {
			menu.findItem(R.id.filter).setVisible(true);
			filter_icon = false;
		} else {
			menu.findItem(R.id.filter).setVisible(false);
			filter_icon = true;
		}
		return;
	}
*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return false;
		// switch (item.getItemId()) {
		//
		// case R.id.information:
		// Log.v("information","information page");
		// Intent i = new Intent(getActivity(), InformationActivity.class);
		// startActivity(i);
		// return true;
		//
		// default:
		// return super.onOptionsItemSelected(item);
		// }
	}
	public void follow(String followid) {
		new FollowResults().execute(followid);
	}

	public void unfollow(String followid) {
		new UnFollowResults().execute(followid);
	}

	class FollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(ProfileFragment.this.getActivity());

		@Override
		protected String doInBackground(String... param) {
			String result = postData(param[0]);
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
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String postData(String followid) {
		String urlAddr = ConstantValues.Follows;
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "?userId=" + GetSet.getUserId() + "&followId="
					+ followid;
		}
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlAddr.trim());
		try {

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {

		}
		return result;
	}

	class UnFollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(ProfileFragment.this.getActivity());

		@Override
		protected String doInBackground(String... param) {
			String result = postUnFollowData(param[0]);
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
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public String postUnFollowData(String followid) {
		String urlAddr = ConstantValues.Unfollows;
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "?userId=" + GetSet.getUserId() + "&followId="
					+ followid;
		}
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlAddr.trim());
		try {

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {

		}
		return result;
	}
	
	


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(ProfileFragment.this.getActivity(),
				DetailActivity.class);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap = HomePageItems.get(position);
		String itemid = tempMap.get(ConstantValues.TAG_ID);
		String sellerid = tempMap.get(ConstantValues.TAG_SELLERID);
		ConstantValues.editor.putString("itemid", itemid);
		Log.v("present userid", "" + GetSet.getUserId());
		ConstantValues.editor.commit();
		i.putExtra("data", HomePageItems.get(position));
		Log.v("hpmepageitems", "" + HomePageItems.get(position));
		i.putExtra("item_id", itemid);
		i.putExtra("sellerid", sellerid);
		i.putExtra("comments", commentsMap);
		i.putExtra("position", position);
		i.putExtra("position", position);
		i.putExtra("fashionPhoto", photosMap);
		Log.v("fashionpos", "" + photosMap);
		i.putExtra("likeUsers", likesMap);
		Log.v("position", "" + position);
		i.putExtra("from", 5);
		startActivity(i);

	}
}
