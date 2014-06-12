package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

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

public class UserAddedItem extends Activity implements OnItemClickListener,OnClickListener {
	public static ArrayList<HashMap<String, String>> HomePageItems;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
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
	private StaggeredGridView gridView = null;
	private static String type = null;
	AdapterForHdpiprof hdpiAdapterprof;
	ImageLoader profileLoader;
	private ImageButton home, near, shop, alert, profile;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	ArrayList<HashMap<String, String>> datas2 = null;
	ArrayList<HashMap<String, String>> datas = null;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.followers_following_common_page);
		
		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		shop = (ImageButton) findViewById(R.id.btn_shop);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		profile = (ImageButton) findViewById(R.id.btn_profile);
		
		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		profile.setOnClickListener(this);

		profileLoader = ImageLoader.getInstance();
		TextView title = (TextView) findViewById(R.id.userNameTitle);
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		title.setText("Boutique");
		HomePageItems = new ArrayList<HashMap<String, String>>();
		gridView = (StaggeredGridView) findViewById(R.id.gridView1);
		gridView.setOnItemClickListener(this);
		ImageButton back = (ImageButton) findViewById(R.id.smenu);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserAddedItem.this.finish();
			}
		});

		setColumns();

		loadData();

	}

	private void setColumns() {
		Display display = UserAddedItem.this.getWindowManager()
				.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		if (screenWidth < 280
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = LDPI_PORTRAID;
		//	gridView.setNumColumns(1);// 1
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = LDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_ldpi;
		} else if (screenWidth < 350
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = MDPI_PORTRAID;
		//	gridView.setNumColumns(1);// 1
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = MDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_mdpi;
		} else if (screenWidth < 500
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			device = HDPI_PORTRAID;
		//	gridView.setNumColumns(1);// 1
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
		//		gridView.setNumColumns(1);// 1
			}
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else if (screenWidth < 1400
				&& getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			device = XHDPI_LANDSCAPE;
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		} else {
			device = HDPI_PORTRAID;
		//	gridView.setNumColumns(2);
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		}
		//setGridViewItems();
	}

	/*private void setGridViewItems() {
		switch (device) {
		case LDPI_PORTRAID:
			gridView.setNumColumns(1);// 1
			break;
		case LDPI_LANDSCAPE:
			gridView.setNumColumns(1);// 1
			break;
		case MDPI_PORTRAID:
			gridView.setNumColumns(1);// 1
			break;
		case MDPI_LANDSCAPE:
			gridView.setNumColumns(1);// 2
			break;
		case HDPI_PORTRAID:
			gridView.setNumColumns(1);// 1
			break;
		case HDPI_LANDSCAPE:
			gridView.setNumColumns(2);// 2
			break;
		case LARGE_MDPI_PORTRAID:
			gridView.setNumColumns(2);// 2
			break;
		case LARGE_MDPI_LANDSCAPE:
			gridView.setNumColumns(3);// 3
			break;
		case XHDPI_PORTRAID:
			gridView.setNumColumns(3);// 3
			break;
		case XHDPI_LANDSCAPE:
			gridView.setNumColumns(4);// 4
			break;
		default:
			gridView.setNumColumns(1);// 1
			break;
		}
	}
*/
	private void loadData() {

		if (isNetworkAvailable(UserAddedItem.this)) {
			try {
				String uId = ConstantValues.pref.getString("userprefid", "");
				new homePageLoadImages().execute(Integer.parseInt(uId));
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}

	}

	private void setAdapter() {
		try {
			hdpiAdapterprof = new AdapterForHdpiprof(getApplicationContext());
			gridView.setAdapter(hdpiAdapterprof);
		} catch (Exception e) {

		}

	}

	class homePageLoadImages extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(UserAddedItem.this);

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				parsing(params[0]);
			} catch (Exception e) {
				Log.v("error", e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Chargement...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}

		@Override
		protected void onPostExecute(Void unused) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			setAdapter();

		}
	}

	private void parsing(Integer page) {
		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.home + "?userAdded=" + page;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				String type = result.getString("type");
				UserAddedItem.type = type;
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
					ArrayList<String> originalarray = new ArrayList<String>();
					JSONArray photos = temp
							.getJSONArray(ConstantValues.TAG_PHOTOS);
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

	public void updateDb(ArrayList<HashMap<String, String>> homePageItems2) {
		DBController controller = new DBController(UserAddedItem.this);
		for (int i = 0; i < homePageItems2.size(); i++) {
			HashMap<String, String> map = homePageItems2.get(i);
			int id = Integer.parseInt(map.get(ConstantValues.TAG_ID));
			String data = map.toString();
			byte[] image = null;
			ImageData imageData = new ImageData(id, data, image);
			controller.insertItems(imageData);
		}
	}

	public class AdapterForHdpiprof extends BaseAdapter {

		private Context mContext;
		LayoutInflater inflater;

		public AdapterForHdpiprof(Context ctx) {
			Log.v(":entered", "adapterconstructor");
			mContext = ctx;
		}

		@Override
		public int getCount() {
			//Log.v(":entered", "adapterconstructor");
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
			View view = convertView;

			if (convertView == null) {
				inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(layout, null);
			} else {
				view = convertView;
				view.forceLayout();
			}
			tempMap = HomePageItems.get(position);
			TextView title = (TextView) view
					.findViewById(R.id.single_item_bottom_title);
			final ProgressBar loader = (ProgressBar) view
					.findViewById(R.id.singleImageLoader);
			final LinearLayout commentsLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_comments);
			TextView cost = (TextView) view
					.findViewById(R.id.single_item_bottom_cost);
			final LinearLayout fashionLayout = (LinearLayout) view
					.findViewById(R.id.single_item_bottom_upload);
			final TextView photosCount = (TextView) view
					.findViewById(R.id.photocount);
			TextView comments = (TextView) view
					.findViewById(R.id.single_item_bottom_comments_count);
			final TextView fancy = (TextView) view.findViewById(R.id.fancyIt);
			final TextView seller = (TextView) view.findViewById(R.id.uname);
			final TextView address = (TextView) view.findViewById(R.id.uadd);
			final ImageView sellerimage = (ImageView) view
					.findViewById(R.id.uimage);
			final ImageView image = (ImageView) view
					.findViewById(R.id.singleImage);
			ArrayList<HashMap<String, String>> commentTemp = commentsMap
					.get(Integer.parseInt(tempMap.get(ConstantValues.TAG_ID)));
			image.setTag(position);
			sellerimage.setTag(position);
			seller.setTag(position);
			address.setTag(position);
			if (commentTemp != null && comments != null) {
				comments.setText(Integer.toString(commentTemp.size()));
			}
			title.setText(tempMap.get(ConstantValues.TAG_TITLE));
			cost.setText("| $ " + tempMap.get(ConstantValues.TAG_PRICE));
			fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
			photosCount.setText(tempMap.get(ConstantValues.TAG_FASHIONCOUNT));
			seller.setText(tempMap.get(ConstantValues.TAG_SELLER));
			String sel = tempMap.get(ConstantValues.TAG_SELLER);
			address.setText(tempMap.get(ConstantValues.TAG_SHOP));
			String sho = tempMap.get(ConstantValues.TAG_SHOP);

			sellerimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent j = new Intent(UserAddedItem.this,
							ProfileFragment.class);
					ConstantValues.editor.putString("userprefid",
							tempMap.get(ConstantValues.TAG_SELLERID));
					ConstantValues.editor.commit();

				}
			});

		/*	fashionLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent j = new Intent(UserAddedItem.this,
							Fashion_photos.class);
					int id = (Integer) image.getTag();
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
					Intent i = new Intent(UserAddedItem.this,
							CommentsActivity.class);
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
			
			commentsLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			fashionLayout.setTag(tempMap.get(ConstantValues.TAG_ID));
			image.setTag(position);
			profileLoader.loadImage(
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
			profileLoader.loadImage(
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
							Bitmap rdbitmap = RoundedCornerBitmap
									.getRoundedCornerBitmap(loadedImage, 10);
							sellerimage.setImageBitmap(rdbitmap);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		Intent i = new Intent(UserAddedItem.this, DetailActivity.class);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap = HomePageItems.get(arg2);
		String itemid = tempMap.get(ConstantValues.TAG_ID);
		String sellerid = tempMap.get(ConstantValues.TAG_SELLERID);
		ConstantValues.editor.putString("itemid", itemid);
		ConstantValues.editor.commit();
		i.putExtra("item_id", itemid);
		i.putExtra("sellerid", sellerid);
		i.putExtra("data", HomePageItems.get(arg2));
		i.putExtra("comments", commentsMap);
		i.putExtra("fashionPhoto", photosMap);
		i.putExtra("likeUsers", likesMap);
		i.putExtra("position", arg2);
		i.putExtra("from", 1);
		startActivity(i);
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

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
				Intent i=new Intent(UserAddedItem.this,LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_profile:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsprofile = true;
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon=false;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent i=new Intent(UserAddedItem.this,LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}


}
