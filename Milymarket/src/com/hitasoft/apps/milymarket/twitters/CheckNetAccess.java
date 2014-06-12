package com.hitasoft.apps.milymarket.twitters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hitasoft.apps.milymarket.LoginActivity;
import com.hitasoft.apps.milymarket.R;

public class CheckNetAccess extends Activity {
	private boolean haveConnectedWifi;
	private boolean haveConnectedMobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checknet);
		Button netB = (Button) findViewById(R.id.netbutton);
		netB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				haveNetworkConnection();
				if ((haveConnectedWifi) || (haveConnectedMobile)) {
					Toast.makeText(CheckNetAccess.this, "Internet is go!", 3000)
							.show();
					Intent i = new Intent(CheckNetAccess.this,
							LoginActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(CheckNetAccess.this,
							"No access to Internet..please try again", 3000)
							.show();
				}
			}
		});

	}

	private boolean haveNetworkConnection() {
		haveConnectedWifi = false;
		haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
