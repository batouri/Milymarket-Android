package com.hitasoft.apps.milymarket;

/*************************************************************\
 * 
 * @author 'Hitasoft Technologies'
 * 
 * Description:
 * This Activity is used for Main Home Page
 * 
 * Revision History:
 * Version 1.0 - Initial Version
 *   
 \************************************************************/

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class FragmentChangeActivity extends SlidingFragmentActivity {

	private Fragment mContent;
	SlidingMenu sm;
	private BroadcastReceiver networkStateReceiver;
	SharedPreferences pref;
	Editor editor;
	private static boolean resumecart = false;
	private static boolean resumeHome = false;
	private static boolean resumeActivity = false;
	public static boolean rscart = false;
	public static boolean rshome = false;
	public static boolean rsnear = false;
	public static boolean rsshop = false;
	public static boolean rsnote = false;
	public static boolean rsprofile = false;
	public static boolean rsmenu = false;
	public static boolean rsmostp = false;
	public static boolean rscatg = false;
	public static boolean rssetg=false;
	public static Dialog settingsDialog;
	public static Locale systemLocale;
	public static int flg1, flg2, flg3, flg4, flg5;
	public static boolean menumap = false;
	public static boolean filter_icon = false;
	public static boolean filtered = false;
	public static String uploadedimgname = null;
	public static FantacyApplication acontroller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		internetCheck();
		FragmentChangeActivity.setLanguage(getBaseContext(),
				Locale.US.getLanguage());
		systemLocale = getResources().getConfiguration().locale;
		pref = getApplicationContext().getSharedPreferences("FantacyPref",
				MODE_PRIVATE);
		acontroller=(FantacyApplication) getApplicationContext();
		editor = pref.edit();
		if (pref.getBoolean("isLogged", false)) {
			GetSet.setLogged(true);
			GetSet.setUserId(pref.getString("userId", null));
			GetSet.setUserName(pref.getString("userName", null));
			GetSet.setEmail(pref.getString("Email", null));
			GetSet.setPassword(pref.getString("Password", null));
			GetSet.setFullName(pref.getString("fullname", null));
			GetSet.setImageUrl(pref.getString("photo", null));
		}
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		setContentView(R.layout.splash_screen);
		Display display = this.getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null){
			menumap = false;
			filter_icon = false;
			rshome =false;
			mContent = new HomeFragment();
		}
		setContentView(R.layout.content_frame);
		sm = getSlidingMenu();

		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		if (screenWidth <= 650) {
			sm.setBehindWidth(screenWidth - screenWidth * 1 / 9);
			// sm.setBehindWidthRes(screenWidth * 3 / 4);
		} else {
			boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
			if (!tabletSize) {
				sm.setBehindWidth(screenWidth * 1 / 2);
			} else {
				sm.setBehindWidth(R.dimen.slidingmenu_Behind_offset_600dp);
				sm.setBehindWidthRes(R.dimen.slidingmenu_Behind_offset_600dp);
			}
		}
		sm.setFadeDegree(0.35f);
		setSlidingActionBarEnabled(false);
		// set the Above View
		ActionBar ab = getSupportActionBar();
		//ab.setLogo(R.drawable.ac_bar_logo);
		ab.setHomeButtonEnabled(false);
		ab.setTitle("");
		ab.setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		Drawable d = getResources().getDrawable(R.drawable.top_bg);
		ab.setBackgroundDrawable(d);
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		getSlidingMenu().setSlidingEnabled(false);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (menumap == true) {
			menu.findItem(R.id.map).setVisible(true);
			menumap = false;
		} else {
			menu.findItem(R.id.map).setVisible(false);
			menumap = true;
		}
		if (filter_icon == true) {
			menu.findItem(R.id.filter).setVisible(true);
			filter_icon = false;
		} else {
			menu.findItem(R.id.filter).setVisible(false);
			filter_icon = true;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			sm.toggle();
			break;
		case R.id.map:
			Log.v("map clicked", "mapping");
			Intent map = new Intent(FragmentChangeActivity.this, Map.class);
			startActivity(map);
			return true;
		case R.id.filter:
			Log.v("filter clicked", "filtering");
			filtered = true;
			LocationFragment.Showfilter();
			return true;
			// case R.id.menu_refresh:
			// switchContent(new HomeFragment());LocationFound
			// break;

			// case R.id.signIn:
			// if (!GetSet.isLogged()) {
			// Intent i = new Intent(FragmentChangeActivity.this,
			// LoginActivity.class);
			// // MenuFragment.this.getActivity().finish();
			// startActivityForResult(i, ConstantValues.LoginRefresh);
			// } else {
			// DialogInterface.OnClickListener dialogClickListener = new
			// DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// switch (which) {
			// case DialogInterface.BUTTON_POSITIVE:
			// GetSet.reset();
			// FragmentChangeActivity.this
			// .switchContent(new HomeFragment());
			// FragmentChangeActivity.this
			// .getSupportFragmentManager()
			// .beginTransaction()
			// .replace(R.id.menu_frame,
			// new MenuFragment()).commit();
			// break;
			//
			// case DialogInterface.BUTTON_NEGATIVE:
			// break;
			// }
			// }
			// };
			//
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setMessage("Do you really want to signOut?")
			// .setPositiveButton("Yes", dialogClickListener)
			// .setNegativeButton("No", dialogClickListener).show();
			//
			// }
			// break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void internetCheck() {
		networkStateReceiver = new BroadcastReceiver() {

			@SuppressWarnings("deprecation")
			@Override
			public void onReceive(Context context, Intent intent) {
				NetworkInfo networkinfo = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (networkinfo != null
						&& networkinfo.getState() == NetworkInfo.State.CONNECTED) {
					Log.v("we are connected", "we are connected");
				} else {
					exit();
				}
			}
		};
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(this.networkStateReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();
		if (resumecart) {
			resumecart = false;
			menumap = false;
			filter_icon = false;
			switchContent(new CartFragmentClass());
		} else if(rscart){
			resumecart = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new CartFragmentClass());
		}
		else if (resumeHome) {
			resumeHome = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new HomeFragment());
		} else if (rshome) {
			rshome = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new HomeFragment());
		} else if (rsnear) {
			rsnear = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new LocationFragment());
		} else if (rsshop) {
			rsshop = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new ShopFragment());
		} else if (rsnote) {
			rsnote = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new MessagesFragment());
		} else if (rsprofile) {
			rsprofile = false;
			menumap = true;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new ProfileFragment());
		} else if (rsmostp) {
			rsmostp = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new MostPopular());
		} else if (rscatg) {
			rscatg = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new CategoryFragment());
		}else if (rsmenu) {
			rsmenu = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new MenuFragment());
		}else if (rssetg) {
			rssetg = false;
			menumap = false;
			filter_icon = false;
			supportInvalidateOptionsMenu();
			switchContent(new SettingFragment());
		}

		if (FragmentChangeActivity.resumeActivity) {
			FragmentChangeActivity.resumeActivity = false;
			Intent i = new Intent(FragmentChangeActivity.this,
					SplashActivity.class);
			i.putExtra("Data", true);
			startActivity(i);
		}
	}

	public void exit() {
		settingsDialog = new Dialog(FragmentChangeActivity.this);
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

	public void dismiss(View v) {
		switch (v.getId()) {
		case R.id.alertClose:
			FragmentChangeActivity.this.finish();
			break;
		}

	}

	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					FragmentChangeActivity.resumeActivity = true;
					moveTaskToBack(false);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.reallyExit))
				.setPositiveButton(getResources().getString(R.string.exit),
						dialogClickListener)
				.setNegativeButton(getResources().getString(R.string.keep),
						dialogClickListener).show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.v("hi", "hi");
		if (savedInstanceState != null) {
			FragmentChangeActivity.resumeActivity = true;
			editor.putBoolean("doResume", true);
			editor.commit();
		} else {
			editor.putBoolean("doResume", false);
			editor.commit();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					Log.i("Class", info[i].getState().toString());
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == ConstantValues.CartRefresh) {
			if (data.hasExtra("returnKey1")) {
				resumecart = true;
			}
		} else if (resultCode == RESULT_OK
				&& requestCode == ConstantValues.LoginRefresh) {
			if (data.hasExtra("returnKey1")) {
				resumeHome = true;
			}
		}

		if (resultCode == RESULT_OK) {
			Log.v("RESULT_OK", "");
			if (requestCode == 1) {
				Log.v("camera selected", "");
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					Bitmap bitmap;
					bitmap = decodeFile(f.getAbsolutePath());
					AddProduct.addphoto.setImageBitmap(bitmap);
					f.delete();
					OutputStream outFile = null;
					File file = new File(
							Environment.getExternalStorageDirectory(),
							String.valueOf(System.currentTimeMillis()) + ".jpg");

					try {
						outFile = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
						galleryAddPic(file.toString());
						outFile.flush();
						outFile.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == 2) {
				try {
					Log.v("gallery code opened", "");
					Uri selectedImage = data.getData();
					String[] filePath = { MediaStore.Images.Media.DATA };
					Cursor c = getContentResolver().query(selectedImage,
							filePath, null, null, null);
					c.moveToFirst();
					int columnIndex = c.getColumnIndex(filePath[0]);
					String picturePath = c.getString(columnIndex);
					Log.v("path of image from gallery......******************.........",
							picturePath + "");
					c.close();
					Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
					Log.v("gallery code bitmap", "" + thumbnail);
					AddProduct.addphoto.setImageBitmap(thumbnail);
					new ImageUploadTask().execute(picturePath);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (OutOfMemoryError ome) {
					ome.printStackTrace();
				}

			}
		}
	}

	public void galleryAddPic(String file) {
		File f = new File(file);
		Uri contentUri = Uri.fromFile(f);
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
		sendBroadcast(mediaScanIntent);
	}

	private Bitmap decodeFile(String fPath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		BitmapFactory.decodeFile(fPath, opts);
		final int REQUIRED_SIZE = 70;
		int scale = 1;

		if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
			final int heightRatio = Math.round((float) opts.outHeight
					/ (float) REQUIRED_SIZE);
			final int widthRatio = Math.round((float) opts.outWidth
					/ (float) REQUIRED_SIZE);
			scale = heightRatio < widthRatio ? heightRatio : widthRatio;//
		}
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(
				Bitmap.Config.RGB_565, false);
		return bm;
	}

	class ImageUploadTask extends AsyncTask<String, Void, String> {
		JSONObject jsonobject = null;
		String Json = "";

		@SuppressWarnings("deprecation")
		protected String doInBackground(String... imgpath) {
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			DataInputStream inStream = null;
			StringBuilder builder = new StringBuilder();
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			String urlString = ConstantValues.uploadurl;
			try {
				String exsistingFileName = imgpath[0];
				Log.v(" exsistingFileName", exsistingFileName);
				FileInputStream fileInputStream = new FileInputStream(new File(
						exsistingFileName));
				URL url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"images\";filename=\""
						+ exsistingFileName + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				Log.e("MediaPlayer", "Headers are written");
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String inputLine;
				Log.v("in", "" + in);
				while ((inputLine = in.readLine()) != null)
					builder.append(inputLine);

				Log.e("MediaPlayer", "File is written");
				fileInputStream.close();
				Json = builder.toString();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {
				Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
			} catch (IOException ioe) {
				Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
			}
			try {
				inStream = new DataInputStream(conn.getInputStream());
				String str;
				while ((str = inStream.readLine()) != null) {
					Log.e("MediaPlayer", "Server Response" + str);
				}
				inStream.close();
			} catch (IOException ioex) {
				Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
			}

			return null;

		}

		@Override
		protected void onPreExecute() {

		}

		protected void onPostExecute(String sResponse) {

			try {
				jsonobject = new JSONObject(Json);
				JSONObject image = jsonobject.getJSONObject("Image");
				JSONArray list = image.getJSONArray("list");
				for (int i = 0; i < list.length(); i++) {
					JSONObject values = list.getJSONObject(i);
					String msg = values.getString("Message");
					uploadedimgname = values.getString("Name");
					Log.v("uploadedimgname",""+uploadedimgname);
				}

				Toast.makeText(getBaseContext(), "L'image a été téléchargée",
						Toast.LENGTH_SHORT).show();

			} catch (Exception e) {

				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	public static void setLanguage(Context context, String languageToLoad) {
		Log.d("TAG", "setting language");
		Locale locale = new Locale(languageToLoad);
		if (systemLocale != null && systemLocale.equals(locale)) {
			Log.d("TAG", "Already correct language set");
			return;
		}
		Locale.setDefault(locale);
		android.content.res.Configuration config = new android.content.res.Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
		Log.d("TAG", "Language set");
	}
}
