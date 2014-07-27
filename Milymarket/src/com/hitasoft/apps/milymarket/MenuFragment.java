package com.hitasoft.apps.milymarket;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.google.android.gcm.GCMRegistrar;
import com.hitasoft.apps.adapters.ItemAdapter;
import com.hitasoft.apps.adapters.Model;
import com.hitasoft.apps.milymarket.SettingFragment.GetSettings;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MenuFragment extends SherlockListFragment implements
		OnItemClickListener, OnClickListener {

	ListView listView;
	ImageLoader laoder;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	private ImageButton home, near, cart, alert, menu;
	ImageView profileImage;
	TextView firstName, userName;
	ProgressDialog pgsDialog;
	ImageLoader settngsLodre;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.menu_list, container, false);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		laoder = ImageLoader.getInstance();
		settngsLodre = ImageLoader.getInstance();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).build();
		laoder.init(config);
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
				.build();
		Model.LoadModel();
		listView = getListView();
		String[] ids = new String[Model.Items.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Integer.toString(i + 1);
		}

		ItemAdapter adapter = new ItemAdapter(MenuFragment.this.getActivity(),
				R.layout.menu_list_item, ids);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		profileImage = (ImageView) getView().findViewById(R.id.profileImage);
		firstName = (TextView) getView().findViewById(R.id.firstName);
		userName = (TextView) getView().findViewById(R.id.userName);
		
		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		cart = (ImageButton) getView().findViewById(R.id.btn_cart);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);
		home.setImageResource(R.drawable.tab_bar_product_selected);
		
		pgsDialog = new ProgressDialog(MenuFragment.this.getActivity());

		home.setOnClickListener((OnClickListener) this);
		near.setOnClickListener((OnClickListener) this);
		cart.setOnClickListener((OnClickListener) this);
		alert.setOnClickListener((OnClickListener) this);
		menu.setOnClickListener((OnClickListener) this);
		
		profileImage.setOnClickListener((OnClickListener) this);
		firstName.setOnClickListener((OnClickListener) this);
		userName.setOnClickListener((OnClickListener) this);
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
			Toast.makeText(getActivity(), "Bienvenue sur Milymarket. Connectez vous",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
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
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = true;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new LocationFragment());
			break;
		case R.id.btn_cart:
			if(GetSet.isLogged()==true){
		    	ConstantValues.editor.clear();
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new CartFragmentClass());
			}
			else{
				Intent i=new Intent(MenuFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_alert:
			if(GetSet.isLogged()==true){
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			}
			else{
				Intent i=new Intent(MenuFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_menu:
			if(GetSet.isLogged()==true){
		    	ConstantValues.editor.clear();
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			}
			else{
				Intent i=new Intent(MenuFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.profileImage:
		case R.id.userName:
		case R.id.firstName:
			if(GetSet.isLogged()==true){
				FragmentChangeActivity.rsprofile = true;
				FragmentChangeActivity.menumap = false;
				FragmentChangeActivity.filter_icon=false;
				getActivity().supportInvalidateOptionsMenu();
				fca.switchContent(new ProfileFragment());
				}
				else{
					Intent i=new Intent(MenuFragment.this.getActivity(),LoginActivity.class);
					startActivity(i);
				}
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (arg2) {
		case 0:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			if (GetSet.isLogged()) {
				fca.switchContent(new SettingFragment());
			} else {
				((FragmentChangeActivity) MenuFragment.this.getActivity()).sm
						.toggle();
				Intent i = new Intent(MenuFragment.this.getActivity(),
						LoginActivity.class);
				startActivity(i);
			}
			break;
		case 1:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MyShopFragment());
			break;
		case 2:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MostPopular());
			break;
		case 3:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ShopFragment());
			break;
		case 4:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ManageAddress());
			break;
		case 5:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MyOrders());
			break;
		case 6:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new InviteFriends());
			break;
		case 7:
			fca.sm.toggle();
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					
					case DialogInterface.BUTTON_POSITIVE:
						GetSet.reset();
						
						//Unregister the GCM   
						
					      GCMRegistrar.unregister(MenuFragment.this.getActivity());
					      FragmentChangeActivity.acontroller.unregister(MenuFragment.this.getActivity(),
					    		  AppConstant.Register_Id);
						
						
						FragmentChangeActivity.menumap = false;
						FragmentChangeActivity.filter_icon = false;
						getActivity().supportInvalidateOptionsMenu();
						((FragmentChangeActivity) MenuFragment.this
								.getActivity())
								.switchContent(new HomeFragment());
						MenuFragment.this.getActivity()
								.getSupportFragmentManager().beginTransaction()
								.replace(R.id.menu_frame, new MenuFragment())
								.commit();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(
					MenuFragment.this.getActivity());
			builder.setMessage(getResources().getString(R.string.reallySignOut))
					.setPositiveButton(getResources().getString(R.string.yes),
							dialogClickListener)
					.setNegativeButton(getResources().getString(R.string.no),
							dialogClickListener).show();
			break;
		// case 5:
		// fca.switchContent(new FindFriends());
		// break;
		// case 6:
		// GetSet.reset();
		// fca.switchContent(new HomeFragment());
		// fca.getSupportFragmentManager().beginTransaction()
		// .replace(R.id.menu_frame, new MenuFragment()).commit();
		// break;
		// case 6:
		// fca.switchContent(new InviteFriends());
		// break;

		default:
			if (getActivity() == null)
				return;

			if (getActivity() instanceof FragmentChangeActivity) {
				fca.switchContent(new UploadFragment());
			}
			break;
		}
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
			pgsDialog.setMessage("Un instant s'il vous plait");
			pgsDialog.setIndeterminate(true);
			pgsDialog.setCancelable(false);
			//pgsDialog.show();
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

					} else {
						Toast.makeText(getActivity(),
								result.getString("message"), Toast.LENGTH_LONG)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(MenuFragment.this != null && MenuFragment.this.getView() != null)
				MenuFragment.this.getView().setVisibility(View.VISIBLE);
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

}
