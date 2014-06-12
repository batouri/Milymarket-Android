package com.hitasoft.apps.milymarket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SplashActivity extends Activity {

	private static int SPLASH_TIME_OUT = 2000;
	private static Dialog settingsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		if (this.getIntent().getExtras() != null) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					SplashActivity.this.finish();
				}
			}, SPLASH_TIME_OUT);
		} else {
			if (isNetworkAvailable(getBaseContext())) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						Intent i = new Intent(SplashActivity.this,
								FragmentChangeActivity.class);
						startActivity(i);
						finish();
						overridePendingTransition(R.anim.fade_in,
								R.anim.fade_out);
					}
				}, SPLASH_TIME_OUT);
			} else {
				exit();
			}
		}
	}

	public void exit() {
		settingsDialog = new Dialog(SplashActivity.this);
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		settingsDialog.setContentView(getLayoutInflater().inflate(
				R.layout.alert, null));
		settingsDialog.setCancelable(false);
		settingsDialog.setCanceledOnTouchOutside(false);
		settingsDialog.setTitle("Network Problem");
		Button retry = (Button) settingsDialog.findViewById(R.id.alertRetry);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
				if (!isNetworkAvailable(getBaseContext())) {
					settingsDialog.show();
				}

			}
		});
		settingsDialog.show();
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
}
