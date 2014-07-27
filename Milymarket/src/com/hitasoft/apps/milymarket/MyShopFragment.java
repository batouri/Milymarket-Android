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

public class MyShopFragment extends SherlockFragment implements
		OnClickListener {

	private TextView  manageSeller, addproduct;
	HashMap<String, String> tempMap = new HashMap<String, String>();
	public static Dialog dialog;
	private Button save, cancel;
	HashMap<String, String> tempMap1 = new HashMap<String, String>();
	
	ProgressDialog pgsDialog;
	private ImageButton home, near, cart, alert, menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.manage_shop, container,
				false);
		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		home = (ImageButton) getView().findViewById(R.id.btn_home);
		near = (ImageButton) getView().findViewById(R.id.btn_near);
		cart = (ImageButton) getView().findViewById(R.id.btn_cart);
		alert = (ImageButton) getView().findViewById(R.id.btn_alert);
		menu = (ImageButton) getView().findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		pgsDialog = new ProgressDialog(MyShopFragment.this.getActivity());
		//MyShopFragment.this.getView().setVisibility(View.INVISIBLE);
		
		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.ManageShop);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);

		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

		if (!GetSet.isLogged()) {
			Toast.makeText(getActivity(), "Bienvenue sur Milymarket. Connectez vous",
					Toast.LENGTH_LONG).show();
		}
		
		
			addproduct = (TextView) getView().findViewById(R.id.selleraddproduct);
			addproduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
				FragmentChangeActivity.menumap = false;
				FragmentChangeActivity.filter_icon = false;
				getActivity().supportInvalidateOptionsMenu();
				fca.switchContent(new AddProduct());

			}
			});
		manageSeller = (TextView) getView().findViewById(R.id.sellermanage);
		manageSeller.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MyShopFragment.this.getActivity(),
						ManageSellerFragment.class);
				startActivity(i);
			}
		});

		
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
				Intent i=new Intent(MyShopFragment.this.getActivity(),LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MessagesFragment());
			break;
		case R.id.btn_menu:
			//FragmentChangeActivity.menumap = true;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			break;
		}
	}
}
