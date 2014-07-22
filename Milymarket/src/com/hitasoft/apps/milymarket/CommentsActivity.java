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

import com.hitasoft.apps.milymarket.R;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CommentsActivity extends Activity implements OnClickListener {

	BounceListView commentsList;
	Button send;
	TextView commentTitle;
	ArrayList<HashMap<String, String>> commetnsList = null;
    HashMap<Integer, ArrayList<HashMap<String, String>>> comments = null;
	ImageLoader imageLoader;
	LazyAdapter adapter;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	EditText commentText;
	ImageButton back;
	private BroadcastReceiver networkStateReceiver;
	private static String itemId=null;
	private ImageButton home, near, cart, alert, menu;
	AlertDialog adialog;
	public static int inc=0;
	Boolean cond=false;


	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		internetCheck();

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		setContentView(R.layout.comments_page);

		    adialog=new AlertDialog.Builder(CommentsActivity.this).create();
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
		cart = (ImageButton) findViewById(R.id.btn_cart);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		commentTitle = (TextView) findViewById(R.id.commentTitle);
		commentTitle.setText(this.getIntent().getExtras().getString("title"));
		back = (ImageButton) findViewById(R.id.home);
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(cond==true){
				Intent i=new Intent(CommentsActivity.this,DetailActivity.class);
				i.putExtra("item_id", DetailActivity.item_id);
				i.putExtra("sellerid", DetailActivity.sellerid);
				i.putExtra("data", HomeFragment.HomePageItems.get(DetailActivity.z));
			//	i.putExtra("item_id", itemId);
				i.putExtra("comments",HomeFragment.commentsMap);
				i.putExtra("fashionPhoto",HomeFragment.photosMap);
				i.putExtra("likeUsers", HomeFragment.likesMap);
				i.putExtra("position", DetailActivity.z);
				i.putExtra("from", DetailActivity.from);
		     	i.putExtra("inc",inc);

				startActivity(i);
				}
				else{
				CommentsActivity.this.finish();
				}
			}
		});
		imageLoader = ImageLoader.getInstance();
		commentsList = (BounceListView) findViewById(R.id.comments_list);
		commetnsList = (ArrayList<HashMap<String, String>>) getIntent()
				.getExtras().get("CommentsData");
		itemId = getIntent().getExtras().getString("position");
		adapter = new LazyAdapter(this, commetnsList);
		commentsList.setAdapter(adapter);
		commentText = (EditText) findViewById(R.id.commentEditText);
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (GetSet.isLogged()==true) {
					if (commentText.getText().toString().trim().length() == 0) {
						Toast.makeText(getBaseContext(),
								"Please Give some comments", Toast.LENGTH_LONG)
								.show();
					} else {
						new SendComment().execute();
					}
				} else {
					adialog.show();
				}
			}
		});
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

			TextView title = (TextView) vi.findViewById(R.id.title);
			TextView artist = (TextView) vi.findViewById(R.id.artist);

			final ImageView thumb_image = (ImageView) vi
					.findViewById(R.id.list_image);

			HashMap<String, String> comment = new HashMap<String, String>();
			comment = data.get(position);

			title.setText("par "+comment.get(ConstantValues.TAG_USERNAME));
			artist.setText(comment.get(ConstantValues.TAG_COMMENT));
			String url = comment.get(ConstantValues.TAG_USER_IMG);
			String[] x = url.split("/");
			String last = x[x.length - 1];
			if (last.equalsIgnoreCase("usr_img.jpg")) {
				thumb_image.setImageResource(R.drawable.tmp);
			} else {
				imageLoader.loadImage(comment.get(ConstantValues.TAG_USER_IMG),
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								thumb_image.setImageBitmap(loadedImage);

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});
			}
			return vi;
		}
	}

	class SendComment extends AsyncTask<Void, Void, JSONObject> {

		String url = ConstantValues.commentSendUrl;
		JSONParser parser = new JSONParser();
		ProgressDialog dialog = new ProgressDialog(CommentsActivity.this);
		String urlAddr;

		@Override
		protected JSONObject doInBackground(Void... arg0) {

			try {
				String cmts = commentText.getText().toString();
				String query = URLEncoder.encode(cmts, "utf-8");
				urlAddr = url + "?userId="+GetSet.getUserId() + "&itemId=" + itemId
						+ "&comment=" + query;
			} catch (Exception e) {
				Log.v("error", "" + e.getMessage());
			}

			JSONObject res = parser.getJSONFromUrl(urlAddr);
			return res;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
				Log.v("result",""+result);
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
					commetnsList.add(tempmap);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
		final Dialog settingsDialog = new Dialog(CommentsActivity.this);
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
			CommentsActivity.this.finish();
			break;
		}

	}

	// @Override
	@SuppressLint("NewApi")
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
		case R.id.btn_cart:
			FragmentChangeActivity.rscart = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_alert:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent i=new Intent(CommentsActivity.this,LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.rsmenu = true;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			}
			else{
				Intent i=new Intent(CommentsActivity.this,LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}
}
