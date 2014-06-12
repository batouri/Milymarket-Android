package com.hitasoft.apps.milymarket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.hitasoft.apps.milymarket.R;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class SettingFragment extends SherlockFragment implements
		OnClickListener {

	private TextView semail, passchange, languageSelction;
	private static CheckBox everything, following;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	public static Dialog dialog;
	private EditText oldpassword, newpassword, confirmpassword;
	private Button save, cancel;
	HashMap<String, String> tempMap1 = new HashMap<String, String>();
	String oldpass;
	String newpass;
	String confirmpass;
	ImageLoader settngsLodre;
	ImageView profileImage;
	TextView firstName, userName, versionno;
	HashMap<String, String> emailSettingsValues = new HashMap<String, String>();
	ProgressDialog pgsDialog;
	private ImageButton home, near, shop, alert, profile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settngsLodre = ImageLoader.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.settings_page, container,
				false);
		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		profile = (ImageButton) getView().findViewById(R.id.btn_profile);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		profile.setOnClickListener(this);

		pgsDialog = new ProgressDialog(SettingFragment.this.getActivity());
		SettingFragment.this.getView().setVisibility(View.INVISIBLE);
		versionno = (TextView) getView().findViewById(R.id.versionno);
		PackageInfo pInfo;
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0);
			String version = pInfo.versionName;
			versionno.setText("v " + version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		profileImage = (ImageView) getView().findViewById(R.id.profileImage);
		firstName = (TextView) getView().findViewById(R.id.firstName);
		userName = (TextView) getView().findViewById(R.id.userName);

		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.settings);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

		if (GetSet.isLogged()) {
			try {
				new GetSettings().execute();
			} catch (NullPointerException npe) {
				if (pgsDialog != null && pgsDialog.isShowing()) {
					pgsDialog.dismiss();
				}
				new GetSettings().execute();
			} catch (Exception e) {
				if (pgsDialog != null && pgsDialog.isShowing()) {
					pgsDialog.dismiss();
				}
				new GetSettings().execute();
			}
		} else {
			Toast.makeText(getActivity(), "Please Login to continue",
					Toast.LENGTH_LONG).show();
		}
		passchange = (TextView) getView().findViewById(R.id.passchange);
		passchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.change_password);
				dialog.setTitle("Changer de mot de passe");
				dialog.setCancelable(true);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
				passchangebutton();

			}
		});

		semail = (TextView) getView().findViewById(R.id.semail);
		semail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                Log.v("emailSettingsValues",""+emailSettingsValues);
				Intent emailsetting = new Intent(getActivity(),
						EmailSettings.class);
				emailsetting.putExtra("data", emailSettingsValues);
				startActivity(emailsetting);
			}
		});
		everything = (CheckBox) getView().findViewById(R.id.everything);
		everything.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					tempMap.put("id", "1");
					tempMap.put("action", "true");
					new CheckedSetting().execute(tempMap);
				} else {

					tempMap.put("id", "1");
					tempMap.put("action", "false");
					new CheckedSetting().execute(tempMap);
				}

			}
		});

		following = (CheckBox) getView().findViewById(R.id.following);
		following.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					tempMap.put("id", "2");
					tempMap.put("action", "true");
					new CheckedSetting().execute(tempMap);
				} else {

					tempMap.put("id", "2");
					tempMap.put("action", "false");
					new CheckedSetting().execute(tempMap);
				}

			}
		});

		/*languageSelction = (TextView) getView().findViewById(R.id.lang);
		languageSelction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLanguageDialog();

			}
		});*/
		profileImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent i=new Intent(SettingFragment.this.getActivity(),userimage_upload.class);
				startActivity(i);
				
			}
			
		});
		
	}

	protected void ShowLanguageDialog() {
		final Dialog dialog = new Dialog(SettingFragment.this.getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.lang_list);
		ImageButton close = (ImageButton) dialog.findViewById(R.id.langcancel);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}

			}
		});
		ListView lv = (ListView) dialog.findViewById(R.id.langList);
		String[] values = new String[] {
				getResources().getString(R.string.defaults), "English",
				getResources().getString(R.string.french) };

		MArrayAdapter adapter = new MArrayAdapter(getActivity(), values, 0);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				arg1.setSelected(true);
			}
		});
		dialog.show();
	}

	public void passchangebutton() {

		oldpassword = (EditText) dialog.findViewById(R.id.oldpassword);
		newpassword = (EditText) dialog.findViewById(R.id.newpassword);
		confirmpassword = (EditText) dialog.findViewById(R.id.confirmpassword);

		oldpass = oldpassword.getText().toString();
		newpass = newpassword.getText().toString();
		confirmpass = confirmpassword.getText().toString();

		save = (Button) SettingFragment.dialog.findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v("current", GetSet.getPassword());
				if (GetSet.getPassword().equals(
						oldpassword.getText().toString())) {
					Log.v("old", oldpassword.getText().toString());
					Log.v("newpass", newpass);
					Log.v("confirmpass", confirmpass);
					if ((oldpassword.getText().toString()).equals("")
							|| (newpassword.getText().toString()).equals("")
							|| (confirmpassword.getText().toString())
									.equals("")) {
						oldpassword.setError("Please fill the fields");
					} else if ((newpassword.getText().toString())
							.equals(confirmpassword.getText().toString())) {
						Log.v("success", newpass);
						new ChangePass().execute();
					} else {
						confirmpassword.setError("password Mismatched.");
					}
				} else {
					oldpassword.setError("Wrong Password.");

				}
			}
		});

		cancel = (Button) SettingFragment.dialog.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SettingFragment.dialog.cancel();

			}
		});
	}

	class CheckedSetting extends
			AsyncTask<HashMap<String, String>, Void, JSONObject> {

		String url = ConstantValues.emailsettingUrl;
		JSONParser parser = new JSONParser();

		@Override
		protected JSONObject doInBackground(HashMap<String, String>... tempMaps) {
			try {
				String type;
				String action;
				type = tempMap.get("id");
				action = tempMap.get("action");
				url = url + "?userId=" + GetSet.getUserId() + "&type=+" + type
						+ "&action=" + action;
				JSONObject res = parser.getJSONFromUrl(url);
				Log.v("response", "response" + res);
				return res;
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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

			}

		}

	}
	
	class ChangePass extends AsyncTask<Void, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected String doInBackground(Void... arg0) {
			String result = postData();
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("S'il vous plaît attendre...");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			try {
				JSONObject jonj = new JSONObject(result);
				if (jonj.getString(ConstantValues.status).equalsIgnoreCase(
						"true")) {
					Log.v("response", jonj.toString());
					Toast.makeText(getActivity(),
							"Password Changed Successfully", Toast.LENGTH_LONG)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String postData() {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ConstantValues.changepasswordUrl);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Log.v("old", "old" + oldpassword.getText().toString());
			Log.v("new", "new" + newpassword.getText().toString());
			nameValuePairs.add(new BasicNameValuePair("userId", GetSet
					.getUserId()));
			nameValuePairs.add(new BasicNameValuePair("oldPassword",
					oldpassword.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("newPassword",
					newpassword.getText().toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.v("response", result.toString());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {

		}
		return result;
	}

	class GetSettings extends AsyncTask<Void, Void, JSONObject> {

		JSONParser parser = new JSONParser();

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject result = parser
					.getJSONFromUrl(ConstantValues.getsettings
							+ GetSet.getUserId());
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pgsDialog.setMessage("S'il vous plaît attendre...");
			pgsDialog.setIndeterminate(true);
			pgsDialog.setCancelable(false);
			pgsDialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					if (result.getString("status").equalsIgnoreCase("true")) {
						JSONObject values = result.getJSONObject("result");
						firstName.setText(values.getString("userName"));
						settngsLodre.loadImage(values.getString("userImage"),
								new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(
											String imageUri, View view) {

									}

									@Override
									public void onLoadingFailed(
											String imageUri, View view,
											FailReason failReason) {

									}

									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										profileImage
												.setImageBitmap(loadedImage);

									}

									@Override
									public void onLoadingCancelled(
											String imageUri, View view) {

									}
								});
						JSONObject timeline = values
								.getJSONObject("userTimeLineDetails");
						String everything = timeline.getString("Everything");
						String Following = timeline.getString("Following");
						if (everything.equalsIgnoreCase("true")) {
							SettingFragment.everything.setChecked(true);
						} else {
							SettingFragment.everything.setChecked(false);
						}
						if (Following.equalsIgnoreCase("true")) {
							SettingFragment.following.setChecked(true);
						} else {
							SettingFragment.following.setChecked(false);
						}
						emailSettingsValues.put("someoneFollow",
								values.getString("someoneFollow"));
						emailSettingsValues.put("someoneShows",
								values.getString("someoneShows"));
						emailSettingsValues.put("someoneComment",
								values.getString("someoneComment"));
						emailSettingsValues.put("someoneFeatured",
								values.getString("someoneFeatured"));
						emailSettingsValues.put("someoneMentions",
								values.getString("someoneMentions"));

					} else {
						Toast.makeText(getActivity(),
								result.getString("message"), Toast.LENGTH_LONG)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				SettingFragment.this.getView().setVisibility(View.VISIBLE);
				if (pgsDialog.isShowing()) {
					pgsDialog.dismiss();
				}
			} else {
				if (pgsDialog != null && pgsDialog.isShowing()) {
					pgsDialog.dismiss();
				}
				new GetSettings().execute();
			}
		}
	}

	private static class MArrayAdapter extends ArrayAdapter<String> {
		private static int positn;

		public MArrayAdapter(final Context context, final String[] objects,
				int n) {
			super(context, android.R.layout.simple_list_item_single_choice,
					objects);
			positn = n;
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			final CheckedTextView view = (CheckedTextView) super.getView(
					position, convertView, parent);
			view.setChecked(true);
			return view;
		}

	}

	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {

		case R.id.btn_home:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new HomeFragment());
			break;
		case R.id.btn_near:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = true;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new LocationFragment());
			break;
		case R.id.btn_shop:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new CategoryFragment());
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_profile:
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ProfileFragment());
			break;
		}
	}
}