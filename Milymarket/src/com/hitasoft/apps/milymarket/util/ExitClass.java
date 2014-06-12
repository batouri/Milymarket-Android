package com.hitasoft.apps.milymarket.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hitasoft.apps.milymarket.HomeFragment;
import com.hitasoft.apps.milymarket.R;

public class ExitClass {
	private Context context;
	private int type = 0;
	private FragmentActivity activity;

	public ExitClass(Context ctx, int type, FragmentActivity activity) {
		this.context = ctx;
		this.type = type;
		this.activity = activity;

	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void exit() {
		final Dialog settingsDialog = new Dialog(this.context);
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		settingsDialog.setContentView(this.activity.getLayoutInflater()
				.inflate(R.layout.alert_for_error, null));
		settingsDialog.setCancelable(false);
		settingsDialog.setCanceledOnTouchOutside(false);
		settingsDialog.setTitle("Network Problem");
		Button retry = (Button) settingsDialog.findViewById(R.id.alertTryAgain);
		Button exit = (Button) settingsDialog.findViewById(R.id.alertExit);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
				ExitClass.this.activity.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.content_frame, new HomeFragment())
						.commit();
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (settingsDialog.isShowing()) {
					settingsDialog.dismiss();
				}
			}
		});
		settingsDialog.show();
	}
}