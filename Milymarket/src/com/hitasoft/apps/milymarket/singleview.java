package com.hitasoft.apps.milymarket;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class singleview extends Activity {
	ImageLoader imagel;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	ImageView img;
	ImageButton back;
	ViewPagerAdapter pagerAdapter;
	ViewPager viewPager;
	ArrayList<String> newary;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_view);
		imagel = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				singleview.this).threadPoolSize(3).memoryCache(new WeakMemoryCache()).build();
	    imagel.init(config);
		defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
				.cacheOnDisc().build();
		viewPager = (ViewPager) findViewById(R.id.pager);
		newary = new ArrayList<String>();
		newary = Map.photosl.get("images");
		back = (ImageButton) findViewById(R.id.home);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				singleview.this.finish();
			}
		});

		pagerAdapter = new ViewPagerAdapter(getBaseContext(), newary);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(getIntent().getExtras().getInt("position"));

	}

	class ViewPagerAdapter extends PagerAdapter {

		Context context;
		ArrayList<HashMap<String, String>> viewPagerItems = null;
		LayoutInflater inflater;
		ArrayList<String> temp;

		public ViewPagerAdapter(Context act, ArrayList<String> newary) {
			this.temp = newary;
			this.context = act;
		}

		public int getCount() {
			return temp.size();

		}

		public Object instantiateItem(ViewGroup collection, int position) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.layout_fullscreen,
					collection, false);
			int j = position - 1;
			final TouchImageView image = (TouchImageView) itemView
					.findViewById(R.id.imgDisplay);
			final ProgressBar loader = (ProgressBar) itemView
					.findViewById(R.id.singleImageLoader);
			String imageloadingurl = temp.get(position);
			String imageurl=imageloadingurl.replace("thumb150", "original");
			imagel.displayImage(imageurl,image,defaultOptions, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					loader.setVisibility(View.VISIBLE);
					
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {

				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					image.setImageBitmap(loadedImage);
					loader.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					loader.setVisibility(View.VISIBLE);
				}
			});
			((ViewPager) collection).addView(itemView, 0);

			return itemView;

		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);

		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

}
