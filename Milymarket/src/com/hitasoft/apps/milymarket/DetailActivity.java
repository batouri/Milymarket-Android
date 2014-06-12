package com.hitasoft.apps.milymarket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Session;
import com.hitasoft.apps.adapters.RoundedCornerBitmap;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class DetailActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private static final int LoginCall = 1;
	private ImageLoader detailImageLoader;
	private ViewPager viewPager;
	private ImageButton back, fashionupload, like, listview;
	ImageView image, sellerimage;
	private LinearLayout addtocartlay, followlay;
	private HashMap<String, String> tempMap;
    private HashMap<Integer, ArrayList<HashMap<String, String>>> detailcommentsMap = null,
			commentsMap = null, detailFashionMap = null, photosMap = null,likesMap=null,detailLikesMap=null;
	public ImageView product1, product2, product3, product4, product5,
			product6, product7, product8, product9;
	HashMap<String, String> loadmore_map = new HashMap<String, String>();
	String u1name, u1add, u1img;
	ArrayList<HashMap<String, String>> datas = null;
	ArrayList<String> newarry;
	private GridView moreproduct_grid;
	private BroadcastReceiver networkStateReceiver;
	Dialog listDialog;
	static ArrayList<String> checkeds = new ArrayList<String>();
	RelativeLayout fashionLayout,likeslayout;
	ViewPagerAdapter pagerAdapter;
	public static int from = 0;
	private static String userEntryAddToList = null;
    BounceScrollView sview;
    int i;
    public static int z=0;
	public static String item_id=null;
	public static String sellerid=null;
	int x, j, v = 0, length = 0,inc=0;
	private ImageButton home, near, shop, alert, profile;
	public static LoadmoreAdapter loadmore;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;
	ArrayList<String> map = null;
	TextView fancy, followtxt;
	public static boolean sellerfollow, sellerunfollow;
	private ClipboardManager myClipboard;
	private ClipData myClip;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	String imageloadingurl;
	AlertDialog adialog;
	

	@SuppressLint("UseSparseArrays")
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		internetCheck();
		DetailActivity.from = this.getIntent().getExtras().getInt("from");
		setContentView(R.layout.view_pager_layout);
		
		sview=(BounceScrollView) findViewById(R.id.scrollbar);
		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		shop = (ImageButton) findViewById(R.id.btn_shop);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		profile = (ImageButton) findViewById(R.id.btn_profile);
		fancy = (TextView) findViewById(R.id.detail_fancyit);
		followtxt = (TextView) findViewById(R.id.followtxt);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		profile.setOnClickListener(this);
		
		//sview.fullScroll(View.FOCUS_UP);
		//sview.smoothScrollTo(0, 0);
		//sview.pageScroll(View.FOCUS_UP);
		
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		  adialog=new AlertDialog.Builder(DetailActivity.this).create();
			adialog.setTitle("Alert");
			adialog.setMessage("S'il vous plaît connecter Pour continuer!!!");
			adialog.setButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.v("alert","alert");
					
				}
			});
		tempMap = new HashMap<String, String>();
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		moreproduct_grid = (GridView) findViewById(R.id.more_product_grid);
		newarry = new ArrayList<String>();
		listDialog = new Dialog(DetailActivity.this);
		detailImageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				DetailActivity.this).build();
		detailImageLoader.init(config);
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().build();
		fashionupload = (ImageButton) findViewById(R.id.fashionUpload);
		viewPager = (ViewPager) findViewById(R.id.detail_view_pager);
		ImageView comment_image = (ImageView) findViewById(R.id.comment_image);
	    sellerimage = (ImageView) findViewById(R.id.detailuserimage);
		TextView title = (TextView) findViewById(R.id.detail_item_bottom_title);
		TextView description = (TextView) findViewById(R.id.detail_item_bottom_description);
		TextView sellername = (TextView) findViewById(R.id.detail_username);
		TextView address = (TextView) findViewById(R.id.detail_address);
		TextView cost = (TextView) findViewById(R.id.detail_cost);
		TextView comments = (TextView) findViewById(R.id.detail_item_bottom_comments_count);
		TextView fancy = (TextView) findViewById(R.id.detail_fancyit);
		TextView fashionCount = (TextView) findViewById(R.id.detail_fashiontxt);
		fashionLayout = (RelativeLayout) findViewById(R.id.fashionlay);
		like = (ImageButton) findViewById(R.id.likedimg);
		listview = (ImageButton) findViewById(R.id.listview);
		moreproduct_grid.setOnItemClickListener(this);
		followlay = (LinearLayout) findViewById(R.id.followlay);
		back = (ImageButton) findViewById(R.id.smenu);
		addtocartlay = (LinearLayout) findViewById(R.id.addtocartlay);
		likeslayout=(RelativeLayout) findViewById(R.id.facyitlay);
		

		tempMap = (HashMap<String, String>) getIntent().getExtras().get("data");
		i = getIntent().getIntExtra("position", 0);
		z=getIntent().getIntExtra("position", 0);
		item_id = getIntent().getStringExtra("item_id");
		detailcommentsMap = (HashMap<Integer, ArrayList<HashMap<String, String>>>) getIntent()
				.getExtras().get("comments");
		detailFashionMap = (HashMap<Integer, ArrayList<HashMap<String, String>>>) getIntent()
				.getExtras().get("fashionPhoto");
		detailLikesMap = (HashMap<Integer, ArrayList<HashMap<String, String>>>) getIntent()
				.getExtras().get("likeUsers");
		sellerid = getIntent().getStringExtra("sellerid");
		inc=getIntent().getExtras().getInt("inc");
		
		
		Display display = getWindowManager().getDefaultDisplay();
		int screenw=display.getWidth();

		
		//Log.v("screnw&h",""+screenw+"\t"+screenh);
		int width = Integer.parseInt(tempMap.get(ConstantValues.TAG_WIDTH));
		int height =Integer.parseInt(tempMap.get(ConstantValues.TAG_HEIGHT));
		Log.v("w&h",""+width+"\t"+height);
		if(height>=250&&height<300){
			Log.v("200","200");
			int screenh=display.getHeight()*60/100;
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenw,screenh);
			viewPager.setLayoutParams(parms);
		}
		else if(height>=300){
			Log.v("300","300");
			if(width>=height){
			int screenh=display.getHeight()*60/100;
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenw,screenh);
			viewPager.setLayoutParams(parms);
			}
			else{
				int screenh=display.getHeight()*80/100;
				screenw=display.getWidth()*110/100;
				RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenw,screenh);
				viewPager.setLayoutParams(parms);
			}
		}
		else if(height<250){
			Log.v("200","200");
			int screenh=display.getHeight()*50/100;
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenw,screenh);
			viewPager.setLayoutParams(parms);
		}
		
		//inc=CommentsActivity.inc;
		Moreproduct();

		if (AppConstant.more_productpage == true) {
			newarry = AppConstant.Detailurls.get("detialimage" + i);
			AppConstant.more_productpage = false;
			AppConstant.Detailurls.clear();
		} else {
			newarry = HomeFragment.urls.get("detialimage" + i);
			AppConstant.Detailurls.clear();
		}
		sellerimage.setOnClickListener(new OnClickListener()
		{

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				ConstantValues.editor.clear();
				ConstantValues.editor.putString("userprefid",sellerid);
				ConstantValues.editor.commit();
				FragmentChangeActivity.rsprofile = true;
				FragmentChangeActivity.filter_icon=false;
				FragmentChangeActivity.menumap=true;
				 invalidateOptionsMenu();
				startActivity(new Intent(DetailActivity.this, FragmentChangeActivity.class));
		
				
			}
			
		});
		

		if(GetSet.isLogged()==true){
		String user_id = GetSet.getUserId();
		if (sellerid.equals(user_id)) {
			addtocartlay.setVisibility(View.INVISIBLE);
			followlay.setVisibility(View.INVISIBLE);
		} else {
			addtocartlay.setVisibility(View.VISIBLE);
			followlay.setVisibility(View.VISIBLE);
		}
		new GetFollowUserID().execute();
		if (GetSet.getUserId().equals(sellerid)) {
			followlay.setVisibility(View.INVISIBLE);
		}
		}
		else{
			addtocartlay.setVisibility(View.VISIBLE);
			followlay.setVisibility(View.VISIBLE);
			followtxt.setText("S'abonner");
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

		String liked = tempMap.get(ConstantValues.TAG_LIKED);
		if (liked.equalsIgnoreCase("Yes")) {
			like.setImageResource(R.drawable.m_favo);
		} else
			like.setImageResource(R.drawable.unliked);

		like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GetSet.isLogged() == false) {
					adialog.show();
				} else {
					String id = item_id;
					new SendFancy().execute(Integer.parseInt(id));
				}
			}

		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppConstant.Detailurls.clear();
				AppConstant.MorePageItems.clear();
				if (DetailActivity.from == 1) {
					//FragmentChangeActivity.rshome = true;
					HomeFragment.urls.clear();
					HomeFragment.HomePageItems.clear();
					HomeFragment.commentsMap.clear();
					HomeFragment.photosMap.clear();
					HomeFragment.likesMap.clear();
					FragmentChangeActivity.menumap = false;
					FragmentChangeActivity.filter_icon=false;
					invalidateOptionsMenu();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
					
				} else if (from == 2) {
					FragmentChangeActivity.rsnear = true;
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
				} else if (from == 3) {
					FragmentChangeActivity.rsshop = true;
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
				} else if (from == 4) {
					FragmentChangeActivity.rsnote = true;
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
				} else if (from == 5) {
					FragmentChangeActivity.rsprofile = true;
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
				} else if (from == 11) {
					FragmentChangeActivity.rsmostp = true;
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);

				} else if (from == 12) {
					FragmentChangeActivity.rscatg = true;
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
				}
				else if (from == 13) {
					
					HomeFragment.urls.clear();
					Intent i = new Intent(DetailActivity.this,
							FragmentChangeActivity.class);
					startActivity(i);
				}


			}
		});

		listview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(GetSet.isLogged()==true){
				Intent i = new Intent(DetailActivity.this, List_Detail.class);
				i.putExtra("itemid", item_id);
				startActivity(i);
				}
				else{
					adialog.show();
				}

			}

		});

		addtocartlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!GetSet.isLogged()) {
					adialog.show();
				} else {
					HashMap<String, String> tempmap = tempMap;
					String id = tempmap.get(ConstantValues.TAG_ID);
					new addToCart().execute(id);
				}

			}
		});

		fashionupload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(GetSet.isLogged()==true){
				Intent i = new Intent(DetailActivity.this,
						Fashion_uploads.class);
				i.putExtra("position", tempMap.get(ConstantValues.TAG_ID));
				startActivity(i);
				}
				else{
					adialog.show();
				}
			}

		});

		fashionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent j = new Intent(DetailActivity.this, Fashion_photos.class);

				j.putExtra("fashionPhoto", detailFashionMap.get(Integer
						.parseInt(tempMap.get(ConstantValues.TAG_ID))));
				j.putExtra("fashionposition",
						tempMap.get(ConstantValues.TAG_ID));
				startActivity(j);

			}

		});

		comment_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DetailActivity.this,
						CommentsActivity.class);
				i.putExtra("title", tempMap.get(ConstantValues.TAG_TITLE));
				i.putExtra("CommentsData", detailcommentsMap.get(Integer
						.parseInt(tempMap.get(ConstantValues.TAG_ID))));

				i.putExtra("position", tempMap.get(ConstantValues.TAG_ID));
				
				startActivity(i);

			}
		});
		likeslayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DetailActivity.this,
						LikedUsers.class);
				i.putExtra("likeUsers", detailLikesMap.get(Integer
						.parseInt(tempMap.get(ConstantValues.TAG_ID))));
				i.putExtra("position",
						tempMap.get(ConstantValues.TAG_ID));
				startActivity(i);
				
			}
			
		});

		fashionCount.setText(Integer.toString(detailFashionMap.get(
				Integer.parseInt(tempMap.get(ConstantValues.TAG_ID))).size()));
		comments.setText(Integer.toString(detailcommentsMap.get(
				Integer.parseInt(tempMap.get(ConstantValues.TAG_ID))).size()+inc));
		title.setText(tempMap.get(ConstantValues.TAG_TITLE));
		cost.setText(" € " + tempMap.get(ConstantValues.TAG_PRICE));
		description.setText(tempMap.get(ConstantValues.TAG_DESC));
		sellername.setText(tempMap.get(ConstantValues.TAG_SELLER));
		address.setText(tempMap.get(ConstantValues.TAG_SHOP));
		fancy.setText(tempMap.get(ConstantValues.TAG_FAVORITES));
		
		
		detailImageLoader.loadImage(
				tempMap.get(ConstantValues.TAG_USER_URL_MAIN_350),
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						//Bitmap rdbitmap = RoundedCornerBitmap
						//		.getRoundedCornerBitmap(loadedImage, 10);
						sellerimage.setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});
		Log.v("newarry",""+newarry);
		
		pagerAdapter = new ViewPagerAdapter(getBaseContext(), newarry);
		viewPager.setAdapter(pagerAdapter);
		
		
		ImageButton share = (ImageButton) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareImage();
			}
		});
		
	}
	
	



	protected void showMsgToLogin() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent i = new Intent(DetailActivity.this,
							LoginActivity.class);
					startActivityForResult(i, LoginCall);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Please Login to continue..")
				.setPositiveButton("Login", dialogClickListener)
				.setNegativeButton("Not now", dialogClickListener).show();
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

	class ViewPagerAdapter extends PagerAdapter {

		Context context;
		ArrayList<HashMap<String, String>> viewPagerItems = null;
		LayoutInflater inflater;
		ArrayList<String> temp;
		int j = 0;

		public ViewPagerAdapter(Context act, ArrayList<String> newarry) {
			this.temp = newarry;
			this.context = act;
		}

		public int getCount() {
			return temp.size();
		}

		public Object instantiateItem(ViewGroup collection, int position) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.item_detail_page,
					collection, false);
			imageloadingurl = temp.get(position);
			
			image = (ImageView) itemView.findViewById(R.id.singleImage1);
			image.setScaleType(ScaleType.FIT_XY);
			//Display display = getWindowManager().getDefaultDisplay();
			//int width = display.getWidth(); 
			//int height = display.getHeight()*80/100;
			//RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,height);
			//image.setLayoutParams(parms);
			
			//final ProgressBar pb = (ProgressBar) itemView
			//		.findViewById(R.id.progressBar1);
			
			
			detailImageLoader.displayImage(imageloadingurl, image, defaultOptions);
			/*detailImageLoader.loadImage(imageloadingurl,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							pb.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							//image.setImageBitmap(loadedImage);
						
							pb.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
			*/
			((ViewPager) collection).addView(itemView, 0);
			return itemView;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	public void shareImage() {
		String[] values = new String[] { "Save", "Copy", "E_mail", "Facebook",
				"Twitter", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.share_new, android.R.id.text1, values);
		final Dialog dialog = new Dialog(DetailActivity.this);
		dialog.setContentView(R.layout.share);
		final ListView lv = (ListView) dialog.findViewById(R.id.lv);
		lv.setAdapter(adapter);
		dialog.setCancelable(true);
		dialog.setTitle("Part via");
		dialog.show();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemValue = (String) lv.getItemAtPosition(position);

				if (position == 0 && itemValue.equals("Save")) {
					saveImage();
					dialog.dismiss();

				}
				if (position == 1 && itemValue.equals("Copy")) {
					copy();
					dialog.dismiss();

				}
				if (position == 2 && itemValue.equals("E_mail")) {
					new sendMail().execute();
					dialog.dismiss();
				}
				if (position == 3 && itemValue.equals("Facebook")) {
					new sendFB().execute();
					dialog.dismiss();

				}
				if (position == 4 && itemValue.equals("Twitter")) {
					new sendFB().execute();
					dialog.dismiss();

				}
				if (position == 5 && itemValue.equals("Cancel")) {
					dialog.dismiss();
				}
			}

		});
	}

	public void saveImage() {

		image.setDrawingCacheEnabled(true);
		image.buildDrawingCache();
		Bitmap sa = image.getDrawingCache();

		String root = Environment.getExternalStorageDirectory().toString();
		File newDir = new File(root + "/saved_images");
		newDir.mkdirs();
		Random gen = new Random();
		int n = 10000;
		n = gen.nextInt(n);
		String fotoname = "photo-" + n + ".jpg";
		File file = new File(newDir, fotoname);

		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			sa.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Toast.makeText(getApplicationContext(), "saved to your folder",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {

		}
	}

	@SuppressLint("NewApi")
	public void copy() {
		myClip = ClipData.newRawUri("uri", Uri.parse(imageloadingurl));
		myClipboard.setPrimaryClip(myClip);
		Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT)
				.show();
	}

	class sendMail extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				URL url = new URL(imageloadingurl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Bitmap mutableBitmap = result.copy(Bitmap.Config.ARGB_8888, true);
			View view = new View(DetailActivity.this);
			view.draw(new Canvas(mutableBitmap));
			String path = Images.Media.insertImage(getContentResolver(),
					mutableBitmap, "Nur", null);
			Uri uri = Uri.parse(path);
			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			emailIntent.setType("image/png");
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}

	}

	class sendFB extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				URL url = new URL(imageloadingurl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Bitmap mutableBitmap = result.copy(Bitmap.Config.ARGB_8888, true);
			View view = new View(DetailActivity.this);
			view.draw(new Canvas(mutableBitmap));
			String path = Images.Media.insertImage(getContentResolver(),
					mutableBitmap, "Nur", null);
			Uri uri = Uri.parse(path);
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("image/png");
			sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(sharingIntent,
					"Share image using"));
		}

	}

	public void internetCheck() {
		networkStateReceiver = new BroadcastReceiver() {

			@SuppressWarnings("deprecation")
			@Override
			public void onReceive(Context context, Intent intent) {
				NetworkInfo networkinfo = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (networkinfo != null
						&& networkinfo.getState() == NetworkInfo.State.CONNECTED) {
					Log.v("we are connected", "we are connected");
				} else {
					exit();
				}
			}
		};
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(this.networkStateReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
	}

	public void exit() {
		final Dialog settingsDialog = new Dialog(DetailActivity.this);
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		settingsDialog.setContentView(getLayoutInflater().inflate(
				R.layout.alert, null));
		settingsDialog.setCancelable(false);
		settingsDialog.setCanceledOnTouchOutside(false);
		settingsDialog.setTitle("Network Problem");
		Button retry = (Button) settingsDialog.findViewById(R.id.alertRetry);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}

			}
		});
		settingsDialog.show();
	}

	public void dismiss(View v) {
		switch (v.getId()) {
		case R.id.alertClose:
			DetailActivity.this.finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		if (requestCode == LoginCall) {
			if (resultCode == RESULT_OK) {
				AddItemtoCart();
			}
		}
	}

	private void AddItemtoCart() {

	}

	class addToCart extends AsyncTask<String, Void, JSONObject> {

		String url = ConstantValues.addtoCart;
		private ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

		@Override
		protected JSONObject doInBackground(String... params) {
			url = url + GetSet.getUserId() + "&itemId=" + params[0]
					+ "&quantity=1";
			JSONParser parser = new JSONParser();
			return parser.getJSONFromUrl(url);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			try {
				String status = result.getString("message");
				Toast.makeText(getBaseContext(), status, Toast.LENGTH_LONG)
						.show();
			} catch (JSONException e) {
				Toast.makeText(getBaseContext(), "Sorry, something went wrong",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}

	}

	class SendFancy extends AsyncTask<Integer, Void, JSONObject> {

		String fancyUrl = ConstantValues.fantacyUrl;
		JSONParser jParser = new JSONParser();
		int itemId;
		ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

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
			dialog.setMessage("S'il vous plaît attendre...");
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
						Toast.makeText(getBaseContext(), "Item Fantacy'd",
								Toast.LENGTH_LONG).show();
						like.setImageResource(R.drawable.m_favo);
						String c = fancy.getText().toString();
						int m = Integer.parseInt(c) + 1;
						fancy.setText(Integer.toString(m));

						try {
							if (DetailActivity.from == 1) {
								HomeFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "Yes");
								//HomeFragment.hdpiAdapter.notifyDataSetChanged();
							} else if (from == 3) {
								ShopFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "Yes");
								//ShopFragment.hdpiAdapter.notifyDataSetChanged();
							} else {
							}
						} catch (NullPointerException npe) {

						} catch (Exception e) {

						}

					} else {
						Toast.makeText(getBaseContext(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						like.setImageResource(R.drawable.unliked);
						String c = fancy.getText().toString();
						int m = Integer.parseInt(c) - 1;
						fancy.setText(Integer.toString(m));
						try {
							if (DetailActivity.from == 1) {
								HomeFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "No");
								//HomeFragment.hdpiAdapter.notifyDataSetChanged();
							} else if (from == 3) {
								ShopFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "No");
								//ShopFragment.hdpiAdapter.notifyDataSetChanged();
							} else {
							}
						} catch (NullPointerException npe) {

						} catch (Exception e) {

						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class AddtoList extends
			AsyncTask<ArrayList<HashMap<String, String>>, Void, JSONObject> {
		ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

		@Override
		protected JSONObject doInBackground(
				ArrayList<HashMap<String, String>>... params) {
			ArrayList<HashMap<String, String>> givenList = params[0];
			int position = viewPager.getCurrentItem();
			HashMap<String, String> mapFromList = datas.get(position);
			String itemId = mapFromList.get(ConstantValues.TAG_ID);
			String type = null;
			String listName = null;
			String listId = null;
			for (int i = 0; i < givenList.size(); i++) {
				HashMap<String, String> mapInLoop = givenList.get(i);
				if (mapInLoop.get("checked").equalsIgnoreCase("1")) {
					if (type == null) {
						type = mapInLoop.get("type");
					} else {
						type = type + "," + mapInLoop.get("type");
					}
					if (listName == null) {
						listName = mapInLoop.get("listName");
					} else {
						listName = listName + "," + mapInLoop.get("listName");
					}
					if (listId == null) {
						listId = mapInLoop.get("listId");
					} else {
						listId = listId + "," + mapInLoop.get("listId");
					}
				}
			}
			if (DetailActivity.userEntryAddToList != null) {
				if (type == null) {
					type = "0";
				} else {
					type = type + "," + "0";
				}
				if (listName == null) {
					listName = DetailActivity.userEntryAddToList;
				} else {
					listName = listName + ","
							+ DetailActivity.userEntryAddToList;
				}
				if (listId == null) {
					listId = ",";
				} else {
					listId = listId + ",";
				}
			}
			String url = ConstantValues.addtoList + GetSet.getUserId()
					+ "&itemId=" + itemId + "&type=" + type + "&listName="
					+ listName + "&listId=" + listId;
			DetailActivity.userEntryAddToList = null;
			JSONParser parsere = new JSONParser();
			return parsere.getJSONFromUrl(url.trim());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			try {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				Toast.makeText(getBaseContext(), result.getString("result"),
						Toast.LENGTH_LONG).show();
				if (listDialog.isShowing()) {
					listDialog.dismiss();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public JSONObject postData(ArrayList<HashMap<String, String>> listData2) {
		JSONObject jObj = null;
		String json = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ConstantValues.UpdateList);

		try {
			JSONObject obj = new JSONObject();
			obj.put("userId", GetSet.getUserId());
			int position = viewPager.getCurrentItem();
			HashMap<String, String> mapFromList = datas.get(position);
			String itemId = mapFromList.get(ConstantValues.TAG_ID);
			obj.put("itemId", itemId);
			JSONArray array = new JSONArray();
			for (int i = 0; i < listData2.size(); i++) {
				JSONObject obj2 = new JSONObject();
				HashMap<String, String> mapInLoop = listData2.get(i);
				obj2.put("listId", mapInLoop.get("listId"));
				obj2.put("listName", mapInLoop.get("listName"));
				if (mapInLoop.get("checked").equalsIgnoreCase("0")) {
					obj2.put("listStatus", "false");
				} else {
					obj2.put("listStatus", "true");
				}
				array.put(obj2);
			}
			if (DetailActivity.userEntryAddToList != null) {
				JSONObject obj3 = new JSONObject();
				obj3.put("listId", "");
				obj3.put("listName", DetailActivity.userEntryAddToList);
				obj3.put("listStatus", "true");
				array.put(obj3);
			}
			obj.put("list", array);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("listData", obj
					.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity httpEntity = response.getEntity();
			json = EntityUtils.toString(httpEntity, HTTP.UTF_8);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;

	}

	class PostListEntry extends
			AsyncTask<ArrayList<HashMap<String, String>>, Void, JSONObject> {
		ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

		@Override
		protected JSONObject doInBackground(
				ArrayList<HashMap<String, String>>... arg0) {
			return postData(arg0[0]);

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
			if (result != null) {
				DetailActivity.userEntryAddToList = null;
			} else {
				Toast.makeText(getBaseContext(), "Network Problems",
						Toast.LENGTH_LONG).show();
			}
			if (listDialog.isShowing()) {
				listDialog.dismiss();
			}
		}

	}

	public void Moreproduct() {
		try {
			new morePageLoadImages().execute(0);
		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}
		setAdapter();
	}

	private void setAdapter() {

		try {
			loadmore = new LoadmoreAdapter(DetailActivity.this);
			moreproduct_grid.setAdapter(loadmore);
		} catch (Exception e) {

		}
	}

	class morePageLoadImages extends AsyncTask<Integer, Void, Void> {

		ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				parsing();
				sview.fullScroll(View.FOCUS_UP);
			} catch (Exception e) {
				Log.v("error", e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			sview.fullScroll(View.FOCUS_UP);
		}

		@Override
		protected void onPostExecute(Void unused) {
			setAdapter();
			sview.fullScroll(View.FOCUS_UP);
		}
	}

	private void parsing() {
		JSONArray items;
		HashMap<String, String> map;
		String url = ConstantValues.Category + "?itemId=" + item_id
				+ "&limit=9";
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url.trim());
		try {

			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				length += items.length();
				for (x = v, j = 0; j < 9; x++, j++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null, user_url_main_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null, fid = null, fimg = null,height=null,width=null;
					JSONObject temp = items.getJSONObject(j);

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
						AppConstant.Detailurls.put("detialimage" + x,
								originalarray);
						v = x + 1;
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
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					map.put(ConstantValues.TAG_HEIGHT, height);
					map.put(ConstantValues.TAG_WIDTH, width);
					AppConstant.MorePageItems.add(map);
				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int imageloader(int num) {
		loadmore_map = AppConstant.MorePageItems.get(num);
		detailImageLoader.loadImage(
				loadmore_map.get(ConstantValues.TAG_ITEM_URL_MAIN_70),
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						product1.setImageBitmap(loadedImage);

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});

		return num;

	}

	public class LoadmoreAdapter extends BaseAdapter {

		private Context mContext;

		public LoadmoreAdapter(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			return AppConstant.MorePageItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.moreproduct_images, parent,
						false);

			} else {
				view = convertView;
				view.forceLayout();
			}

			loadmore_map = AppConstant.MorePageItems.get(position);
			final ImageView image = (ImageView) view
					.findViewById(R.id.more_image);
			image.setScaleType(ScaleType.FIT_CENTER);
			detailImageLoader.loadImage(
					loadmore_map.get(ConstantValues.TAG_ITEM_URL_MAIN_350),
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							//Bitmap rdbitmap = RoundedCornerBitmap
							//		.getRoundedCornerBitmap(loadedImage, 10);
							image.setImageBitmap(loadedImage);
							
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});

			return view;
		}

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
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon=true;
			invalidateOptionsMenu();
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
				Intent i=new Intent(DetailActivity.this,LoginActivity.class);
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
				Intent i=new Intent(DetailActivity.this,LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}

	public void follow(String followid) {
		new FollowResults().execute(followid);
	}

	public void unfollow(String followid) {
		new UnFollowResults().execute(followid);
	}

	class FollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

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

		private ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

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
	public void onItemClick(AdapterView<?> parent, View view, final int arg2,
			long id) {
		AppConstant.more_productpage = true;
		finish();
		Intent i = new Intent(DetailActivity.this, DetailActivity.class);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap = AppConstant.MorePageItems.get(arg2);
		String itemid = tempMap.get(ConstantValues.TAG_ID);
		String sellerid = tempMap.get(ConstantValues.TAG_SELLERID);
		ConstantValues.editor.putString("itemid", itemid);
		ConstantValues.editor.commit();
		i.putExtra("data", AppConstant.MorePageItems.get(arg2));
		i.putExtra("item_id", itemid);
		i.putExtra("sellerid", sellerid);
		i.putExtra("original", AppConstant.Detailurls.get(arg2));
		i.putExtra("comments", commentsMap);
		i.putExtra("position", arg2);
		i.putExtra("fashionPhoto", photosMap);
		i.putExtra("likeUsers", likesMap);
		i.putExtra("from", 1);
		AppConstant.MorePageItems.clear();
		startActivity(i);
	}
}
