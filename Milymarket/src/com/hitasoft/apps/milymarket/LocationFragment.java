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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import com.hitasoft.apps.adapters.RoundedCornerBitmap;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.DBController;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.ImageData;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class LocationFragment extends SherlockFragment implements
		OnScrollListener, OnClickListener, LocationListener,
		OnItemClickListener {

	private ImageLoader homeImageLoader;
	private static RelativeLayout filterlay;
	private TextView product_nearme, shop_nearme;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	private StaggeredGridView gridView;
	public static ArrayList<HashMap<String, String>> NearmePageItems,
			NearmeShopItems;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = null;
	private int previousTotal = 0;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
	private int visibleThreshold = 0;
	private boolean loading = true;
	LinearLayout bottomLoading;
	private int currentPage = 0;
	static AdapterForHdpi hdpiAdapter;
	RelativeLayout LoagingLayout, mainLayout;
	private static final int LDPI_PORTRAID = 1;
	private static final int LDPI_LANDSCAPE = 2;
	private static final int MDPI_PORTRAID = 3;
	private static final int MDPI_LANDSCAPE = 4;
	private static final int HDPI_PORTRAID = 5;
	private static final int HDPI_LANDSCAPE = 6;
	private static final int LARGE_MDPI_PORTRAID = 7;
	private static final int LARGE_MDPI_LANDSCAPE = 8;
	private static final int XHDPI_PORTRAID = 9;
	private static final int XHDPI_LANDSCAPE = 10;
	private static int device = 0;
	private static int layout = 0;
	private static String type = null;
	TextView centerHome;
	private static final long DOUBLE_PRESS_INTERVAL = 750; // in millis
	private long lastPressTime;
	private ImageButton home, near, shop, alert, menu;
	private static boolean mHasDoubleClicked = false;
	String u1name, u1add, u1img;
	ArrayList<HashMap<String, String>> datas = null;
	ArrayList<HashMap<String, String>> datas2 = null;
	AdapterForShopProduct hdpiAdaptershop;
	LocationManager lc;
	String provider;
	double lat, lon;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;
	public static boolean shopfollow, shopunfollow;
	ProgressDialog pgsDialog;
	String distance, latitude, longtitude, offset, limit;
	AlertDialog adialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeImageLoader = ImageLoader.getInstance();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.location_grid, container,
				false);
		return v;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("UseSparseArrays")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
		ConstantValues.pref = getActivity()
				.getSharedPreferences("fantacyid", 0);
		ConstantValues.editor = ConstantValues.pref.edit();
		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);
		near.setImageResource(R.drawable.tab_bar_near_selected);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		 adialog=new AlertDialog.Builder(LocationFragment.this.getActivity()).create();
			adialog.setTitle("Alert");
			adialog.setMessage("S'il vous plaît connecter Pour continuer!!!");
			adialog.setButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.v("alert","alert");
					
				}
			});

		filterlay = (RelativeLayout) getView().findViewById(R.id.filterlay);
		product_nearme = (TextView) getView().findViewById(R.id.product_nearme);
		shop_nearme = (TextView) getView().findViewById(R.id.shop_nearme);
		lc = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = lc.getBestProvider(criteria, false);
		Location locate = lc.getLastKnownLocation(provider);
		if (locate != null) {
			onLocationChanged(locate);
		}

		centerHome = (TextView) getView().findViewById(R.id.homenulltext);
		centerHome.setVisibility(View.INVISIBLE);
		bottomLoading = (LinearLayout) getView().findViewById(R.id.bottomhome);
		bottomLoading.setVisibility(View.VISIBLE);
		mainLayout = (RelativeLayout) getView().findViewById(R.id.main);
		mainLayout.setVisibility(View.INVISIBLE);
		NearmePageItems = new ArrayList<HashMap<String, String>>();
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		LoagingLayout = (RelativeLayout) (getView()
				.findViewById(R.id.loadImageLayout));
		gridView = (StaggeredGridView) (getView().findViewById(R.id.homePage_gallery));
		gridView.setSmoothScrollbarEnabled(true);
		gridView.setOnScrollListener(LocationFragment.this);
		gridView.setOnItemClickListener(this);
		((FragmentChangeActivity) getActivity())
				.getSupportActionBar()
				.setTitle(
						getActivity().getResources().getString(R.string.nearme));
		setColumns();

		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.location);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);
		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);
		if (isNetworkAvailable(getActivity())) {
			loadData();
		}

		product_nearme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				filterlay.setVisibility(View.GONE);
				if (isNetworkAvailable(getActivity())) {
					loadProductData();
				}
			}
		});

		shop_nearme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				filterlay.setVisibility(View.GONE);
				if (isNetworkAvailable(getActivity())) {
					loadShopData();
				}

			}
		});
	}

	public static void Showfilter() {
		if (FragmentChangeActivity.filtered == true) {
			filterlay.setVisibility(View.VISIBLE);
		} else {
			filterlay.setVisibility(View.VISIBLE);
		}
	}

	private void setColumns() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		if (screenWidth < 280
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = LDPI_PORTRAID;
		//	gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = LDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = MDPI_PORTRAID;
		//	gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = MDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = HDPI_PORTRAID;
		//	gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 900
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = HDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 700
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = LARGE_MDPI_PORTRAID;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 1000
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = LARGE_MDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 800
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = XHDPI_PORTRAID;
			boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
			if (!tabletSize) {
		//		gridView.setNumColumns(1);
			}
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 1400
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = XHDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else {
			device = HDPI_PORTRAID;
		//	gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		}
	}

	private void loadData() {
		NearmePageItems = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(getActivity())) {
			try {
				new NearmePageLoadImages().execute(lat, lon);
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
		setAdapter();

	}

	private void loadProductData() {
		NearmePageItems = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(getActivity())) {
			try {
				new NearmeProductData().execute(lat, lon);
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
		setAdapterProduct();

	}

	private void loadShopData() {
		NearmeShopItems = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(getActivity())) {
			try {
				new NearmeShopData().execute(lat, lon);
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
				e.printStackTrace();
			}
		} else {

		}
		setAdapterShop();

	}

	private void setAdapter() {
		try {
			setColumns();
			hdpiAdapter = new AdapterForHdpi(getActivity());
			gridView.setAdapter(hdpiAdapter);
		} catch (Exception e) {

		}
	}

	private void setAdapterProduct() {
		try {
			setColumns();
			hdpiAdapter = new AdapterForHdpi(getActivity());
			gridView.setAdapter(hdpiAdapter);
		} catch (Exception e) {

		}
	}

	private void setAdapterShop() {
		try {
			hdpiAdaptershop = new AdapterForShopProduct(getActivity());
			gridView.setAdapter(hdpiAdaptershop);
			//hdpiAdaptershop.notifyDataSetChanged();

		} catch (Exception e) {

		}
	}

	class NearmePageLoadImages extends AsyncTask<Double, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				LocationFragment.this.getActivity());

		@Override
		protected Void doInBackground(Double... params) {
			try {
				parsing(params[0], params[1]);
			} catch (Exception e) {
				Log.v("error", e.toString());
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

		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (currentPage == 0) {
					bottomLoading.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
				}
				if (NearmePageItems.size() == 0) {
					centerHome.setVisibility(View.VISIBLE);
					exit();
				}
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}
	}

	private void parsing(Double lat, Double lan) {

		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.getnearme + "?lat=" + lat + "&long="
				+ lan + "&distance=100";
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				String type = result.getString("type");
				LocationFragment.type = type;
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null, user_url_main_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null, fid = null, fimg = null,height=null,width=null;
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
							.getString(ConstantValues.TAG_SELLERID);
					String favorites = temp
							.getString(ConstantValues.TAG_FAVORITES);
					String fash_count = temp
							.getString(ConstantValues.TAG_FASHIONCOUNT);
					String liked = temp.getString(ConstantValues.TAG_LIKED);
					String seller = temp.getString(ConstantValues.TAG_SELLER);
					String shopname = temp.getString(ConstantValues.TAG_SHOP);

					JSONArray photos = temp
							.getJSONArray(ConstantValues.TAG_PHOTOS);
					ArrayList<String> originalarray = new ArrayList<String>();
					for (int j = 0; j < photos.length(); j++) {
						JSONObject photosTemp = photos.getJSONObject(j);
						if (j == 0) {
							item_url_main_70 = photosTemp
									.getString(ConstantValues.TAG_ITEM_URL_MAIN_70);
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
					}
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
						username = commentsTemp
								.getString(ConstantValues.TAG_USERNAME);

						tmpMap.put(ConstantValues.TAG_COMMENT_ID, comment_id);
						tmpMap.put(ConstantValues.TAG_COMMENT, comment);
						tmpMap.put(ConstantValues.TAG_USER_ID, user_id);
						tmpMap.put(ConstantValues.TAG_USER_IMG, user_img);
						tmpMap.put(ConstantValues.TAG_USERNAME, username);
						tmp.add(tmpMap);

						u1name = username;
						u1add = null;
						u1img = user_img;
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

					}
					JSONArray likescounts = temp
							.getJSONArray(ConstantValues.TAG_LIKED_USERS);
					tmp3 = new ArrayList<HashMap<String, String>>();
					for (int m = 0; m <likescounts.length(); m++){
						JSONObject values = likescounts.getJSONObject(m);
						tmpMap3 = new HashMap<String, String>();
						String itemid =values.getString(ConstantValues.TAG_ITEM_ID);
						String userid=values.getString(ConstantValues.TAG_USER_ID);
						String status=values.getString(ConstantValues.TAG_STATUS);
						String userimg=values.getString(ConstantValues.TAG_USER_IMG);
						String uname=values.getString(ConstantValues.TAG_USER_NAME);
						String fname=values.getString(ConstantValues.TAG_FULLNAME_NEARSHOP);
						
						tmpMap3.put(ConstantValues.TAG_ITEM_ID, itemid);
						tmpMap3.put(ConstantValues.TAG_USER_ID, userid);
						tmpMap3.put(ConstantValues.TAG_STATUS, status);
						tmpMap3.put(ConstantValues.TAG_USER_IMG, userimg);
						tmpMap3.put(ConstantValues.TAG_USER_NAME, uname);
						tmpMap3.put(ConstantValues.TAG_FULLNAME_NEARSHOP, fname);
						
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
					map.put(ConstantValues.TAG_SELLERID, sellerid);
					map.put(ConstantValues.TAG_SHOP, shopname);
					map.put(ConstantValues.TAG_FAVORITES, favorites);
					map.put(ConstantValues.TAG_FASHIONCOUNT, fash_count);
					map.put(ConstantValues.TAG_LIKED, liked);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_70,
							item_url_main_70);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
							item_url_main_350);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					map.put(ConstantValues.TAG_HEIGHT, height);
					map.put(ConstantValues.TAG_WIDTH, width);
					NearmePageItems.add(map);

				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class NearmeProductData extends AsyncTask<Double, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				LocationFragment.this.getActivity());

		@Override
		protected Void doInBackground(Double... params) {
			try {
				parsingProduct(params[0], params[1]);
			} catch (Exception e) {
				Log.v("error", e.toString());
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

		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				if (currentPage == 0) {
					bottomLoading.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);

				}
				if (NearmePageItems.size() == 0) {
					centerHome.setVisibility(View.VISIBLE);
					exit();
				}
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}
			setAdapterProduct();

		}
	}

	private void parsingProduct(Double lat, Double lan) {

		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.NearmeProducturl + "?lat=" + lat
				+ "&long=" + lan + "&distance=100&limit=20";
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
		}
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				String type = result.getString("type");
				LocationFragment.type = type;
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null, user_url_main_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null, fid = null, fimg = null,height=null,width=null;
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
							.getString(ConstantValues.TAG_SELLERID);
					String favorites = temp
							.getString(ConstantValues.TAG_FAVORITES);
					String fash_count = temp
							.getString(ConstantValues.TAG_FASHIONCOUNT);
					String liked = temp.getString(ConstantValues.TAG_LIKED);
					String seller = temp.getString(ConstantValues.TAG_SELLER);
					String shopname = temp.getString(ConstantValues.TAG_SHOP);

					JSONArray photos = temp
							.getJSONArray(ConstantValues.TAG_PHOTOS);
					ArrayList<String> originalarray = new ArrayList<String>();
					for (int j = 0; j < photos.length(); j++) {
						JSONObject photosTemp = photos.getJSONObject(j);
						if (j == 0) {
							item_url_main_70 = photosTemp
									.getString(ConstantValues.TAG_ITEM_URL_MAIN_70);
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
					}
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
						username = commentsTemp
								.getString(ConstantValues.TAG_USERNAME);

						tmpMap.put(ConstantValues.TAG_COMMENT_ID, comment_id);
						tmpMap.put(ConstantValues.TAG_COMMENT, comment);
						tmpMap.put(ConstantValues.TAG_USER_ID, user_id);
						tmpMap.put(ConstantValues.TAG_USER_IMG, user_img);
						tmpMap.put(ConstantValues.TAG_USERNAME, username);
						tmp.add(tmpMap);

						u1name = username;
						u1add = null;
						u1img = user_img;
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

					}

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
					map.put(ConstantValues.TAG_SELLERID, sellerid);
					map.put(ConstantValues.TAG_SHOP, shopname);
					map.put(ConstantValues.TAG_FAVORITES, favorites);
					map.put(ConstantValues.TAG_FASHIONCOUNT, fash_count);
					map.put(ConstantValues.TAG_LIKED, liked);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_70,
							item_url_main_70);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
							item_url_main_350);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					map.put(ConstantValues.TAG_HEIGHT, height);
					map.put(ConstantValues.TAG_WIDTH, width);
					NearmePageItems.add(map);

				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class NearmeShopData extends AsyncTask<Double, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				LocationFragment.this.getActivity());

		@Override
		protected Void doInBackground(Double... params) {
			try {
				parsingShop(params[0], params[1]);
			} catch (Exception e) {
				Log.v("error", e.toString());
				e.printStackTrace();
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

		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (currentPage == 0) {
					bottomLoading.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);

				}
				if (NearmePageItems.size() == 0) {
					centerHome.setVisibility(View.VISIBLE);
				}
				LoagingLayout.setVisibility(View.GONE);
				//hdpiAdaptershop.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
				e.printStackTrace();
			}
			setAdapterShop();

		}
	}

	private void parsingShop(Double lat, Double lan) {

		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.NearmeShop + "?lat=" + lat + "&long="
				+ lan + "&distance=100&limit=20";
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
		}
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS_SHOP);
			if (response.equalsIgnoreCase("true")) {
				items = json.getJSONArray(ConstantValues.TAG_RESULT_NEARSHOP);
				Log.v("items length", "" + items.length());
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();

					JSONObject temp = items.getJSONObject(i);
					String userid = temp
							.getString(ConstantValues.TAG_USERID_NEARSHOP);
					String username = temp
							.getString(ConstantValues.TAG_USERNAME_NEARSHOP);
					String fullname = temp
							.getString(ConstantValues.TAG_FULLNAME_NEARSHOP);

					String address = temp
							.getString(ConstantValues.TAG_ADDRESS_NEARSHOP);
					String statusreport = temp
							.getString(ConstantValues.TAG_STATUSREPORT_NEARSHOP);
					String image = temp
							.getString(ConstantValues.TAG_IMAGENAME_NEARSHOP);
					Log.v("image url", "" + image);

					map.put(ConstantValues.TAG_USERID_NEARSHOP, userid);
					map.put(ConstantValues.TAG_USERNAME_NEARSHOP, username);
					Log.v("username shopnearme", "" + username);
					map.put(ConstantValues.TAG_FULLNAME_NEARSHOP, fullname);
					map.put(ConstantValues.TAG_ADDRESS_NEARSHOP, address);
					map.put(ConstantValues.TAG_STATUSREPORT_NEARSHOP,
							statusreport);
					map.put(ConstantValues.TAG_IMAGENAME_NEARSHOP, image);
					NearmeShopItems.add(map);
					Log.v("NearmeShopItems length", "" + NearmeShopItems.size());

				}
			} else {

			}
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

	public class AdapterForShopProduct extends BaseAdapter {

		private Context mContext;

		public AdapterForShopProduct(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			return NearmeShopItems.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.followinginflate, parent,
						false);
			} else {
				view = convertView;
				view.forceLayout();
			}
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap = NearmeShopItems.get(position);
			Log.v("tempmap",""+tempMap);

			final ImageView image = (ImageView) view
					.findViewById(R.id.followingimage);

			final TextView username = (TextView) view
					.findViewById(R.id.username);

			final TextView fullname = (TextView) view
					.findViewById(R.id.fullname);
			final Button userstatus = (Button) view.findViewById(R.id.button1);
			final ProgressBar loader = (ProgressBar) view
					.findViewById(R.id.progressBar1);
			final String followuserid;
			image.setTag(position);
			username.setText(tempMap.get(ConstantValues.TAG_USERNAME_NEARSHOP));
			String ustatus=tempMap.get(ConstantValues.TAG_STATUS);
			if(ustatus.equals("follow")){
				userstatus.setText("S'abonner");
			}
			else{
				userstatus.setText("Se désabonner");
			}
			if (userstatus.getText().toString().equals("S'abonner")) {
				shopfollow = true;
				userstatus.setBackgroundColor(android.graphics.Color.RED);
			} else if (userstatus.getText().toString().equals("Se désabonner")) {
				shopunfollow = true;
				userstatus.setBackgroundColor(android.graphics.Color.GRAY);
			}
			followuserid = tempMap.get(ConstantValues.TAG_USERID_NEARSHOP);
			if (followuserid.equals(GetSet.getUserId())) {
				userstatus.setVisibility(View.GONE);
			}

			userstatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(GetSet.isLogged()==true){
					Log.v("username", "" + username.getText().toString());
					Log.v("user status", "" + userstatus.getText().toString());
					if (userstatus.getText().equals("S'abonner")
							&& shopfollow == true) {
						shopfollow = false;
						Log.v("following", "following");
						follow(followuserid);
					} else if (userstatus.getText().equals("Se désabonner")
							&& shopunfollow == true) {
						shopunfollow = false;
						Log.v("unnnnnfollowing", "unnnnnfollowing");
						unfollow(followuserid);
					}
					}
					else{
						adialog.show();
					}

				}

			});
			image.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					ConstantValues.editor.clear();
					ConstantValues.editor.putString("userprefid",NearmeShopItems.get(position).get("UserId"));
					ConstantValues.editor.commit();
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					FragmentChangeActivity.rsprofile = true;
					FragmentChangeActivity.menumap = true;
					FragmentChangeActivity.filter_icon=false;
					getActivity().supportInvalidateOptionsMenu();
					fca.switchContent(new ProfileFragment());
			
					
				}
				
			});
			fullname.setText(tempMap.get(ConstantValues.TAG_ADDRESS_NEARSHOP));
			homeImageLoader.loadImage(
					tempMap.get(ConstantValues.TAG_IMAGENAME_NEARSHOP),
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
							image.setImageBitmap(loadedImage);
							loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							loader.setVisibility(View.VISIBLE);
							image.setImageResource(R.drawable.usrimg);

						}
					});
			//notifyDataSetChanged();
			return view;
		}
	}

	public void follow(String followid) {
		new FollowResults().execute(followid);
	}

	public void unfollow(String followid) {
		new UnFollowResults().execute(followid);
	}

	class FollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected String doInBackground(String... param) {
			String result = postData(param[0]);
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("Please Wait...");
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
					Log.v("response", jonj.toString());
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
			Log.v("userid", "" + GetSet.getUserId());
			Log.v("followid", "" + followid);
			Log.v("in", urlAddr.trim());
		}
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlAddr.trim());
		try {

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

	class UnFollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected String doInBackground(String... param) {
			String result = postUnFollowData(param[0]);
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("Please Wait...");
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
					Log.v("response", jonj.toString());
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
			Log.v("userid", "" + GetSet.getUserId());
			Log.v("followid", "" + followid);
			Log.v("in", urlAddr.trim());
		}
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlAddr.trim());
		try {

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

	public class AdapterForHdpi extends BaseAdapter {

		private Context mContext;

		public AdapterForHdpi(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			return NearmePageItems.size();
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
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(layout, parent, false);
			} else {
				view = convertView;
				view.forceLayout();
			}
			tempMap = NearmePageItems.get(position);
			final ImageView image = (ImageView) view
					.findViewById(R.id.singleImage);
			final ProgressBar loader = (ProgressBar) view
					.findViewById(R.id.singleImageLoader);
			final TextView seller = (TextView) view.findViewById(R.id.uname);
			final TextView address = (TextView) view.findViewById(R.id.uadd);
			final ImageView sellerimage = (ImageView) view
					.findViewById(R.id.uimage);
			ArrayList<HashMap<String, String>> commentTemp = commentsMap
					.get(Integer.parseInt(tempMap.get(ConstantValues.TAG_ID)));
			final LinearLayout commentsLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_comments);
			final LinearLayout fashionLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_upload);
			final TextView photosCount = (TextView) view
					.findViewById(R.id.photocount);
			TextView title = (TextView) view
					.findViewById(R.id.single_item_bottom_title);
			TextView cost = (TextView) view
					.findViewById(R.id.single_item_bottom_cost);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final LinearLayout likesLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_fantacy);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);
			image.setTag(position);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);

			if (commentTemp != null && comments != null) {
				comments.setText(Integer.toString(commentTemp.size()));
			}
			title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			cost.setText(" € " + tempMap.get(ConstantValues.TAG_PRICE));
			fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			photosCount.setText(tempMap.get(ConstantValues.TAG_FASHIONCOUNT));
			address.setText(tempMap.get(ConstantValues.TAG_SHOP));

			sellerimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					ConstantValues.editor.putString("userprefid",
							tempMap.get(ConstantValues.TAG_SELLERID));
					ConstantValues.editor.commit();
					getActivity().supportInvalidateOptionsMenu();
					fca.switchContent(new ProfileFragment());

				}
			});
		/*	fashionLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent j = new Intent(getActivity(), Fashion_photos.class);
					int id = (Integer) image.getTag();
					Log.v("id", "" + id);
					datas2 = NearmePageItems;
					j.putExtra("fashionPhoto", photosMap.get(Integer
							.parseInt((String) fashionLayout.getTag())));
					j.putExtra("position", (String) fashionLayout.getTag());
					startActivity(j);
				}

			});
			likesLayout.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					Intent i=new Intent(getActivity(),LikedUsers.class);
					i.putExtra("likeUsers",likesMap.get(Integer.parseInt((String) likesLayout.getTag())));
					i.putExtra("position", (String) likesLayout.getTag());
	
					startActivity(i);
				}
				
			});

			commentsLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), CommentsActivity.class);
					int id = (Integer) image.getTag();
					datas = NearmePageItems;
					HashMap<String, String> dataMap = datas.get(id);
					i.putExtra("title", dataMap.get(ConstantValues.TAG_TITLE));
					i.putExtra("CommentsData", commentsMap.get(Integer
							.parseInt((String) commentsLayout.getTag())));
					i.putExtra("position", (String) commentsLayout.getTag());
					startActivity(i);

				}
			});
			
		*/	
			likesLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			commentsLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			fashionLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			homeImageLoader.loadImage(
					tempMap.get(ConstantValues.TAG_ITEM_URL_MAIN_350),
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
							image.setImageBitmap(loadedImage);
							loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
			homeImageLoader.loadImage(
					tempMap.get(ConstantValues.TAG_USER_URL_MAIN_350),
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
							loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
			return view;
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}

		if (!loading
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			loading = true;
		}

	}

	public int getWindowState() {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			return 2;
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			return 1;
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			return 0;
		} else {
			return 3;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setAdapter();

	}

	public int deviceNumber() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int n = dm.densityDpi;
		return 0;

	}

	class SendFancy extends AsyncTask<Integer, Void, JSONObject> {

		String fancyUrl = ConstantValues.fantacyUrl;
		JSONParser jParser = new JSONParser();
		int itemId;
		ProgressDialog dialog = new ProgressDialog(
				LocationFragment.this.getActivity());

		@Override
		protected JSONObject doInBackground(Integer... params) {
			itemId = params[0];
			HashMap<String, String> map = NearmePageItems.get(itemId);
			fancyUrl = fancyUrl + GetSet.getUserId() + "&itemId="
					+ map.get(ConstantValues.TAG_ID);
			JSONObject response = jParser.getJSONFromUrl(fancyUrl);
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Please wait...");
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
					if (result.getString("message").equalsIgnoreCase(
							"Item Liked")) {
						Toast.makeText(getActivity(), "Item Fantacy'd",
								Toast.LENGTH_LONG).show();
						try {
							NearmePageItems.get(itemId).put(
									ConstantValues.TAG_LIKED, "Yes");
							hdpiAdapter.notifyDataSetChanged();
						} catch (NullPointerException npe) {

						} catch (Exception e) {

						}

					} else {
						Toast.makeText(getActivity(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						try {
							NearmePageItems.get(itemId).put(
									ConstantValues.TAG_LIKED, "No");
							hdpiAdapter.notifyDataSetChanged();
						} catch (NullPointerException npe) {

						} catch (Exception e) {

						}
					}
				} else {
					Toast.makeText(getActivity(),
							"Sorry, something went wrong", Toast.LENGTH_LONG)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.refresh, menu);
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		fca.switchContent(new LocationFragment());
		return super.onOptionsItemSelected(item);
	}

	public void exit() {
		final Dialog settingsDialog = new Dialog(
				LocationFragment.this.getActivity());
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		settingsDialog.setContentView(getActivity().getLayoutInflater()
				.inflate(R.layout.alert_for_error, null));
		settingsDialog.setCancelable(false);
		settingsDialog.setCanceledOnTouchOutside(false);
		settingsDialog.setTitle("Network Problem");
		Button retry = (Button) settingsDialog.findViewById(R.id.alertTryAgain);
		Button exit = (Button) settingsDialog.findViewById(R.id.alertExit);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
				FragmentChangeActivity.menumap = false;
				FragmentChangeActivity.filter_icon = false;
				getActivity().supportInvalidateOptionsMenu();
				FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
				fca.switchContent(new HomeFragment());
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
				LocationFragment.this.getActivity().finish();
			}
		});
		settingsDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		lat = (Double) (location.getLatitude());
		lon = (Double) (location.getLongitude());

		Log.v("lati", "lat" + lat);
		Log.v("longi", "longi" + lon);

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {

		case R.id.btn_home:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new HomeFragment());
			break;
		case R.id.btn_near:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = true;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new LocationFragment());
			break;
		case R.id.btn_shop:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ShopFragment());
			break;
		case R.id.btn_alert:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			}
			else{
				Intent i=new Intent(LocationFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			}
			else{
				Intent i=new Intent(LocationFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int arg2,
			long id) {
		long pressTime = System.currentTimeMillis();

		if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
			mHasDoubleClicked = true;
			LinearLayout ll = (LinearLayout) gridView.getChildAt(arg2);
			LinearLayout fantacy = (LinearLayout) ll
					.findViewById(R.id.single_item_bottom_fantacy);
			if (GetSet.isLogged()) {
				int point = (Integer) fantacy.getTag();
				new SendFancy().execute(point);
				Handler h = new Handler();
				h.postDelayed(new Runnable() {

					@Override
					public void run() {
					}
				}, 1500);
			} else {
				Toast.makeText(getActivity(), "Please Login To continue",
						Toast.LENGTH_LONG).show();
			}

		} else { // If not double click....
			mHasDoubleClicked = false;
			Handler myHandler = new Handler() {
				public void handleMessage(Message m) {
					if (!mHasDoubleClicked) {
						Intent i = new Intent(
								LocationFragment.this.getActivity(),
								DetailActivity.class);
						HashMap<String, String> tempMap = new HashMap<String, String>();
						tempMap = NearmePageItems.get(arg2);
						String itemid = tempMap.get(ConstantValues.TAG_ID);
						String sellerid = tempMap
								.get(ConstantValues.TAG_SELLERID);
						ConstantValues.editor.putString("itemid", itemid);
						ConstantValues.editor.commit();
						i.putExtra("data", NearmePageItems.get(arg2));
						i.putExtra("item_id", itemid);
						i.putExtra("sellerid", sellerid);
						i.putExtra("comments", commentsMap);
						i.putExtra("fashionPhoto", photosMap);
						i.putExtra("likeUsers", likesMap);
						i.putExtra("position", arg2);
						i.putExtra("from", 2);
						startActivity(i);
					}
				}
			};
			Message m = new Message();
			myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
		}
		lastPressTime = pressTime;

	}
}
