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
import org.json.JSONException;
import org.json.JSONObject;

import com.etsy.android.grid.StaggeredGridView;
import com.hitasoft.apps.adapters.RoundedCornerBitmap;
import com.hitasoft.apps.milymarket.Fashion_photos.LazyAdapter;
import com.hitasoft.apps.milymarket.FollowersActivity.AdapterForHdpis;
import com.hitasoft.apps.milymarket.FollowersActivity.FollowResults;
import com.hitasoft.apps.milymarket.FollowersActivity.UnFollowResults;
import com.hitasoft.apps.milymarket.HomeFragment.AdapterForHdpi;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LikedUsers extends Activity implements OnClickListener{
	
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
	private ImageButton home, near, shop, alert, menu, back, upload;
	StaggeredGridView gv;
	ImageLoader imageLoader;
	AdapterForHdpis adapter;
	private BroadcastReceiver networkStateReceiver;
	public static ArrayList<HashMap<String, String>> likesary;
	String itemId;
	AlertDialog adialog;
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.likedusers);
		imageLoader = ImageLoader.getInstance();
		
		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		shop = (ImageButton) findViewById(R.id.btn_shop);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		back = (ImageButton) findViewById(R.id.smenu);
		
		TextView title = (TextView) findViewById(R.id.userNameTitle);
		title.setText("Utilisateurs aimé");
		
		gv = (StaggeredGridView) findViewById(R.id.gridView1);
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		
		    adialog=new AlertDialog.Builder(LikedUsers.this).create();
			adialog.setTitle("Alert");
			adialog.setMessage("You are not logged in!!! Login to continue!!!");
			adialog.setButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.v("alert","alert");
					
				}
			});
		

		likesary = (ArrayList<HashMap<String, String>>) getIntent().getExtras()
				.get("likeUsers");
		itemId = getIntent().getExtras().getString("position");

		Log.v("af", "" + likesary);
		
		setAdapter();
		
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LikedUsers.this.finish();
			}
		});
		
		
		
	}
	
	private void setAdapter() {
		try {
			
			adapter = new AdapterForHdpis(LikedUsers.this,likesary);
		    setColumns();
			gv.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {

		}
	}
	private void setColumns() {
		Display display = LikedUsers.this.getWindowManager().getDefaultDisplay();
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
		//	gridView.setNumColumns(1);
			layout = R.layout.home_page_grid_item_port_for_hdpi;
		}
		// setGridViewItems();
	}

	
	public class AdapterForHdpis extends BaseAdapter {
		private Context mContext;
		private ArrayList<HashMap<String, String>> data;
		private Activity activity;
		private LayoutInflater inflater = null;

		public AdapterForHdpis(Activity a, ArrayList<HashMap<String, String>> likesary) {
		
			activity = a;
			data = likesary;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return data.size();
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
				
				view = inflater.inflate(R.layout.followinginflate, parent,
						false);

			} else {
				view = convertView;
				view.forceLayout();
			}

			HashMap<String, String> tempMap = new HashMap<String, String>();

			tempMap = data.get(position);

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

			username.setText(tempMap.get(ConstantValues.TAG_USER_NAME));
			userstatus.setText(tempMap.get(ConstantValues.TAG_STATUS));
			if (userstatus.getText().toString().equals("follow")) {
				userstatus.setBackgroundColor(android.graphics.Color.RED);
			} else {
				userstatus.setBackgroundColor(android.graphics.Color.GRAY);
			}
			followuserid = tempMap.get(ConstantValues.TAG_USER_ID);
			Log.v("followuserid",""+followuserid);
			if (followuserid.equals(GetSet.getUserId())) {
				userstatus.setVisibility(View.GONE);
			}
			fullname.setText(tempMap.get(ConstantValues.TAG_FULLNAME_NEARSHOP));
			userstatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					if(GetSet.isLogged()==true){
					Log.v("status clicked", "");
					if (userstatus.getText().equals("follow")) {
						follow(followuserid);
						userstatus.setBackgroundColor(android.graphics.Color.GRAY);
						userstatus.setText("Unfollow");
					} else if (userstatus.getText().equals("unfollow")) {
						Log.v("if u want unfollow click here", "slksjflksajfld");
						unfollow(followuserid);
						userstatus.setBackgroundColor(android.graphics.Color.RED);
						userstatus.setText("follow");
					}
					}
					else{
						adialog.show();
					}

				}

			});

			imageLoader.loadImage(tempMap.get(ConstantValues.TAG_USER_IMG),
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
							//image.setImageBitmap(loadedImage);
							HashMap<String, String> map = data
									.get(Integer.parseInt(image.getTag()
											.toString()));

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
	
	public void follow(String followid) {
		new FollowResults().execute(followid);
	}

	public void unfollow(String followid) {
		new UnFollowResults().execute(followid);
	}

	class FollowResults extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(
				LikedUsers.this);

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

		private ProgressDialog dialog = new ProgressDialog(
				LikedUsers.this);

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
				Intent i=new Intent(LikedUsers.this,LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsprofile = true;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent i=new Intent(LikedUsers.this,LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}

}
