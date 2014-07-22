package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hitasoft.apps.milymarket.util.GetSet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class UserListActivity extends Activity implements OnClickListener {

	ImageButton back;
	ListView lv;
	ImageLoader imageLoader;
	LazyAdapter adapter;
	ArrayList<HashMap<String, String>> userList = null;
	private ImageButton home, near, cart, alert, menu;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list);

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

		imageLoader = ImageLoader.getInstance();
		back = (ImageButton) findViewById(R.id.smenu);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserListActivity.this.finish();
			}
		});
		lv = (ListView) findViewById(R.id.userList);
		userList = (ArrayList<HashMap<String, String>>) getIntent().getExtras()
				.get("data");
		adapter = new LazyAdapter(this, userList);
		lv.setAdapter(adapter);
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
			title.setText(comment.get("userName"));
			artist.setText(comment.get(""));
			String url = comment.get("imageName");
			imageLoader.loadImage(url, new ImageLoadingListener() {

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
					thumb_image.setImageBitmap(loadedImage);

				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

				}
			});
			return vi;
		}
	}

	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getBaseContext();
		switch (v.getId()) {

		case R.id.btn_home:
			fca.switchContent(new HomeFragment());
			break;
		case R.id.btn_near:
			fca.switchContent(new LocationFragment());
			break;
		case R.id.btn_cart:
			fca.switchContent(new CartFragment());
			break;
		case R.id.btn_alert:
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_menu:
			fca.switchContent(new ProfileFragment());

		}
	}

}
