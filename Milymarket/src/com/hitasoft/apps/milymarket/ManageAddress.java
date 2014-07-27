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

public class ManageAddress extends SherlockFragment implements OnClickListener {

    private ImageButton home, near, cart, alert, menu;
    public static ImageView addphoto;
    private Button send;
    private String mcountryid, mshippingId;
    private Spinner countryspin;
    private EditText fullname, address1, address2, nickname, town, countryname, postalcode, state, phone;
    public static ArrayList<HashMap<String, String>> CountryItems;
    private ArrayAdapter<String> countryadapter;
    private HashMap<String, String> userDatas;
    private List<String> countrylist = new ArrayList<String>();
    private ArrayList<String> globalArrlist = new ArrayList<String>();
    private ArrayList<String> countryID = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.manage_address, container, false);
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
        
        fullname = (EditText) getView().findViewById(R.id.addressfullname);
        nickname = (EditText) getView().findViewById(R.id.nickname);
        address1 = (EditText) getView().findViewById(R.id.address1);
        address2 = (EditText) getView().findViewById(R.id.address2);
        town = (EditText) getView().findViewById(R.id.town);
        postalcode = (EditText) getView().findViewById(R.id.postalcode);
        state = (EditText) getView().findViewById(R.id.state);
        phone = (EditText) getView().findViewById(R.id.phone);
        //countryname = (EditText) getView().findViewById(R.id.countryname);
        countryspin = (Spinner) getView().findViewById(R.id.spin_country);
        send = (Button) getView().findViewById(R.id.sendaddress);
        userDatas = new HashMap<String, String>();
        
        
        CountryItems = new ArrayList<HashMap<String, String>>();
        
        new GetCountries().execute();
        
        countryspin.setOnItemSelectedListener(new OnItemSelectedListener() {
        
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        send.setOnClickListener(this);
        int colorBlack = getResources().getColor(R.color.black);
        String text = getString(R.string.ManageAddress);
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
        text.length(), 0);
        ((FragmentChangeActivity) getActivity()).getSupportActionBar().setTitle(spannable);
    
    }
    
    class GetCountries extends AsyncTask<Void, Void, Void> {
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
            List<String> countryArr = new ArrayList<String>();
            
            if(userDatas.get(ConstantValues.TAG_ADDRESS_SHIPPINGID) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_SHIPPINGID).isEmpty())
                mshippingId = userDatas.get(ConstantValues.TAG_ADDRESS_SHIPPINGID);
            if(userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME) != "null" && !userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME).isEmpty())
                fullname.setText(userDatas.get(ConstantValues.TAG_PROFILE_FULLNAME));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_NICKNAME) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_NICKNAME).isEmpty())
                nickname.setText(userDatas.get(ConstantValues.TAG_ADDRESS_NICKNAME));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_ADDRESS1) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_ADDRESS1).isEmpty())
                address1.setText(userDatas.get(ConstantValues.TAG_ADDRESS_ADDRESS1));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_ADDRESS2) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_ADDRESS2).isEmpty())
                address2.setText(userDatas.get(ConstantValues.TAG_ADDRESS_ADDRESS2));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_TOWN) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_TOWN).isEmpty())
                town.setText(userDatas.get(ConstantValues.TAG_ADDRESS_TOWN));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_STATE) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_STATE).isEmpty())
                state.setText(userDatas.get(ConstantValues.TAG_ADDRESS_STATE));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_POSTALCODE) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_POSTALCODE).isEmpty())
                postalcode.setText(userDatas.get(ConstantValues.TAG_ADDRESS_POSTALCODE));
            if(userDatas.get(ConstantValues.TAG_ADDRESS_PHONE) != "null" && !userDatas.get(ConstantValues.TAG_ADDRESS_PHONE).isEmpty())
                phone.setText(userDatas.get(ConstantValues.TAG_ADDRESS_PHONE));        
            if(mcountryid == "null" || mcountryid.isEmpty())
            	mcountryid = "73";
            /*
            * COUNTRY
            */
            countryArr.add("Sélectionnez un pays");
            
            countryID.add(null);
            for (int i = 0; i < countrylist.size(); i++) {
                String jsn = countrylist.get(i);
                try {
                    JSONObject countryJson = new JSONObject(jsn);
                    JSONObject countryelt = countryJson.getJSONObject("Country");
                    String countryid = countryelt.getString("id");
                    
                    countryID.add(countryid);
                    String countryname = countryelt.getString("country");
                    countryArr.add(countryname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            countryadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countryArr);
            countryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countryspin.setAdapter(countryadapter);
            countryspin.setSelection(Integer.parseInt(mcountryid));
        }
    }
    
    public void parseValues() {
        JSONParser parser = new JSONParser();
        String userId = GetSet.getUserId();
        String urlAddr = ConstantValues.getShippingAddress + userId;
        JSONObject useraddress = parser.getJSONFromUrl(urlAddr);
        String stats;
        try {
            stats = useraddress.getString(ConstantValues.status);
            if (stats.equalsIgnoreCase("true")) {
                JSONObject results = useraddress.getJSONObject("result");
                JSONArray country = results.getJSONArray("countrylist");
                userId = results.getString(ConstantValues.TAG_PROFILE_USERID);
                String shippingId = results.getString(ConstantValues.TAG_ADDRESS_SHIPPINGID);
                String mfullName = results.getString(ConstantValues.TAG_PROFILE_FULLNAME);
                String mnickname = results.getString(ConstantValues.TAG_ADDRESS_NICKNAME);
                String maddress1 = results.getString(ConstantValues.TAG_ADDRESS_ADDRESS1);
                String maddress2 = results.getString(ConstantValues.TAG_ADDRESS_ADDRESS2);
                String mtown = results.getString(ConstantValues.TAG_ADDRESS_TOWN);
                String mstate = results.getString(ConstantValues.TAG_ADDRESS_STATE);
                String mpostalcode = results.getString(ConstantValues.TAG_ADDRESS_POSTALCODE);
                String mphone = results.getString(ConstantValues.TAG_ADDRESS_PHONE);
                mcountryid = results.getString(ConstantValues.TAG_ADDRESS_COUNTRYID);
                userDatas.put(ConstantValues.TAG_PROFILE_USERID, userId);
                userDatas.put(ConstantValues.TAG_ADDRESS_SHIPPINGID, shippingId);
                userDatas.put(ConstantValues.TAG_PROFILE_FULLNAME, mfullName);
                userDatas.put(ConstantValues.TAG_ADDRESS_NICKNAME, mnickname);
                userDatas.put(ConstantValues.TAG_ADDRESS_ADDRESS1, maddress1);
                userDatas.put(ConstantValues.TAG_ADDRESS_ADDRESS2, maddress2);
                userDatas.put(ConstantValues.TAG_ADDRESS_TOWN, mtown);
                userDatas.put(ConstantValues.TAG_ADDRESS_STATE, mstate);
                userDatas.put(ConstantValues.TAG_ADDRESS_POSTALCODE, mpostalcode);
                userDatas.put(ConstantValues.TAG_ADDRESS_PHONE, mphone);
                userDatas.put(ConstantValues.TAG_ADDRESS_COUNTRYID, mcountryid);
                for (int y = 0; y < country.length(); y++) {
                    countrylist.add(country.getString(y));
                }
            }
        } catch (JSONException e) {
        e.printStackTrace();
        }
    }
        
    @Override
    public void onClick(View v) {
        FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
        switch (v.getId()) {
            case R.id.sendpro:
                if (GetSet.isLogged()) {
                    SendAddressValues();
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
                    Intent i=new Intent(ManageAddress.this.getActivity(),LoginActivity.class);
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
    
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void SendAddressValues() {
        if (isEmpty(fullname) || isEmpty(nickname)    || isEmpty(address1) || isEmpty(address2) || isEmpty(town) || isEmpty(postalcode)) {
            Toast.makeText(getActivity(), "Remplissez tous les champs svp",
            Toast.LENGTH_SHORT).show();
        } else {
            try {
                new SendAddress().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    class SendAddress extends AsyncTask<Void, Void, String> {    
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
                if (jonj.getString(ConstantValues.status).equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), "Félicitations, votre adresse a bien été mise à jour.",
                    Toast.LENGTH_LONG).show();
                    FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
                    FragmentChangeActivity.menumap = false;
                    FragmentChangeActivity.filter_icon = false;
                    getActivity().supportInvalidateOptionsMenu();
                    fca.switchContent(new HomeFragment());
                } else {
                    Toast.makeText(getActivity(), "Nous n'avons pas pu mettre à jour votre adresse. Veuillez réessayer plus tard",
                    Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String postData() {
        String result = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(ConstantValues.updateShippingAddress);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(12);
            nameValuePairs.add(new BasicNameValuePair("userid", GetSet.getUserId()));
            nameValuePairs.add(new BasicNameValuePair("shippingid", mshippingId));
            nameValuePairs.add(new BasicNameValuePair("fullname", fullname.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("nickname", nickname.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("address1", address1.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("address2", address2.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("town", town.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("postalcode", postalcode.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("state", state.getText().toString()));    
            nameValuePairs.add(new BasicNameValuePair("phone",phone.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("countryid", ""+countryspin.getSelectedItemPosition()));
            nameValuePairs.add(new BasicNameValuePair("countryname", countryspin.getSelectedItem().toString()));
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
