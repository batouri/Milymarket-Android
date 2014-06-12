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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Fashion_view extends Activity {
	ImageLoader imagel;
	ViewPager viewPager;
	ViewFashionAdapter fashionAdapter;
	ArrayList<String> fashionary;
	ArrayList<HashMap<String, String>> fn = new ArrayList<HashMap<String, String>>();
	ImageButton back;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_view);
		imagel = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				Fashion_view.this).threadPoolSize(3).memoryCache(new WeakMemoryCache()).build();
	    imagel.init(config);
		defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
				.cacheOnDisc().build();
		viewPager = (ViewPager) findViewById(R.id.pager);
		fashionary = new ArrayList<String>();
		fn = Fashion_photos.fnphotos;
		back = (ImageButton) findViewById(R.id.home);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fashion_view.this.finish();
			}
		});
		fashionAdapter = new ViewFashionAdapter(getBaseContext(), fn);
		viewPager.setAdapter(fashionAdapter);
		viewPager.setCurrentItem(getIntent().getExtras().getInt("position"));

	}

	class ViewFashionAdapter extends PagerAdapter {

		Context context;
		ArrayList<HashMap<String, String>> viewPagerItems = null;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> temp;

		public ViewFashionAdapter(Context act,
				ArrayList<HashMap<String, String>> newary) {
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
			final TouchImageView image = (TouchImageView) itemView
					.findViewById(R.id.imgDisplay);
			final ProgressBar loader = (ProgressBar) itemView
					.findViewById(R.id.singleImageLoader);

			String imageloadingurl = temp.get(position).get(
					ConstantValues.TAG_FIMG);
			String imageurl=imageloadingurl.replace("thumb150", "original");
            imagel.displayImage(imageurl, image, defaultOptions, new ImageLoadingListener(){
			//imagel.loadImage(imageurl,defaultOptions, new ImageLoadingListener() {

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