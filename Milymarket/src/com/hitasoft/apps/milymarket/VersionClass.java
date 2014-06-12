package com.hitasoft.apps.milymarket;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class VersionClass extends Activity {

	ImageButton back;
	TextView versions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.version_layout);
		versions = (TextView) findViewById(R.id.version);
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			versions.setText("Version " + version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		back = (ImageButton) findViewById(R.id.smenu);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				VersionClass.this.finish();
			}
		});

	}

}
