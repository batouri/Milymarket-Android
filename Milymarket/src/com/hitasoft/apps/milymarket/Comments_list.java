package com.hitasoft.apps.milymarket;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Comments_list extends Activity implements OnClickListener {
	ArrayList<HashMap<String, String>> arl = null;

	BounceListView lv;
	private BroadcastReceiver networkStateReceiver;
	ArrayList<HashMap<String, String>> commetnsList = null;
	ImageLoader imageLoader;
	LazyAdapter adapter;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	private ImageButton home, near, shop, alert, profile, back;
	EditText commentText;
	Button send;
	private static String userId;
	private static String uId;
	AlertDialog adialog;
	public static int inc=0;
	Boolean cond=false;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments_page);
		imageLoader = ImageLoader.getInstance();
		internetCheck();
		
		 adialog=new AlertDialog.Builder(Comments_list.this).create();
			adialog.setTitle("Alert");
			adialog.setMessage("You are not logged in!!! Login to continue!!!");
			adialog.setButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.v("alert","alert");
					
				}
			});

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		lv = (BounceListView) findViewById(R.id.comments_list);
		arl = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra("cmtsarray");

		adapter = new LazyAdapter(Comments_list.this, arl);

		lv.setAdapter(adapter);
		
		commentText = (EditText) findViewById(R.id.commentEditText);
		send = (Button) findViewById(R.id.send);
		uId = ConstantValues.pref.getString("userprefid", "");
		userId = GetSet.getUserId();
		//setAdapter();
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (GetSet.isLogged()==true) {
					if (commentText.getText().toString().trim().length() == 0) {
						Toast.makeText(getBaseContext(),
								"Please Give some comments", Toast.LENGTH_LONG)
								.show();
					} else {
						//loadData();
						new SendComment().execute();
					}
				} else {
					adialog.show();
				}
			}
		});

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
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(Comments_list.this,Map.class);
				
				startActivity(i);
				//Comments_list.this.finish();
			}
		});

	}

	private void loadData() {
		commetnsList = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(Comments_list.this)) {
			try {
				new SendComment().execute();
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
		setAdapter();

	}

	private void setAdapter() {

		try {

			adapter = new LazyAdapter(Comments_list.this, arl);

			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {

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
				vi = inflater.inflate(R.layout.comment_item, null);

			TextView uname = (TextView) vi.findViewById(R.id.title);
			TextView comments = (TextView) vi.findViewById(R.id.artist);

			final ImageView thumb_image = (ImageView) vi
					.findViewById(R.id.list_image);

			HashMap<String, String> comment = new HashMap<String, String>();
			comment = data.get(position);

			uname.setText("par "+comment.get(ConstantValues.TAG_NAME_INFO));
			comments.setText(comment.get(ConstantValues.TAG_CMT_INFO));
			String url = comment.get(ConstantValues.TAG_UIMG_INFO);
			String[] x = url.split("/");
			String last = x[x.length - 1];
			if (last.equalsIgnoreCase("usr_img.jpg")) {
				thumb_image.setImageResource(R.drawable.tmp);
			} else {
				imageLoader.loadImage(
						comment.get(ConstantValues.TAG_UIMG_INFO),
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
						});
			}
			return vi;
		}
	}

	class SendComment extends AsyncTask<Void, Void, JSONObject> {

		String url = ConstantValues.sellerComments;
		JSONParser parser = new JSONParser();
		ProgressDialog dialog = new ProgressDialog(Comments_list.this);
		String urlAddr;

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			try {
				String cmts = commentText.getText().toString();
				String query = URLEncoder.encode(cmts, "utf-8");
				urlAddr = url + "userId=" + userId + "&comment=" + query
						+ "&sellerId=" + uId;
			} catch (Exception e) {
				Log.v("error", "" + e.getMessage());
			}

			JSONObject res = parser.getJSONFromUrl(urlAddr);
			return res;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("ajout...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			String status = null;
			try {
				status = result.getString("status");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (status != null && status.equalsIgnoreCase("true")) {
				Toast.makeText(getBaseContext(), "Comment Successful",
						Toast.LENGTH_LONG).show();
				commentText.setText("");
				inc=inc+1;
				cond=true;
				HashMap<String, String> tempmap = new HashMap<String, String>();
				String comment_id;
				try {
					comment_id = result
							.getString(ConstantValues.TAG_COMMENT_ID);
					String comment = result
							.getString(ConstantValues.TAG_COMMENT);
					String user_id = result
							.getString(ConstantValues.TAG_USER_ID);
					String user_img = result
							.getString(ConstantValues.TAG_USER_IMG);
					String username = result
							.getString(ConstantValues.TAG_USERNAME);
					tempmap.put(ConstantValues.TAG_COMMENT_ID, comment_id);
					tempmap.put(ConstantValues.TAG_COMMENT, comment);
					tempmap.put(ConstantValues.TAG_USER_ID, user_id);
					tempmap.put(ConstantValues.TAG_USER_IMG, user_img);
					tempmap.put(ConstantValues.TAG_USERNAME, username);
					arl.add(tempmap);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getBaseContext(), "Failed-Something went wrong",
						Toast.LENGTH_LONG).show();
			}

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

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
		final Dialog settingsDialog = new Dialog(Comments_list.this);
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
			Comments_list.this.finish();
			break;
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
			FragmentChangeActivity.rsprofile = true;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;

		}

	}

}
