package com.hitasoft.apps.milymarket;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;

import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;

public class EmailSettings extends Activity implements OnClickListener {

	private CheckBox follow, comment, feature;
	String key;
	String value;
	ImageButton smenu;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	HashMap<String, String> dataMap = new HashMap<String, String>();
	private ImageButton home, near, shop, alert, profile;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_settings);
		dataMap = (HashMap<String, String>) this.getIntent().getExtras()
				.get("data");
		Log.v("dataMap",""+dataMap);

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

		smenu = (ImageButton) findViewById(R.id.smenu);
		smenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EmailSettings.this.finish();
			}
		});
		follow = (CheckBox) findViewById(R.id.follow);
		follow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					tempMap.put("id", "3");
					tempMap.put("action", "true");
					new EmailSetting().execute(tempMap);
				} else {

					tempMap.put("id", "3");
					tempMap.put("action", "false");
					new EmailSetting().execute(tempMap);
				}

			}
		});

		comment = (CheckBox) findViewById(R.id.comment);
		comment.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					tempMap.put("id", "4");
					tempMap.put("action", "true");
					new EmailSetting().execute(tempMap);
				} else {

					tempMap.put("id", "4");
					tempMap.put("action", "false");
					new EmailSetting().execute(tempMap);
				}

			}
		});

		feature = (CheckBox) findViewById(R.id.feature);
		feature.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					tempMap.put("id", "5");
					tempMap.put("action", "true");
					new EmailSetting().execute(tempMap);
				} else {

					tempMap.put("id", "5");
					tempMap.put("action", "true");
					new EmailSetting().execute(tempMap);
				}

			}
		});
		if (dataMap.get("someoneFollow").equalsIgnoreCase("true")) {
			follow.setChecked(true);
		}
		if (dataMap.get("someoneComment").equalsIgnoreCase("true")) {
			comment.setChecked(true);
		}
		if (dataMap.get("someoneFeatured").equalsIgnoreCase("true")) {
			feature.setChecked(true);
		}

	}

	class EmailSetting extends
			AsyncTask<HashMap<String, String>, Void, JSONObject> {

		String url = ConstantValues.emailsettingUrl;
		JSONParser parser = new JSONParser();

		// ProgressDialog dialog = new ProgressDialog(EmailSettings.this);

		@Override
		protected JSONObject doInBackground(HashMap<String, String>... tempMaps) {
			String type;
			String action;
			type = tempMap.get("id");
			action = tempMap.get("action");
			url = url + "?userId="+GetSet.getUserId() + "&type=+" + type + "&action=" + action;
			JSONObject res = parser.getJSONFromUrl(url);
			return res;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog.setIndeterminate(true);
			// dialog.setCancelable(false);
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			String status = null;
			try {
				status = result.getString("status");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (status != null && status.equalsIgnoreCase("true")) {
				HashMap<String, String> tempmap = new HashMap<String, String>();

				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * task to get the comments detail form the server as response
				 * and notify the dataset to be changed
				 */
			} else {
				// Toast.makeText(getBaseContext(),
				// "Failed-Something went wrong",
				// Toast.LENGTH_LONG).show();
			}

		}

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
			Intent i = new Intent(EmailSettings.this, ProfileTabHolder.class);
			i.putExtra("userId", GetSet.getUserId());
			startActivity(i);
			break;

		}
	}

}
