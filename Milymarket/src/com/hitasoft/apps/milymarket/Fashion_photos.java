package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.hitasoft.apps.milymarket.HomeFragment.AdapterForHdpi;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Fashion_photos extends Activity implements OnClickListener {
	BounceGridView gv;
	ImageLoader imageLoader;
	LazyAdapter adapter;
	private ImageButton home, near, shop, alert, profile, back, upload;
	public static ArrayList<HashMap<String, String>> ary = null;
	private BroadcastReceiver networkStateReceiver;
	public static HashMap<String, String> comment = new HashMap<String, String>();
	public static AdapterForHdpi hdpiAdapter;
	Utils utils;
	private int columnWidth;
	private static String itemId;
	ArrayList<String> fsnary;
	public static ArrayList<HashMap<String, String>> fnphotos;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photos_grid);
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				Fashion_photos.this).build();
	    imageLoader.init(config);
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().build();
		internetCheck();

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		gv = (BounceGridView) findViewById(R.id.gridView1);
		utils = new Utils(this);
		InitilizeGridLayout();
		ary = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get(
				"fashionPhoto");
		itemId = getIntent().getExtras().getString("position");
		fnphotos = (ArrayList<HashMap<String, String>>) getIntent().getExtras()
				.get("fashionPhoto");
		setAdapter();

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

		back = (ImageButton) findViewById(R.id.home);
		upload = (ImageButton) findViewById(R.id.upload);
		upload.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fashion_photos.this.finish();
			}
		});
		adapter.notifyDataSetChanged();
	}

	private void setAdapter() {
		try {
			adapter = new LazyAdapter(Fashion_photos.this, ary);
			gv.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {

		}
	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppConstant.GRID_PADDING, r.getDisplayMetrics());
		columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);
		gv.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		gv.setColumnWidth(columnWidth);
		gv.setStretchMode(GridView.NO_STRETCH);
		gv.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gv.setHorizontalSpacing((int) padding);
		gv.setVerticalSpacing((int) padding);
	}

	public class LazyAdapter extends BaseAdapter {

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater = null;

		public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
				vi = inflater.inflate(R.layout.moreproduct_images, null);
			final ImageView thumb_image = (ImageView) vi
					.findViewById(R.id.more_image);
			thumb_image.setScaleType(ScaleType.FIT_CENTER);
			comment = data.get(position);
			//String url = comment.get(ConstantValues.TAG_FIMG);
			//String[] x = url.split("/");
			//String last = x[x.length - 1];
			//if (last.equalsIgnoreCase("usr_img.jpg")) {
			//	thumb_image.setImageResource(R.drawable.tmp);
			//} else {
			String img=comment.get(ConstantValues.TAG_FIMG);
			imageLoader.displayImage(img, thumb_image, defaultOptions);
				/**imageLoader.loadImage(comment.get(ConstantValues.TAG_FIMG),
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								thumb_image.setImageBitmap(loadedImage);

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}

						});**/

			
			gv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					comment = data.get(position);
					Intent i = new Intent(getApplicationContext(),
							Fashion_view.class);
					i.putExtra("position", position);
					startActivity(i);
				}

			});

			return vi;
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
		final Dialog settingsDialog = new Dialog(Fashion_photos.this);
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
			Fashion_photos.this.finish();
			break;
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

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
				Intent i=new Intent(Fashion_photos.this,LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_profile:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsprofile = true;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent i=new Intent(Fashion_photos.this,LoginActivity.class);
				startActivity(i);
			}
			break;

		}

	}

}
