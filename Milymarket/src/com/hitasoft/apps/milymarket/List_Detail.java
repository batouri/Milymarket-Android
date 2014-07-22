package com.hitasoft.apps.milymarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;

public class List_Detail extends Activity implements OnClickListener {
	private static String itemid;
	Button cancel, save;
	BounceListView lv;
	Dialog listDialog;
	AdapterForHdpi adapter;
	public static ArrayList<HashMap<String, String>> tmp;
	HashMap<String, String> newary;
	private static String userEntryAddToList = null;
	private ImageButton home, near, cart, alert, menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_detail);

		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		cart = (ImageButton) findViewById(R.id.btn_cart);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);
		home.setImageResource(R.drawable.tab_bar_product_selected);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		itemid = getIntent().getStringExtra("itemid");
		Log.v("itemid", "" + itemid);
		cancel = (Button) findViewById(R.id.smenu);
		save = (Button) findViewById(R.id.save_list);
		lv = (BounceListView) findViewById(R.id.user_List);
		listDialog = new Dialog(List_Detail.this);
		newary = new HashMap<String, String>();
		loadData();
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List_Detail.this.finish();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {

				new PostListEntry().execute(tmp);

			}
		});

	}

	private void loadData() {
		tmp = new ArrayList<HashMap<String, String>>();
		try {

			new getList().execute(Integer.parseInt(itemid));

		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}

	}

	private void setAdapter() {
		try {
			Log.v("tmpadpter", "" + tmp);
			adapter = new AdapterForHdpi(List_Detail.this);
			lv.setAdapter(adapter);
		} catch (Exception e) {

		}

	}

	class getList extends AsyncTask<Integer, Void, Void> {

		int itemid;
		String url = ConstantValues.getlist;
		ProgressDialog dialog = new ProgressDialog(List_Detail.this);
		JSONParser jParser = new JSONParser();

		@Override
		protected Void doInBackground(Integer... params) {
			itemid = params[0];
			String urlAddr = url + GetSet.getUserId() + "&itemId=" + itemid;
			JSONObject response = jParser.getJSONFromUrl(urlAddr);
			try {
				String status = response.getString(ConstantValues.status);
				if (status.equalsIgnoreCase("true")) {
					JSONArray result = response
							.getJSONArray(ConstantValues.TAG_RESULT);
					tmp = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < result.length(); i++) {
						JSONObject values = result.getJSONObject(i);
						HashMap<String, String> tmpMap = new HashMap<String, String>();
						String listid = values
								.getString(ConstantValues.TAG_LIST_ID);
						String listname = values
								.getString(ConstantValues.TAG_LIST_NAME);
						String type = values.getString(ConstantValues.TAG_TYPE);
						String checked = values
								.getString(ConstantValues.TAG_CHECKED);

						tmpMap.put(ConstantValues.TAG_LIST_ID, listid);
						tmpMap.put(ConstantValues.TAG_LIST_NAME, listname);
						tmpMap.put(ConstantValues.TAG_TYPE, type);
						tmpMap.put(ConstantValues.TAG_CHECKED, checked);

						tmp.add(tmpMap);

					}
					Log.v("tmp", "" + tmp);

				}
			} catch (Exception e) {
				Log.v("error", "listview" + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				setAdapter();
				adapter.notifyDataSetChanged();

			}
		}

	}

	public class AdapterForHdpi extends BaseAdapter {

		private Context mContext;

		public AdapterForHdpi(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			Log.v("tmpsize", "" + tmp.size());
			return tmp.size();
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.simplerow, parent, false);

			} else {
				view = convertView;
				view.forceLayout();
			}

			newary = tmp.get(position);
			Log.v("newary", "" + newary);

			final TextView litem = (TextView) view
					.findViewById(R.id.rowTextView);
			final CheckBox cb = (CheckBox) view.findViewById(R.id.CheckBox01);

			litem.setText(newary.get(ConstantValues.TAG_LIST_NAME));
			Log.v("litem", "" + newary.get(ConstantValues.TAG_LIST_NAME));
			String check = newary.get(ConstantValues.TAG_CHECKED);
			Log.v("check", "" + check);
			if (check.equals("1")) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}

			cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (cb.isChecked()) {
						tmp.get(position).put(ConstantValues.TAG_CHECKED, "1");
						Log.v("cb cheked", "" + tmp);
					} else {
						tmp.get(position).put(ConstantValues.TAG_CHECKED, "0");
						Log.v("cb uncheked", "" + tmp);
					}

				}

			});

			return view;
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
			// int position = viewPager.getCurrentItem();
			// HashMap<String, String> mapFromList = datas.get(position);
			// String itemId = mapFromList.get(ConstantValues.TAG_ID);
			obj.put("itemId", itemid);
			JSONArray array = new JSONArray();
			for (int i = 0; i < listData2.size(); i++) {
				JSONObject obj2 = new JSONObject();
				HashMap<String, String> mapInLoop = listData2.get(i);
				obj2.put("listName", mapInLoop.get("listName"));
				obj2.put("listId", mapInLoop.get("listId"));
				if (mapInLoop.get("checked").equalsIgnoreCase("0")) {
					obj2.put("listStatus", "false");
				} else {
					obj2.put("listStatus", "true");
				}
				array.put(obj2);
			}
			if (List_Detail.userEntryAddToList != null) {
				JSONObject obj3 = new JSONObject();
				obj3.put("listName", List_Detail.userEntryAddToList);
				obj3.put("listId", "");
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
		ProgressDialog dialog = new ProgressDialog(List_Detail.this);

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
				List_Detail.userEntryAddToList = null;
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
		case R.id.btn_cart:
			FragmentChangeActivity.rscart = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_menu:
			// Intent i = new Intent(CommentsActivity.this,
			// ProfileTabHolder.class);
			// i.putExtra("userId", GetSet.getUserId());
			// startActivity(i);
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;

			FragmentChangeActivity.rsmenu = true;
			invalidateOptionsMenu();
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;

		}

	}

}
