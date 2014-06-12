package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@SuppressLint({ "DefaultLocale", "UseSparseArrays" })
public class CategoryFragment extends SherlockFragment implements
		OnScrollListener, OnItemClickListener, OnClickListener {

	Button category;
	public static String query;
	InputMethodManager imm;
	TextView nullText;
	String urlAddr;
	EditText searchbar;
	Button btn_search;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	Map<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> UserPageItems;
	public static ArrayList<HashMap<String, String>> prodPageItems;
	public static ArrayList<HashMap<String, String>> originalPageItems;
	private static ArrayList<HashMap<String, String>> firstChild = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> secondChild = new ArrayList<HashMap<String, String>>();
	List<String> colours = new ArrayList<String>();
	List<String> prices = new ArrayList<String>();
	List<String> listDataHeader = new ArrayList<String>();
	HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
	Dialog dialog;
	protected static String Color = null, price = null, newest = null,
			MainCategory = null, sub1Text = null, sub2Text = null, sub1 = null,
			sub2 = null;
	CharSequence[] newestda = { "Plus récents", "Plus anciens" };
	static int screenWidth = 0;
	protected static String sub3;

	private ImageLoader homeImageLoader;
	public static ArrayList<HashMap<String, String>> HomePageItems;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = null;
	private int previousTotal = 0;
	private int visibleThreshold = 0;
	private boolean loading = true;
	private int currentPage = 0;
	static AdapterForHdpi hdpiAdapter;
	LinearLayout bottomLoading;
	private StaggeredGridView gridView;
	Context context;
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
	private ImageButton home, near, shop, alert, profile;
	String u1name, u1add, u1img;
	ArrayList<HashMap<String, String>> datas = null;
	ArrayList<HashMap<String, String>> datas2 = null;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
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
		final View v = inflater.inflate(R.layout.category_gridview, container,
				false);
		return v;
	}

	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
		ConstantValues.pref = getActivity()
				.getSharedPreferences("fantacyid", 0);
		ConstantValues.editor = ConstantValues.pref.edit();

		nullText = (TextView) getView().findViewById(R.id.searchNull);
		nullText.setVisibility(View.INVISIBLE);

		category = (Button) getView().findViewById(R.id.category);
		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		profile = (ImageButton) getView().findViewById(R.id.btn_profile);

		category.setOnClickListener(this);
		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		profile.setOnClickListener(this);

		context = CategoryFragment.this.getActivity();
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		bottomLoading = (LinearLayout) getView().findViewById(R.id.bottomhome);
		bottomLoading.setVisibility(View.VISIBLE);
		mainLayout = (RelativeLayout) getView().findViewById(R.id.main);
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		 commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		 photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		mainLayout.setVisibility(View.INVISIBLE);
		UserPageItems = new ArrayList<HashMap<String, String>>();
		originalPageItems = new ArrayList<HashMap<String, String>>();
		prodPageItems = new ArrayList<HashMap<String, String>>();
		HomePageItems = new ArrayList<HashMap<String, String>>();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		LoagingLayout = (RelativeLayout) (getView()
				.findViewById(R.id.loadImageLayout));
		LoagingLayout.setVisibility(View.INVISIBLE);
		gridView = (StaggeredGridView) (getView().findViewById(R.id.homePage_gallery));
		gridView.setSmoothScrollbarEnabled(true);
		gridView.setOnScrollListener(CategoryFragment.this);
		gridView.setOnItemClickListener(this);
		setColumns();
		loadData();

		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.category);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

	}

	@SuppressWarnings("deprecation")
	private void setColumns() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		if (screenWidth < 280
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = LDPI_PORTRAID;
			//gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = LDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = MDPI_PORTRAID;
			//gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = MDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = HDPI_PORTRAID;
			//gridView.setNumColumns(1);
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
				//gridView.setNumColumns(1);
			}
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 1400
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = XHDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else {
			device = HDPI_PORTRAID;
			//gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		}
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		HomePageItems = new ArrayList<HashMap<String, String>>();
		try {
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("page", "0");
			dataMap.put("url", ConstantValues.shopFilter);
			new homePageLoadImages().execute(dataMap);
		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}
		setAdapter();

	}

	@SuppressWarnings("deprecation")
	private void setAdapter() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		setColumns();
		hdpiAdapter = new AdapterForHdpi(getActivity());
		if (screenWidth < 600) {
			gridView.setSelection(currentPage * 10);
		}
		boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
		if (!tabletSize) {
			gridView.setSelection(currentPage * 10);
		}

		gridView.setAdapter(hdpiAdapter);
	}

	class homePageLoadImages extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				CategoryFragment.this.getActivity());

		@Override
		protected Void doInBackground(HashMap<String, String>... params) {
			HashMap<String, String> tmpMap = params[0];
			String page = tmpMap.get("page");
			String url = tmpMap.get("url");
			try {
				parsing(Integer.parseInt(page), url);
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			LoagingLayout.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				mainLayout.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (currentPage == 0) {
					mainLayout.setVisibility(View.VISIBLE);
				}
				bottomLoading.setVisibility(View.GONE);
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}

	}

	private void parsing(Integer page, String url) {

		JSONArray items;
		HashMap<String, String> map;
		urlAddr = url + "&offset=" + page * 10;
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
		}
		JSONParser jParser = new JSONParser();
		Log.v("urlAddr",""+urlAddr);
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
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
					HomePageItems.add(map);

				}
				Log.v("searchpageitems",""+HomePageItems);
			} else {
				CategoryFragment.this.getActivity().runOnUiThread(
						new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), "No Data Found",
										Toast.LENGTH_LONG).show();

							}
						});

			}
		} catch (JSONException e) {
			e.printStackTrace();
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
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(layout, parent, false);
			} else {
				view = convertView;
				view.forceLayout();
			}

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
			final LinearLayout commentsLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_comments);
			TextView title = (TextView) view
					.findViewById(R.id.single_item_bottom_title);
			TextView cost = (TextView) view
					.findViewById(R.id.single_item_bottom_cost);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final LinearLayout fashionLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_upload);
			final TextView photosCount = (TextView) view
					.findViewById(R.id.photocount);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);
			final LinearLayout likesLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_fantacy);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);
			image.setTag(position);
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
					fca.switchContent(new ProfileFragment());

				}
			});

			/*fashionLayout.setOnClickListener(new OnClickListener() {

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
					datas = HomePageItems;
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
			// I load the next page of thumbnails using a background task,
			setUpFilter();
		}

	}

	@SuppressWarnings("unchecked")
	private void setUpFilter() {
		setAdapter();
		
		HashMap<String, String> seleMap = new HashMap<String, String>();
		HashMap<String, String> seleMap2 = new HashMap<String, String>();
		HashMap<String, String> seleMap3 = new HashMap<String, String>();
		String url = ConstantValues.shopFilter;
		if (CategoryFragment.sub3 != null) {
			seleMap = map.get(CategoryFragment.sub3);
			seleMap2 = map.get(CategoryFragment.sub2);
			seleMap3 = map.get(CategoryFragment.sub1);
			url = url + "category=" + seleMap3.get("catId") + "&superCat="
					+ seleMap2.get("catId") + "&subCat=" + seleMap.get("catId");
		} else if (CategoryFragment.sub2 != null) {
			seleMap = map.get(CategoryFragment.sub2);
			seleMap2 = map.get(CategoryFragment.sub1);
			url = url + "category=" + seleMap2.get("catId") + "&superCat="
					+ seleMap.get("catId");
		} else if (CategoryFragment.sub1 != null) {
			seleMap = map.get(CategoryFragment.sub1);
			url = url + "category=" + seleMap.get("catId");
		}
		if (CategoryFragment.Color != null) {
			String s = CategoryFragment.Color;
			s = s.replace(' ', '_');
			url = url + "&color=" + s.toUpperCase();
			currentPage=0;
			Log.v("color",""+url);
		}
		if (CategoryFragment.newest != null) {
			if (CategoryFragment.newest.equalsIgnoreCase("Newest First")) {
				url = url + "&sorting=ASC";
			} else if (CategoryFragment.newest.equalsIgnoreCase("Oldest First")) {
				url = url + "&sorting=DESC";
			}
		}
		if (CategoryFragment.price != null) {
			String[] prices = price.split("-");
			if (prices.length == 2) {
				url = url + "&priceMin=" + prices[0] + "&priceMax=" + prices[1];
			} else if (prices.length == 1) {
				url = url + "&priceMin=" + prices[0];
			}
		}
		seleMap.put("url", url);
		seleMap.put("page", Integer.toString(currentPage));
		new homePageLoadImages().execute(seleMap);
		loading = true;
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

	class SendFancy extends AsyncTask<Integer, Void, JSONObject> {

		String fancyUrl = ConstantValues.fantacyUrl;
		JSONParser jParser = new JSONParser();
		int itemId;
		ProgressDialog dialog = new ProgressDialog(
				CategoryFragment.this.getActivity());

		@Override
		protected JSONObject doInBackground(Integer... params) {
			itemId = params[0];
			fancyUrl = fancyUrl + GetSet.getUserId() + "&itemId=" + itemId;
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
						HomePageItems.get(itemId).put(ConstantValues.TAG_LIKED,
								"Yes");
						hdpiAdapter.notifyDataSetChanged();

					} else {
						Toast.makeText(getActivity(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						HomePageItems.get(itemId).put(ConstantValues.TAG_LIKED,
								"No");
						hdpiAdapter.notifyDataSetChanged();
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.refresh, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		fca.switchContent(new CategoryFragment());
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {

		case R.id.category:
			if (list.size() == 0) {
				new ShopVales().execute();
			} else {
				resetValues();
				showDialog(0);
			}
			break;
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
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_profile:
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ProfileFragment());
		}
	}

	class ShopVales extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog = new ProgressDialog(
				CategoryFragment.this.getActivity());

		@Override
		protected Void doInBackground(Void... params) {
			parseValues();
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			listDataHeader.add("Category");
			List<String> everything = new ArrayList<String>();
			everything.add("Everything");
			listDataChild.put(listDataHeader.get(0), everything);
			for (int x = 0; x < list.size(); x++) {
			}
			showDialog(0);
		}

	}

	public void parseValues() {
		JSONParser parser = new JSONParser();
		JSONObject shopCategories = parser
				.getJSONFromUrl(ConstantValues.shopCategory);
		String stats;
		try {
			stats = shopCategories.getString(ConstantValues.status);
			if (stats.equalsIgnoreCase("true")) {
				JSONObject res = shopCategories.getJSONObject("result");
				JSONArray colors = res.getJSONArray("Color");
				for (int y = 0; y < colors.length(); y++) {
					colours.add(colors.getString(y));
				}

				JSONArray price = res.getJSONArray("Price");
				for (int z = 0; z < price.length(); z++) {
					prices.add(price.getString(z));
				}
				JSONObject category = res.getJSONObject("Category");
				JSONArray parent = category.getJSONArray("parent");
				for (int i = 0; i < parent.length(); i++) {
					HashMap<String, String> parentcate = new HashMap<String, String>();
					HashMap<String, String> childcate = new HashMap<String, String>();
					HashMap<String, String> secondchildcate = new HashMap<String, String>();
					JSONObject item = parent.getJSONObject(i);
					parentcate.put("catId", item.getString("id"));
					parentcate.put("id", item.getString("id"));
					parentcate.put("name", item.getString("name"));
					JSONArray subcate1 = item.getJSONArray("subcategory");
					int n1 = subcate1.length();
					if (n1 == 0) {
						parentcate.put("isSub", "false");
						map.put(item.getString("id"), parentcate);
						list.add(parentcate);
					} else {
						parentcate.put("isSub", "true");
						parentcate.put("subNos", Integer.toString(n1));
						map.put(item.getString("id"), parentcate);
						list.add(parentcate);
						for (int j = 0; j < subcate1.length(); j++) {
							JSONObject sub1onj = subcate1.getJSONObject(j);
							parentcate
									.put("subId" + j, sub1onj.getString("id"));
							childcate = new HashMap<String, String>();
							childcate.put("catId", sub1onj.getString("id"));
							childcate.put("id", sub1onj.getString("id"));
							childcate.put("name", sub1onj.getString("name"));
							JSONArray subcate2 = sub1onj
									.getJSONArray("subcategory");
							int n2 = subcate2.length();
							if (n2 == 0) {
								childcate.put("isSub", "false");
								map.put(sub1onj.getString("id"), childcate);
							} else {
								childcate.put("isSub", "true");
								childcate.put("subNos", Integer.toString(n2));
								map.put(sub1onj.getString("id"), childcate);
								for (int k = 0; k < subcate2.length(); k++) {
									JSONObject sub2onj = subcate2
											.getJSONObject(k);
									childcate.put("subId" + k,
											sub2onj.getString("id"));
									secondchildcate = new HashMap<String, String>();
									secondchildcate.put("catId",
											sub2onj.getString("id"));
									secondchildcate.put("id",
											sub2onj.getString("id"));
									secondchildcate.put("name",
											sub2onj.getString("name"));
									secondchildcate.put("isSub", "false");
									map.put(sub2onj.getString("id"),
											secondchildcate);
								}
							}
						}
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void showDialog(int i) {

		switch (i) {
		case 0:
			dialog = new Dialog(CategoryFragment.this.getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.shop_filter);
			TextView color = (TextView) dialog.findViewById(R.id.color);
			color.setWidth(screenWidth * 3 / 4);
			TextView price = (TextView) dialog.findViewById(R.id.price);
			TextView category = (TextView) dialog.findViewById(R.id.category);
			TextView newest = (TextView) dialog.findViewById(R.id.newest);
			TextView sub1 = (TextView) dialog.findViewById(R.id.sub1);
			sub1.setVisibility(View.GONE);
			TextView sub2 = (TextView) dialog.findViewById(R.id.sub2);
			sub2.setVisibility(View.GONE);
			Button cancel = (Button) dialog.findViewById(R.id.filterCancel);
			Button apply = (Button) dialog.findViewById(R.id.filterApply);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					resetValues();
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
			apply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (dialog.isShowing()) {
						dialog.dismiss();

					}
					HomePageItems = new ArrayList<HashMap<String, String>>();
					hdpiAdapter.notifyDataSetChanged();
					setUpFilter();
				}
			});
			if (CategoryFragment.Color != null) {
				color.setText("Color :" + CategoryFragment.Color);
			}
			if (CategoryFragment.price != null) {
				price.setText("Price :$" + CategoryFragment.price);
			}
			if (CategoryFragment.newest != null) {
				newest.setText(CategoryFragment.newest);
			}
			if (CategoryFragment.sub1 != null) {
				HashMap<String, String> firstSubSelectedMap = map
						.get(CategoryFragment.sub1);
				if (firstSubSelectedMap.get("isSub").equalsIgnoreCase("true")) {
					sub1.setVisibility(View.VISIBLE);
					sub1.setText("Everything");
					sub2.setVisibility(View.GONE);
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					sub1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							CategoryFragment.secondChild = new ArrayList<HashMap<String, String>>();
							CategoryFragment.sub2 = null;
							CategoryFragment.sub3 = null;
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							showDialog(3);
						}
					});
				}
				String s = firstSubSelectedMap.get("name");
				category.setText(s);

			}
			if (CategoryFragment.sub2 != null) {
				HashMap<String, String> secondSubSelectedMap = map
						.get(CategoryFragment.sub2);
				if (secondSubSelectedMap.get("isSub").equalsIgnoreCase("true")) {
					sub2.setVisibility(View.VISIBLE);
					sub2.setText("Everything");
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					sub2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							CategoryFragment.sub3 = null;
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							showDialog(4);
						}
					});

				} else {
					sub2.setVisibility(View.GONE);
				}
				String s = secondSubSelectedMap.get("name");
				sub1.setText(s);
			}
			if (CategoryFragment.sub3 != null) {
				HashMap<String, String> thirdSubSelectedMap = map
						.get(CategoryFragment.sub3);
				String s = thirdSubSelectedMap.get("name");
				sub2.setText(s);
			}
			color.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (colours.size() > 0) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						showDialog(2);
					} else {
						new ShopVales().execute();
					}
				}
			});
			category.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CategoryFragment.firstChild = new ArrayList<HashMap<String, String>>();
					CategoryFragment.secondChild = new ArrayList<HashMap<String, String>>();
					CategoryFragment.sub1 = null;
					CategoryFragment.sub2 = null;
					CategoryFragment.sub3 = null;
					if (list.size() > 0) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						showDialog(1);
					} else {
						new ShopVales().execute();
					}
				}
			});
			price.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					if (prices.size() > 0) {

						CharSequence[] priceListData = prices
								.toArray(new CharSequence[prices.size()]);
						AlertDialog.Builder builder = new AlertDialog.Builder(
								CategoryFragment.this.getActivity());
						builder.setTitle("Sélectionnez Prix");
						builder.setItems(priceListData,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										String s = prices.get(item);
										CategoryFragment.price = s;
										showDialog(0);

									}
								}).show();
					} else {
						new ShopVales().execute();
					}
				}
			});
			newest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CategoryFragment.this.getActivity());
					builder.setTitle("Sélectionnez ");
					builder.setItems(newestda,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									switch (item) {
									case 0:
										CategoryFragment.newest = newestda[0]
												.toString();
										break;
									case 1:
										CategoryFragment.newest = newestda[1]
												.toString();
										break;
									default:
										CategoryFragment.newest = null;
										break;

									}
									showDialog(0);

								}
							}).show();
				}
			});
			dialog.show();
			break;
		case 1:
			final Dialog dialog3 = new Dialog(
					CategoryFragment.this.getActivity());
			dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog3.setContentView(R.layout.filter_color_list);
			TextView everything = (TextView) dialog3
					.findViewById(R.id.everything);
			everything.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CategoryFragment.sub1 = null;
					dialog3.dismiss();
					showDialog(0);
				}
			});
			ListView categoryList = (ListView) dialog3
					.findViewById(R.id.colorList);
			ParentShower categoryadapter = new ParentShower(
					CategoryFragment.this.getActivity(), list);
			categoryList.setAdapter(categoryadapter);
			categoryList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					HashMap<String, String> selectedMap = list.get(arg2);
					String issub = selectedMap.get("isSub");
					if (issub.equalsIgnoreCase("true")) {
						CategoryFragment.sub1 = selectedMap.get("catId");
						dialog3.dismiss();
						showDialog(0);
					} else {
						CategoryFragment.sub1 = selectedMap.get("catId");
						dialog3.dismiss();
						showDialog(0);
					}
				}

			});
			dialog3.show();
			break;
		case 2:
			final Dialog dialog2 = new Dialog(
					CategoryFragment.this.getActivity());
			dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog2.setContentView(R.layout.filter_color_list);
			TextView everything2 = (TextView) dialog2
					.findViewById(R.id.everything);
			everything2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CategoryFragment.Color = null;
					dialog2.dismiss();
					showDialog(0);
				}
			});
			ListView colorList = (ListView) dialog2
					.findViewById(R.id.colorList);
			LazyAdapter adapter = new LazyAdapter(
					CategoryFragment.this.getActivity(), colours);
			colorList.setAdapter(adapter);
			colorList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String jsn = colours.get(arg2);
					try {
						JSONObject colorJson = new JSONObject(jsn);
						String colorname = colorJson.getString("color");
						CategoryFragment.Color = colorname;
						dialog2.dismiss();
						showDialog(0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			});
			dialog2.show();
			break;
		case 3:
			if (firstChild.size() == 0) {
				HashMap<String, String> firstParent = new HashMap<String, String>();
				firstParent = map.get(CategoryFragment.sub1);
				String nos = firstParent.get("subNos");
				for (int x = 0; x < Integer.parseInt(nos); x++) {
					String no = "subId" + x;
					String id = firstParent.get(no);
					HashMap<String, String> childMap = map.get(id);
					firstChild.add(childMap);
				}
			}
			final Dialog dialog4 = new Dialog(
					CategoryFragment.this.getActivity());
			dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog4.setContentView(R.layout.filter_color_list);
			TextView everything3 = (TextView) dialog4
					.findViewById(R.id.everything);
			everything3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CategoryFragment.sub2 = null;
					dialog4.dismiss();
					showDialog(0);
				}
			});
			ListView subcategoryList = (ListView) dialog4
					.findViewById(R.id.colorList);
			ParentShower subcategoryadapter = new ParentShower(
					CategoryFragment.this.getActivity(), firstChild);
			subcategoryList.setAdapter(subcategoryadapter);
			subcategoryList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					HashMap<String, String> selectedMap = firstChild.get(arg2);
					CategoryFragment.sub2 = selectedMap.get("catId");
					dialog4.dismiss();
					showDialog(0);
				}

			});
			dialog4.show();
			break;
		case 4:
			if (secondChild.size() == 0) {
				HashMap<String, String> firstParent = new HashMap<String, String>();
				firstParent = map.get(CategoryFragment.sub2);
				String nos = firstParent.get("subNos");
				for (int x = 0; x < Integer.parseInt(nos); x++) {
					String no = "subId" + x;
					String id = firstParent.get(no);
					HashMap<String, String> childMap = map.get(id);
					secondChild.add(childMap);
				}
			}
			final Dialog dialog5 = new Dialog(
					CategoryFragment.this.getActivity());
			dialog5.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog5.setContentView(R.layout.filter_color_list);
			TextView everything4 = (TextView) dialog5
					.findViewById(R.id.everything);
			everything4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CategoryFragment.sub3 = null;
					dialog5.dismiss();
					showDialog(0);
				}
			});
			ListView subcategoryList2 = (ListView) dialog5
					.findViewById(R.id.colorList);
			ParentShower subcategoryadapter2 = new ParentShower(
					CategoryFragment.this.getActivity(), secondChild);
			subcategoryList2.setAdapter(subcategoryadapter2);
			subcategoryList2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					HashMap<String, String> selectedMap = secondChild.get(arg2);
					CategoryFragment.sub3 = selectedMap.get("catId");
					dialog5.dismiss();
					showDialog(0);
				}

			});
			dialog5.show();
			break;
		}
	}

	protected void resetValues() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		CategoryFragment.Color = null;
		CategoryFragment.price = null;
		CategoryFragment.newest = null;
		CategoryFragment.MainCategory = null;
		CategoryFragment.sub1 = null;
		CategoryFragment.sub2 = null;
	}

	public class LazyAdapter extends BaseAdapter {

		private Activity activity;
		private List<String> data;
		private LayoutInflater inflater = null;

		public LazyAdapter(Activity a, List<String> d) {
			activity = a;
			data = d;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.color_list_row, null);

			TextView colorText = (TextView) vi.findViewById(R.id.colorText);
			colorText.setWidth(screenWidth * 3 / 4);
			TextView colorz = (TextView) vi.findViewById(R.id.colorColor);
			String jsn = data.get(position);
			try {
				JSONObject colorJson = new JSONObject(jsn);
				String colorname = colorJson.getString("color");
				String colorcode = colorJson.getString("code");
				colorText.setText(colorname);
				colorz.setBackgroundColor(Integer.parseInt(
						colorcode.substring(1, colorcode.length()), 16) + 0xFF000000);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return vi;
		}
	}

	public class ParentShower extends BaseAdapter {

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater = null;

		public ParentShower(Activity a, ArrayList<HashMap<String, String>> d) {
			activity = a;
			data = d;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.color_list_row, null);

			HashMap<String, String> listitem = data.get(position);
			TextView colorText = (TextView) vi.findViewById(R.id.colorText);
			TextView colorz = (TextView) vi.findViewById(R.id.colorColor);
			colorz.setVisibility(View.GONE);
			colorText.setWidth(screenWidth * 3 / 4);
			colorText.setText(listitem.get("name"));
			return vi;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int arg2, long id) {
		Intent i = new Intent(CategoryFragment.this.getActivity(),
				DetailActivity.class);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap = HomePageItems.get(arg2);
		String itemid = tempMap.get(ConstantValues.TAG_ID);
		String sellerid = tempMap.get(ConstantValues.TAG_SELLERID);
		ConstantValues.editor.putString("itemid", itemid);
		ConstantValues.editor.commit();
		i.putExtra("data", HomePageItems.get(arg2));
		i.putExtra("item_id", itemid);
		i.putExtra("sellerid", sellerid);
		i.putExtra("comments", commentsMap);
		i.putExtra("fashionPhoto", photosMap);
		i.putExtra("likeUsers", likesMap);
		i.putExtra("position", arg2);
		i.putExtra("from", 12);
		startActivity(i);

	}
}
