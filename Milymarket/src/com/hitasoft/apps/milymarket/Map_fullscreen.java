package com.hitasoft.apps.milymarket;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class Map_fullscreen extends Activity implements OnClickListener {
	LocationManager lm;
	public static GoogleMap map;
	public Marker markernew;
	public static LatLng lastLatLng;
	public static String gotcomment = "current location", key = "YourLocation";
	ImageButton back;
	private ImageButton home, near, shop, alert, profile;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fullscreen);

		back = (ImageButton) findViewById(R.id.smenu);

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

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2))
				.getMap();
		map.getUiSettings().setZoomControlsEnabled(false);
		getCurrentLocation();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Map_fullscreen.this.finish();
			}
		});

	}

	public void getCurrentLocation() {

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, false);
		Location lc = lm.getLastKnownLocation(provider);
		if (lc != null) {
			onLocationChanged(lc);
			lastLatLng = new LatLng(ProfileFragment.lat, ProfileFragment.lon);
			Log.v("ProfileFragment.lat", "" + ProfileFragment.lat);
			Log.v("ProfileFragment.lon", "" + ProfileFragment.lon);
			Log.v("lastlatlang", "" + lastLatLng);
			markernew = map.addMarker(new MarkerOptions().position(lastLatLng)
					.title(ProfileFragment.currentshop));

		} else {
		}
	}

	public void onLocationChanged(Location location) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(ProfileFragment.lat, ProfileFragment.lon))
				.zoom(10).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

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