package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hitasoft.apps.adapters.HorizontalListView;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CartFragmentClass extends SherlockFragment implements
		OnItemClickListener, OnClickListener {

	private ImageLoader cartImageLoader;
	ArrayList<HashMap<String, String>> cartList = new ArrayList<HashMap<String, String>>();
	ListView lv;
	LazyAdapter adapter;
	TextView nomore;
	private ImageButton home, near, cart, alert, menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cartImageLoader = ImageLoader.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.checkout_layout, container,
				false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		cart = (ImageButton) getView().findViewById(R.id.btn_cart);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		nomore = (TextView) getView().findViewById(R.id.nomoreItems);
		nomore.setVisibility(View.INVISIBLE);
		lv = (ListView) getView().findViewById(R.id.checkout_list);
		adapter = new LazyAdapter(CartFragmentClass.this.getActivity(),
				cartList);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(true);
		lv.setDivider(null);
		lv.setOnItemClickListener(this);

		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.cart);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);
		try {
			new homePageLoadImages().execute(0);
		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}

	}

	public class LazyAdapter extends BaseAdapter {

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater = null;
		HashMap<String, String> tempMap = null;

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
				vi = inflater.inflate(R.layout.main_checkout_layout, null);

			tempMap = data.get(position);
			int count = Integer.parseInt(tempMap.get("child"));
			String itemNumbers = "";
			for (int i = 0; i < count; i++) {
				try {
					JSONObject obj = new JSONObject(tempMap.get("child" + i));
					if (i == 0) {
						itemNumbers = obj.getString("id");
					} else {
						itemNumbers = itemNumbers + "," + obj.getString("id");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			tempMap.put("cartdetaildata", itemNumbers);
			final RelativeLayout proceed = (RelativeLayout) vi
					.findViewById(R.id.proceed);
			proceed.setTag(tempMap);
			proceed.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					Intent i = new Intent(CartFragmentClass.this.getActivity(),
							CartDetailClass.class);
					HashMap<String, String> temp = (HashMap<String, String>) proceed
							.getTag();
					i.putExtra("id", temp.get("merchantId"));
					i.putExtra("merchant", temp.get("merchantName"));
					i.putExtra("data", data);
					i.putExtra("cartdetaildata", temp.get("cartdetaildata"));
					getActivity().startActivityForResult(i,
							ConstantValues.CartRefresh);
				}
			});
			TextView merchantname = (TextView) vi
					.findViewById(R.id.merchantname);
			merchantname.setText("Vendeur: " + tempMap.get("merchantName"));
			merchantname.setSelected(true);
			HorizontalListView lv = (HorizontalListView) vi
					.findViewById(R.id.cartListHorizontal);
			AdapterForHdpi adapr = new AdapterForHdpi(getActivity(), tempMap);
			lv.setAdapter(adapr);
			vi.setClickable(true);
			vi.setFocusable(true);
			return vi;
		}
	}

	class homePageLoadImages extends AsyncTask<Integer, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				CartFragmentClass.this.getActivity());

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				parsing();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Un instant s'il vous plait");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}

		@Override
		protected void onPostExecute(Void unused) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				if (cartList.size() == 0) {
					nomore.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
			}

		}

	}

	private void parsing() {

		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.cart + GetSet.getUserId();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT);
				items = result.getJSONArray("data");
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					JSONObject merchant = items.getJSONObject(i);
					String merchantName = merchant.getString("merchantName");
					map.put("merchantName", merchantName);
					String merchantId = merchant.getString("merchantId");
					map.put("merchantId", merchantId);
					JSONArray images = merchant.getJSONArray("images");
					map.put("child", Integer.toString(images.length()));
					for (int j = 0; j < images.length(); j++) {
						JSONObject itemchild = images.getJSONObject(j);
						map.put("child" + j, itemchild.toString());
					}
					cartList.add(map);
				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public class AdapterForHdpi extends BaseAdapter {

		private Context mContext;
		private HashMap<String, String> jsonString;

		public AdapterForHdpi(Context ctx, HashMap<String, String> json) {
			mContext = ctx;
			jsonString = json;
		}

		@Override
		public int getCount() {
			String count = jsonString.get("child");
			return Integer.parseInt(count);
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
				view = inflater.inflate(R.layout.grid_item, parent, false);
			} else {
				view = convertView;
			}

			String child = jsonString.get("child" + position);
			JSONObject obj;
			try {
				obj = new JSONObject(child);
				String url = obj.getString("url");

				final ImageView imageView = (ImageView) view
						.findViewById(R.id.image);
				cartImageLoader.loadImage(url, new ImageLoadingListener() {

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
						imageView.setImageBitmap(loadedImage);

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
			view.setClickable(false);
			view.setFocusable(false);
			return view;
		}
	}

	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {

		case R.id.btn_home:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon=false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new HomeFragment());
			break;
		case R.id.btn_near:
			FragmentChangeActivity.rsnear = true;
			FragmentChangeActivity.filter_icon=true;
			FragmentChangeActivity.menumap = false;
			startActivity(new Intent(getActivity(), LocationFragment.class));
			break;
		case R.id.btn_cart:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon=false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new CartFragment());
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon=false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_menu:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon=false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
}
