package com.hitasoft.apps.milymarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.google.android.gcm.GCMRegistrar;
import com.hitasoft.apps.adapters.ItemAdapter;
import com.hitasoft.apps.adapters.Model;
import com.hitasoft.apps.milymarket.util.AppConstant;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MenuFragment extends SherlockListFragment implements
		OnItemClickListener, OnClickListener {

	ListView listView;
	ImageLoader laoder;
	DisplayImageOptions defaultOptions;
	ImageLoaderConfiguration config;
	private ImageButton home, near, shop, alert, menu;

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
		
		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		shop = (ImageButton) getView().findViewById(R.id.btn_shop);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);
		home.setImageResource(R.drawable.tab_bar_product_selected);

		home.setOnClickListener((OnClickListener) this);
		near.setOnClickListener((OnClickListener) this);
		shop.setOnClickListener((OnClickListener) this);
		alert.setOnClickListener((OnClickListener) this);
		menu.setOnClickListener((OnClickListener) this);

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
		case R.id.btn_shop:
			FragmentChangeActivity.menumap = false;
			// getActivity().supportInvalidateOptionsMenu();
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new ShopFragment());
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
			FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
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
			fca.switchContent(new MostPopular());
			break;
		case 2:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new InviteFriends());
			
			break;
		case 3:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new AddProduct());
			break;

		case 4:
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

}
