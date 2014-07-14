package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hitasoft.apps.milymarket.R;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileListFragment extends SherlockFragment implements
		OnItemClickListener {

	private ImageLoader homeImageLoader;
	private GridView gridView = null;
	private static int orientation = 0;
	private ArrayList<HashMap<String, String>> ListPageItems;
	private int currentPage = 0;
	AdapterForHdpi hdpiAdapter;
	int screenWidth, screenHeight;
	LinearLayout bottomhome;
	TextView txt;
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeImageLoader = ImageLoader.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profile_list_grid, container,
				false);
		return v;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bottomhome = (LinearLayout) getView().findViewById(R.id.bottomhome);
		txt = (TextView) getView().findViewById(R.id.homenulltext2);
		txt.setVisibility(View.GONE);
		orientation = ProfileListFragment.this.getActivity().getWindowManager()
				.getDefaultDisplay().getOrientation();
		gridView = (GridView) (getView().findViewById(R.id.list_gallery));
		gridView.setSmoothScrollbarEnabled(true);
		gridView.setOnItemClickListener(this);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		if (screenWidth < 600) {
			gridView.setNumColumns(1);
		}
		loadData();
	}

	private void loadData() {
		ListPageItems = new ArrayList<HashMap<String, String>>();
		try {
			new ProfilePageListImages().execute(GetSet.getProfileUserId());
		} catch (Exception e) {
			Log.d("doInBackground", e.toString());
		}
		setAdapter();

	}

	private void setAdapter() {
		hdpiAdapter = new AdapterForHdpi(getActivity());
		gridView.setAdapter(hdpiAdapter);
	}

	class ProfilePageListImages extends AsyncTask<String, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(
				ProfileListFragment.this.getActivity());

		@Override
		protected Void doInBackground(String... params) {
			parsing(params[0]);
			return null;
		}

		@Override
		protected void onPreExecute() {
			if (currentPage == 0) {
				bottomhome.setVisibility(View.VISIBLE);
			}

		}

		@Override
		protected void onPostExecute(Void unused) {
			gridView.setAdapter(hdpiAdapter);
			try {
				if (currentPage == 0) {
					bottomhome.setVisibility(View.INVISIBLE);
				}
				if (ListPageItems.size() == 0) {
					txt.setVisibility(View.VISIBLE);
				}
				hdpiAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.d("doIndb", e.toString());
			}

		}

	}

	private void parsing(String page) {
		JSONArray items;
		HashMap<String, String> map;
		String urlAddr = ConstantValues.ProfilePageList + page;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(urlAddr.trim());
		try {
			String response = json.getString(ConstantValues.TAG_STATUS_LIST);
			if (response.equalsIgnoreCase("true")) {
				JSONObject result = json
						.getJSONObject(ConstantValues.TAG_RESULT_LIST);
				items = result.getJSONArray(ConstantValues.TAG_ITEMS_LIST);
				for (int i = 0; i < items.length(); i++) {
					map = new HashMap<String, String>();
					String imageUrl = null;
					JSONObject temp = items.getJSONObject(i);
					String id = temp.getString(ConstantValues.TAG_LIST_ID);
					String item_listname = temp
							.getString(ConstantValues.TAG_LIST_NAME);

					JSONArray listitems = temp
							.getJSONArray(ConstantValues.TAG_LIST_ITEM);
					map.put("child", Integer.toString(listitems.length()));
					if (listitems.length() > 0) {
						for (int j = 0; j < listitems.length(); j++) {
							JSONObject ListItemTemp = listitems
									.getJSONObject(j);
							imageUrl = ListItemTemp
									.getString(ConstantValues.TAG_LIST_IMAGEURL);
							map.put("child" + j, imageUrl);
						}

						map.put(ConstantValues.TAG_LIST_ID, id);
						map.put(ConstantValues.TAG_LIST_NAME, item_listname);
						map.put(ConstantValues.TAG_LIST_IMAGEURL, imageUrl);

						ListPageItems.add(map);
					}
				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public class AdapterForHdpi extends BaseAdapter {

		private Context mContext;

		public AdapterForHdpi(Context ctx) {
			mContext = ctx;
		}

		@Override
		public int getCount() {
			return ListPageItems.size();
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
				view = inflater.inflate(R.layout.profile_list_item, parent,
						false);
			} else {
				view = convertView;
				view.forceLayout();
			}
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap = ListPageItems.get(position);
			final ImageView image1 = (ImageView) view.findViewById(R.id.Image1);
			final ImageView image2 = (ImageView) view.findViewById(R.id.Image2);
			final ImageView image3 = (ImageView) view.findViewById(R.id.Image3);
			final ImageView image4 = (ImageView) view.findViewById(R.id.Image4);
			Display display = getActivity().getWindowManager()
					.getDefaultDisplay();
			LayoutParams lp;
			@SuppressWarnings("deprecation")
			int screenWidth = display.getWidth();
			if (screenWidth < 600) {
				lp = new LayoutParams(screenWidth / 2 - screenWidth / 5,
						screenWidth / 2 - screenWidth / 5);
			} else {
				lp = new LayoutParams(screenWidth / 2 - screenWidth / 10,
						screenWidth / 2 - screenWidth / 10);
			}

			image1.setVisibility(View.INVISIBLE);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.INVISIBLE);
			image4.setVisibility(View.GONE);
			TextView name = (TextView) view.findViewById(R.id.ImageName);
			TextView things = (TextView) view.findViewById(R.id.Things);
			name.setText(tempMap.get(ConstantValues.TAG_LIST_NAME));
			things.setText(tempMap.get("child") + " items");

			if (Integer.parseInt(tempMap.get("child")) >= 3) {
				for (int j = 0; j < Integer.parseInt(tempMap.get("child")); j++) {
					String url = tempMap.get("child" + j);
					switch (j) {
					case 0:
						image1.setVisibility(View.VISIBLE);
						homeImageLoader.displayImage(url, image1);
						break;
					case 1:
						image2.setVisibility(View.VISIBLE);
						homeImageLoader.displayImage(url, image2);
						break;
					case 2:
						image3.setVisibility(View.VISIBLE);
						homeImageLoader.displayImage(url, image3);
						break;
					}

				}
			} else {
				for (int k = 0; k < Integer.parseInt(tempMap.get("child")); k++) {
					String url = tempMap.get("child" + k);
					switch (k) {
					case 0:
						image1.setVisibility(View.VISIBLE);
						homeImageLoader.displayImage(url, image1);
						break;
					case 1:
						image2.setVisibility(View.VISIBLE);
						homeImageLoader.displayImage(url, image2);
						break;
					case 2:
						image3.setVisibility(View.VISIBLE);
						homeImageLoader.displayImage(url, image3);
						break;
					}
				}
			}
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		HashMap<String, String> listMap = ListPageItems.get(arg2);
		String listId = listMap.get(ConstantValues.TAG_LIST_ID);
		Intent i = new Intent(ProfileListFragment.this.getActivity(),
				ListDetailpage.class);
		i.putExtra("id", listId);
		getActivity().startActivity(i);
	}

}
