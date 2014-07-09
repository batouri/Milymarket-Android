package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.etsy.android.grid.StaggeredGridView;
import com.hitasoft.apps.adapters.RoundedCornerBitmap;
import com.hitasoft.apps.milymarket.R;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.DBController;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.ImageData;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@SuppressLint("UseSparseArrays")
public class MostPopular extends SherlockFragment implements
		OnItemClickListener, OnClickListener {

	private ImageLoader homeImageLoader;
	private StaggeredGridView gridView;
	public String sellerid = null;
	private ArrayList<HashMap<String, String>> HomePageItems;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();;
	private int currentPage = 0;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
	AdapterForHdpi hdpiAdapter;
	RelativeLayout LoagingLayout, mainLayout;
	TextView centerHome;
	LinearLayout bottomLoading;
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
	private static String type;
	LinearLayout bottomhome;
	TextView centerText;
	private ImageButton home, near, shop, alert, menu;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	ArrayList<HashMap<String, String>> datas = null;
	ArrayList<HashMap<String, String>> datas2 = null;
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeImageLoader = ImageLoader.getInstance();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.home_grid_layout, container,
				false);
		return v;
	}

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

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		centerHome = (TextView) getView().findViewById(R.id.homenulltext);
		centerHome.setVisibility(View.INVISIBLE);
		bottomLoading = (LinearLayout) getView().findViewById(R.id.bottomhome);
		bottomLoading.setVisibility(View.VISIBLE);
		mainLayout = (RelativeLayout) getView().findViewById(R.id.main);
		mainLayout.setVisibility(View.INVISIBLE);
		HomePageItems = new ArrayList<HashMap<String, String>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		LoagingLayout = (RelativeLayout) (getView()
				.findViewById(R.id.loadImageLayout));
		gridView = (StaggeredGridView) (getView().findViewById(R.id.homePage_gallery));
		gridView.setSmoothScrollbarEnabled(true);
		gridView.setOnItemClickListener(this);
		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(
						getActivity().getResources().getString(
								R.string.MostPopular));
		setColumns();
		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.MostPopular);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);
		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);
		setColumns();
		loadData();
	}

	private void setColumns() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		if (screenWidth < 280
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = LDPI_PORTRAID;
			// gridView.setNumColumns(1);//1
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = LDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = MDPI_PORTRAID;
			// gridView.setNumColumns(1);//1
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = MDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = HDPI_PORTRAID;
			// gridView.setNumColumns(1);//1
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
				// gridView.setNumColumns(1);//1
			}
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 1400
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = XHDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else {
			device = HDPI_PORTRAID;
			// gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		}

	}

	private void loadData() {
		HomePageItems = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(getActivity())) {
			try {
				new homePageLoadImages().execute(0);
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
		setAdapter();

	}

	private void setAdapter() {

		try {

			hdpiAdapter = new AdapterForHdpi(getActivity());
			gridView.setAdapter(hdpiAdapter);
		} catch (Exception e) {

		}

	}

	class homePageLoadImages extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				MostPopular.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				parsing(params[0]);
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
				}
				if (HomePageItems.size() == 0) {
					centerHome.setVisibility(View.VISIBLE);
				}
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}
	}

	private void parsing(Integer page) {

		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.urcollection + GetSet.getUserId();
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
		}
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		Log.v("given url", "" + urlAddr);
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				String type = result.getString("type");
				MostPopular.type = type;
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null, user_url_main_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null, fid = null, fimg = null,height=null,width=null;
					JSONObject temp = items.getJSONObject(i);
					String id = temp.getString(ConstantValues.TAG_ID);
					String item_title = temp
							.getString(ConstantValues.TAG_TITLE);
					String seller_id = temp.getString("userId");
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
						Log.v("detialimage" + i, "" + originalarray);
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
					map.put(ConstantValues.TAG_SHOP, shopname);
					map.put(ConstantValues.TAG_FAVORITES, favorites);
					map.put(ConstantValues.TAG_SELLERID, sellerid);
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
					HomePageItems.add(map);

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(layout, parent, false);

			} else {
				view = convertView;
				view.forceLayout();
			}

			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap = HomePageItems.get(position);

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

			TextView title = (TextView) view
					.findViewById(R.id.single_item_bottom_title);
			final LinearLayout likesLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_fantacy);
			final LinearLayout commentsLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_comments);
			final LinearLayout fashionLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_upload);
			final TextView photosCount = (TextView) view
					.findViewById(R.id.photocount);
			TextView cost = (TextView) view
					.findViewById(R.id.single_item_bottom_cost);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);

			image.setTag(position);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);

	/*		commentsLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), CommentsActivity.class);
					int id = (Integer) image.getTag();
					Log.v("imgid", ":" + id);

					HashMap<String, String> dataMap = HomePageItems.get(id);
					Log.v("datagetid", "" + dataMap);
					i.putExtra("title", dataMap.get(ConstantValues.TAG_TITLE));
					i.putExtra("CommentsData", commentsMap.get(Integer
							.parseInt((String) commentsLayout.getTag())));
					i.putExtra("position", (String) commentsLayout.getTag());
					startActivity(i);

				}
			});
			
			*/
			commentsLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
    
			sellerimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ConstantValues.editor.clear();
					String uid = HomePageItems.get(position).get(ConstantValues.TAG_SELLERID);
					ConstantValues.editor.putString("userprefid", uid);
					ConstantValues.editor.commit();
					//Toast.makeText(getActivity(), "image is clicked" + uid,
					//		Toast.LENGTH_SHORT).show();
					FragmentChangeActivity.menumap = true;
					FragmentChangeActivity.filter_icon = false;
					getActivity().supportInvalidateOptionsMenu();
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
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
					datas2 = HomePageItems;
					j.putExtra("fashionPhoto", photosMap.get(Integer
							.parseInt((String) fashionLayout.getTag())));
					j.putExtra("position", (String) fashionLayout.getTag());
					startActivity(j);
				}

			});
		*/	
			likesLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			commentsLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			fashionLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);

			if (commentTemp != null && comments != null) {
				comments.setText(Integer.toString(commentTemp.size()));
			}
			title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			cost.setText(" € " + tempMap.get(ConstantValues.TAG_PRICE));
			fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			String sel = tempMap.get(ConstantValues.TAG_SELLER);
			Log.v("sellername", sel);
			photosCount.setText(tempMap.get(ConstantValues.TAG_FASHIONCOUNT));
			address.setText(tempMap.get(ConstantValues.TAG_SHOP));
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.v("imageurl0", "" + HomeFragment.urls.get(0));
		Intent i = new Intent(MostPopular.this.getActivity(),
				DetailActivity.class);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap = HomePageItems.get(position);
		String itemid = tempMap.get(ConstantValues.TAG_ID);
		String sellerid = tempMap.get(ConstantValues.TAG_SELLERID);
		ConstantValues.editor.putString("itemid", itemid);
		ConstantValues.editor.commit();
		i.putExtra("data", HomePageItems.get(position));
		i.putExtra("item_id", itemid);
		i.putExtra("sellerid", sellerid);
		i.putExtra("original", HomeFragment.urls.get(position));
		i.putExtra("comments", commentsMap);
		i.putExtra("fashionPhoto", photosMap);
		i.putExtra("likeUsers", likesMap);
		i.putExtra("position", position);
		i.putExtra("from", 11);
		startActivity(i);
	}

	public int deviceNumber() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int n = dm.densityDpi;
		Log.v("dpi", n + ":");
		return 0;

	}

	class SendFancy extends AsyncTask<Integer, Void, JSONObject> {

		String fancyUrl = ConstantValues.fantacyUrl;
		JSONParser jParser = new JSONParser();
		int itemId;
		ProgressDialog dialog = new ProgressDialog(
				MostPopular.this.getActivity());

		@Override
		protected JSONObject doInBackground(Integer... params) {
			itemId = params[0];
			fancyUrl = fancyUrl + 38 + "&itemId=" + 6;
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
						LinearLayout ll = (LinearLayout) gridView
								.getChildAt(itemId);
						TextView fancy = (TextView) ll
								.findViewById(R.id.fancyIt);
						fancy.setText("Fantacy'd");

					} else {
						Toast.makeText(getActivity(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						LinearLayout ll = (LinearLayout) gridView
								.getChildAt(itemId);
						TextView fancy = (TextView) ll
								.findViewById(R.id.fancyIt);
						fancy.setText("Fantacy");
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
			Log.v("shop clicked", "");
			fca.switchContent(new ShopFragment());
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_menu:
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			Log.v("present userid", "" + GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ProfileFragment());
			break;
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

}
