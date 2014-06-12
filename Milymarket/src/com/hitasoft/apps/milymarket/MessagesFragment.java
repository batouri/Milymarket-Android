package com.hitasoft.apps.milymarket;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.hitasoft.apps.adapters.RoundedCornerBitmap;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MessagesFragment extends SherlockFragment implements
		OnClickListener {

	ImageLoader messageImageLoader;
	ListView notelist;
	CustomListViewAdapter adapter;
	public static ArrayList<HashMap<String, String>> Messagepageitems;
	public static String noti_user_id = null, notifiid = null,
			notif_itemid = null;
	ImageButton home, near, shop, alert, profile;
	TextView nulltxt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		messageImageLoader = ImageLoader.getInstance();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater
				.inflate(R.layout.notification, container, false);
		return v;
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
		ConstantValues.pref = getActivity()
				.getSharedPreferences("fantacyid", 0);
		ConstantValues.editor = ConstantValues.pref.edit();
		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		profile = (ImageButton) getView().findViewById(R.id.btn_profile);
		alert.setImageResource(R.drawable.tab_bar_alert_selected);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		profile.setOnClickListener(this);
		nulltxt = (TextView) getView().findViewById(R.id.notenulltxt);
		notelist = (ListView) getView().findViewById(R.id.notify_list);
		Messagepageitems = new ArrayList<HashMap<String, String>>();
		loadData();
		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.messages);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);
		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

		adapter = new CustomListViewAdapter(
				MessagesFragment.this.getActivity(),
				R.layout.notification_item, Messagepageitems);
		notelist.setAdapter(adapter);
	}

	private void loadData() {
		Messagepageitems = new ArrayList<HashMap<String, String>>();
		if (isNetworkAvailable(getActivity())) {
			try {
				new GetNotifications().execute(Integer.parseInt(GetSet
						.getUserId()));
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
			}
		} else {

		}
	}

	class GetNotifications extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				MessagesFragment.this.getActivity());

		@Override
		protected Void doInBackground(Integer... userid) {
			try {
				parsing(userid[0]);
			} catch (Exception e) {
				e.printStackTrace();
				Log.v("error", e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("S'il vous plaît attendre...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				dialog.dismiss();
				if (Messagepageitems.size() == 0) {
					nulltxt.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				dialog.dismiss();
			}
			adapter.notifyDataSetChanged();

		}
	}

	private void parsing(int userId) {
		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.GetMessages;
		if (GetSet.getUserId() != null) {
			urlAddr = urlAddr + "?userId=" + userId;
		}
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String type = null, username = null, settype = null, sellermsg = null, daytime = null, title = null, userimage = null, subimg = null, item_id = null, ucomment = null;
			String additem = "additem", sellernews = "Sellernews", comment = "Comment", favour = "Favorite", follow = "Follow", key = null;
			String response = json.getString(ConstantValues.TAG_STATUS_MESSAGE);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT_MESSAGE);
				items = result
						.getJSONArray(ConstantValues.TAG_NOTIFICATION_MESSAGE);
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();

					JSONObject temp = items.getJSONObject(i);
					type = temp.getString(ConstantValues.TAG_TYPE_MESSAGE);

					if (type.equals(sellernews)) {
						key = sellernews;
						settype = "Posted";
						noti_user_id = temp
								.getString(ConstantValues.TAG_USER_ID_MESSAGE);
						username = temp
								.getString(ConstantValues.TAG_USER_NAME_MESSAGE);
						sellermsg = temp
								.getString(ConstantValues.TAG_SELLER_MESSAGE);
						daytime = temp
								.getString(ConstantValues.TAG_DAYTIME_MESSAGE);
						userimage = temp
								.getString(ConstantValues.TAG_USERIMAGE_MESSAGE);

					} else if (type.equals(additem)) {
						key = additem;
						settype = "ADDED ITEM";
						noti_user_id = temp
								.getString(ConstantValues.TAG_USER_ID_MESSAGE);
						item_id = temp
								.getString(ConstantValues.TAG_ITEM_ID_MESSAGE);
						username = temp
								.getString(ConstantValues.TAG_USER_NAME_MESSAGE);
						title = temp
								.getString(ConstantValues.TAG_TITLE_MESSAGE);
						daytime = temp
								.getString(ConstantValues.TAG_DAYTIME_MESSAGE);
						userimage = temp
								.getString(ConstantValues.TAG_USERIMAGE_MESSAGE);
						subimg = temp
								.getString(ConstantValues.TAG_SUB_IMAGE_MESSAGE);

					} else if (type.equals(favour)) {
						key = favour;
						settype = "FAVOURED";
						noti_user_id = temp
								.getString(ConstantValues.TAG_USER_ID_MESSAGE);
						item_id = temp
								.getString(ConstantValues.TAG_ITEM_ID_MESSAGE);
						username = temp
								.getString(ConstantValues.TAG_USER_NAME_MESSAGE);
						title = temp
								.getString(ConstantValues.TAG_TITLE_MESSAGE);
						daytime = temp
								.getString(ConstantValues.TAG_DAYTIME_MESSAGE);
						userimage = temp
								.getString(ConstantValues.TAG_USERIMAGE_MESSAGE);
						subimg = temp
								.getString(ConstantValues.TAG_SUB_IMAGE_MESSAGE);

					} else if (type.equals(comment)) {
						key = comment;
						settype = "COMMENTED ON";
						noti_user_id = temp
								.getString(ConstantValues.TAG_USER_ID_MESSAGE);
						item_id = temp
								.getString(ConstantValues.TAG_ITEM_ID_MESSAGE);
						username = temp
								.getString(ConstantValues.TAG_USER_NAME_MESSAGE);
						ucomment = temp
								.getString(ConstantValues.TAG_COMMENT_MESSAGE);
						title = temp
								.getString(ConstantValues.TAG_TITLE_MESSAGE);
						daytime = temp
								.getString(ConstantValues.TAG_DAYTIME_MESSAGE);
						userimage = temp
								.getString(ConstantValues.TAG_USERIMAGE_MESSAGE);
						subimg = temp
								.getString(ConstantValues.TAG_SUB_IMAGE_MESSAGE);

					} else if (type.equals(follow)) {
						key = follow;
						settype = " is following ";
						noti_user_id = temp
								.getString(ConstantValues.TAG_USER_ID_MESSAGE);
						username = temp
								.getString(ConstantValues.TAG_USER_NAME_MESSAGE);
						daytime = temp
								.getString(ConstantValues.TAG_DAYTIME_MESSAGE);
						userimage = temp
								.getString(ConstantValues.TAG_USERIMAGE_MESSAGE);

					}
					map.put("key", key);
					map.put(ConstantValues.TAG_TYPE_MESSAGE, settype);
					map.put(ConstantValues.TAG_USER_ID_MESSAGE, noti_user_id);
					map.put(ConstantValues.TAG_ITEM_ID_MESSAGE, item_id);
					map.put(ConstantValues.TAG_USER_NAME_MESSAGE, username);
					map.put(ConstantValues.TAG_TITLE_MESSAGE, title);
					map.put(ConstantValues.TAG_DAYTIME_MESSAGE, daytime);
					map.put(ConstantValues.TAG_USERIMAGE_MESSAGE, userimage);
					map.put(ConstantValues.TAG_SUB_IMAGE_MESSAGE, subimg);
					map.put(ConstantValues.TAG_COMMENT_MESSAGE, ucomment);
					map.put(ConstantValues.TAG_SELLER_MESSAGE, sellermsg);
					Messagepageitems.add(map);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class CustomListViewAdapter extends ArrayAdapter<HashMap<String, String>> {

		Context context;
		ViewHolder holder = null;
		HashMap<String, String> tempMap = new HashMap<String, String>();

		public CustomListViewAdapter(Context context, int resourceId,
				ArrayList<HashMap<String, String>> items) {
			super(context, resourceId, items);
			this.context = context;

		}

		/* private view holder class */
		private class ViewHolder {
			ImageView product_img;
			ImageView user_image;
			TextView user_name;
			TextView item_type;
			TextView item_title;
			TextView comment;
			TextView time;
			RelativeLayout mainlayout;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			String type = null;
			tempMap = Messagepageitems.get(position);

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.message_items, null);
				holder = new ViewHolder();

			
				type = tempMap.get("key");
				holder.mainlayout=(RelativeLayout) convertView.findViewById(R.id.mainlayout);
				holder.user_name = (TextView) convertView
						.findViewById(R.id.user_name);
				holder.product_img = (ImageView) convertView
						.findViewById(R.id.product_img);
				holder.item_type = (TextView) convertView
						.findViewById(R.id.item_type);
				holder.item_title = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.user_image = (ImageView) convertView
						.findViewById(R.id.user_image);
				holder.comment = (TextView) convertView
						.findViewById(R.id.comment);
				
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.user_image.setOnClickListener(new OnClickListener()

				{
					@Override
					public void onClick(View arg0) {
						String uid = Messagepageitems.get(position).get(
								notifiid);
						ConstantValues.editor.putString("userprefid", uid);
						ConstantValues.editor.commit();
						FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
						fca.switchContent(new ProfileFragment());

					}
				});

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String time = tempMap.get(ConstantValues.TAG_DAYTIME_MESSAGE);
			long timestamp = Long.parseLong(time) * 1000;
			holder.user_name.setText(tempMap
					.get(ConstantValues.TAG_USER_NAME_MESSAGE));
			
			String filter=tempMap
					.get(ConstantValues.TAG_TYPE_MESSAGE).trim();
			Log.v("filter",""+filter);
			if(filter.equalsIgnoreCase("is following")){
				holder.item_type.setText("s'est abonné à votre profil");
				holder.mainlayout.setBackgroundResource(R.color.white);
			}
			else if(filter.equalsIgnoreCase("Posted")){
				holder.item_type.setText("posted");
				holder.mainlayout.setBackgroundResource(R.color.grey);
			}
			else if(filter.equalsIgnoreCase("COMMENTED ON")){
				holder.item_type.setText("a commenté sur");
				holder.mainlayout.setBackgroundResource(R.color.white);
			}
			else if(filter.equalsIgnoreCase("FAVOURED")){
				holder.item_type.setText("Favourited this");
				holder.mainlayout.setBackgroundResource(R.color.white);
			}
			else{
				holder.item_type.setText(tempMap
						.get(ConstantValues.TAG_TYPE_MESSAGE));
				holder.mainlayout.setBackgroundResource(R.color.white);
			}

			if (ConstantValues.TAG_TITLE_MESSAGE.equals("")
					|| ConstantValues.TAG_TITLE_MESSAGE.equals(null)) {
				holder.item_title.setText("");
			} else {
				holder.item_title.setText(tempMap
						.get(ConstantValues.TAG_TITLE_MESSAGE));

			}

			if (ConstantValues.TAG_SELLER_MESSAGE.equals("")
					|| ConstantValues.TAG_SELLER_MESSAGE.equals(null)) {
				holder.item_title.setText("");
			} else {
				holder.item_title.setText(tempMap
						.get(ConstantValues.TAG_SELLER_MESSAGE));
				
			}

			if (ConstantValues.TAG_COMMENT_MESSAGE.equals("")
					|| ConstantValues.TAG_COMMENT_MESSAGE.equals(null)) {
				holder.comment.setText("");
			} else {
				//holder.comment.setText(tempMap
				//		.get(ConstantValues.TAG_COMMENT_MESSAGE));
				//Log.v("holder.commnet",""+tempMap
				//		.get(ConstantValues.TAG_COMMENT_MESSAGE));
				
				String msg=tempMap
						.get(ConstantValues.TAG_COMMENT_MESSAGE);
				if(msg!=null){
					if(msg.contains("@<a")){
						String parts[]=msg.split("'>");
						String part1=parts[0];
						String part2=parts[1];
						String newparts[]=part2.split("</a>");
						String part3=newparts[0];
						String part4=newparts[1];
					//	Log.v("part1",""+part1);
					//	Log.v("part2",""+part2);
					//	Log.v("part3",""+part3);
					//	Log.v("part4",""+part4);
						holder.comment.setText("@"+part3+"\t"+part4);
						
						}
						else{
							holder.comment.setText(tempMap
								.get(ConstantValues.TAG_COMMENT_MESSAGE));
							  
					//		  Log.v("erreer",""+"String " + msg + " does not contain >");
						}
					
			//		Log.v("msg",""+ msg);
				}
				else{
					holder.comment.setText("");
					//holder.comment.setText(tempMap
					//		.get(ConstantValues.TAG_COMMENT_MESSAGE));
				}
			}

			if (ConstantValues.TAG_DAYTIME_MESSAGE.equals("")
					|| ConstantValues.TAG_DAYTIME_MESSAGE.equals(null)) {
				holder.time.setText("");
			} else {
				holder.time.setText("@" + getDate(timestamp));

			}

			messageImageLoader.loadImage(
					tempMap.get(ConstantValues.TAG_USERIMAGE_MESSAGE),
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
							holder.user_image.setImageBitmap(loadedImage);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
			if (tempMap.get(ConstantValues.TAG_SUB_IMAGE_MESSAGE) == null) {
				holder.product_img.setVisibility(View.GONE);

			} else {
				messageImageLoader.loadImage(
						tempMap.get(ConstantValues.TAG_SUB_IMAGE_MESSAGE),
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
								//Bitmap rdbitmap = RoundedCornerBitmap
								//		.getRoundedCornerBitmap(loadedImage, 10);
								holder.product_img.setImageBitmap(loadedImage);

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}
						});

				holder.product_img.setVisibility(View.VISIBLE);
			}
			holder.product_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String itemid = Messagepageitems.get(position).get(
							ConstantValues.TAG_ITEM_ID);
					String userid = Messagepageitems.get(position).get(
							ConstantValues.TAG_USER_ID_MESSAGE);
					Intent i = new Intent(MessagesFragment.this.getActivity(),
							DetailActivitynew.class);
					ConstantValues.editor.putString("itemid", itemid);
					ConstantValues.editor.commit();
					i.putExtra("from", 4);

					i.putExtra("data", Messagepageitems.get(position));
				//	i.putExtra("data", HomeFragment.HomePageItems.get(position));
					i.putExtra("item_id", itemid);
					i.putExtra("sellerid", userid);
					i.putExtra("comments", HomeFragment.commentsMap);
					i.putExtra("position", position);
					i.putExtra("from", 1);
					startActivity(i);

				}
			});
			return convertView;
		}
	}

	private String getDate(long timeStamp) {

		try {
			DateFormat sdf = DateFormat.getDateTimeInstance();
			Date netDate = (new Date(timeStamp));
			return sdf.format(netDate);
		} catch (Exception ex) {
			return "xx";
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.refresh, menu);
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		fca.switchContent(new MessagesFragment());
		return super.onOptionsItemSelected(item);
	}

	public void exit() {
		final Dialog settingsDialog = new Dialog(
				MessagesFragment.this.getActivity());
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		settingsDialog.setContentView(getActivity().getLayoutInflater()
				.inflate(R.layout.alert_for_error, null));
		settingsDialog.setCancelable(false);
		settingsDialog.setCanceledOnTouchOutside(false);
		settingsDialog.setTitle("Network Problem");
		Button retry = (Button) settingsDialog.findViewById(R.id.alertTryAgain);
		Button exit = (Button) settingsDialog.findViewById(R.id.alertExit);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
				FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
				fca.switchContent(new HomeFragment());
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
				MessagesFragment.this.getActivity().finish();
			}
		});
		settingsDialog.show();
	}

	public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, outputStream);
		return outputStream.toByteArray();
	}

	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {
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
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			}
			else{
				Intent i=new Intent(MessagesFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_profile:
			if(GetSet.isLogged()==true){
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ProfileFragment());
			}
			else{
				Intent i=new Intent(MessagesFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;

		}
	}
}
