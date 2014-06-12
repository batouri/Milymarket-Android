package com.hitasoft.apps.milymarket.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url) {

		// Making HTTP request
		try {
			final int CONN_WAIT_TIME = 3000;
			final int CONN_DATA_WAIT_TIME = 2000;

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					CONN_WAIT_TIME);
			HttpConnectionParams.setSoTimeout(httpParams, CONN_DATA_WAIT_TIME);

			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			json = EntityUtils.toString(httpEntity, HTTP.UTF_8);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}

		// try {
		// BufferedReader reader = new BufferedReader(new InputStreamReader(
		// is, "iso-8859-1"), 8);
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// sb.append(line + "\n");
		// }
		// is.close();
		// json = sb.toString();
		// } catch (Exception e) {
		// Log.e("Buffer Error", "Error converting result " + e.toString());
		// }

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}