package com.hitasoft.apps.milymarket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;

public class AddProduct extends SherlockFragment implements OnClickListener {

	private ImageButton home, near, cart, alert, menu;
	public static ImageView addphoto;
	private Button send;
	private String maincatid, msubcatid, msubcatid1, mgenderid, mshippingid,
			mrelationid, mcountryid;
	private Spinner mainCategory, mSubCategory, mSubCategory1, gender,
			relation, country, shipping;
	private EditText item_name, item_desc, item_price, item_quan, addprice1,
			addprice2;
	public static ArrayList<HashMap<String, String>> CategoryItems,
			RelationItems, CountryItems, GenderItems, ShippingItems;
	private HashMap<String, ArrayList<String>> subCategory = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> subCategory1 = new HashMap<String, ArrayList<String>>();
	private ArrayAdapter<String> reladapter, countryadapter, genderadapter,
			shippingadapter;

	private List<String> relationlist = new ArrayList<String>();
	private List<String> countrylist = new ArrayList<String>();
	private List<String> genderlist = new ArrayList<String>();
	private List<String> shippinglist = new ArrayList<String>();
	private ArrayList<String> globalArrlist = new ArrayList<String>();
	private ArrayList<String> categID1 = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> dicCategID2 = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> categID2 = new ArrayList<String>();
	private ArrayList<String> categID3 = new ArrayList<String>();
	private ArrayList<String> relationID = new ArrayList<String>();
	private ArrayList<String> countryID = new ArrayList<String>();
	private ArrayList<String> genderID = new ArrayList<String>();
	private ArrayList<String> shippingID = new ArrayList<String>();
	private ArrayList<String> genderCategory = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.add_product, container, false);
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

		addphoto = (ImageView) getView().findViewById(R.id.add_uploadbtn);
		mainCategory = (Spinner) getView().findViewById(R.id.categoryspinner1);
		mSubCategory = (Spinner) getView().findViewById(R.id.categoryspinner2);
		mSubCategory1 = (Spinner) getView().findViewById(R.id.categoryspinner3);
		item_name = (EditText) getView().findViewById(R.id.add_itemname);
		item_desc = (EditText) getView().findViewById(R.id.add_itemdesc);
		item_price = (EditText) getView().findViewById(R.id.add_itemprice);
		item_quan = (EditText) getView().findViewById(R.id.add_itemquan);
		addprice1 = (EditText) getView().findViewById(R.id.add_price1);
		addprice2 = (EditText) getView().findViewById(R.id.add_price2);
		relation = (Spinner) getView().findViewById(R.id.add_Relationship);
		country = (Spinner) getView().findViewById(R.id.add_editcountry);
		gender = (Spinner) getView().findViewById(R.id.add_gender);
		shipping = (Spinner) getView().findViewById(R.id.add_shipping);
		send = (Button) getView().findViewById(R.id.sendpro);

		CategoryItems = new ArrayList<HashMap<String, String>>();
		RelationItems = new ArrayList<HashMap<String, String>>();
		CountryItems = new ArrayList<HashMap<String, String>>();
		GenderItems = new ArrayList<HashMap<String, String>>();
		ShippingItems = new ArrayList<HashMap<String, String>>();

		new GetCategories().execute();

		mainCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				String mainName = genderCategory.get(pos);
				if (!mainName.equals("Choisissez une catégorie")) {
					mSubCategory.setVisibility(View.VISIBLE);
					ArrayList<String> sub = subCategory.get(mainName);
					globalArrlist = sub;
					genericSpinnerAdapter(mSubCategory, sub);
				} else {
					mSubCategory.setVisibility(View.GONE);
					mSubCategory1.setVisibility(View.GONE);
				}
				if(pos >= 1) pos= pos-1;
				maincatid = categID1.get(pos);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		mSubCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				String mainName = globalArrlist.get(pos);
				if (!mainName.equals("Choisissez une catégorie")) {
					//mSubCategory1.setVisibility(View.VISIBLE);
					ArrayList<String> sub = subCategory1.get(mainName);
					//genericSpinnerAdapter(mSubCategory1, sub);
				} else {
					mSubCategory1.setVisibility(View.GONE);
				}
				if(pos >= 1) pos= pos-1;
				msubcatid = dicCategID2.get(maincatid).get(pos);
				//msubcatid = categID2.get(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		mSubCategory1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				msubcatid1 = categID3.get(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		gender.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mgenderid = genderID.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		shipping.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mshippingid = shippingID.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		relation.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mrelationid = relationID.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		country.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mcountryid = countryID.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		addphoto.setOnClickListener(this);
		send.setOnClickListener(this);
		int colorBlack = getResources().getColor(R.color.black);
		String text = getString(R.string.addproduct);
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);
		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);

	}

	class GetCategories extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected Void doInBackground(Void... params) {
			parseValues();
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Un instant s'il vous plaît");
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			List<String> relationspin = new ArrayList<String>();
			List<String> countryspin = new ArrayList<String>();
			List<String> shippingspin = new ArrayList<String>();
			List<String> genderspin = new ArrayList<String>();
			genericSpinnerAdapter(mainCategory, genderCategory);

			/*
			 * RELATIONSHIP
			 */
			relationspin.add("Relation");
			relationID.add(null);
			for (int i = 0; i < relationlist.size(); i++) {
				String jsn = relationlist.get(i);
				try {
					JSONObject relationJson = new JSONObject(jsn);
					String relid = relationJson.getString("id");
					relationID.add(relid);
					String relname = relationJson.getString("relationShipName");
					relationspin.add(relname);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			reladapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, relationspin);
			reladapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			relation.setAdapter(reladapter);

			/*
			 * COUNTRY
			 */
			countryspin.add("Sélectionnez un pays");

			countryID.add(null);
			for (int i = 0; i < countrylist.size(); i++) {
				String jsn = countrylist.get(i);
				try {
					JSONObject countryJson = new JSONObject(jsn);
					String countryid = countryJson.getString("id");
					countryID.add(countryid);
					String countryname = countryJson.getString("CountryName");
					countryspin.add(countryname);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			countryadapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, countryspin);
			countryadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			country.setAdapter(countryadapter);
			//France by default
			country.setSelection(73);

			/*
			 * GENDER
			 */

			genderspin.add("Sexe");
			genderID.add(null);
			for (int i = 0; i < genderlist.size(); i++) {
				String jsn = genderlist.get(i);
				try {
					JSONObject genderJson = new JSONObject(jsn);
					String genderid = genderJson.getString("id");
					genderID.add(genderid);
					String gendername = genderJson.getString("Name");
					genderspin.add(gendername);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			genderadapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, genderspin);
			genderadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			gender.setAdapter(genderadapter);

			/*
			 * SHIPPING
			 */
			shippingspin.add("Livraison");
			shippingID.add(null);
			for (int i = 0; i < shippinglist.size(); i++) {
				String jsn = shippinglist.get(i);
				try {
					JSONObject countryJson = new JSONObject(jsn);
					String shipid = countryJson.getString("id");
					shippingID.add(shipid);
					String shiptime = countryJson.getString("Time");
					shippingspin.add(shiptime);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			shippingadapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, shippingspin);
			shippingadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			shipping.setAdapter(shippingadapter);

		}
	}

	public void parseValues() {
		JSONParser parser = new JSONParser();
		JSONObject shopCategories = parser
				.getJSONFromUrl(ConstantValues.getproduct);
		String stats;
		try {
			stats = shopCategories.getString(ConstantValues.status);
			if (stats.equalsIgnoreCase("true")) {
				JSONObject res = shopCategories.getJSONObject("result");

				JSONArray relation = res.getJSONArray("relationShip");
				for (int x = 0; x < relation.length(); x++) {
					relationlist.add(relation.getString(x));
				}

				JSONArray country = res.getJSONArray("Country");
				for (int y = 0; y < country.length(); y++) {
					countrylist.add(country.getString(y));
				}

				JSONArray gender = res.getJSONArray("gender");
				for (int z = 0; z < gender.length(); z++) {
					genderlist.add(gender.getString(z));
				}

				JSONArray shipping = res.getJSONArray("shipDeliveryTime");
				for (int s = 0; s < shipping.length(); s++) {
					shippinglist.add(shipping.getString(s));
				}
				
				genderCategory.add("Choisissez une catégorie");
				JSONObject jCategory = res.getJSONObject("Category");
				JSONArray jParent = jCategory.getJSONArray("parent");

				for (int i = 0; i < jParent.length(); i++) {
					JSONObject jsonObj = jParent.getJSONObject(i);
					JSONArray subCategoryArr = jsonObj
							.getJSONArray("subcategory");
					String id1 = jsonObj.getString("id");
					categID1.add(id1);
					ArrayList<String> subCat = new ArrayList<String>();
					subCat.add("Choisissez une catégorie");
					categID2 = new ArrayList<String>();
					for (int j = 0; j < subCategoryArr.length(); j++) {
						JSONObject subJson = subCategoryArr.getJSONObject(j);
						String id2 = subJson.getString("id");
						categID2.add(id2);
						JSONArray subCategoryArr1 = subJson
								.getJSONArray("subcategory");
						ArrayList<String> subCat1 = new ArrayList<String>();
						subCat1.add("Choisissez une catégorie");
						for (int k = 0; k < subCategoryArr1.length(); k++) {
							JSONObject subJson1 = subCategoryArr1
									.getJSONObject(k);
							String id3 = subJson1.getString("id");
							categID3.add(id3);
							subCat1.add(subJson1.getString("name"));
						}
						subCat.add(subJson.getString("name"));
						subCategory1.put(subJson.getString("name"), subCat1);
						dicCategID2.put(id1, categID2);
					}
					
					String name = jsonObj.getString("name");
					genderCategory.add(name);
					subCategory.put(name, subCat);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void genericSpinnerAdapter(Spinner sp, ArrayList<String> al) {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				al);
		sp.setAdapter(dataAdapter);
	}

	@Override
	public void onClick(View v) {
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		switch (v.getId()) {
		case R.id.add_uploadbtn:
			selectImage();
			break;
		case R.id.sendpro:
			if (GetSet.isLogged()) {
				SendPorValues();
			} else {
				Toast.makeText(getActivity(), "Please signin to continue",
						Toast.LENGTH_SHORT).show();
			}
			break;
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
				Intent i=new Intent(AddProduct.this.getActivity(),LoginActivity.class);
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
			ConstantValues.editor.putString("userprefid", GetSet.getUserId());
			ConstantValues.editor.commit();
			FragmentChangeActivity.menumap = false;
			FragmentChangeActivity.filter_icon = false;
			getActivity().supportInvalidateOptionsMenu();
			fca.switchContent(new MenuFragment());
			break;

		}
	}

	private void selectImage() {

		final CharSequence[] options = { "Choisir dans l'album",
				"Annuler" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Ajouter une Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Choisir dans l'album")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					getActivity().startActivityForResult(intent, 2);

				} else if (options[item].equals("Annuler")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public void SendPorValues() {
		/*if (mrelationid.equals(null) || mcountryid.equals(null)
				|| mgenderid.equals(null) || mshippingid.equals(null)) {
			Toast.makeText(getActivity(), "Please select all details",
					Toast.LENGTH_SHORT).show();
		} else {*/
			try {
				new SendProducts().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
	}

	class SendProducts extends AsyncTask<Void, Void, String> {

		private ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected String doInBackground(Void... arg0) {
			String result = postData();
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("Un instant s'il vous plait");
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
					Toast.makeText(getActivity(), "Votre produit a été ajouté avec succès. Après validation, il sera affiché sur la place de marché.",
							Toast.LENGTH_LONG).show();
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					FragmentChangeActivity.menumap = false;
					FragmentChangeActivity.filter_icon = false;
					getActivity().supportInvalidateOptionsMenu();
					fca.switchContent(new HomeFragment());
				} else {
					Toast.makeText(getActivity(), "Nous n'avons pas pu ajouté votre produit. Veuillez vérifier tous les champs et réessayer.",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String postData() {
		String result = null;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ConstantValues.sendproduct);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					15);
			nameValuePairs.add(new BasicNameValuePair("userId", GetSet
					.getUserId()));
			nameValuePairs.add(new BasicNameValuePair("itemName", item_name
					.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("itemDescription",
					item_desc.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("itemQuantity", item_quan
					.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("itemPrice", item_price
					.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("categoryId", maincatid));
			nameValuePairs.add(new BasicNameValuePair("superCatId", msubcatid));
			nameValuePairs.add(new BasicNameValuePair("subCatId", msubcatid1));
			nameValuePairs.add(new BasicNameValuePair("imageName",
					FragmentChangeActivity.uploadedimgname));
			/*nameValuePairs.add(new BasicNameValuePair("relationShip",mrelationid));
			nameValuePairs.add(new BasicNameValuePair("gender", mgenderid));
			nameValuePairs.add(new BasicNameValuePair("countryId", mcountryid));
			nameValuePairs.add(new BasicNameValuePair("shipingCost", addprice1.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("businessday",mshippingid));
			nameValuePairs.add(new BasicNameValuePair("everyWhereCost",addprice2.getText().toString()));*/
			
			
			nameValuePairs.add(new BasicNameValuePair("relationShip","0"));
			nameValuePairs.add(new BasicNameValuePair("gender", "0"));
			nameValuePairs.add(new BasicNameValuePair("countryId", "73"));
			nameValuePairs.add(new BasicNameValuePair("shipingCost", "4"));
			nameValuePairs.add(new BasicNameValuePair("businessday","3d"));
			nameValuePairs.add(new BasicNameValuePair("everyWhereCost","10"));
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.v("result",""+EntityUtils.toString(response.getEntity()));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
