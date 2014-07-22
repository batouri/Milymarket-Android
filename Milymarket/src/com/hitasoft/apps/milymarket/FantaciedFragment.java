package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class FantaciedFragment extends SherlockFragment implements
		OnScrollListener, OnItemClickListener {
	private ImageLoader homeImageLoader;
	private GridView gridView;
	private ArrayList<HashMap<String, String>> HomePageItems;
	private int currentPage = 0;
	AdapterForHdpi hdpiAdapter;
	RelativeLayout LoagingLayout;
	private static int orientation = 0;
	int screenWidth, screenHeight;
	LinearLayout bottom;
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
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
	TextView nulltext;
	LinearLayout hometab;

	// private ImageButton home, near, shop, alert, profile;

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

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// home = (ImageButton) getView().findViewById(R.id.btn_home);
		// near = (ImageButton) getView().findViewById(R.id.btn_near);
		// shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		// alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		// profile = (ImageButton) getView().findViewById(R.id.btn_profile);
		//
		// home.setOnClickListener(this);
		// near.setOnClickListener(this);
		// shop.setOnClickListener(this);
		// alert.setOnClickListener(this);
		// profile.setOnClickListener(this);

		hometab = (LinearLayout) getView().findViewById(R.id.tabbar);
		hometab.setVisibility(View.INVISIBLE);
		nulltext = (TextView) getView().findViewById(R.id.homenulltext2);
		bottom = (LinearLayout) getView().findViewById(R.id.bottomhome);
		orientation = FantaciedFragment.this.getActivity().getWindowManager()
				.getDefaultDisplay().getOrientation();
		LoagingLayout = (RelativeLayout) (getView()
				.findViewById(R.id.loadImageLayout));
		LoagingLayout.setVisibility(View.INVISIBLE);
		gridView = (GridView) (getView().findViewById(R.id.homePage_gallery));
		gridView.setSmoothScrollbarEnabled(true);
		gridView.setOnScrollListener(FantaciedFragment.this);
		gridView.setOnItemClickListener(this);
		((FragmentChangeActivity) getActivity())
				.getSupportActionBar()
				.setTitle(getActivity().getResources().getString(R.string.home));
		setColumns();
		loadData();
	}

	private void setColumns() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		if (screenWidth < 280
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = LDPI_PORTRAID;
			// gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = LDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = MDPI_PORTRAID;
			// gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = MDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = HDPI_PORTRAID;
			// gridView.setNumColumns(1);
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
				// gridView.setNumColumns(1);
			}
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 1400
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = XHDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else {
			device = HDPI_PORTRAID;
			// gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		}
		// setGridViewItems();
	}

	private void loadData() {
		HomePageItems = new ArrayList<HashMap<String, String>>();
		try {
			String uId = ConstantValues.pref.getString("userprefid", "");
			new homePageLoadImages().execute(Integer.parseInt(uId));
		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}
		setAdapter();

	}

	@SuppressWarnings("deprecation")
	private void setAdapter() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		hdpiAdapter = new AdapterForHdpi(getActivity());
		gridView.setAdapter(hdpiAdapter);
	}

	class homePageLoadImages extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				FantaciedFragment.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				parsing(params[0]);
			} catch (NullPointerException e) {
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			LoagingLayout.setVisibility(View.INVISIBLE);
			if (currentPage == 0) {
				bottom.setVisibility(View.VISIBLE);
			}

		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				if (currentPage == 0) {
					bottom.setVisibility(View.INVISIBLE);
				}
				if (HomePageItems.size() == 0) {
					nulltext.setVisibility(View.VISIBLE);
				}
				LoagingLayout.setVisibility(View.GONE);
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}

	}

	private void parsing(int page) {
		GetSet.setProfileUserId(Integer.toString(page));
		JSONArray items;
		JSONParser jParser = new JSONParser();
		HashMap<String, String> map;
		String urlAddr = ConstantValues.home + "?userId=" + page;
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
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null;
					JSONObject temp = items.getJSONObject(i);
					String id = temp.getString(ConstantValues.TAG_ID);
					String item_title = temp
							.getString(ConstantValues.TAG_TITLE);
					String item_description = temp
							.getString(ConstantValues.TAG_DESC);
					String price = temp.getString(ConstantValues.TAG_PRICE);
					String quantity = temp
							.getString(ConstantValues.TAG_QUANTITY);
					String favorites = temp
							.getString(ConstantValues.TAG_FAVORITES);
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
						item_url_main_original = photosTemp
								.getString(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL);
						user_url_main_350 = photosTemp
								.getString(ConstantValues.TAG_USER_URL_MAIN_350);
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
					map.put(ConstantValues.TAG_LIKED, "yes");
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_70,
							item_url_main_70);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
							item_url_main_350);
					map.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
							item_url_main_original);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
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
			TextView cost = (TextView) view
					.findViewById(R.id.single_item_bottom_cost);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);
			comments.setText(Integer.toString(commentTemp.size()));
			title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			cost.setText("$ " + tempMap.get(ConstantValues.TAG_PRICE));
			fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			String sel = tempMap.get(ConstantValues.TAG_SELLER);
			address.setText(tempMap.get(ConstantValues.TAG_SHOP));
			String sho = tempMap.get(ConstantValues.TAG_SHOP);
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
							sellerimage.setImageBitmap(loadedImage);
							loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
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
							image.setImageBitmap(loadedImage);
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

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	class SendFancy extends AsyncTask<Integer, Void, JSONObject> {
		String fancyUrl = ConstantValues.fantacyUrl;
		JSONParser jParser = new JSONParser();
		int itemId;
		ProgressDialog dialog = new ProgressDialog(
				FantaciedFragment.this.getActivity());

		@Override
		protected JSONObject doInBackground(Integer... params) {
			itemId = params[0];
			HashMap<String, String> map = HomePageItems.get(itemId);
			fancyUrl = fancyUrl + GetSet.getUserId() + "&itemId="
					+ map.get(ConstantValues.TAG_ID);
			JSONObject response = jParser.getJSONFromUrl(fancyUrl);
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Un instant s'il vous plait...");
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
							if (GetSet.getUserId().equalsIgnoreCase(
									GetSet.getProfileUserId())) {
								ViewGroup ll = (ViewGroup) gridView
										.getChildAt(itemId);
								TextView fancy = (TextView) ll
										.findViewById(R.id.fancyIt);
								fancy.setText("Fantacy'd");
							}
						} catch (NullPointerException npe) {

						} catch (Exception e) {

						}

					} else {
						Toast.makeText(getActivity(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						try {
							if (GetSet.getUserId().equalsIgnoreCase(
									GetSet.getProfileUserId())) {
								LinearLayout ll = (LinearLayout) gridView
										.getChildAt(itemId);
								TextView fancy = (TextView) ll
										.findViewById(R.id.fancyIt);
								fancy.setText("Fantacy");
								loadData();
							}
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}
