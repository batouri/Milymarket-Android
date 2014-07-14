package com.hitasoft.apps.milymarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.hitasoft.apps.milymarket.FollowersActivity.FollowResults;
import com.hitasoft.apps.milymarket.FollowersActivity.UnFollowResults;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@SuppressLint({ "DefaultLocale", "UseSparseArrays", "ResourceAsColor" })
public class ShopFragment extends SherlockFragment implements OnScrollListener,
		OnItemClickListener, OnClickListener {

	// Button category;
	public static String query;
	InputMethodManager imm;
	TextView nullText;
	String urlAddr;
	EditText searchbar;
	Button btn_search;
	HashMap<String, String> tempMap =null;
	Map<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
	Map<String, HashMap<String, String>> map1 = new HashMap<String, HashMap<String, String>>();
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
	CharSequence[] newestda = { "Newest First", "Oldest First" };
	static int screenWidth = 0;
	protected static String sub3;

	private ImageLoader homeImageLoader;
	// private GridView gridView = null;
	public static ArrayList<HashMap<String, String>> HomePageItems=null;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = null;
	private int previousTotal = 0;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
	private int visibleThreshold = 0;
	private boolean loading = true;
	private int currentPage = 0;
    AdapterForHdpi hdpiAdapter;
	LinearLayout bottomLoading;
	private StaggeredGridView gridView;
	Context context;
	Button article, members, boutiques;
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
	private ImageButton home, near, shop, alert, menu;
	String u1name, u1add, u1img;
	ArrayList<HashMap<String, String>> datas = null;
	ArrayList<HashMap<String, String>> datas2 = null;
	AdapterForHdpis hdpiAdapterprofile;
	AdapterForHdpip hdpiAdapterplaces;
	ArrayList<HashMap<String, String>> followingdatas;
	ArrayList<HashMap<String, String>> findPlaces;
	ImageLoader profileLoader;
	ImageLoader placeloader;
	boolean art, mem, bou, memfollow, memunfollow, boufollow, bouunfollow;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;
	String uId;
	AlertDialog adialog;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeImageLoader = ImageLoader.getInstance();
		profileLoader = ImageLoader.getInstance();
		placeloader = ImageLoader.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.shop_gridview, container,
				false);
		return v;
	}

	@SuppressLint({ "UseSparseArrays", "ResourceAsColor" })
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
		ConstantValues.pref = getActivity()
				.getSharedPreferences("fantacyid", 0);
		// ConstantValues.pref = getActivity().getSharedPreferences("BlueDot",);
		ConstantValues.editor = ConstantValues.pref.edit();

		nullText = (TextView) getView().findViewById(R.id.searchNull);
		nullText.setVisibility(View.INVISIBLE);
		
		    adialog=new AlertDialog.Builder(ShopFragment.this.getActivity()).create();
			adialog.setTitle("Alert");
			adialog.setMessage("S'il vous plaît connecter Pour continuer!!!");
			adialog.setButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.v("alert","alert");
					
				}
			});

		btn_search = (Button) getView().findViewById(R.id.btn_search);
		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);
		shop.setImageResource(R.drawable.tab_bar_shop_selected);

		btn_search.setOnClickListener(this);
		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		searchbar = (EditText) getView().findViewById(R.id.searchbar);
		context = ShopFragment.this.getActivity();
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		bottomLoading = (LinearLayout) getView().findViewById(R.id.bottomhome);
		bottomLoading.setVisibility(View.VISIBLE);
		mainLayout = (RelativeLayout) getView().findViewById(R.id.main);
		mainLayout.setVisibility(View.INVISIBLE);
		// ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
		// .setTitle("Shop");
		tempMap = new HashMap<String, String>();
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		UserPageItems = new ArrayList<HashMap<String, String>>();
		originalPageItems = new ArrayList<HashMap<String, String>>();
		prodPageItems = new ArrayList<HashMap<String, String>>();
		HomePageItems = new ArrayList<HashMap<String, String>>();
		followingdatas = new ArrayList<HashMap<String, String>>();
		findPlaces = new ArrayList<HashMap<String, String>>();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		LoagingLayout = (RelativeLayout) (getView()
				.findViewById(R.id.loadImageLayout));
		LoagingLayout.setVisibility(View.INVISIBLE);
		gridView = (StaggeredGridView) (getView().findViewById(R.id.homePage_gallery));
		gridView.setSmoothScrollbarEnabled(true);
		gridView.setOnScrollListener(ShopFragment.this);
		gridView.setOnItemClickListener(this);
		((FragmentChangeActivity) getActivity())
				.getSupportActionBar()
				.setTitle(getActivity().getResources().getString(R.string.home));

		// SearchManager searchManager = (SearchManager)
		// getActivity().getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView) findViewById(R.id.searchbar);
		// if (null != searchView) {
		// searchView.setSearchableInfo(searchManager
		// .getSearchableInfo(getActivity().getComponentName()));
		// searchView.setIconifiedByDefault(false);
		// // searchView.setBackgroundColor("#000000");
		// }
		//
		// SearchView.OnQueryTextListener queryTextListener = new
		// SearchView.OnQueryTextListener() {
		// public boolean onQueryTextChange(String newText) {
		// // this is your adapter that will be filtered
		// return true;
		// }
		//
		// public boolean onQueryTextSubmit(String query) {
		// Intent i = new Intent(getActivity(),
		// SearchActivity.class);
		// i.putExtra("query", query);
		// startActivity(i);
		// return true;
		// }
		// };
		// searchView.setOnQueryTextListener(queryTextListener);
		// category = (Button) getView().findViewById(R.id.category);

		// category.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (list.size() == 0) {
		// new ShopVales().execute();
		// } else {
		// resetValues();
		// showDialog(0);
		// }
		// }
		// });
		// expListView.setOnGroupClickListener(new OnGroupClickListener() {
		//
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		// // Toast.makeText(getApplicationContext(),
		// // "Group Clicked " + listDataHeader.get(groupPosition),
		// // Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });
	    uId = ConstantValues.pref.getString("userprefid", "");
		Log.v("uId", "uuuuuuu" + uId);


		setColumns();
		loadData();
		shop.setImageResource(R.drawable.tab_bar_shop_selected);
		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.search);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

		/*
		 * ((FragmentChangeActivity) getActivity()) .getSupportActionBar()
		 * .setTitle(getActivity().getResources().getString(R.string.shop));
		 */
		// new ShopVales().execute();
		article = (Button) (getView().findViewById(R.id.txarticle));
		members = (Button) (getView().findViewById(R.id.txmembers));
		boutiques = (Button) (getView().findViewById(R.id.txboutiques));

		art = true;
		mem = false;
		bou = false;

		article.setBackgroundResource(R.color.pale_blue);
		members.setBackgroundResource(R.color.white);
		boutiques.setBackgroundResource(R.color.white);

		article.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View arg0) {

				art = true;
				mem = false;
				bou = false;
				currentPage=0;
				Log.v("article", "article clicked");
				article.setBackgroundResource(R.color.pale_blue);
				members.setBackgroundResource(R.color.white);
				boutiques.setBackgroundResource(R.color.white);
				nullText.setVisibility(View.INVISIBLE);
				setColumns();
				loadData();

			}

		});
		members.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				Log.v("members", "members clicked");
				article.setBackgroundResource(R.color.white);
				members.setBackgroundResource(R.color.pale_blue);
				boutiques.setBackgroundResource(R.color.white);
				art = false;
				mem = true;
				bou = false;
				nullText.setVisibility(View.INVISIBLE);
				setColumns();
				loadfindfriends();
			}

		});
		boutiques.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("boutiques", "boutiques clicked");
				article.setBackgroundResource(R.color.white);
				members.setBackgroundResource(R.color.white);
				boutiques.setBackgroundResource(R.color.pale_blue);
				art = false;
				mem = false;
				bou = true;
				nullText.setVisibility(View.INVISIBLE);
				setColumns();
				loadfindplace();

			}

		});

	}

	@SuppressWarnings("deprecation")
	private void setColumns() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
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
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		}
		// setGridViewItems();
	}

	/*
	 * private void setGridViewItems() { switch (device) { case LDPI_PORTRAID:
	 * gridView.setNumColumns(1); break; case LDPI_LANDSCAPE:
	 * gridView.setNumColumns(1); break; case MDPI_PORTRAID:
	 * gridView.setNumColumns(1); break; case MDPI_LANDSCAPE:
	 * gridView.setNumColumns(2); break; case HDPI_PORTRAID:
	 * gridView.setNumColumns(1); break; case HDPI_LANDSCAPE:
	 * gridView.setNumColumns(2); break; case LARGE_MDPI_PORTRAID:
	 * gridView.setNumColumns(2); break; case LARGE_MDPI_LANDSCAPE:
	 * gridView.setNumColumns(3); break; case XHDPI_PORTRAID:
	 * gridView.setNumColumns(3); break; case XHDPI_LANDSCAPE:
	 * gridView.setNumColumns(4); break; default: gridView.setNumColumns(1);
	 * break; } }
	 */

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

	@SuppressWarnings("unchecked")
	private void loadDataSearch() {

		// Log.v("lsearch",""+HomePageItems);
		try {
			HashMap<String, String> datas = new HashMap<String, String>();
			if (art == true && mem == false && bou == false) {

				HomePageItems = new ArrayList<HashMap<String, String>>();
				datas.put("page", "0");
				datas.put("url", ConstantValues.searchUrl);
				new SearchpageLoadImages().execute(datas);
				setAdapter();
			} else if (art == false && mem == true && bou == false) {
				followingdatas = new ArrayList<HashMap<String, String>>();
				datas.put("page", "0");
				datas.put("url", ConstantValues.findFriendsSearch);
				new SearchFriends().execute(datas);
				setAdapterFF();
			} else if (art == false && mem == false && bou == true) {
				findPlaces = new ArrayList<HashMap<String, String>>();
				datas.put("page", "0");
				datas.put("url", ConstantValues.find_place);
				new SearchPlaces().execute(datas);
				setAdapterFP();
			}

		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}

	}

	private void loadfindfriends() {
		Log.v("pref", "loadfindfriends");
		followingdatas = new ArrayList<HashMap<String, String>>();
		try {
			if (GetSet.getUserId() != null) {
				String uId = GetSet.getUserId();
				Log.v("in", uId);

				// String uId = ConstantValues.pref.getString("userprefid");
				// Log.v("pref",""+uId);
				new GetFollowerDetail().execute(Integer.parseInt(uId));
				// new GetFollowerDetail().execute(Integer.parseInt(uId));
			}
			else{
				new GetFollowerDetail().execute(0);
			}
		} catch (Exception e) {
			Log.v("bug", e.toString());
		}
		setAdapterFF();
	}

	private void loadfindplace() {
		findPlaces = new ArrayList<HashMap<String, String>>();
		try {
			// String uId = ConstantValues.pref.getString("userprefid", "");
			new GetFindPlace().execute();
			// new GetFollowerDetail().execute(Integer.parseInt(uId));

		} catch (Exception e) {
			Log.v("bug", e.toString());
		}
		setAdapterFP();
	}

	// public void setAdapter() {
	//
	// Display display = getActivity().getWindowManager().getDefaultDisplay();
	// @SuppressWarnings("deprecation")
	// int screenWidth = display.getWidth();
	// // int screenHeight = display.getHeight();
	// // if (screenWidth > 700) {
	// // hdpiAdapterLand = new AdapterForHdpiLand(getActivity());
	// // gridView.setAdapter(hdpiAdapterLand);
	// // } else {
	// setColumns();
	// hdpiAdapter = new AdapterForHdpi(getActivity());
	// /*
	// * if (screenWidth < 600) { gridView.setSelection(currentPage * 10); }
	// * boolean tabletSize = getResources().getBoolean(R.bool.isTablet); if
	// * (!tabletSize) { gridView.setSelection(currentPage * 10); }
	// */
	// gridView.setAdapter(hdpiAdapter);
	// // }
	// }

	private void setAdapter() {

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		// int screenHeight = display.getHeight();
		// if (screenWidth > 700) {
		// hdpiAdapterLand = new AdapterForHdpiLand(getActivity());
		// gridView.setAdapter(hdpiAdapterLand);
		// } else {
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
		hdpiAdapter.notifyDataSetChanged();

		// }
	}

	private void setAdapterFF() {
		try {
			hdpiAdapterprofile = new AdapterForHdpis(
					ShopFragment.this.getActivity());
			// setColumns();
			gridView.setAdapter(hdpiAdapterprofile);
			hdpiAdapterprofile.notifyDataSetChanged();

		} catch (Exception e) {

		}
	}

	private void setAdapterFP() {
		try {
			hdpiAdapterplaces = new AdapterForHdpip(
					ShopFragment.this.getActivity());
			// setColumns();
			gridView.setAdapter(hdpiAdapterplaces);
			hdpiAdapterplaces.notifyDataSetChanged();

		} catch (Exception e) {

		}
	}

	class homePageLoadImages extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				ShopFragment.this.getActivity());

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
			//bottomLoading.setVisibility(View.VISIBLE);
			//LoagingLayout.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				// dialog.setMessage("Please wait...");
				// dialog.setIndeterminate(true);
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.show();
				bottomLoading.setVisibility(View.VISIBLE);
				mainLayout.setVisibility(View.INVISIBLE);
			}
			else{
				LoagingLayout.setVisibility(View.VISIBLE);
			}
			// bottomLoading.setVisibility(View.VISIBLE);

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
				
				
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}

	}

	private void parsing(Integer page, String url) {

		JSONArray items;
		HashMap<String, String> map, map1, map2, map3;
		urlAddr = url + "offset=" + page * 10;
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
		}
		Log.v("searchpage",""+urlAddr.trim());
		JSONParser jParser = new JSONParser();
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
					Log.v("selerid", "" + sellerid);
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
						// map1 = new HashMap<String, String>();
						// map2 = new HashMap<String, String>();
						// map3 = new HashMap<String, String>();
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
						/*
						 * //map1.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
						 * item_url_main_350);
						 * map2.put(ConstantValues.TAG_USER_URL_MAIN_350,
						 * user_url_main_350);
						 * map3.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
						 * item_url_main_original); UserPageItems.add(map2);
						 * prodPageItems.add(map1); originalPageItems.add(map3);
						 */
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

						/*
						 * u1name = seller; u1add = shopname; u1img =
						 * user_url_main_350 ; Log.v("u1img",""+u1img);
						 * Log.v("uname",""+u1name);
						 */
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
					// map.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
					// item_url_main_original);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					map.put(ConstantValues.TAG_HEIGHT, height);
					map.put(ConstantValues.TAG_WIDTH, width);
					HomePageItems.add(map);

				}
			} else {
				ShopFragment.this.getActivity().runOnUiThread(new Runnable() {

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

	class SearchpageLoadImages extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected Void doInBackground(HashMap<String, String>... param) {
			/*
			 * try { Log.v("search",""+params[0]); parsing(params[0]); } catch
			 * (Exception e) {
			 * 
			 * }
			 */
			HashMap<String, String> tmp = param[0];
			String page = tmp.get("page");
			String url = tmp.get("url");
			try {
				parsi(Integer.parseInt(page), url);
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			bottomLoading.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				// dialog.setMessage("Please wait...");
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.setIndeterminate(true);
				// dialog.show();
				// bottom.setVisibility(View.VISIBLE);
				mainLayout.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (currentPage == 0) {
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
					// bottom.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
				}
				bottomLoading.setVisibility(View.GONE);
				LoagingLayout.setVisibility(View.GONE);

				if (HomePageItems.size() == 0) {
					nullText.setVisibility(View.VISIBLE);
				}
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}

	}

	private void parsi(Integer page, String url) {

		JSONArray items;
		HashMap<String, String> map;
		// String urlAddr = ConstantValues.searchUrl + query + "&offset=0"; // +
		String urlAddr = url + query + "&offfset=0";
		Log.v("out", urlAddr); // page*
		// 10;
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
			Log.v("in", urlAddr);
		}
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		Log.v("center", urlAddr);
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null, item_url_main_350 = null, item_url_main_original = null, user_url_main_350 = null;
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
					for (int j = 0; j < 1; j++) {
						JSONObject photosTemp = photos.getJSONObject(j);
						item_url_main_70 = photosTemp
								.getString(ConstantValues.TAG_ITEM_URL_MAIN_70);
						item_url_main_350 = photosTemp
								.getString(ConstantValues.TAG_ITEM_URL_MAIN_350);
						user_url_main_350 = photosTemp
								.getString(ConstantValues.TAG_USER_URL_MAIN_350);
						height=photosTemp.getString(ConstantValues.TAG_HEIGHT);
						width=photosTemp.getString(ConstantValues.TAG_WIDTH);
						item_url_main_original = photosTemp
								.getString(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL);
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
					map.put(ConstantValues.TAG_SELLERID, sellerid);
					map.put(ConstantValues.TAG_SELLER, seller);
					map.put(ConstantValues.TAG_SHOP, shopname);
					map.put(ConstantValues.TAG_FAVORITES, favorites);
					map.put(ConstantValues.TAG_FASHIONCOUNT, fash_count);
					map.put(ConstantValues.TAG_LIKED, liked);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_70,
							item_url_main_70);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
							item_url_main_350);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
							item_url_main_original);
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
			Log.v("tempmap",""+tempMap);
			/*
			 * HashMap<String, String> usertemp = new HashMap<String, String>();
			 * usertemp = UserPageItems.get(position);
			 * 
			 * HashMap<String, String> producttemp = new HashMap<String,
			 * String>(); producttemp = prodPageItems.get(position);
			 */
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
			final LinearLayout likesLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_fantacy);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final LinearLayout fashionLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_upload);
			final TextView photosCount = (TextView) view
					.findViewById(R.id.photocount);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);
			image.setTag(position);
			// final TextView addtoList = (TextView) view
			// .findViewById(R.id.addtolistText);
			//
			// if (GetSet.isLogged()) {
			// String liked = tempMap.get(ConstantValues.TAG_LIKED);
			// if (liked.equalsIgnoreCase("yes")) {
			// fancy.setText(getResources().getString(R.string.unlike));
			// addtoList.setText(getResources().getString(
			// R.string.editList));
			// } else {
			// fancy.setText(getResources().getString(R.string.like));
			// addtoList.setText(getResources().getString(
			// R.string.addToList));
			// }
			// }
			// final LinearLayout fantacy = (LinearLayout) view
			// .findViewById(R.id.single_item_bottom_fantacy);
			// fantacy.setTag(position);
			// fantacy.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// if (GetSet.isLogged()) {
			// int point = (Integer) fantacy.getTag();
			// HashMap<String, String> map = HomePageItems.get(point);
			// String liked = map.get(ConstantValues.TAG_LIKED);
			// // if (liked.equalsIgnoreCase("yes")) {
			// // new SendFancy().execute()
			// // } else {
			// // fancy.setTextColor(Color.BLUE);
			// // }
			// new SendFancy().execute(point);
			// } else {
			// Toast.makeText(
			// getActivity(),
			// getResources().getString(
			// R.string.PleaseLoginToContinue),
			// Toast.LENGTH_LONG).show();
			// }
			// }
			// });
			// Log.v("commenttemp","size"+Integer.toString(commentTemp.size()));
			// comments.setText(Integer.toString(commentTemp.size()));
			// title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			// cost.setText("| $ " + tempMap.get(ConstantValues.TAG_PRICE));
			// fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			// seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			// String sel= tempMap.get(ConstantValues.TAG_SELLER);
			// Log.v("sellername", sel);
			// address.setText(tempMap.get(ConstantValues.TAG_SHOP));
			// String sho= tempMap.get(ConstantValues.TAG_SHOP);
			// Log.v("shopname", sho);
			if (commentTemp != null && comments != null) {
				comments.setText(Integer.toString(commentTemp.size()));
			}
			title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			cost.setText(" € " + tempMap.get(ConstantValues.TAG_PRICE));
			fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			// String sel = tempMap.get(ConstantValues.TAG_SELLER);
			// Log.v("sellername", sel);
			photosCount.setText(tempMap.get(ConstantValues.TAG_FASHIONCOUNT));
			address.setText(tempMap.get(ConstantValues.TAG_SHOP));
			// String sho = tempMap.get(ConstantValues.TAG_SHOP);
			// Log.v("shopname", sho);

			sellerimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// FragmentChangeActivity fc = new FragmentChangeActivity();
					// fc.switchContent(new ProfileFragment());
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					ConstantValues.editor.putString("userprefid",
							tempMap.get(ConstantValues.TAG_SELLERID));
					Log.v("present userid", "" + GetSet.getUserId());
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
							// final Bitmap bm =
							// Bitmap.createScaledBitmap(loadedImage, 480, 500,
							// );
							HashMap<String, String> map = HomePageItems
									.get(Integer.parseInt(image.getTag()
											.toString()));
							// final int id = Integer.parseInt(map
							// .get(ConstantValues.TAG_ID));
							//Handler handler = new Handler();
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
			return view;
		}
	}

	class GetFollowerDetail extends AsyncTask<Integer, Void, Void> {

		String url = ConstantValues.findfriendsUrl;
		ProgressDialog dialog = new ProgressDialog(
				ShopFragment.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			/* hdpiAdapterprofile.notifyDataSetChanged(); */
			int UserId = params[0];
			JSONParser parser = new JSONParser();

			JSONObject object = parser.getJSONFromUrl(url + UserId);

			Log.v("findfriends", "" + url + UserId);

			try {
				String status = object.getString(ConstantValues.status);
				Log.v("status", "" + status);
				if (status.equalsIgnoreCase("true")) {
					// JSONObject followers = result.getJSONObject("result");
					JSONArray followers = object.getJSONArray("result");

					for (int i = 0; i < followers.length(); i++) {
						Log.v("followers", "" + followers.length());

						HashMap<String, String> map = new HashMap<String, String>();

						JSONObject value = followers.getJSONObject(i);

						String userid = value.getString("UserId");
						String userstatus = value.getString("status");
						String userName = value.getString("userName");
						String fullname = value.getString("fullName");
						String profileImage = value.getString("imageName");

						map.put("userId", userid);
						map.put("status", userstatus);
						map.put(ConstantValues.TAG_USERNAME, userName);
						map.put(ConstantValues.TAG_FULLNAME, fullname);
						map.put("profileImage", profileImage);

						Log.v("map", map.toString());
						followingdatas.add(map);
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bottomLoading.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				// dialog.setMessage("Please wait...");
				// dialog.setIndeterminate(true);
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.show();

				mainLayout.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			try {
				if (currentPage == 0) {
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
					// bottom.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
				}
				bottomLoading.setVisibility(View.GONE);
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapterprofile.notifyDataSetChanged();
				if (followingdatas.size() == 0) {
					nullText.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}
			//setAdapterFF();

			/*
			 * if (dialog != null && dialog.isShowing()) { dialog.dismiss(); }
			 * setAdapterFF();
			 */
			// count.setText(Integer.toString(followingdatas.size()));
			// adapter.notifyDataSetChanged();
		}
	}

	class SearchFriends extends AsyncTask<HashMap<String, String>, Void, Void> {

		String url = ConstantValues.findfriendsUrl;
		ProgressDialog dialog = new ProgressDialog(
				ShopFragment.this.getActivity());

		@Override
		protected Void doInBackground(HashMap<String, String>... para) {
			/* hdpiAdapterprofile.notifyDataSetChanged(); */
			// int UserId = params[0];

			HashMap<String, String> tmp = para[0];
			String page = tmp.get("page");
			String url = tmp.get("url");
			String urlAddr = url + query + "&offfset=0";
			Log.v("out", urlAddr); // page*
			// 10;
			if (GetSet.getUserId() != null) {
				urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
				Log.v("in", urlAddr);
			}
			JSONParser jParser = new JSONParser();
			JSONObject object = jParser.getJSONFromUrl(urlAddr.trim());
			Log.v("searchfriends", "" + urlAddr);

			// JSONParser parser = new JSONParser();

			// JSONObject object = parser.getJSONFromUrl(url +);

			// Log.v("findfriends", "" + url + UserId);

			try {
				String status = object.getString(ConstantValues.status);
				Log.v("status", "" + status);
				if (status.equalsIgnoreCase("true")) {
					// JSONObject followers = result.getJSONObject("result");
					JSONArray followers = object.getJSONArray("result");

					for (int i = 0; i < followers.length(); i++) {
						Log.v("followers", "" + followers.length());

						HashMap<String, String> map = new HashMap<String, String>();

						JSONObject value = followers.getJSONObject(i);

						String userid = value.getString("UserId");
						String userName = value.getString("userName");
						Log.v("username",""+userName);
						String fullname = value.getString("fullName");
						String profileImage = value.getString("imageName");
						Log.v("profileimage",""+profileImage);
						String userstatus = value
								.getString(ConstantValues.TAG_STATUS);
						map.put("userId", userid);
						map.put(ConstantValues.TAG_USERNAME, userName);
						map.put(ConstantValues.TAG_FULLNAME, fullname);
						map.put("profileImage", profileImage);
						map.put(ConstantValues.TAG_STATUS, userstatus);

						Log.v("map", map.toString());
						followingdatas.add(map);
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bottomLoading.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				// dialog.setMessage("Please wait...");
				// dialog.setIndeterminate(true);
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.show();

				mainLayout.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			try {
				if (currentPage == 0) {
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
					// bottom.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);

				}
				LoagingLayout.setVisibility(View.GONE);
				bottomLoading.setVisibility(View.GONE);
				hdpiAdapterprofile.notifyDataSetChanged();
				if (followingdatas.size() == 0) {
					nullText.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}
			//setAdapterFF();

			/*
			 * if (dialog != null && dialog.isShowing()) { dialog.dismiss(); }
			 * setAdapterFF();
			 */
			// count.setText(Integer.toString(followingdatas.size()));
			// adapter.notifyDataSetChanged();
		}
	}

	class GetFindPlace extends AsyncTask<Integer, Void, Void> {

		String url = ConstantValues.find_place;
		ProgressDialog dialog = new ProgressDialog(
				ShopFragment.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			/* hdpiAdapterprofile.notifyDataSetChanged(); */
			// int UserId = params[0];
			if(GetSet.isLogged()==true){
			url=url+"&userId="+GetSet.getUserId();
			}
			else{
				url=url+"&userId=0";
			}
			JSONParser parser = new JSONParser();

			// JSONObject object = parser.getJSONFromUrl(url + UserId);
			JSONObject object = parser.getJSONFromUrl(url);
			Log.v("findPlace", "" + url);

			try {
				String status = object.getString(ConstantValues.status);
				Log.v("status", "" + status);
				if (status.equalsIgnoreCase("true")) {
					// JSONObject followers = result.getJSONObject("result");
					JSONArray result = object.getJSONArray("result");

					for (int i = 0; i < result.length(); i++) {
						Log.v("followers", "" + result.length());

						HashMap<String, String> map1 = new HashMap<String, String>();

						JSONObject value = result.getJSONObject(i);

						String userid = value.getString("UserId");
						String username = value.getString("userName");
						
						String fullname = value.getString("fullName");
						String Place = value.getString("place");
						String profileImage = value.getString("imageName");
						
						String userstatus = value
								.getString(ConstantValues.TAG_STATUS);
						map1.put("userId", userid);
						map1.put("userName", username);
						map1.put("fullName", fullname);
						map1.put("place", Place);
						map1.put("profileImage", profileImage);

						map1.put(ConstantValues.TAG_STATUS, userstatus);

						Log.v("map", map1.toString());
						findPlaces.add(map1);
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bottomLoading.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				// dialog.setMessage("Please wait...");
				// dialog.setIndeterminate(true);
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.show();

				mainLayout.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			try {
				if (currentPage == 0) {
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
					// bottom.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
				}
				LoagingLayout.setVisibility(View.GONE);
				bottomLoading.setVisibility(View.GONE);
				hdpiAdapterplaces.notifyDataSetChanged();
				if (findPlaces.size() == 0) {
					nullText.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}
			/*
			 * if (dialog != null && dialog.isShowing()) { dialog.dismiss(); }
			 */
			//setAdapterFP();
			// count.setText(Integer.toString(followingdatas.size()));
			// adapter.notifyDataSetChanged();
		}
	}

	class SearchPlaces extends AsyncTask<HashMap<String, String>, Void, Void> {

		// String url = ConstantValues.find_place;
		ProgressDialog dialog = new ProgressDialog(
				ShopFragment.this.getActivity());

		@Override
		protected Void doInBackground(HashMap<String, String>... para) {

			HashMap<String, String> tmp = para[0];
			String page = tmp.get("page");
			String url = tmp.get("url");
			String urlAddr = url + query + "&offfset=0";
			Log.v("out", urlAddr); // page*
			// 10;
			if (GetSet.getUserId() != null) {
				urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
				Log.v("in", urlAddr);
			}
			JSONParser jParser = new JSONParser();
			JSONObject object = jParser.getJSONFromUrl(urlAddr.trim());
			Log.v("searchPlace", "" + urlAddr);

			// Log.v("findPlace", "" + url);

			try {
				String status = object.getString(ConstantValues.status);
				Log.v("status", "" + status);
				if (status.equalsIgnoreCase("true")) {
					// JSONObject followers = result.getJSONObject("result");
					JSONArray result = object.getJSONArray("result");

					for (int i = 0; i < result.length(); i++) {
						Log.v("places", "" + result.length());

						HashMap<String, String> map1 = new HashMap<String, String>();

						JSONObject value = result.getJSONObject(i);

						String userid = value.getString("UserId");
						String userstatus = value.getString("status");
						String username = value.getString("userName");
						String fullname = value.getString("fullName");
						String Place = value.getString("place");
						String profileImage = value.getString("imageName");

						map1.put("userId", userid);
						map1.put("userName", username);
						map1.put("fullName", fullname);
						map1.put("place", Place);
						map1.put("profileImage", profileImage);

						map1.put("status", userstatus);

						Log.v("map", map1.toString());
						findPlaces.add(map1);
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bottomLoading.setVisibility(View.VISIBLE);
			if (currentPage == 0) {
				// dialog.setMessage("Please wait...");
				// dialog.setIndeterminate(true);
				// dialog.setCancelable(false);
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.show();

				mainLayout.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			try {
				if (currentPage == 0) {
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
					// bottom.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
				}
				LoagingLayout.setVisibility(View.GONE);
				bottomLoading.setVisibility(View.GONE);
				hdpiAdapterplaces.notifyDataSetChanged();
				if (findPlaces.size() == 0) {
					nullText.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}
			/*
			 * if (dialog != null && dialog.isShowing()) { dialog.dismiss(); }
			 */
			//setAdapterFP();
			// count.setText(Integer.toString(followingdatas.size()));
			// adapter.notifyDataSetChanged();
		}
	}

	public class AdapterForHdpis extends BaseAdapter {
		private Context mContext;

		public AdapterForHdpis(Context ctx) {
			this.mContext = ctx;
		}

		@Override
		public int getCount() {
			return followingdatas.size();
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

			tempMap = followingdatas.get(position);

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

			username.setText(tempMap.get("username"));
			String ustatus=tempMap.get(ConstantValues.TAG_STATUS);
			if(ustatus.equals("follow")){
				userstatus.setText("S'abonner");
			}
			else{
				userstatus.setText("Se désabonner");
			}

			if (userstatus.getText().toString().equals("S'abonner")) {
				memfollow = true;
				userstatus.setBackgroundColor(android.graphics.Color.RED);
			} else if (userstatus.getText().toString().equals("Se désabonner")) {
				memunfollow = true;
				userstatus.setBackgroundColor(android.graphics.Color.GRAY);
			}
			followuserid = tempMap.get("userId");
			if (followuserid.equals(GetSet.getUserId())) {
				userstatus.setVisibility(View.GONE);
			}
			fullname.setText(tempMap.get(ConstantValues.TAG_FULLNAME));
			userstatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(GetSet.isLogged()==true){
					Log.v("username", "" + username.getText().toString());
					Log.v("user status", "" + userstatus.getText().toString());
					if (userstatus.getText().equals("S'abonner")
							&& memfollow == true) {
						memfollow = false;
						Log.v("following", "following");
						follow(followuserid);
						userstatus.setBackgroundColor(android.graphics.Color.GRAY);
						userstatus.setText("Se désabonner");
					} else if (userstatus.getText().equals("Se désabonner")
							&& memunfollow == true) {
						memunfollow = false;
						Log.v("unnnnnfollowing", "unnnnnfollowing");
						unfollow(followuserid);
						userstatus.setBackgroundColor(android.graphics.Color.RED);
						userstatus.setText("S'abonner");
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
					ConstantValues.editor.putString("userprefid",followingdatas.get(position).get("userId"));
					ConstantValues.editor.commit();
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					FragmentChangeActivity.rsprofile = true;
					//FragmentChangeActivity.menumap = true;
					FragmentChangeActivity.filter_icon=false;
					getActivity().supportInvalidateOptionsMenu();
					fca.switchContent(new ProfileFragment());
			
					
				}
				
			});
			profileLoader.loadImage(tempMap.get("profileImage"),
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
							image.setImageBitmap(loadedImage);
							// final Bitmap bm =
							// Bitmap.createScaledBitmap(loadedImage, 480, 500,
							// );
							HashMap<String, String> map = followingdatas
									.get(Integer.parseInt(image.getTag()
											.toString()));
							/*
							 * final int id = Integer.parseInt(map
							 * .get(ConstantValues.TAG_ID)); Handler handler =
							 * new Handler(); // handler.post(new Runnable() {
							 *///
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
							loader.setVisibility(View.VISIBLE);
							image.setImageResource(R.drawable.usrimg);

						}
					});
			// notifyDataSetChanged();
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
				if (memfollow = false) {
					Log.v("loading", "loading");
					loadData();
				} else if (boufollow = false) {
					Log.v("loading", "loading");
					loadfindplace();
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
				if (memunfollow = false) {
					loadData();
				} else if (bouunfollow = false) {
					loadfindplace();
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

	public class AdapterForHdpip extends BaseAdapter {
		private Context mContext;

		public AdapterForHdpip(Context ctx) {
			this.mContext = ctx;
		}

		@Override
		public int getCount() {
			return findPlaces.size();
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

			tempMap = findPlaces.get(position);

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

			username.setText(tempMap.get("userName"));
		//	Log.v("username", "" + tempMap.get("userName"));
			String ustatus=tempMap.get(ConstantValues.TAG_STATUS);
			if(ustatus.equals("follow")){
				userstatus.setText("S'abonner");
			}
			else{
				userstatus.setText("Se désabonner");
			}
			if (userstatus.getText().toString().equals("S'abonner")) {
				boufollow = true;
				userstatus.setBackgroundColor(android.graphics.Color.RED);
			} else {
				bouunfollow = true;
				userstatus.setBackgroundColor(android.graphics.Color.GRAY);
			}
			followuserid = tempMap.get("userId");
			if (followuserid.equals(GetSet.getUserId())) {
				userstatus.setVisibility(View.GONE);
			}

			fullname.setText(tempMap.get("fullName"));
			userstatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(GetSet.isLogged()==true){
					if (userstatus.getText().equals("S'abonner")
							&& boufollow == true) {
						boufollow = false;
						follow(followuserid);
						userstatus.setBackgroundColor(android.graphics.Color.GRAY);
						userstatus.setText("Se désabonner");
					} else if (userstatus.getText().equals("Se désabonner")
							&& bouunfollow == true) {
						bouunfollow = false;
						unfollow(followuserid);
						userstatus.setBackgroundColor(android.graphics.Color.RED);
						userstatus.setText("S'abonner");
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
					ConstantValues.editor.putString("userprefid",findPlaces.get(position).get("userId"));
					ConstantValues.editor.commit();
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					FragmentChangeActivity.rsprofile = true;
					//FragmentChangeActivity.menumap = true;
					FragmentChangeActivity.filter_icon=false;
					getActivity().supportInvalidateOptionsMenu();
					fca.switchContent(new ProfileFragment());
			
					
				}
				
			});
			placeloader.loadImage(tempMap.get("profileImage"),
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
							image.setImageBitmap(loadedImage);
							// final Bitmap bm =
							// Bitmap.createScaledBitmap(loadedImage, 480, 500,
							// );
							HashMap<String, String> map = findPlaces
									.get(Integer.parseInt(image.getTag()
											.toString()));
							/*
							 * final int id = Integer.parseInt(map
							 * .get(ConstantValues.TAG_ID)); Handler handler =
							 * new Handler(); // handler.post(new Runnable() {
							 *///
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
							loader.setVisibility(View.VISIBLE);
							image.setImageResource(R.drawable.usrimg);

						}
					});
			// notifyDataSetChanged();
			return view;
		}
	}

	@SuppressWarnings("unchecked")
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
			HashMap<String, String> seleMap = new HashMap<String, String>();
			String url=ConstantValues.shopFilter;
			seleMap.put("url", url);
			Log.v("ur;", url);
			seleMap.put("page", Integer.toString(currentPage));
			new homePageLoadImages().execute(seleMap);
			loading = true;
			// I load the next page of thumbnails using a background task,
			//setUpFilter();
		}

	}

	@SuppressWarnings("unchecked")
	private void setUpFilter() {
		setAdapter();
		HashMap<String, String> seleMap = new HashMap<String, String>();
		HashMap<String, String> seleMap2 = new HashMap<String, String>();
		HashMap<String, String> seleMap3 = new HashMap<String, String>();
		String url = ConstantValues.shopFilter;
		if (ShopFragment.sub3 != null) {
			seleMap = map.get(ShopFragment.sub3);
			seleMap2 = map.get(ShopFragment.sub2);
			seleMap3 = map.get(ShopFragment.sub1);
			url = url + "category=" + seleMap3.get("catId") + "&superCat="
					+ seleMap2.get("catId") + "&subCat=" + seleMap.get("catId");
		} else if (ShopFragment.sub2 != null) {
			seleMap = map.get(ShopFragment.sub2);
			seleMap2 = map.get(ShopFragment.sub1);
			url = url + "category=" + seleMap2.get("catId") + "&superCat="
					+ seleMap.get("catId");
		} else if (ShopFragment.sub1 != null) {
			seleMap = map.get(ShopFragment.sub1);
			url = url + "category=" + seleMap.get("catId");
		}
		if (ShopFragment.Color != null) {
			String s = ShopFragment.Color;
			s = s.replace(' ', '_');
			url = url + "&color=" + s.toUpperCase();
		}
		if (ShopFragment.newest != null) {
			if (ShopFragment.newest.equalsIgnoreCase("Newest First")) {
				url = url + "&sorting=ASC";
			} else if (ShopFragment.newest.equalsIgnoreCase("Oldest First")) {
				url = url + "&sorting=DESC";
			}
		}
		if (ShopFragment.price != null) {
			String[] prices = price.split("-");
			if (prices.length == 2) {
				url = url + "&priceMin=" + prices[0] + "&priceMax=" + prices[1];
			} else if (prices.length == 1) {
				url = url + "&priceMin=" + prices[0];
			}
		}
		seleMap.put("url", url);
		Log.v("ur;", url);
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

	/*
	 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) { Intent i = new Intent(ShopFragment.this.getActivity(),
	 * DetailActivity.class); i.putExtra("data", HomePageItems);
	 * i.putExtra("comments", commentsMap); i.putExtra("position", arg2);
	 * i.putExtra("from", 3); startActivity(i); }
	 */
	class SendFancy extends AsyncTask<Integer, Void, JSONObject> {

		String fancyUrl = ConstantValues.fantacyUrl;
		JSONParser jParser = new JSONParser();
		int itemId;
		ProgressDialog dialog = new ProgressDialog(
				ShopFragment.this.getActivity());

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
						// LinearLayout ll = (LinearLayout) gridView
						// .getChildAt(itemId);
						// TextView fancy = (TextView) ll
						// .findViewById(R.id.fancyIt);
						// fancy.setText("Fantacy'd");
						HomePageItems.get(itemId).put(ConstantValues.TAG_LIKED,
								"Yes");
						hdpiAdapter.notifyDataSetChanged();

					} else {
						Toast.makeText(getActivity(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						// LinearLayout ll = (LinearLayout) gridView
						// .getChildAt(itemId);
						// TextView fancy = (TextView) ll
						// .findViewById(R.id.fancyIt);
						// fancy.setText("Fantacy");
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
		fca.switchContent(new ShopFragment());
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {

		case R.id.btn_search:
			query = searchbar.getText().toString();
			imm.hideSoftInputFromWindow(searchbar.getWindowToken(), 0);
			loadDataSearch();
			searchbar.setText(null);
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
				Intent i=new Intent(ShopFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			//FragmentChangeActivity.menumap = true;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			}
			else{
				Intent i=new Intent(ShopFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int arg2, long id) {
		Intent i = new Intent(ShopFragment.this.getActivity(),
				DetailActivity.class);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap = HomePageItems.get(arg2);
		String itemid = tempMap.get(ConstantValues.TAG_ID);
		String sellerid = tempMap.get(ConstantValues.TAG_SELLERID);
		ConstantValues.editor.putString("itemid", itemid);
		Log.v("present userid", "" + GetSet.getUserId());
		ConstantValues.editor.commit();
		i.putExtra("data", HomePageItems.get(arg2));
		i.putExtra("sellerid", sellerid);
		i.putExtra("item_id", itemid);
		i.putExtra("comments", commentsMap);
		i.putExtra("fashionPhoto", photosMap);
		i.putExtra("likeUsers", likesMap);
		i.putExtra("position1", arg2);
		i.putExtra("position", arg2);
		i.putExtra("from", 3);
		startActivity(i);

	}
}
