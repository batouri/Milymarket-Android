package com.hitasoft.apps.milymarket;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.hitasoft.apps.milymarket.util.GetSet;
import com.hitasoft.apps.milymarket.util.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;

public class CartDetailClass extends Activity implements OnClickListener {

	private static String mrechantId = null, itemidss = null;
	private ImageLoader cartImageLoader;
	private static TextView merchant, itemprice, shipprice, total, Nomore;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> receiptent = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> admininvoiceData = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> sellerinvoiceData = new ArrayList<HashMap<String, String>>();
	static ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
	HashMap<String, Integer> countPosition = new HashMap<String, Integer>();
	private static ListView checkoutlist;
	protected static LazyAdapter adapter;
	static ImageButton paypalbtn;
	ImageButton smenu;
	String datasss = null, name;
	LinearLayout cartBottom, carttop;
	private static boolean shipable = true;
	private ImageButton home, near, cart, alert, menu;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shipable = true;
		cartImageLoader = ImageLoader.getInstance();
		setContentView(R.layout.checkout_list_item);
		// initLibrary();

		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		cart = (ImageButton) findViewById(R.id.btn_cart);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		cart.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);

		Nomore = (TextView) findViewById(R.id.noitem);
		Nomore.setVisibility(View.INVISIBLE);
		smenu = (ImageButton) findViewById(R.id.smenu);
		smenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//Intent data = new Intent(CartDetailClass.this,CartFragmentClass.class);
				//data.putExtra("returnKey1", "Swinging on a star. ");
				//setResult(RESULT_OK, data);
				CartDetailClass.this.finish();
			}
		});
		cartBottom = (LinearLayout) findViewById(R.id.bottomcart);
		carttop = (LinearLayout) findViewById(R.id.topcart);
		cartBottom.setVisibility(View.VISIBLE);
		carttop.setVisibility(View.INVISIBLE);
		paypalbtn = (ImageButton) findViewById(R.id.paywith);
		paypalbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						HashMap<String, String> tmp = list.get(i);
						if (tmp.get("itemApprove").equalsIgnoreCase("0")) {
							shipable = false;
							break;
						}
					}
				}
				if (shipable) {
					doPayment();
				} else {
					Toast.makeText(getBaseContext(),
							"One of the items cannot be ship.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		checkoutlist = (ListView) findViewById(R.id.checkout_list_item);
		adapter = new LazyAdapter(CartDetailClass.this, list);
		checkoutlist.setAdapter(adapter);
		merchant = (TextView) findViewById(R.id.merchantname);
		name = this.getIntent().getExtras().getString("merchant");
		merchant.setText(name);
		itemprice = (TextView) findViewById(R.id.itemtotal);
		shipprice = (TextView) findViewById(R.id.Shippingtotal);
		total = (TextView) findViewById(R.id.total);
		mrechantId = this.getIntent().getExtras().getString("id");
		itemidss = this.getIntent().getExtras().getString("cartdetaildata");
		datas = (ArrayList<HashMap<String, String>>) this.getIntent()
				.getExtras().get("array");
		new SetupCart().execute(mrechantId);
	}

	protected void doPayment() {
		new getValues().execute(ConstantValues.payment + GetSet.getUserId()
				+ "&merchantId=" + mrechantId);
	}

	class SetupCart extends AsyncTask<String, Void, JSONObject> {

		String url = ConstantValues.getcart;
		String arra;
		// ProgressDialog dialog = new ProgressDialog(CartDetailClass.this);
		JSONParser parser = new JSONParser();

		@Override
		protected JSONObject doInBackground(String... params) {
			url = url + GetSet.getUserId() + "&itemId=" + itemidss
					+ "&merchantId=" + mrechantId;
			return parser.getJSONFromUrl(url);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog.setMessage("Please wait...");
			// dialog.setIndeterminate(true);
			// dialog.setCancelable(false);
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
			cartBottom.setVisibility(View.VISIBLE);
			carttop.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// if (dialog.isShowing()) {
			// dialog.dismiss();
			// }
			cartBottom.setVisibility(View.INVISIBLE);
			carttop.setVisibility(View.VISIBLE);
			String status;
			try {
				status = result.getString("status");
				if (status.equalsIgnoreCase("true")) {
					JSONObject res = result.getJSONObject("result");
					JSONObject item = res.getJSONObject("items");
					String merchantname = item.getString("MerchantName");
					String totalCost = item.getString("totalCost");
					String grandTotal = item.getString("grandTotal");
					String shipping = item.getString("shipping");
					CartDetailClass.merchant.setText(merchantname);
					CartDetailClass.itemprice.setText("$ " + totalCost);
					CartDetailClass.total.setText("$ " + grandTotal);
					CartDetailClass.shipprice.setText("$ " + shipping);
					JSONArray products = item.getJSONArray("products");

					for (int i = 0; i < products.length(); i++) {

						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject ong = products.getJSONObject(i);
						String itemId = ong.getString("itemId");
						String itemName = ong.getString("itemName");
						String itemUrl = ong.getString("itemUrl");
						String itemPrice = ong.getString("itemPrice");
						String itemCount = ong.getString("itemCount");
						String itemTotalCount = ong.getString("itemTotalCount");
						String itemApprove = ong.getString("itemApprove");
						map.put("itemId", itemId);
						map.put("itemName", itemName);
						map.put("itemUrl", itemUrl);
						map.put("itemPrice", itemPrice);
						map.put("itemCount", itemCount);
						map.put("itemTotalCount", itemTotalCount);
						map.put("itemApprove", itemApprove);
						list.add(map);
					}
				} else {
					Toast.makeText(getBaseContext(),
							"Sorry something went wrong", Toast.LENGTH_LONG)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
		}

	}

	public class LazyAdapter extends BaseAdapter {

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater = null;
		HashMap<String, String> tempMap = null;

		public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
			activity = a;
			data = d;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.checkout_list_item_item, null);

			tempMap = data.get(position);
			TextView title = (TextView) vi.findViewById(R.id.title);
			TextView cst = (TextView) vi.findViewById(R.id.price);
			TextView id = (TextView) vi.findViewById(R.id.id);
			final Button count = (Button) vi.findViewById(R.id.count);

			count.setText(tempMap.get("itemCount"));
			count.setTag(tempMap);
			count.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showDialogForCount(count.getTag());
				}
			});
			final TextView remove = (TextView) vi.findViewById(R.id.remove);
			remove.setTag(tempMap);
			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					removeItem(remove.getTag());
				}
			});
			final ImageView image = (ImageView) vi.findViewById(R.id.image);

			title.setText(tempMap.get("itemName"));
			title.setSelected(true);
			cst.setText("$ " + tempMap.get("itemPrice"));
			if (tempMap.get("itemApprove").equalsIgnoreCase("0")) {
				id.setVisibility(View.VISIBLE);
			} else {
				id.setVisibility(View.GONE);
			}
			cartImageLoader.loadImage(tempMap.get("itemUrl"),
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							image.setImageBitmap(loadedImage);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
			return vi;
		}
	}

	@SuppressWarnings("unchecked")
	public void removeItem(Object tag) {
		HashMap<String, String> getMap = (HashMap<String, String>) tag;
		String id = getMap.get("itemId");
		String[] nos = itemidss.split(",");
		ArrayList<String> List_Of_Array = new ArrayList<String>();
		for (int k = 0; k < nos.length; k++) {
			List_Of_Array.add(nos[k]);
		}
		if (List_Of_Array.size() > 0) {
			for (int i = List_Of_Array.size() - 1; i >= 0; i--) {
				if (nos[i].equalsIgnoreCase(id)) {
					List_Of_Array.remove(i);
				}
			}
		}

		String itemNumbers = "";
		for (int i = 0; i < List_Of_Array.size(); i++) {
			if (i == 0) {
				itemNumbers = List_Of_Array.get(i);
			} else {
				itemNumbers = itemNumbers + "," + List_Of_Array.get(i);
			}
		}
		if (list.size() == 0) {
			Nomore.setVisibility(View.VISIBLE);
			paypalbtn.setEnabled(false);
		} else {
			try {
				list = new ArrayList<HashMap<String, String>>();
				new RemoVeItemFromCart().execute(id);
			} catch (Exception e) {

			}
			Nomore.setVisibility(View.INVISIBLE);
			paypalbtn.setEnabled(true);
		}
	}

	@SuppressWarnings("unchecked")
	public void showDialogForCount(Object object) {

		final HashMap<String, String> tempMap = (HashMap<String, String>) object;
		String countno = tempMap.get("itemTotalCount");
		String[] counts = new String[Integer.parseInt(countno)];
		for (int i = 1; i <= Integer.parseInt(countno); i++) {
			counts[i - 1] = Integer.toString(i);
		}
		final Dialog countDialog = new Dialog(CartDetailClass.this);
		countDialog.setContentView(R.layout.cart_count_list);
		ListView lv = (ListView) countDialog.findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.count_list_item, counts);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (countDialog.isShowing()) {
					countDialog.dismiss();
				}
				tempMap.put("countSelected", Integer.toString(arg2 + 1));
				if (!tempMap.get("itemCount").equalsIgnoreCase(
						tempMap.get("countSelected"))) {
					tempMap.put("itemCount", Integer.toString(arg2 + 1));
					new ChangeQuantity().execute(tempMap);
				} else {

					Toast.makeText(getBaseContext(),
							"The Count doesn't changed.", Toast.LENGTH_LONG)
							.show();
				}
			}

		});
		countDialog.setTitle("Select Count");
		countDialog.show();
	}

	class getValues extends AsyncTask<String, Void, JSONObject> {

		JSONParser parser = new JSONParser();

		ProgressDialog dialog = new ProgressDialog(CartDetailClass.this);

		@Override
		protected JSONObject doInBackground(String... arg0) {
			String url = arg0[0];
			return parser.getJSONFromUrl(url);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Please Wait");
			dialog.setMessage("Initializing payment");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			try {
				if (result.getString("status").equals("true")) {

					JSONObject value = result.getJSONObject("result");
					String ipn = value.getString("ipnUrl");
					String memo = value.getString("memo");
					String shipping = value.getString("shipping");
					String currencyCode = value.getString("currencyCode");
					JSONArray receipntnt = value.getJSONArray("receiptent");
					for (int i = 0; i < receipntnt.length(); i++) {
						HashMap<String, String> tmpMap = new HashMap<String, String>();
						JSONObject obj = receipntnt.getJSONObject(i);
						if (i == 0) {
							String adminEmail = obj.getString("adminEmail");
							String adminAmount = obj.getString("adminAmount");
							tmpMap.put("adminEmail", adminEmail);
							tmpMap.put("adminAmount", adminAmount);
							JSONArray admininvoicedata = obj
									.getJSONArray("adminInvoice");
							for (int k = 0; k < admininvoicedata.length(); k++) {
								JSONObject admobj = admininvoicedata
										.getJSONObject(k);
								String name = admobj.getString("name");
								String price = admobj.getString("price");
								String itemPrice = admobj
										.getString("itemPrice");
								String itemCount = admobj
										.getString("itemCount");
								String identifier = admobj
										.getString("identifier");
								HashMap<String, String> tmpMap2 = new HashMap<String, String>();
								tmpMap2.put("name", name);
								tmpMap2.put("price", price);
								tmpMap2.put("itemPrice", itemPrice);
								tmpMap2.put("itemCount", itemCount);
								tmpMap2.put("identifier", identifier);
								admininvoiceData.add(tmpMap2);
							}
						} else if (i == 1) {
							String sellerEmail = obj.getString("sellerEmail");
							String sellerAmount = obj.getString("sellerAmount");
							tmpMap.put("sellerEmail", sellerEmail);
							tmpMap.put("sellerAmount", sellerAmount);
							JSONArray sellerinvoicedata = obj
									.getJSONArray("sellerInvoice");
							for (int k = 0; k < sellerinvoicedata.length(); k++) {
								JSONObject admobj = sellerinvoicedata
										.getJSONObject(k);
								String name = admobj.getString("name");
								String price = admobj.getString("price");
								String itemPrice = admobj
										.getString("itemPrice");
								String itemCount = admobj
										.getString("itemCount");
								String identifier = admobj
										.getString("identifier");
								HashMap<String, String> tmpMap2 = new HashMap<String, String>();
								tmpMap2.put("name", name);
								tmpMap2.put("price", price);
								tmpMap2.put("itemPrice", itemPrice);
								tmpMap2.put("itemCount", itemCount);
								tmpMap2.put("identifier", identifier);
								sellerinvoiceData.add(tmpMap2);
							}
						}
						receiptent.add(tmpMap);
					}

					if (dialog.isShowing()) {
						dialog.dismiss();
					}

					Log.v("Data_before_pay", "ipn=" + ipn + "memo=" + memo
							+ "shipping=" + shipping + "currencycode="
							+ currencyCode + "receiptent=" + receiptent
							+ "adminivoice=" + admininvoiceData.size()
							+ "sellerinvoice=" + sellerinvoiceData.size());

					Intent checkoutIntent = PayPal
							.getInstance()
							.checkout(
									GetPaymentBundle(ipn, memo, shipping,
											currencyCode, receiptent,
											admininvoiceData, sellerinvoiceData),
									CartDetailClass.this /*
														 * , new
														 * ResultDelegate()
														 */);
					// This will start the library.
					startActivityForResult(checkoutIntent, 2);

				} else {
					ProgressDialog dialog_false = new ProgressDialog(
							CartDetailClass.this);
					dialog_false.setTitle("!Warning");
					dialog_false
							.setMessage("Your Shipping address is Missing.Please provide the shipping address");
					dialog_false.setCancelable(true);
					dialog_false.setCanceledOnTouchOutside(true);
					dialog_false.show();

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public void sendShipping(String ipn, String memo, String shipping,
			String currencyCode,
			ArrayList<HashMap<String, String>> receiptent2,
			ArrayList<HashMap<String, String>> admininvoice2,
			ArrayList<HashMap<String, String>> sellerinvoiceData2) {
		PayPalAdvancedPayment payment = new PayPalAdvancedPayment();
		PayPalReceiverDetails admin = new PayPalReceiverDetails();
		PayPalReceiverDetails seller = new PayPalReceiverDetails();
		payment.setIpnUrl("http://dev.hitasoft.com/new/success.php");
		payment.setMemo(memo);
		payment.setCurrencyType(currencyCode);

		for (int j = 0; j < receiptent2.size(); j++) {
			PayPalInvoiceData data = new PayPalInvoiceData();
			HashMap<String, String> map23 = receiptent2.get(j);
			if (j == 0) {
				admin.setSubtotal(new BigDecimal(map23.get("adminAmount")));
				admin.setRecipient(map23.get("adminEmail"));
				for (int i = 0; i < admininvoice2.size(); i++) {
					PayPalInvoiceItem item = new PayPalInvoiceItem();
					HashMap<String, String> map = admininvoice2.get(i);
					item.setID(map.get("identifier"));
					item.setName(map.get("name"));
					item.setQuantity(Integer.parseInt(map.get("itemCount")));
					item.setTotalPrice(new BigDecimal(map.get("price")));
					item.setUnitPrice(new BigDecimal(map.get("itemPrice")));
					data.add(item);
				}
				admin.setInvoiceData(data);

			} else if (j == 1) {
				data.setShipping(new BigDecimal(shipping));
				seller.setSubtotal(new BigDecimal("90"));
				seller.setRecipient(map23.get("sellerEmail"));
				for (int i = 0; i < sellerinvoiceData2.size(); i++) {
					PayPalInvoiceItem item = new PayPalInvoiceItem();
					HashMap<String, String> map = sellerinvoiceData2.get(i);
					item.setID(map.get("identifier"));
					item.setName(map.get("name"));
					item.setQuantity(Integer.parseInt(map.get("itemCount")));
					item.setTotalPrice(new BigDecimal(map.get("price")));
					item.setUnitPrice(new BigDecimal(map.get("itemPrice")));
					data.add(item);
				}
				seller.setInvoiceData(data);
				seller.setPaymentType(PayPal.PAY_TYPE_PARALLEL);

			}
		}
		ArrayList<PayPalReceiverDetails> receivers = new ArrayList<PayPalReceiverDetails>();
		receivers.add(admin);
		receivers.add(seller);
		payment.setReceivers(receivers);
		Intent paypalIntent = PayPal.getInstance().checkout(payment, this);
		this.startActivityForResult(paypalIntent, 1);
	}

	public void sendShipping2(String ipn, String memo, String shipping,
			String currencyCode,
			ArrayList<HashMap<String, String>> receiptent2,
			ArrayList<HashMap<String, String>> admininvoiceData2,
			ArrayList<HashMap<String, String>> sellerinvoiceData2) {
		PayPalPayment payment = new PayPalPayment();
		payment.setCurrencyType("USD");
		HashMap<String, String> map23 = receiptent2.get(0);
		// payment.setPaymentType(PayPal.PAY_TYPE_PARALLEL);
		payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
		payment.setRecipient(map23.get("adminEmail"));
		// payment.setIpnUrl(ipn);
		BigDecimal st = new BigDecimal(map23.get("adminAmount"));
		// st = st.setScale(2, RoundingMode.HALF_UP);
		HashMap<String, String> map22 = receiptent2.get(1);
		BigDecimal st2 = new BigDecimal(map22.get("sellerAmount"));
		// st2 = st2.setScale(2, RoundingMode.HALF_UP);
		st2 = st2.add(st);
		payment.setSubtotal(st2);
		PayPalInvoiceData invoice = new PayPalInvoiceData();
		BigDecimal st3 = new BigDecimal(shipping);
		st3 = st3.setScale(2, RoundingMode.HALF_UP);
		invoice.setShipping(st3);
		for (int i = 0; i < admininvoiceData2.size(); i++) {
			PayPalInvoiceItem item = new PayPalInvoiceItem();
			HashMap<String, String> map = admininvoiceData2.get(i);
			HashMap<String, String> map2 = sellerinvoiceData2.get(i);
			item.setID(map.get("identifier"));
			item.setName(map.get("name"));
			item.setQuantity(Integer.parseInt(map.get("itemCount")));
			item.setTotalPrice(new BigDecimal(map.get("price"))
					.add(new BigDecimal(map2.get("price"))));
			item.setUnitPrice(new BigDecimal(map.get("itemPrice")));
			invoice.getInvoiceItems().add(item);
		}
		payment.setInvoiceData(invoice);
		payment.setMerchantName(memo);
		payment.setCustomID(memo);
		// this.memo = memo;
		Intent checkoutIntent = PayPal.getInstance()
				.checkout(payment, this /* , new ResultDelegate() */);
		// This will start the library.
		startActivityForResult(checkoutIntent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case Activity.RESULT_OK:
			Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG)
					.show();
			Intent datas = new Intent();
			datas.putExtra("returnKey1", "Swinging on a star. ");
			setResult(RESULT_OK, datas);
			CartDetailClass.this.finish();
			break;
		case Activity.RESULT_CANCELED:
			Toast.makeText(this, "Paymnet Cancel", Toast.LENGTH_LONG).show();
			Intent datasw = new Intent();
			datasw.putExtra("returnKey1", "Swinging on a star. ");
			setResult(RESULT_OK, datasw);
			CartDetailClass.this.finish();
			break;
		case PayPalActivity.RESULT_FAILURE:

			String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
			String errorMessage = data
					.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
			Toast.makeText(this,
					"Paymnet Failed" + errorID + ":" + errorMessage,
					Toast.LENGTH_LONG).show();
			Intent datasww = new Intent();
			datasww.putExtra("returnKey1", "Swinging on a star. ");
			setResult(RESULT_OK, datasww);
			CartDetailClass.this.finish();
			break;
		}

	}

	class SendValues extends AsyncTask<HashMap<String, String>, Void, String> {

		JSONParser parser = new JSONParser();

		@Override
		protected String doInBackground(HashMap<String, String>... arg0) {
			HashMap<String, String> map = arg0[0];
			String s = postData(map.get("key"), map.get("memo"));
			return s;
		}
	}

	public String postData(String s1, String s2) {
		String result = null;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		String url = ConstantValues.url + "api/mobileipnprocess";
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("payKey", s1));
			nameValuePairs.add(new BasicNameValuePair("custom", s2));
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

	// private PayPalAdvancedPayment exampleParallelPayment() {
	// // Create the PayPalAdvancedPayment.
	// PayPalAdvancedPayment payment = new PayPalAdvancedPayment();
	// // Sets the currency type for this payment.
	// payment.setCurrencyType("USD");
	// // Sets the Instant Payment Notification url. This url will be hit by
	// // the PayPal server upon completion of the payment.
	// //
	// payment.setIpnUrl("http://fancyclone.net/dev/paypal/adaptiveipnprocess/");
	// // Sets the memo. This memo will be part of the notification sent by
	// // PayPal to the necessary parties.
	// payment.setMemo("This sure is a swell memo for a parallel payment.");
	//
	// // Create the PayPalReceiverDetails. You must have at least one of these
	// // to make an advanced payment and you should have
	// // more than one for a Parallel or Chained payment.
	// PayPalReceiverDetails receiver1 = new PayPalReceiverDetails();
	// // Sets the recipient for the PayPalReceiverDetails. This can also be a
	// // phone number.
	// receiver1.setRecipient("rajahussain64@yahoo.com");
	// // Sets the subtotal of the payment for this receiver, not including tax
	// // and shipping amounts.
	// receiver1.setSubtotal(new BigDecimal("13.50"));
	// // Sets the primary flag for this receiver. This is defaulted to false.
	// // No receiver can be a primary for a parallel payment.
	// receiver1.setIsPrimary(false);
	// // Sets the payment type. This can be PAYMENT_TYPE_GOODS,
	// // PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE.
	// receiver1.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
	//
	// // PayPalInvoiceData can contain tax and shipping amounts. It also
	// // contains an ArrayList of PayPalInvoiceItem which can
	// // be filled out. These are not required for any transaction.
	// PayPalInvoiceData invoice1 = new PayPalInvoiceData();
	// // Sets the tax amount.
	// invoice1.setTax(new BigDecimal("2.20"));
	// // Sets the shipping amount.
	// invoice1.setShipping(BigDecimal.ZERO);
	//
	// // PayPalInvoiceItem has several parameters available to it. None of
	// // these parameters is required.
	// PayPalInvoiceItem item1 = new PayPalInvoiceItem();
	// // Sets the name of the item.
	// item1.setName("Laser Show");// (1) create account in sandbox
	// // environment:
	// // Sets the ID. This is any ID that you would like to have associated
	// // with the item.
	// item1.setID("4211");
	// // Sets the total price which should be (quantity * unit price). The
	// // total prices of all PayPalInvoiceItem should add up
	// // to less than or equal the subtotal of the payment.
	// item1.setTotalPrice(new BigDecimal("7.30"));
	// // Sets the unit price.
	// item1.setUnitPrice(new BigDecimal("7.30"));
	// // Sets the quantity.
	// item1.setQuantity(1);
	// // Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively,
	// // you can create an ArrayList
	// // and pass it to the PayPalInvoiceData function setInvoiceItems().
	// invoice1.getInvoiceItems().add(item1);
	//
	// // Create and add another PayPalInvoiceItem to add to the
	// // PayPalInvoiceData.
	// PayPalInvoiceItem item2 = new PayPalInvoiceItem();
	// item2.setName("Fog Machine");
	// item2.setID("6325");
	// item2.setTotalPrice(new BigDecimal("4.80"));
	// item2.setUnitPrice(new BigDecimal("1.20"));
	// item2.setQuantity(4);
	// invoice1.getInvoiceItems().add(item2);
	//
	// // Create and add another PayPalInvoiceItem to add to the
	// // PayPalInvoiceData.
	// PayPalInvoiceItem item3 = new PayPalInvoiceItem();
	// item3.setName("Fog Liquid");
	// item3.setID("2196");
	// item3.setTotalPrice(new BigDecimal("1.40"));
	// item3.setUnitPrice(new BigDecimal("0.20"));
	// item3.setQuantity(7);
	// invoice1.getInvoiceItems().add(item3);
	//
	// // Sets the PayPalReceiverDetails invoice data.
	// receiver1.setInvoiceData(invoice1);
	// // Sets the merchant name. This is the name of your Application or
	// // Company.
	// receiver1.setMerchantName("Laser Shop");
	// // Sets the description of the payment.
	// receiver1.setDescription("The first of two party guys");
	// // Sets the Custom ID. This is any ID that you would like to have
	// // associated with the PayPalReceiverDetails.
	// receiver1.setCustomID("001813");
	// // Add the receiver to the payment. Alternatively, you can create an
	// // ArrayList
	// // and pass it to the PayPalAdvancedPayment function setReceivers().
	// payment.getReceivers().add(receiver1);
	//
	// // Create another receiver for the parallel payment
	// PayPalReceiverDetails receiver2 = new PayPalReceiverDetails();
	// receiver2.setRecipient("example-merchant-3@paypal.com");
	// receiver2.setSubtotal(new BigDecimal("16.00"));
	// receiver2.setIsPrimary(false);
	// receiver2.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
	//
	// // PayPalInvoiceData can contain tax and shipping amounts. It also
	// // contains an ArrayList of PayPalInvoiceItem which can
	// // be filled out. These are not required for any transaction.
	// PayPalInvoiceData invoice2 = new PayPalInvoiceData();
	// // Sets the tax amount.
	// invoice2.setTax(new BigDecimal("3.40"));
	// // Sets the shipping amount.
	// invoice2.setShipping(new BigDecimal("5.15"));
	//
	// // PayPalInvoiceItem has several parameters available to it. None of
	// // these parameters is required.
	// PayPalInvoiceItem item4 = new PayPalInvoiceItem();
	// // Sets the name of the item.
	// item4.setName("Beverages");
	// // Sets the ID. This is any ID that you would like to have associated
	// // with the item.
	// item4.setID("7254");
	// // Sets the total price which should be (quantity * unit price). The
	// // total prices of all PayPalInvoiceItem should add up
	// // to less than or equal the subtotal of the payment.
	// item4.setTotalPrice(new BigDecimal("11.00"));
	// // Sets the unit price.
	// item4.setUnitPrice(new BigDecimal("1.00"));
	// // Sets the quantity.
	// item4.setQuantity(11);
	// // Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively,
	// // you can create an ArrayList
	// // and pass it to the PayPalInvoiceData function setInvoiceItems().
	// invoice2.getInvoiceItems().add(item4);
	//
	// // Create and add another PayPalInvoiceItem to add to the
	// // PayPalInvoiceData.
	// PayPalInvoiceItem item5 = new PayPalInvoiceItem();
	// item5.setName("Refreshments");
	// item5.setID("1288");
	// item5.setTotalPrice(new BigDecimal("5.00"));
	// item5.setUnitPrice(new BigDecimal("1.25"));
	// item5.setQuantity(4);
	// invoice2.getInvoiceItems().add(item5);
	//
	// // Sets the PayPalReceiverDetails invoice data.
	// receiver2.setInvoiceData(invoice2);
	// // Sets the merchant name. This is the name of your Application or
	// // Company.
	// receiver2.setMerchantName("Drinks & Refreshments");
	// // Sets the description of the payment.
	// receiver2.setDescription("The second of two party guys");
	// // Sets the Custom ID. This is any ID that you would like to have
	// // associated with the PayPalReceiverDetails.
	// receiver2.setCustomID("001768");
	// payment.getReceivers().add(receiver2);
	//
	// return payment;
	// }

	private PayPalAdvancedPayment GetPaymentBundle(String ipn, String memos,
			String shipping, String currencyCode,
			ArrayList<HashMap<String, String>> receiptent2,
			ArrayList<HashMap<String, String>> admininvoice2,
			ArrayList<HashMap<String, String>> sellerinvoiceData2) {

		PayPalAdvancedPayment payment = new PayPalAdvancedPayment();
		PayPalReceiverDetails admin = new PayPalReceiverDetails();
		PayPalReceiverDetails seller = new PayPalReceiverDetails();
		payment.setCurrencyType("USD");
		payment.setMemo(memos);
		HashMap<String, String> map23 = receiptent2.get(0);
		HashMap<String, String> map22 = receiptent2.get(1);
		BigDecimal st2 = new BigDecimal(map22.get("sellerAmount"));
		BigDecimal st = new BigDecimal(map23.get("adminAmount"));
		admin.setRecipient(map23.get("adminEmail"));
		admin.setSubtotal(st.add(st2));
		admin.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
		admin.setIsPrimary(false);

		PayPalInvoiceData invoice1 = new PayPalInvoiceData();
		PayPalInvoiceData invoice2 = new PayPalInvoiceData();

		for (int i = 0; i < admininvoice2.size(); i++) {
			PayPalInvoiceItem item1 = new PayPalInvoiceItem();
			HashMap<String, String> map = admininvoice2.get(i);
			HashMap<String, String> map2 = sellerinvoiceData2.get(i);
			item1.setID(map.get("identifier"));
			item1.setName(map.get("name") + "-_-" + map.get("identifier"));
			item1.setQuantity(Integer.parseInt(map.get("itemCount")));
			item1.setTotalPrice(new BigDecimal(map.get("price"))
					.add(new BigDecimal(map2.get("price"))));
			item1.setUnitPrice(new BigDecimal(map.get("itemPrice")));
			invoice1.getInvoiceItems().add(item1);
		}
		admin.setInvoiceData(invoice1);
		admin.setCustomID(memos);
		seller.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
		seller.setIsPrimary(false);
		seller.setRecipient(map22.get("sellerEmail"));
		seller.setSubtotal(st2);
		invoice1.setShipping(new BigDecimal(shipping));

		for (int i = 0; i < sellerinvoiceData2.size(); i++) {
			PayPalInvoiceItem item2 = new PayPalInvoiceItem();
			HashMap<String, String> map = sellerinvoiceData2.get(i);
			item2.setID(map.get("identifier"));
			item2.setName(map.get("name") + "-_-" + map.get("identifier"));
			item2.setQuantity(Integer.parseInt(map.get("itemCount")));
			item2.setTotalPrice(new BigDecimal(map.get("price")));
			item2.setUnitPrice(new BigDecimal(map.get("itemPrice")));
			invoice2.getInvoiceItems().add(item2);
		}
		seller.setInvoiceData(invoice2);
		payment.getReceivers().add(admin);

		return payment;

	}

	class RemoVeItemFromCart extends AsyncTask<String, Void, JSONObject> {

		String url = ConstantValues.Remove;
		ProgressDialog dialog = new ProgressDialog(CartDetailClass.this);

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser parser = new JSONParser();
			return parser.getJSONFromUrl(url + GetSet.getUserId() + "&itemId="
					+ params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Please Wait");
			dialog.setMessage("Removing selected Item");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				String status = result.getString("status");
				if (status.equalsIgnoreCase("true")) {
					new SetupCart().execute(mrechantId);
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			CartDetailClass.this.onBackPressed();
		}

	}

	class ChangeQuantity extends AsyncTask<Object, Void, JSONObject> {

		ProgressDialog dialog = new ProgressDialog(CartDetailClass.this);

		@SuppressWarnings("unchecked")
		@Override
		protected JSONObject doInBackground(Object... arg0) {
			HashMap<String, String> tmpMap = (HashMap<String, String>) arg0[0];
			JSONParser parser = new JSONParser();
			return parser.getJSONFromUrl(ConstantValues.changeItemCount
					+ GetSet.getUserId() + "&itemId=" + tmpMap.get("itemId")
					+ "&quantity=" + tmpMap.get("countSelected"));

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Please Wait");
			dialog.setMessage("Updating quantity");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				if (result.getString("status").equalsIgnoreCase("true")) {
					Intent i = new Intent(CartDetailClass.this,
							CartDetailClass.class);
					i.putExtra("id", mrechantId);
					i.putExtra("merchant", name);
					i.putExtra("data", datas);
					i.putExtra("cartdetaildata", itemidss);
					CartDetailClass.this.finish();
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("returnKey1", "Swinging on a star. ");
		setResult(RESULT_OK, data);
		CartDetailClass.this.finish();
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
		case R.id.btn_cart:
			FragmentChangeActivity.rscart = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_menu:
			/*
			 * Intent i = new Intent(CartDetailClass.this,
			 * ProfileTabHolder.class); i.putExtra("userId",
			 * GetSet.getUserId()); startActivity(i);
			 */
			FragmentChangeActivity.rsmenu = true;
			FragmentChangeActivity.menumap = false;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;

		}
	}

}
