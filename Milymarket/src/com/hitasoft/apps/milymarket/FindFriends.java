package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class FindFriends extends SherlockFragment implements OnClickListener {

	LinearLayout fb, twit, gPlus, contact;
	private ImageButton home, near, shop, alert, menu;
	private GridView grid;
	AdapterForHdpis hdpiAdapterprofile;
	ArrayList<HashMap<String, String>> followingdatas = new ArrayList<HashMap<String, String>>();
	ImageLoader profileLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.find_frineds_common_page,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);
		grid = (GridView) getView().findViewById(R.id.gridView1);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		int colorBlack = getResources().getColor(R.color.black);
		String text = "Find Friends";
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);
		profileLoader = ImageLoader.getInstance();
		try {
			String uId = ConstantValues.pref.getString("userprefid", "");
			new GetFollowerDetail().execute(Integer.parseInt(uId));

		} catch (Exception e) {
			Log.v("bug", e.toString());
		}
	}

	private void setAdapter() {
		try {
			hdpiAdapterprofile = new AdapterForHdpis(
					FindFriends.this.getActivity());
			grid.setAdapter(hdpiAdapterprofile);
		} catch (Exception e) {

		}
	}

	class GetFollowerDetail extends AsyncTask<Integer, Void, Void> {

		String url = ConstantValues.findfriendsUrl;
		ProgressDialog dialog = new ProgressDialog(
				FindFriends.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			int UserId = params[0];
			JSONParser parser = new JSONParser();
			JSONObject object = parser.getJSONFromUrl(url + UserId);
			try {
				String status = object.getString(ConstantValues.status);
				if (status.equalsIgnoreCase("true")) {
					JSONArray followers = object.getJSONArray("result");
					for (int i = 0; i < followers.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject value = followers.getJSONObject(i);
						String userid = value.getString("UserId");
						String userName = value.getString("userName");
						String fullname = value.getString("fullName");
						String profileImage = value.getString("imageName");
						String userstatus = value
								.getString(ConstantValues.TAG_STATUS);
						map.put("userId", userid);
						map.put(ConstantValues.TAG_USERNAME, userName);
						map.put(ConstantValues.TAG_FULLNAME, fullname);
						map.put("profileImage", profileImage);
						map.put(ConstantValues.TAG_STATUS, userstatus);

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
			dialog.setMessage("Fetching datas...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			setAdapter();
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
		public View getView(int position, View convertView, ViewGroup parent) {
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
			image.setTag(position);
			username.setText(tempMap.get("username"));
			userstatus.setText("UNFOLLOW");
			fullname.setText(tempMap.get(ConstantValues.TAG_FULLNAME));
			userstatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

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
							HashMap<String, String> map = followingdatas
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

	@Override
	public void onClick(View arg0) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (arg0.getId()) {
		case R.id.btn_home:
			FragmentChangeActivity.menumap = false;
			getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new HomeFragment());
			break;
		case R.id.btn_near:
			FragmentChangeActivity.rsnear = false;
			getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = true;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new LocationFragment());
			break;
		case R.id.btn_shop:
			FragmentChangeActivity.menumap = false;
			getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ShopFragment());
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.menumap = false;
			getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_menu:
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			Log.v("present userid", "" + GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = true;
			getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			break;

		}
	}

	/*
	 * private void performLocalSearch() { final Dialog dialog = new
	 * Dialog(FindFriends.this.getActivity(),
	 * android.R.style.Theme_Translucent);
	 * dialog.setContentView(R.layout.contact_friends_finder);
	 * dialog.setTitle("Search...");
	 * 
	 * final EditText name = (EditText) dialog.findViewById(R.id.name); Button
	 * searchButton = (Button) dialog.findViewById(R.id.search); Button
	 * cancelButton = (Button) dialog.findViewById(R.id.cancel);
	 * searchButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { if
	 * (name.getText().toString().trim().length() == 0) {
	 * name.setError("Please enter a query"); } else { if (dialog.isShowing()) {
	 * dialog.dismiss(); } new SearchContact().execute(name.getText().toString()
	 * .trim()); } } }); cancelButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { if (dialog.isShowing()) {
	 * dialog.dismiss(); } } });
	 * 
	 * dialog.show(); }
	 * 
	 * class SearchContact extends AsyncTask<String, Void, JSONObject> {
	 * 
	 * String url = ConstantValues.findFriendsSearch; ArrayList<HashMap<String,
	 * String>> userDatas = new ArrayList<HashMap<String, String>>();
	 * ProgressDialog dialog = new ProgressDialog(
	 * FindFriends.this.getActivity(),
	 * android.R.style.Theme_Translucent_NoTitleBar);
	 * 
	 * @Override protected JSONObject doInBackground(String... params) { url =
	 * url + params[0]; JSONParser parser = new JSONParser(); return
	 * parser.getJSONFromUrl(url); }
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * dialog.setMessage("Please wait..."); dialog.setCancelable(false);
	 * dialog.setCanceledOnTouchOutside(false); dialog.show(); }
	 * 
	 * @Override protected void onPostExecute(JSONObject result) {
	 * super.onPostExecute(result); String status; try { status =
	 * result.getString("status"); if (status.equalsIgnoreCase("true")) {
	 * JSONArray array = result.getJSONArray("result"); for (int i = 0; i <
	 * array.length(); i++) { HashMap<String, String> tmpMap = new
	 * HashMap<String, String>(); JSONObject obj = array.getJSONObject(i);
	 * String userId = obj.getString("UserId"); String userName =
	 * obj.getString("userName"); String imageName = obj.getString("imageName");
	 * 
	 * tmpMap.put("userId", userId); tmpMap.put("userName", userName);
	 * tmpMap.put("imageName", imageName); userDatas.add(tmpMap); } } } catch
	 * (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } if (dialog.isShowing()) { dialog.dismiss(); } if
	 * (userDatas.size() > 0) { Intent i = new
	 * Intent(FindFriends.this.getActivity(), UserListActivity.class);
	 * i.putExtra("data", userDatas); getActivity().startActivity(i);
	 * 
	 * } } }
	 */

	/*
	 * public void postData(String rid, String uid) {
	 * hdpiAdapterprofile.notifyDataSetChanged(); grid.invalidateViews();
	 * JSONParser parser = new JSONParser(); JSONObject result =
	 * parser.getJSONFromUrl
	 * ("http://199.230.52.9/fantacy/api/followuser?userId="+uid
	 * +"&followId="+rid); Log.v("result", ""+result); // Create a new
	 * HttpClient and Post Header // HttpClient httpclient = new
	 * DefaultHttpClient(); // HttpPost httppost = new
	 * HttpPost("http://199.230.52.9/fantacy/api/followuser?userId="
	 * +uid+"&followId="+rid);
	 * 
	 * // String uId = ConstantValues.pref.getString("userprefid", ""); try{
	 * hdpiAdapterprofile.notifyDataSetChanged(); } catch(NullPointerException
	 * e) {} //new GetFollowerDetail().execute(Integer.parseInt(uid)); }
	 */

}
