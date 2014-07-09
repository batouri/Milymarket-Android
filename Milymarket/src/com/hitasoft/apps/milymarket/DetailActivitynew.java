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

import net.londatiga.android.QuickAction;

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
import android.annotation.TargetApi;
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
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.hitasoft.apps.adapters.RoundedCornerBitmap;
import com.hitasoft.apps.milymarket.DetailActivity.sendFB;
import com.hitasoft.apps.milymarket.DetailActivity.sendMail;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class DetailActivitynew extends Activity implements OnClickListener,
		OnItemClickListener {

	private static final int LoginCall = 1;

	String u1img = null;
	public ImageLoader detailImageLoader;
	ViewPager viewPager;
	ImageButton back, fashionupload, like, listview;
	private LinearLayout addtocartlay, followlay;
	ProgressDialog dialog2;// =new ProgressDialog(DetailActivitynew.this);;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> photosMap = null;
	public static ArrayList<HashMap<String, String>> tmp2;
	public static HashMap<String, String> tmpMap2;
	QuickAction mQuickAction;
	ArrayList<String> newarry;
	private GridView moreproduct_grid;
	public static ArrayList<HashMap<String, String>> HomePageItems;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> itemsMap = null;
	public static ArrayList<String> ImagePageItems;
	public static ArrayList<String> UserPageItems;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = null;
	public static HashMap<Integer, ArrayList<HashMap<String, String>>> likesMap = null;
	private static ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> datas = null;
	private mItems[] itemss;
	ImageView comment_image;
	private BroadcastReceiver networkStateReceiver;
	Dialog listDialog;
	TextView fancy, comment, title, description, sellername, address, cost,
			fashionCount;
	private ArrayAdapter<mItems> listAdapter;
	private ListView mainListView;
	static ArrayList<String> checkeds = new ArrayList<String>();
	private Facebook facebook;
	BounceScrollView sview;
	private static String type = null;
	ViewPagerAdapter pagerAdapter;
	private static int from = 0;
	HashMap<String, ArrayList<String>> url;
	ImageView sellerimg, productimg;
	private static String userEntryAddToList = null;
	private ImageButton home, near, shop, alert, profile;
	public static LoadmoreAdapter loadmore;
	HashMap<String, String> loadmore_map = new HashMap<String, String>();
	private HashMap<String, String> tempMap;
	int x, j, v = 0, length = 0;
	String item_id;
	RelativeLayout fashionLayout,likeslayout;
	ImageView image;
	private ClipboardManager myClipboard;
	private ClipData myClip;
	String imageloadingurl;
	ArrayList<String> map = null;
	TextView followtxt;
	public static boolean sellerfollow, sellerunfollow;
	Bitmap myBitmap;
	String sellerid;
	public static ArrayList<HashMap<String, String>> tmp3;
	public static HashMap<String, String> tmpMap3;
	AlertDialog adialog;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint({ "NewApi", "UseSparseArrays" })
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		internetCheck();

		facebook = new Facebook(AppConstant.App_ID);
		// facebook = new FacebookFacade(this, "166699250182095");

		DetailActivitynew.from = this.getIntent().getExtras().getInt("from");
		Log.v("form", "" + DetailActivitynew.from);
		setContentView(R.layout.view_pager_layout);
		sview=(BounceScrollView) findViewById(R.id.scrollbar);
		 adialog=new AlertDialog.Builder(DetailActivitynew.this).create();
			adialog.setTitle("Alert");
			adialog.setMessage("You are not logged in!!! Login to continue!!!");
			adialog.setButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.v("alert","alert");
					
				}
			});

		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		shop = (ImageButton) findViewById(R.id.btn_shop);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		profile = (ImageButton) findViewById(R.id.btn_profile);
		followtxt = (TextView) findViewById(R.id.followtxt);
		likeslayout=(RelativeLayout) findViewById(R.id.facyitlay);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		profile.setOnClickListener(this);
		dialog2 = new ProgressDialog(DetailActivitynew.this);
		HomePageItems = new ArrayList<HashMap<String, String>>();
		itemsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		photosMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		likesMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		ImagePageItems = new ArrayList<String>();
		UserPageItems = new ArrayList<String>();
		moreproduct_grid = (GridView) findViewById(R.id.more_product_grid);
		newarry = new ArrayList<String>();
		detailImageLoader = ImageLoader.getInstance();
		url = new HashMap<String, ArrayList<String>>();
		//cost = (TextView) findViewById(R.id.detail_cost);
		sellername = (TextView) findViewById(R.id.detail_username);
		address = (TextView) findViewById(R.id.detail_address);
		title = (TextView) findViewById(R.id.detail_item_bottom_title);
		fancy = (TextView) findViewById(R.id.detail_fancyit);
		fashionCount = (TextView) findViewById(R.id.detail_fashiontxt);
		fashionLayout = (RelativeLayout) findViewById(R.id.fashionlay);
		followlay = (LinearLayout) findViewById(R.id.followlay);
		description = (TextView) findViewById(R.id.detail_item_bottom_description);
		tempMap = new HashMap<String, String>();
		comment_image = (ImageView) findViewById(R.id.comment_image);
		comment = (TextView) findViewById(R.id.detail_item_bottom_comments_count);
		sellerimg = (ImageView) findViewById(R.id.detailuserimage);
		// productimg=(ImageView) findViewById(R.id.product_image);
		moreproduct_grid.setOnItemClickListener(this);
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				DetailActivitynew.this).build();
		detailImageLoader.init(config);
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().build();
		listDialog = new Dialog(DetailActivitynew.this);
		mQuickAction = new QuickAction(getBaseContext());
		myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		
		viewPager = (ViewPager) findViewById(R.id.detail_view_pager);
		Display display = getWindowManager().getDefaultDisplay();
		int screenw=display.getWidth();
		int screenh=display.getHeight()*70/100;
		RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(screenw,screenh);
		viewPager.setLayoutParams(parms);

		// datas = (ArrayList<HashMap<String, String>>) getIntent().getExtras()
		// .get("data");
		// commentsMap = (HashMap<Integer, ArrayList<HashMap<String, String>>>)
		// getIntent()
		// .getExtras().get("comments");
		tempMap = (HashMap<String, String>) getIntent().getExtras().get("data");
		Log.v("tempmap", "" + tempMap);
		item_id = getIntent().getStringExtra("item_id");
		commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
		fashionupload = (ImageButton) findViewById(R.id.fashionUpload);
		back = (ImageButton) findViewById(R.id.smenu);
		like = (ImageButton) findViewById(R.id.likedimg);
		listview = (ImageButton) findViewById(R.id.listview);
		// AddtoCart = (ImageButton) findViewById(R.id.addToCart);
		addtocartlay = (LinearLayout) findViewById(R.id.addtocartlay);

		like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GetSet.isLogged() == false) {
					adialog.show();
				} else {
					String id = item_id;
					// new likeStatus().execute(id);
					new SendFancy().execute(Integer.parseInt(id));
				}
			}

		});
		fashionupload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(GetSet.isLogged()==true){
				Intent i = new Intent(DetailActivitynew.this,
						Fashion_uploads.class);
				i.putExtra("position", tempMap.get(ConstantValues.TAG_ID));
				startActivity(i);
				}
				else{
					adialog.show();
				}
			}

		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppConstant.Detailurls.clear();
				AppConstant.MorePageItems.clear();
				
			DetailActivitynew.this.finish();
			}
		});

		listview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(GetSet.isLogged()==true){
				Intent i = new Intent(DetailActivitynew.this, List_Detail.class);
				i.putExtra("itemid", item_id);
				startActivity(i);
				}
				else{
					adialog.show();
				}

			}

		});
		try {
			String Id = ConstantValues.pref.getString("itemid", "");
			int itemid = Integer.parseInt(Id);
			Log.v("itemid detail", "" + itemid);

			new detailPageLoadImages().execute(itemid);
		} catch (Exception e) {
		}
		// viewPager.setAdapter(new
		// ViewPagerAdapter(getApplicationContext(),ImagePageItems));
		// pagerAdapter = (ViewPager)findViewById(R.id.jewel_pager);

		// pagerAdapter = new
		// ViewPagerAdapter(getApplicationContext(),ImagePageItems);
		// viewPager.setAdapter(pagerAdapter);
		sellerid = getIntent().getStringExtra("sellerid");

		if(GetSet.isLogged()==true){
		String user_id = GetSet.getUserId();

		Toast.makeText(getApplicationContext(),
				"seller_id=" + sellerid + "/userid=" + user_id,
				Toast.LENGTH_SHORT).show();

		if (sellerid.equals(user_id)) {
			// AddtoCart.setVisibility(View.INVISIBLE);
			addtocartlay.setVisibility(View.INVISIBLE);

		} else {

			// AddtoCart.setVisibility(View.VISIBLE);
			addtocartlay.setVisibility(View.VISIBLE);

		}

		if (sellerid.equals(user_id)) {
			addtocartlay.setVisibility(View.INVISIBLE);
		} else {
			addtocartlay.setVisibility(View.VISIBLE);
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
					followlay.setBackgroundColor(android.graphics.Color.GRAY);
					followtxt.setText("Se désabonner");
				} else if (followtxt.getText().toString().equals("Se désabonner")
						&& sellerunfollow == true) {
					sellerunfollow = false;
					unfollow(sellerid);
					followlay.setBackgroundColor(android.graphics.Color.RED);
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

		addtocartlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!GetSet.isLogged()) {
					adialog.show();
				} else {
					HashMap<String, String> tempmap = datas.get(viewPager
							.getCurrentItem());
					String id = tempmap.get(ConstantValues.TAG_ID);
					new addToCart().execute(id);
				}

			}
		});

		// AddtoCart.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (!GetSet.isLogged()) {
		// showMsgToLogin();
		// } else {
		// HashMap<String, String> tempmap = datas.get(viewPager
		// .getCurrentItem());
		// String id = tempmap.get(ConstantValues.TAG_ID);
		// new addToCart().execute(id);
		// }
		// }
		// });

		/*
		 * ActionItem fbItem = new ActionItem(ID_Facebook, "Facebook",
		 * getResources().getDrawable(R.drawable.share_fb)); ActionItem twitItem
		 * = new ActionItem(ID_Twitter, "Twitter",
		 * getResources().getDrawable(R.drawable.twitter)); ActionItem mailItem
		 * = new ActionItem(ID_Mail, "Mail", getResources()
		 * .getDrawable(R.drawable.email)); fbItem.setSticky(true);
		 * twitItem.setSticky(true); mailItem.setSticky(true);
		 * mQuickAction.addActionItem(fbItem);
		 * mQuickAction.addActionItem(twitItem);
		 * mQuickAction.addActionItem(mailItem); mQuickAction
		 * .setOnActionItemClickListener(new
		 * QuickAction.OnActionItemClickListener() {
		 * 
		 * @Override public void onItemClick(QuickAction source, int pos, int
		 * actionId) { // here we can filter which action item was clicked with
		 * // pos or actionId parameter
		 * 
		 * if (pos == 0) { int n = viewPager.getCurrentItem(); final
		 * HashMap<String, String> maps = datas.get(n); if
		 * (facebook.isAuthorized()) { publishImage(maps); finish(); } else { //
		 * Start authentication dialog and publish // message after successful
		 * authentication facebook.authorize(new AuthListener() {
		 * 
		 * @Override public void onAuthSucceed() {
		 * 
		 * publishImage(maps); finish(); }
		 * 
		 * @Override public void onAuthFail(String error) { // Do // noting }
		 * }); } } else if (pos == 1) { TwitterFacade twitter = new
		 * TwitterFacade( DetailActivitynew.this, Constants.CONSUMER_KEY,
		 * Constants.CONSUMER_SECRET); if (!twitter.isAuthorized()) {
		 * twitter.authorize(); }
		 * twitter.publishMessage("This is great app! For www.fancyclone.net");
		 * } else if (pos == 2) { Intent emailIntent = new Intent(
		 * android.content.Intent.ACTION_SEND); int n =
		 * viewPager.getCurrentItem(); HashMap<String, String> maps =
		 * datas.get(n); emailIntent .putExtra(
		 * android.content.Intent.EXTRA_EMAIL,
		 * "This is great app! For www.fancyclone.net"); emailIntent.putExtra(
		 * android.content.Intent.EXTRA_TEXT, "share");
		 * emailIntent.setType("image/png");
		 * 
		 * emailIntent.putExtra( Intent.EXTRA_STREAM, Uri.parse(maps
		 * .get(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL)));
		 * 
		 * startActivity(emailIntent); } } });
		 * mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener()
		 * {
		 * 
		 * @Override public void onDismiss() { } });
		 */
		Moreproduct();

		ImageButton share = (ImageButton) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mQuickAction.show(arg0);
			}
		});
		itemss = (mItems[]) getLastNonConfigurationInstance();
	}

	public void shareImage() {
		String[] values = new String[] { "Save", "Copy", "E_mail", "Facebook",
				"Twitter", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		final Dialog dialog = new Dialog(DetailActivitynew.this);
		dialog.setContentView(R.layout.share);
		final ListView lv = (ListView) dialog.findViewById(R.id.lv);
		lv.setAdapter(adapter);
		dialog.setCancelable(true);
		dialog.setTitle("Share Via");
		dialog.show();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
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

	@SuppressLint("NewApi")
	public void copy() {
		// String text = copyField.getText().toString();
		myClip = ClipData.newRawUri("uri", Uri.parse(imageloadingurl));
		myClipboard.setPrimaryClip(myClip);
		Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT)
				.show();
		Log.v("clip", "" + myClip);
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
	}

	public void saveImage() {

		Log.v("insave", "insave");
		image.setDrawingCacheEnabled(true);
		// image.setDrawingCacheQuality(0);

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
			Log.v("saved", "saved");
		} catch (Exception e) {

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
			View view = new View(DetailActivitynew.this);
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


	@SuppressWarnings("deprecation")
	public void loginAndPostToWall() {
		facebook.authorize(this, new LoginDialogListener());
	}

	class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			// saveCredentials(facebook);
			// if (messageToPost != null) {
			// postToWall();
			// }
			// int n = viewPager.getCurrentItem();
			// Log.v("n value", "" + n);
			// final String maps = tempMap.get(n);
			// publishStory(maps);
			publishFeedDialog();
		}

		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
			finish();
		}

		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed!");
			finish();
		}

		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
			finish();
		}
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	private void publishFeedDialog() {
		Bundle params = new Bundle();
		params.putString("name", "FantacyLite");
		params.putString("caption", "Testing From FantacyLite");
		params.putString("description", "FantacyLite Testing");
		params.putString("link", "http://www.fancyclone.net/");
		params.putString("picture",
				"http://199.230.52.7/dev/media/avatars/thumb350/1392665051.jpg");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				DetailActivitynew.this, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(DetailActivitynew.this,
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										DetailActivitynew.this
												.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									DetailActivitynew.this
											.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(
									DetailActivitynew.this
											.getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}

					}

				}).build();
		feedDialog.show();
	}

	/*
	 * protected void publishImage(HashMap<String, String> maps) { Map actions =
	 * new HashMap<String, String>();
	 * facebook.publishMessage("Look at this great App!", "share From ",
	 * "http://www.fancyclone.net/", maps.get(ConstantValues.TAG_TITLE),
	 * maps.get(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL), actions);
	 * 
	 * }
	 */

	protected void showMsgToLogin() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent i = new Intent(DetailActivitynew.this,
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
			// TODO Auto-generated method stub
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
					Log.v("items lenght", "" + items.length());
					for (int i = 0; i < items.length(); i++) {

						JSONObject temp = items.getJSONObject(i);
						String userid = temp.getString("userId");
						Log.v("userid", "" + userid);
						map.add(userid);
						Log.v("map size", "" + map.size());
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
						Log.v("result val", "" + fuserid);
						Log.v("seller id", "" + sellerid);
						if (fuserid.equals(sellerid)) {
							Log.v("if condition", "");
							Log.v("fuserid", "" + fuserid);
							Log.v("sellerid", "" + sellerid);
							followtxt.setText("Se désabonner");
							followlay.setBackgroundColor(Color.GRAY);
							sellerunfollow = true;
							break outerloop;

						} else {
							Log.v("else condition", "");
							Log.v("fuserid", "" + fuserid);
							Log.v("sellerid", "" + sellerid);
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

	class detailPageLoadImages extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(DetailActivitynew.this);

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
			super.onPreExecute();
			dialog.setMessage("Fetching datas...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}

		@Override
		protected void onPostExecute(Void unused) {
			super.onPostExecute(unused);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (HomePageItems.size() == 0) {
				Toast.makeText(getBaseContext(), "image not found",
						Toast.LENGTH_SHORT).show();
			} else {
				// newarry = HomeFragment.urls.get("detialimage"+0);
				// Log.v("newarray",""+newarry.size());
				// Log.v("newarrayindex0",""+newarry.get(0));
				int position = 0;
				final HashMap<String, String> tempmap;
				String tempMap;
				tempmap = HomePageItems.get(position);
				tempMap = UserPageItems.get(position);
				Log.v("imageurl", "" + tempMap);
				Drawable drawable = LoadImageFromWebOperations(tempMap);

				comment.setText(Integer.toString(commentsMap.size()));

				Log.v("comment size", "" + Integer.toString(commentsMap.size()));

				cost.setText("$" + tempmap.get(ConstantValues.TAG_PRICE));
				sellername.setText(tempmap.get(ConstantValues.TAG_SELLER));
				address.setText(tempmap.get(ConstantValues.TAG_SHOP));
				comment.setText(tempmap.get(ConstantValues.TAG_COMMENTS));
				fashionCount.setText(tempmap
						.get(ConstantValues.TAG_FASHIONCOUNT));
				fancy.setText(tempmap.get(ConstantValues.TAG_FAVORITES));
				title.setText(tempmap.get(ConstantValues.TAG_TITLE));
				description.setText(tempmap.get(ConstantValues.TAG_DESC));
				sellerimg.setImageDrawable(drawable);

				comment.setText(Integer.toString(commentsMap.get(
						Integer.parseInt(tempmap.get(ConstantValues.TAG_ID)))
						.size()));
				Log.v("commentsize",
						""
								+ commentsMap.get(
										Integer.parseInt(tempmap
												.get(ConstantValues.TAG_ID)))
										.size());
				fashionCount.setText(Integer.toString(photosMap.get(
						Integer.parseInt(tempmap.get(ConstantValues.TAG_ID)))
						.size()));
				Log.v("photosize",
						""
								+ photosMap.get(
										Integer.parseInt(tempmap
												.get(ConstantValues.TAG_ID)))
										.size());

				String liked = tempmap.get(ConstantValues.TAG_LIKED);
				Log.v("likemap", "" + liked);

				if (liked.equalsIgnoreCase("Yes")) {
					like.setImageResource(R.drawable.m_favo);
				} else
					like.setImageResource(R.drawable.unliked);
				comment_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(DetailActivitynew.this,
								CommentsActivity.class);

						i.putExtra("title",
								tempmap.get(ConstantValues.TAG_TITLE));

						Log.v("commenttitle",
								"" + tempmap.get(ConstantValues.TAG_TITLE));

						i.putExtra("CommentsData", commentsMap.get(Integer
								.parseInt(tempmap.get(ConstantValues.TAG_ID))));

						Log.v("commenttitledata",
								""
										+ commentsMap.get(Integer.parseInt(tempmap
												.get(ConstantValues.TAG_ID))));

						i.putExtra("position",
								tempmap.get(ConstantValues.TAG_ID));

						Log.v("commentposition",
								"" + tempmap.get(ConstantValues.TAG_ID));
						startActivity(i);

					}
				});
				likeslayout.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						Intent i = new Intent(DetailActivitynew.this,
								LikedUsers.class);
						i.putExtra("likeUsers", likesMap.get(Integer
								.parseInt(tempmap.get(ConstantValues.TAG_ID))));
						i.putExtra("position",
								tempmap.get(ConstantValues.TAG_ID));
						startActivity(i);
						
					}
					
				});
				fashionLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent j = new Intent(DetailActivitynew.this,
								Fashion_photos.class);

						j.putExtra("fashionPhoto", photosMap.get(Integer
								.parseInt(tempmap.get(ConstantValues.TAG_ID))));
						j.putExtra("fashionposition",
								tempmap.get(ConstantValues.TAG_ID));
						startActivity(j);

					}

				});

				HomePageItems.clear();
				viewPager.setAdapter(new ViewPagerAdapter(
						getApplicationContext(), newarry));

			}
		}
	}

	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

	private void parsing(Integer page) {

		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.home + "?itemId=" + page;
		// urlAddr=ConstantValues.home;
		// if (GetSet.getUserId() != null) {
		// urlAddr = urlAddr + "&userId=" + GetSet.getUserId();
		// }/*else{
		// urlAddr=urlAddr ;
		Log.v("addurl", "" + urlAddr);
		// }*/
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		Log.v("given url", "" + urlAddr);
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);

			if (response == "false") {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("SORRY! it is not available")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// do things
										DetailActivitynew.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

			}
			// if (response.equalsIgnoreCase("true"))
			if (response.equals("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS);
				String type = result.getString("type");
				DetailActivitynew.type = type;
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String item_url_main_70 = null, user_url_main_350 = null, item_url_sub_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null, fid = null, fimg = null;
					String u1img = null;
					JSONObject temp = items.getJSONObject(i);
					String id = temp.getString(ConstantValues.TAG_ID);
					String item_title = temp
							.getString(ConstantValues.TAG_TITLE);
					Log.v("title",
							"" + temp.getString(ConstantValues.TAG_TITLE));
					String item_description = temp
							.getString(ConstantValues.TAG_DESC);

					String price = temp.getString(ConstantValues.TAG_PRICE);
					// String quantity = temp
					// .getString(ConstantValues.TAG_QUANTITY);
					String favorites = temp
							.getString(ConstantValues.TAG_FAVORITES);
					String fash_count = temp
							.getString(ConstantValues.TAG_FASHIONCOUNT);
					String liked = temp.getString(ConstantValues.TAG_LIKED);
					String seller = temp.getString(ConstantValues.TAG_SELLER);
					String shopname = temp.getString(ConstantValues.TAG_SHOP);
					Log.v("shop", "" + shop);
					// u1name = seller;
					// u1add = shopname;

					// Log.v("u1img",""+u1img);
					// Log.v("uname",""+u1name);
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
							/*
							 * item_url_sub_350=photosTemp
							 * .getString(ConstantValues.TAG_ITEM_URL_SUB_350);
							 */
							/*
							 * map.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
							 * item_url_main_original);
							 */
							// ImagePageItems.add(item_url_main_original);
							UserPageItems.add(user_url_main_350);
						}
						item_url_main_original = photosTemp
								.getString(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL);

						if (AppConstant.more_productpage == true) {

							newarry = AppConstant.Detailurls.get("detialimage"
									+ i);
							Log.v("Length of NewArray", "" + newarry.size());
							AppConstant.more_productpage = false;
							AppConstant.Detailurls.clear();
						} else {

							newarry.add(item_url_main_original);
							Log.v("originalarray", item_url_main_original);
							AppConstant.Detailurls.clear();
						}
						// originalarray

						// url.put("detialimage"+i , originalarray);
						// Log.v("detialimageparsing", "detialimage"+i);

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
						Log.v("no of comment", "" + comment);
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
					// map.put(ConstantValues.TAG_QUANTITY, quantity);
					map.put(ConstantValues.TAG_SELLER, seller);
					map.put(ConstantValues.TAG_SHOP, shopname);
					map.put(ConstantValues.TAG_FAVORITES, favorites);
					map.put(ConstantValues.TAG_FASHIONCOUNT, fash_count);
					map.put(ConstantValues.TAG_LIKED, liked);
					// map.put(ConstantValues.TAG_ITEM_URL_MAIN_70,
					// item_url_main_70);
					/*
					 * map.put(ConstantValues.TAG_ITEM_URL_SUB_350,
					 * item_url_sub_350);
					 */
					// map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
					// item_url_main_350);
					// map.put(ConstantValues.TAG_ITEM_URL_MAIN_ORIGINAL,
					// item_url_main_original);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					// HomePageI.add(map);
					HomePageItems.add(map);
					Log.v("size of array", "" + HomePageItems.size());
					Log.v("Exited parsing", "Exited");

				}
			} else {
				FragmentChangeActivity.rsnote = true;
				startActivity(new Intent(this, FragmentChangeActivity.class));
				Toast.makeText(getApplicationContext(),
						"SORRY! detail view is not availble", Toast.LENGTH_LONG)
						.show();
				/*
				 * dialog2.setTitle("Warning..!");
				 * dialog2.setMessage("Detail view is not available");
				 * dialog2.setIndeterminate(true); dialog2.setCancelable(true);
				 * dialog2.setCanceledOnTouchOutside(true); dialog2.show();
				 */
				/*
				 * FragmentChangeActivity.rsnote = true; startActivity(new
				 * Intent(this, FragmentChangeActivity.class));
				 */

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class ViewPagerAdapter extends PagerAdapter {

		Context context;
		ArrayList<HashMap<String, String>> viewPagerItems = null;
		LayoutInflater inflater;
		ArrayList<String> temp;

		public ViewPagerAdapter(Context act, ArrayList<String> newarry) {
			this.temp = newarry;
			this.context = act;
		}

		public int getCount() {
			return temp.size();
		}

		public Object instantiateItem(ViewGroup collection, final int position) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.item_detail_page,
					collection, false);
			// int j= position-1;

			image = (ImageView) itemView.findViewById(R.id.singleImage1);
			image.setScaleType(ScaleType.FIT_XY);

			imageloadingurl = temp.get(position);

			Log.v("imageloaded", "" + imageloadingurl);
			detailImageLoader.displayImage(imageloadingurl, image, defaultOptions);
			
			/*
			 * Bitmap bmp= null; try { URL url = new URL(imageloadingurl);
			 * Log.v("bitmapurl",""+url); bmp =
			 * BitmapFactory.decodeStream(url.openConnection
			 * ().getInputStream()); } catch (IOException e) {
			 * 
			 * e.printStackTrace(); } image.setImageBitmap(bmp);
			 */

			// pb.setVisibility(View.INVISIBLE);

			// Drawable drawable =
			// LoadImageFromWebOperationsforproduct(temp.get(position));

			// image.setImageDrawable(drawable);

			// ((ViewPager) view).addView(itemView, 0);

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
							image.setImageBitmap(loadedImage);

							pb.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							pb.setVisibility(View.INVISIBLE);

						}
					});
             */
			((ViewPager) collection).addView(itemView, 0);

			return itemView;
		}

		/*
		 * class ViewPagerAdapter extends PagerAdapter {
		 * 
		 * Context context;
		 * 
		 * private LayoutInflater inflater;
		 * 
		 * 
		 * private ArrayList<String> imagePage; //private ArrayList<View> views
		 * = new ArrayList<View>();
		 * 
		 * 
		 * public ViewPagerAdapter(Context cnxt, ArrayList<String>
		 * imagePageItems) { this.context = cnxt; imagePage= new
		 * ArrayList<String>(); this.imagePage = imagePageItems; inflater =
		 * getLayoutInflater(); Log.v("entered", "pageradapter"); }
		 * 
		 * @Override public int getItemPosition (Object object) { int index =
		 * views.indexOf (object); if (index == -1) return POSITION_NONE; else
		 * return index; } public int getCount() { Log.v("Pageradapter_Size",
		 * ""+imagePage.size()); return imagePage.size(); }
		 * 
		 * 
		 * @Override public boolean isViewFromObject(View arg0, Object arg1) {
		 * return arg0 == ((View) arg1); }
		 * 
		 * @Override public Parcelable saveState() { return null; }
		 * 
		 * 
		 * public Object instantiateItem(View view, int position) {
		 * 
		 * final FrameLayout itemView =
		 * (FrameLayout)inflater.inflate(R.layout.item_detail_page, null); final
		 * ImageView image = (ImageView)
		 * itemView.findViewById(R.id.singleImage1);
		 * 
		 * Log.v("producturl",""+imagePage.get(position));
		 * 
		 * final ProgressBar pb = (ProgressBar) itemView
		 * .findViewById(R.id.progressBar1); Drawable drawable =
		 * LoadImageFromWebOperationsforproduct(imagePage.get(position));
		 * 
		 * image.setImageDrawable(drawable);
		 * 
		 * ((ViewPager) view).addView(itemView, 0); Log.v("Returned_Img_Url",
		 * ""+imagePage.get(position));
		 * 
		 * Log.v("Pageradapter_Inflated", "Returned");
		 * pb.setVisibility(View.INVISIBLE); return itemView;
		 * 
		 * }
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			((ViewPager) container).removeView((View) object);
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
		final Dialog settingsDialog = new Dialog(DetailActivitynew.this);
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
			DetailActivitynew.this.finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LoginCall) {
			if (resultCode == RESULT_OK) {
				AddItemtoCart();
			}
		}
	}

	private void AddItemtoCart() {

	}

	/** Holds planet data. */
	private static class mItems {
		private String name = "";
		private boolean checked = false;
		private String id = "";
		private int position = 0;

		public mItems() {
		}

		public mItems(String name) {
			this.name = name;
		}

		public mItems(String name, boolean checked) {
			this.name = name;
			this.checked = checked;
		}

		public mItems(String name, boolean checked, String id, int position) {
			this.name = name;
			this.checked = checked;
			this.id = id;
			this.position = position;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String toString() {
			return name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public void toggleChecked() {
			checked = !checked;
			checkeds.add(this.getName() + "+" + this.getId() + "+"
					+ this.isChecked());
			if (this.isChecked()) {
				DetailActivitynew.listData.get(this.getPosition()).put(
						"checked", "1");
			} else {
				DetailActivitynew.listData.get(this.getPosition()).put(
						"checked", "0");
			}

		}
	}

	/** Holds child views for one row. */
	private static class SelectViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public SelectViewHolder() {
		}

		public SelectViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
	}

	/** Custom adapter for displaying an array of Planet objects. */
	private static class SelectArralAdapter extends ArrayAdapter<mItems> {
		private LayoutInflater inflater;

		public SelectArralAdapter(Context context, List<mItems> planetList) {
			super(context, R.layout.simplerow, R.id.rowTextView, planetList);
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Planet to display
			mItems planet = (mItems) this.getItem(position);

			// The child views in each row.
			CheckBox checkBox;
			TextView textView;

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.simplerow, null);

				// Find the child views.
				textView = (TextView) convertView
						.findViewById(R.id.rowTextView);
				checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
				// Optimization: Tag the row with it's child views, so we don't
				// have to
				// call findViewById() later when we reuse the row.
				convertView.setTag(new SelectViewHolder(textView, checkBox));
				// If CheckBox is toggled, update the planet it is tagged with.
				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						mItems planet = (mItems) cb.getTag();
						planet.setChecked(cb.isChecked());
					}
				});
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				SelectViewHolder viewHolder = (SelectViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			// Tag the CheckBox with the Planet it is displaying, so that we can
			// access the planet in onClick() when the CheckBox is toggled.
			checkBox.setTag(planet);
			// Display planet data
			checkBox.setChecked(planet.isChecked());
			textView.setText(planet.getName());
			return convertView;
		}
	}

	public Object onRetainNonConfigurationInstance() {
		return itemss;
	}

	// class UpdateList extends AsyncTask<String, Void, JSONObject> {
	//
	// private ProgressDialog dialog = new ProgressDialog(
	// DetailActivitynew.this,
	// android.R.style.Theme_Translucent_NoTitleBar);
	//
	// @Override
	// protected JSONObject doInBackground(String... params) {
	// JSONParser parser = new JSONParser();
	// Log.v("URLSSSS", ConstantValues.getlist + GetSet.getUserId()
	// + "&itemId=" + params[0]);
	// return parser.getJSONFromUrl(ConstantValues.getlist
	// + GetSet.getUserId() + "&itemId=" + params[0]);
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog.setMessage("Updating List");
	// dialog.setCancelable(false);
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	//
	// }
	//
	// @SuppressWarnings("deprecation")
	// @Override
	// protected void onPostExecute(JSONObject result) {
	// super.onPostExecute(result);
	// if (this.dialog.isShowing()) {
	// this.dialog.dismiss();
	// }
	// DetailActivitynew.listData = new ArrayList<HashMap<String, String>>();
	// String status;
	// try {
	// status = result.getString("status");
	// if (status.equalsIgnoreCase("true")) {
	// JSONArray data = result.getJSONArray("result");
	// for (int i = 0; i < data.length(); i++) {
	// JSONObject obj = data.getJSONObject(i);
	// String listId = obj.getString("listId");
	// String listName = obj.getString("listName");
	// String type = obj.getString("type");
	// String checks = obj.getString("checked");
	// HashMap<String, String> tempmap = new HashMap<String, String>();
	// tempmap.put("listId", listId);
	// tempmap.put("listName", listName);
	// tempmap.put("type", type);
	// tempmap.put("checked", checks);
	// listData.add(tempmap);
	// }
	// Log.v("listDataFromEntry", result.toString());
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// itemss = (mItems[]) getLastNonConfigurationInstance();
	// ArrayList<mItems> planetList = new ArrayList<mItems>();
	//
	// for (int j = 0; j < listData.size(); j++) {
	// HashMap<String, String> tempmap = listData.get(j);
	// if (Integer.parseInt(tempmap.get("checked")) == 1) {
	// planetList.add(new mItems(tempmap.get("listName"), true,
	// tempmap.get("listId"), j));
	// } else {
	// planetList.add(new mItems(tempmap.get("listName"), false,
	// tempmap.get("listId"), j));
	// }
	// }
	// listDialog.setContentView(R.layout.main);
	// mainListView = (ListView) listDialog
	// .findViewById(R.id.mainListView);
	// listAdapter = new SelectArralAdapter(DetailActivitynew.this,
	// planetList);
	// mainListView.setAdapter(listAdapter);
	// listDialog.setTitle("Edit Lists");
	// mainListView
	// .setOnItemClickListener(new AdapterView.OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> parent,
	// View item, int position, long id) {
	// mItems planet = listAdapter.getItem(position);
	// planet.toggleChecked();
	// SelectViewHolder viewHolder = (SelectViewHolder) item
	// .getTag();
	// viewHolder.getCheckBox().setChecked(
	// planet.isChecked());
	// }
	// });
	//
	// final EditText addtoListEntry = (EditText) listDialog
	// .findViewById(R.id.addList);
	// Button AddtoList = (Button) listDialog
	// .findViewById(R.id.addListAddItem);
	// final Button AddtoListFancy = (Button) listDialog
	// .findViewById(R.id.addListFancyIt);
	// int position = viewPager.getCurrentItem();
	// AddtoListFancy.setTag(position);
	// HashMap<String, String> mapFromList = datas.get(position);
	// if (GetSet.isLogged()) {
	// String liked = mapFromList.get(ConstantValues.TAG_LIKED);
	// if (liked.equalsIgnoreCase("yes")) {
	// AddtoListFancy.setText("Fantacy'd");
	// } else {
	// AddtoListFancy.setText("Fantacy");
	// }
	// }
	// AddtoListFancy.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// if (listDialog.isShowing()) {
	// listDialog.dismiss();
	// }
	// if (GetSet.isLogged()) {
	// int point = (Integer) AddtoListFancy.getTag();
	// HashMap<String, String> map = datas.get(point);
	// String liked = map.get(ConstantValues.TAG_LIKED);
	// // if (liked.equalsIgnoreCase("yes")) {
	// // new SendFancy().execute()
	// // } else {
	// // fancy.setTextColor(Color.BLUE);
	// // }
	// new SendFancy().execute(point);
	// } else {
	// Toast.makeText(DetailActivitynew.this,
	// "Please Login To continue", Toast.LENGTH_LONG)
	// .show();
	// }
	//
	// }
	// });
	// AddtoList.setOnClickListener(new OnClickListener() {
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public void onClick(View arg0) {
	// if (addtoListEntry.getText().toString().trim().length() > 0) {
	// for (int j = 0; j < listData.size(); j++) {
	// HashMap<String, String> tempm = listData.get(j);
	// if (tempm.get("listName").equalsIgnoreCase(
	// addtoListEntry.getText().toString().trim())) {
	// Toast.makeText(getBaseContext(),
	// "The list Name already Exists",
	// Toast.LENGTH_LONG).show();
	// break;
	// } else {
	// if (j == listData.size() - 1) {
	// DetailActivitynew.userEntryAddToList = addtoListEntry
	// .getText().toString().trim();
	// new PostListEntry().execute(listData);
	// }
	// // if (j == listData.size() - 1) {
	// // DetailActivity.userEntryAddToList =
	// // addtoListEntry
	// // .getText().toString().trim();
	// // Log.v("reached", "ereds");
	// // for (int i = 0; i < listData.size(); i++)
	// // {
	// // HashMap<String, String> tempms = listData
	// // .get(i);
	// // Log.v("data",
	// // tempms.get("listId")
	// // + tempms.get("listName")
	// // + tempms.get("type")
	// // + tempms.get("checked"));
	// // }
	// // new AddtoList().execute(listData);
	// // }
	// }
	// }
	//
	// } else {
	// DetailActivitynew.userEntryAddToList = null;
	// new PostListEntry().execute(listData);
	//
	// // Log.v("reached", "ereds");
	// // for (int i = 0; i < listData.size(); i++) {
	// // HashMap<String, String> tempms = listData.get(i);
	// // Log.v("data",
	// // tempms.get("listId")
	// // + tempms.get("listName")
	// // + tempms.get("type")
	// // + tempms.get("checked"));
	// // }
	// // new AddtoList().execute(listData);
	// }
	//
	// // DetailActivity.checkeds = new ArrayList<String>();
	// // DetailActivity.listData = new
	// // ArrayList<HashMap<String,
	// // String>>();
	// }
	// });
	// // if (listDialog != null) {
	// try {
	// listDialog.show();
	// } catch (Exception e) {
	//
	// }
	// // }
	//
	// }
	// }

	class addToCart extends AsyncTask<String, Void, JSONObject> {

		String url = ConstantValues.addtoCart;
		private ProgressDialog dialog = new ProgressDialog(
				DetailActivitynew.this);

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
			dialog.setMessage("Please Wait...");
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
		ProgressDialog dialog = new ProgressDialog(DetailActivitynew.this);

		@Override
		protected JSONObject doInBackground(Integer... params) {
			itemId = params[0];
			// HashMap<String, String> map = datas.get(itemId);
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
						Toast.makeText(getBaseContext(), "Item Fantacy'd",
								Toast.LENGTH_LONG).show();
						like.setImageResource(R.drawable.m_favo);
						String c = fancy.getText().toString();
						Log.v("c", "" + c);
						int m = Integer.parseInt(c) + 1;
						Log.v("m", "" + m);
						fancy.setText(Integer.toString(m));
						Log.v("item", "itemId" + itemId);

						try {
							// ViewGroup ll = (ViewGroup) gridView
							// .getChildAt(itemId);
							// TextView fancy = (TextView) ll
							// .findViewById(R.id.fancyIt);
							// fancy.setText("Fantacy'd");
							if (DetailActivitynew.from == 1) {
								HomeFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "Yes");
								//HomeFragment.hdpiAdapter.notifyDataSetChanged();
							} else if (from == 3) {
								ShopFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "Yes");
								//ShopFragment.hdpiAdapter.notifyDataSetChanged();
							} else {
								/*
								 * SearchActivity.HomePageItems.get(itemId).put(
								 * ConstantValues.TAG_LIKED, "Yes");
								 * SearchActivity.hdpiAdapter
								 * .notifyDataSetChanged();
								 */
							}
						} catch (NullPointerException npe) {

						} catch (Exception e) {

						}

					} else {
						Toast.makeText(getBaseContext(), "Item UnFantacy'd",
								Toast.LENGTH_LONG).show();
						like.setImageResource(R.drawable.unliked);
						String c = fancy.getText().toString();
						Log.v("c", "" + c);
						int m = Integer.parseInt(c) - 1;
						Log.v("m", "" + m);
						fancy.setText(Integer.toString(m));
						Log.v("item", "itemId" + itemId);
						try {
							// LinearLayout ll = (LinearLayout) gridView
							// .getChildAt(itemId);
							// TextView fancy = (TextView) ll
							// .findViewById(R.id.fancyIt);
							// fancy.setText("Fantacy");
							if (DetailActivitynew.from == 1) {
								HomeFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "No");
								//HomeFragment.hdpiAdapter.notifyDataSetChanged();
							} else if (from == 3) {
								ShopFragment.HomePageItems.get(itemId).put(
										ConstantValues.TAG_LIKED, "No");
								//ShopFragment.hdpiAdapter.notifyDataSetChanged();
							} else {
								/*
								 * SearchActivity.HomePageItems.get(itemId).put(
								 * ConstantValues.TAG_LIKED, "No");
								 * SearchActivity.hdpiAdapter
								 * .notifyDataSetChanged();
								 */
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
		ProgressDialog dialog = new ProgressDialog(DetailActivitynew.this);

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
			if (DetailActivitynew.userEntryAddToList != null) {
				if (type == null) {
					type = "0";
				} else {
					type = type + "," + "0";
				}
				if (listName == null) {
					listName = DetailActivitynew.userEntryAddToList;
				} else {
					listName = listName + ","
							+ DetailActivitynew.userEntryAddToList;
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
			Log.v("Url", url);
			DetailActivitynew.userEntryAddToList = null;
			JSONParser parsere = new JSONParser();
			Log.v("s", parsere.getJSONFromUrl(url.trim()).toString());
			return parsere.getJSONFromUrl(url.trim());
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
		// Create a new HttpClient and Post Header
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
			if (DetailActivitynew.userEntryAddToList != null) {
				JSONObject obj3 = new JSONObject();
				obj3.put("listId", "");
				obj3.put("listName", DetailActivitynew.userEntryAddToList);
				obj3.put("listStatus", "true");
				array.put(obj3);
			}
			obj.put("list", array);
			Log.v("listdata", obj.toString());

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("listData", obj
					.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
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

		// return JSON String
		return jObj;

	}

	class PostListEntry extends
			AsyncTask<ArrayList<HashMap<String, String>>, Void, JSONObject> {
		ProgressDialog dialog = new ProgressDialog(DetailActivitynew.this);

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
				DetailActivitynew.userEntryAddToList = null;
				Log.v("JONOBJ", result.toString());
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
		Log.v("eneters the mostproduct", "eneters the mostproduct");
		try {
			new morePageLoadImages().execute(0);
		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}

	}

	private void setAdapter() {

		Log.v("eneters Adapter", "eneters Adapter");
		try {
			/*
			 * product1=(ImageView)
			 * findViewById(R.id.product_1);product2=(ImageView)
			 * findViewById(R.id.product_2); product3=(ImageView)
			 * findViewById(R.id.product_3);product4=(ImageView)
			 * findViewById(R.id.product_4); product5=(ImageView)
			 * findViewById(R.id.product_5);product6=(ImageView)
			 * findViewById(R.id.product_6); product7=(ImageView)
			 * findViewById(R.id.product_7);product8=(ImageView)
			 * findViewById(R.id.product_8); product9=(ImageView)
			 * findViewById(R.id.product_9);
			 */
			loadmore = new LoadmoreAdapter(DetailActivitynew.this);

			moreproduct_grid.setAdapter(loadmore);
			/*
			 * for(int y=0;y<=9;y++){
			 * 
			 * imageloader(y); }
			 */
		} catch (Exception e) {

		}
	}

	class morePageLoadImages extends AsyncTask<Integer, Void, Void> {

		ProgressDialog dialog = new ProgressDialog(DetailActivitynew.this);

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				parsing();
			} catch (Exception e) {
				Log.v("error", e.toString());
			}
			Log.v("page", Integer.toString(params[0]));
			return null;
		}

		@Override
		protected void onPreExecute() {
			sview.fullScroll(View.FOCUS_UP);
		}

		@Override
		protected void onPostExecute(Void unused) {
			Log.v("completes parsing", "completed parsing");
			setAdapter();
			sview.fullScroll(View.FOCUS_UP);
		}
	}

	private void parsing() {
		Log.v("eneters parsing", "enters parsing");

		JSONArray items;
		HashMap<String, String> map;

		String url = ConstantValues.Category + "?itemId=" + item_id
				+ "&limit=9";// "http://milymarket.com/api/home?userAdded=2"
								// ;//ConstantValues.home + "?userAdded=2";//
								// +;// + "";// Give the userid of current
								// product
		// Toast.makeText(getApplicationContext(), ""+url, 1000).show();
		Log.v("urlAddr==", "" + url);
		// Log.v("$$$$$$$$$",""+urlAddr);

		/*
		 * if (GetSet.getUserId() != null) { urlAddr = ConstantValues.home +
		 * "?userAdded=" + GetSet.getUserId(); }
		 */
		JSONParser jParser = new JSONParser();

		Log.v("urlAddr==", "jsonparser");

		JSONObject json = jParser.getJSONFromUrl(url.trim());

		Log.v("urlAddr==", "jsonobject");

		try {

			Log.v("urlAddr==", "enters try");
			String response = json.getString(ConstantValues.TAG_STATUS);
			Log.v("Response", "Response=" + response);

			if (response.equalsIgnoreCase("true")) {

				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);

				String type = result.getString("type");

				items = result.getJSONArray(ConstantValues.TAG_ITEMS);

				DetailActivitynew.type = type;
				length += items.length();
				Log.v("length", "" + length);
				Log.v("v when started", "" + v);
				for (x = v, j = 0; j < 9; x++, j++) {

					// Log.v("v when started",""+v);
					map = new HashMap<String, String>();
					String item_url_main_70 = null, user_url_main_350 = null, item_url_main_350 = null, item_url_main_original = null;
					String comment_id = null, comment = null, user_id = null, user_img = null, username = null, fid = null, fimg = null;
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

					Log.v("icon imahe url",
							"main_70="
									+ map.get(ConstantValues.TAG_ITEM_URL_MAIN_70));

					map.put(ConstantValues.TAG_ITEM_URL_MAIN_350,
							item_url_main_350);
					map.put(ConstantValues.TAG_USER_URL_MAIN_350,
							user_url_main_350);
					AppConstant.MorePageItems.add(map);
					Log.v("size of moreproduct", "size="
							+ AppConstant.MorePageItems.size());

				}
			} else {

			}
		} catch (JSONException e) {
			Log.v("there is json exception", "json exception entered");
			e.printStackTrace();
		}
	}

	public class LoadmoreAdapter extends BaseAdapter {

		private Context mContext;

		public LoadmoreAdapter(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("enters count",
					"enters count=" + AppConstant.MorePageItems.size());
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

				Log.v("enters the getview", "enters the getview_if");
			} else {
				view = convertView;
				view.forceLayout();
				Log.v("enters the getview", "enters the getview_else");
			}

			loadmore_map = AppConstant.MorePageItems.get(position);
			Log.v("position", "position=" + position);

			image = (ImageView) view.findViewById(R.id.more_image);

			image.setScaleType(ScaleType.FIT_XY);
			// image.setTag(position);

			Log.v("image url",
					"image url="
							+ loadmore_map
									.get(ConstantValues.TAG_ITEM_URL_MAIN_70));

			detailImageLoader.loadImage(
					loadmore_map.get(ConstantValues.TAG_ITEM_URL_MAIN_70),
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// loader.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							//Bitmap rdbitmap = RoundedCornerBitmap
							//		.getRoundedCornerBitmap(loadedImage, 10);
							image.setImageBitmap(loadedImage);

							/*
							 * HashMap<String, String> map = MorePageItems
							 * .get(Integer.parseInt(image.getTag()
							 * .toString()));
							 */
							// loader.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});

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

		private ProgressDialog dialog = new ProgressDialog(
				DetailActivitynew.this);

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
					// Log.v("response", jonj.toString());
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
			// Log.v("userid", "" + GetSet.getUserId());
			// Log.v("followid", "" + followid);
			// Log.v("in", urlAddr.trim());
		}
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlAddr.trim());
		try {

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
				// Log.v("response", result.toString());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {

		}
		return result;
	}

	class UnFollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(
				DetailActivitynew.this);

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
					// Log.v("response", jonj.toString());
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
			// Log.v("userid", "" + GetSet.getUserId());
			// Log.v("followid", "" + followid);
			// Log.v("in", urlAddr.trim());
		}
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlAddr.trim());
		try {

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
				// Log.v("response", result.toString());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {

		}
		return result;
	}

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
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_profile:
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			Log.v("present userid", "" + GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.rsprofile = true;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int arg2,
			long id) {
		Log.v("clicked_position", "clicked_position=" + arg2);
		AppConstant.more_productpage = true;// constant to check load more page
		finish();

		Intent i = new Intent(DetailActivitynew.this, DetailActivitynew.class);

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
		Log.v("url from home to detail ", "" + AppConstant.Detailurls.get(arg2));
		i.putExtra("comments", commentsMap);
		i.putExtra("fashionPhoto", photosMap);
		i.putExtra("likeUsers", likesMap);
		i.putExtra("position", arg2);
		Log.v("position from home to detail ", "" + arg2);
		i.putExtra("from", 1);
		AppConstant.MorePageItems.clear();

		startActivity(i);
	}
}
