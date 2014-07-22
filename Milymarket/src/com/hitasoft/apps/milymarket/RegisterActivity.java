package com.hitasoft.apps.milymarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hitasoft.apps.milymarket.util.ConstantValues;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class RegisterActivity extends Activity {

	EditText fullname, username, email, password, confirmpassword;
	ImageButton register;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	private static Toast toast;
	private RelativeLayout parent;
	ImageLoader loader;

	/*
	 * (non-Javadoc)
	 * 
	 * @see for Background scroll
	 */
	JSONObject jsonObject;
	private LinearLayout horizontalOuterLayout;
	private HorizontalScrollView horizontalScrollview;
	private TextView horizontalTextView;
	private int scrollMax;
	private int scrollPos = 0;
	private int scrollMax1;
	private int scrollPos1 = 0;
	private TimerTask clickSchedule;
	private TimerTask scrollerSchedule;
	private TimerTask faceAnimationSchedule;
	private Button clickedButton = null;
	private Timer scrollTimer = null;
	private Timer clickTimer = null;
	private Timer faceTimer = null;
	private Boolean isFaceDown = true;
	private TimerTask clickSchedule1;
	private TimerTask scrollerSchedule1;
	private TimerTask faceAnimationSchedule1;
	private Button clickedButton1 = null;
	private Timer scrollTimer1 = null;
	private Timer clickTimer1 = null;
	private Timer faceTimer1 = null;
	private Boolean isFaceDown1 = true;
	int t = 1;
	int g = 0;
	JSONObject jsonobj;
	private LinearLayout horizontalOuterLayout1;
	private HorizontalScrollView horizontalScrollview1;
	private String[] imageNameArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		loader = ImageLoader.getInstance();
		parent = (RelativeLayout) findViewById(R.id.parent);
		setupUI(parent);
		fullname = (EditText) findViewById(R.id.firstName);
		username = (EditText) findViewById(R.id.userName);
		email = (EditText) findViewById(R.id.Email);
		password = (EditText) findViewById(R.id.passWord);
		confirmpassword = (EditText) findViewById(R.id.confirmPassword);
		register = (ImageButton) findViewById(R.id.signUp);
		fullname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fullname.setError(null);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				username.setError(null);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				email.setError(null);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				password.setError(null);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (fullname.getText().toString().trim().length() == 0) {
					fullname.setError("Please fill this field");
				} else if (username.getText().toString().trim().length() == 0) {
					username.setError("Please fill this field");
				} else if (!email.getText().toString().matches(emailPattern)
						&& email.length() > 0) {
					email.setError("Please verify your mail id");
				} else if (password.getText().toString().trim().length() < 6
						|| confirmpassword.getText().toString().trim().length() < 6) {
					password.setError("Password should have atleast 6 chars");
				} else if (!password
						.getText()
						.toString()
						.trim()
						.equalsIgnoreCase(
								confirmpassword.getText().toString().trim())) {
					password.setError("Password doesn't match");
					password.setText("");
					confirmpassword.setText("");
				} else {
					new registerAsync().execute();
				}
			}

		});
	}

	class registerAsync extends AsyncTask<Void, Void, String> {
		private ProgressDialog dialog = new ProgressDialog(
				RegisterActivity.this);

		@Override
		protected String doInBackground(Void... arg0) {
			String result = postData();
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("Un instant s'il vous plait...");
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
					String msg = jonj.getString("message");
					showAToast(msg);
					RegisterActivity.this.finish();
				} else {
					showAToast(jonj.getString(ConstantValues.msg));
					email.setText("");
					password.setText("");
					confirmpassword.setText("");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String postData() {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ConstantValues.signUp);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("username", username
					.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("fullname", fullname
					.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("email", email.getText()
					.toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("password", password
					.getText().toString().trim()));
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

	public void showAToast(String st) {
		try {
			toast.getView().isShown();
			toast.setText(st);
		} catch (Exception e) {
			toast = Toast.makeText(RegisterActivity.this, st,
					Toast.LENGTH_SHORT);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void setupUI(View view) {

		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					hideSoftKeyboard(RegisterActivity.this);
					return false;
				}

			});
		}

		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		} catch (NullPointerException npe) {

		} catch (Exception e) {

		}
	}

	public void right() {
		horizontalScrollview = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id);
		horizontalOuterLayout = (LinearLayout) findViewById(R.id.horiztonal_outer_layout_id);
		horizontalScrollview.setHorizontalScrollBarEnabled(false);
		addImagesToView();
		ViewTreeObserver vto = horizontalOuterLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				horizontalOuterLayout.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				getScrollMaxAmount();
				startAutoScrolling();

			}
		});
	}

	public void left() {
		horizontalScrollview1 = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id1);
		horizontalOuterLayout1 = (LinearLayout) findViewById(R.id.horiztonal_outer_layout_id1);
		horizontalScrollview1.setHorizontalScrollBarEnabled(false);
		addImagesToView1();
		ViewTreeObserver vto = horizontalOuterLayout1.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				horizontalOuterLayout1.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				getScrollMaxAmount1();
				startAutoScrolling1();
			}
		});
	}

	public void getScrollMaxAmount() {
		int actualWidth = (horizontalOuterLayout.getMeasuredWidth() - 512);
		scrollMax = actualWidth;
	}

	public void getScrollMaxAmount1() {
		int actualWidth = (horizontalOuterLayout1.getMeasuredWidth() - 512);
		scrollMax1 = actualWidth;
	}

	public void startAutoScrolling() {
		if (scrollTimer == null) {
			scrollTimer = new Timer();
			final Runnable Timer_Tick = new Runnable() {
				public void run() {
					lefttoright();
				}
			};

			if (scrollerSchedule != null) {
				scrollerSchedule.cancel();
				scrollerSchedule = null;
			}
			scrollerSchedule = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(Timer_Tick);
				}
			};

			scrollTimer.schedule(scrollerSchedule, 30, 30);
		}
	}

	public void startAutoScrolling1() {
		if (scrollTimer1 == null) {
			scrollTimer1 = new Timer();
			final Runnable Timer_Tick = new Runnable() {
				public void run() {
					moveScrollView();
				}
			};

			if (scrollerSchedule1 != null) {
				scrollerSchedule1.cancel();
				scrollerSchedule1 = null;
			}
			scrollerSchedule1 = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(Timer_Tick);
				}
			};

			scrollTimer1.schedule(scrollerSchedule1, 30, 30);
		}
	}

	public void moveScrollView() {
		scrollPos1 = (int) (horizontalScrollview1.getScrollX() + 1.0);
		if (scrollPos1 >= scrollMax1) {
			scrollPos1 = 0;
		}
		horizontalScrollview1.scrollTo(scrollPos1, 0);
	}

	/** Adds the images to view. */
	public void addImagesToView() {
		for (int i = 0; i < imageNameArray.length; i++) {
			final int s = i;
			final ImageView imageButton = new ImageView(this);
			new Thread(new Runnable() {
				public void run() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// System.out.println("Downloading started");
						}
					});
					try {
						loader.loadImage(imageNameArray[s],
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
										imageButton.setImageBitmap(loadedImage);

									}

									@Override
									public void onLoadingCancelled(
											String imageUri, View view) {

									}
								});
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
			}).start();
			imageButton.setTag(i);
			imageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (isFaceDown) {
						if (clickTimer != null) {
							clickTimer.cancel();
							clickTimer = null;
						}
						clickedButton = (Button) arg0;
						stopAutoScrolling();
						clickedButton.startAnimation(scaleFaceUpAnimation());
						clickedButton.setSelected(true);
						clickTimer = new Timer();

						if (clickSchedule != null) {
							clickSchedule.cancel();
							clickSchedule = null;
						}

						clickSchedule = new TimerTask() {
							public void run() {
								startAutoScrolling();
							}
						};

						clickTimer.schedule(clickSchedule, 1500);
					}
				}
			});

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					100, 100);
			params.setMargins(0, 5, 0, 5);
			imageButton.setLayoutParams(params);
			horizontalOuterLayout.addView(imageButton);
		}
	}

	public void addImagesToView1() {
		for (int i = 0; i < imageNameArray.length; i++) {
			final int s = i;
			final ImageView imageButton = new ImageView(this);

			new Thread(new Runnable() {
				public void run() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// System.out.println("Downloading started");
						}
					});
					try {
						URL url1 = new URL(imageNameArray[s]);
						loader.loadImage(imageNameArray[s],
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
										imageButton.setImageBitmap(loadedImage);

									}

									@Override
									public void onLoadingCancelled(
											String imageUri, View view) {
										// TODO Auto-generated method stub

									}
								});
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
			}).start();
			imageButton.setTag(i);
			imageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (isFaceDown1) {
						if (clickTimer1 != null) {
							clickTimer1.cancel();
							clickTimer1 = null;
						}
						clickedButton1 = (Button) arg0;
						stopAutoScrolling1();
						clickedButton1.startAnimation(scaleFaceUpAnimation1());
						clickedButton1.setSelected(true);
						clickTimer1 = new Timer();

						if (clickSchedule1 != null) {
							clickSchedule1.cancel();
							clickSchedule1 = null;
						}

						clickSchedule1 = new TimerTask() {
							public void run() {
								startAutoScrolling();
							}
						};

						clickTimer1.schedule(clickSchedule1, 1500);
					}
				}
			});

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					100, 100);
			params.setMargins(0, 5, 0, 5);
			imageButton.setLayoutParams(params);
			horizontalOuterLayout1.addView(imageButton);
		}
	}

	public Animation scaleFaceUpAnimation() {
		Animation scaleFace = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(500);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				// horizontalTextView.setText(nameArray[(Integer)
				// clickedButton.getTag()]);
				isFaceDown = false;
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (faceTimer != null) {
					faceTimer.cancel();
					faceTimer = null;
				}

				faceTimer = new Timer();
				if (faceAnimationSchedule != null) {
					faceAnimationSchedule.cancel();
					faceAnimationSchedule = null;
				}
				faceAnimationSchedule = new TimerTask() {
					@Override
					public void run() {
						faceScaleHandler.sendEmptyMessage(0);
					}
				};

				faceTimer.schedule(faceAnimationSchedule, 750);
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	public Animation scaleFaceUpAnimation1() {
		Animation scaleFace = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(500);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				// horizontalTextView1.setText(nameArray[(Integer)
				// clickedButton1.getTag()]);
				isFaceDown1 = false;
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (faceTimer1 != null) {
					faceTimer1.cancel();
					faceTimer1 = null;
				}

				faceTimer1 = new Timer();
				if (faceAnimationSchedule1 != null) {
					faceAnimationSchedule1.cancel();
					faceAnimationSchedule1 = null;
				}
				faceAnimationSchedule1 = new TimerTask() {
					@Override
					public void run() {
						faceScaleHandler1.sendEmptyMessage(0);
					}
				};

				faceTimer1.schedule(faceAnimationSchedule1, 750);
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	private Handler faceScaleHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (clickedButton.isSelected() == true)
				clickedButton.startAnimation(scaleFaceDownAnimation(500));
		}
	};
	private Handler faceScaleHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (clickedButton1.isSelected() == true)
				clickedButton1.startAnimation(scaleFaceDownAnimation1(500));
		}
	};

	public Animation scaleFaceDownAnimation(int duration) {
		Animation scaleFace = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(duration);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				horizontalTextView.setText("");
				isFaceDown = true;
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	public Animation scaleFaceDownAnimation1(int duration) {
		Animation scaleFace = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleFace.setDuration(duration);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				isFaceDown1 = true;
			}
		};
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}

	public void stopAutoScrolling() {
		if (scrollTimer1 != null) {
			scrollTimer1.cancel();
			scrollTimer1 = null;
		}
	}

	public void stopAutoScrolling1() {
		if (scrollTimer != null) {
			scrollTimer.cancel();
			scrollTimer = null;
		}
	}

	public void onDestroy() {
		clearTimerTaks(clickSchedule);
		clearTimerTaks(scrollerSchedule);
		clearTimerTaks(faceAnimationSchedule);
		clearTimers(scrollTimer);
		clearTimers(clickTimer);
		clearTimers(faceTimer);

		clickSchedule = null;
		scrollerSchedule = null;
		faceAnimationSchedule = null;
		scrollTimer = null;
		clickTimer = null;
		faceTimer = null;
		clearTimerTaks(clickSchedule1);
		clearTimerTaks(scrollerSchedule1);
		clearTimerTaks(faceAnimationSchedule1);
		clearTimers(scrollTimer1);
		clearTimers(clickTimer1);
		clearTimers(faceTimer1);

		clickSchedule1 = null;
		scrollerSchedule1 = null;
		faceAnimationSchedule1 = null;
		scrollTimer1 = null;
		clickTimer1 = null;
		super.onDestroy();
	}

	private void clearTimers(Timer timer) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void clearTimerTaks(TimerTask timerTask) {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	public void lefttoright() {
		if (t == 1) {
			scrollPos = scrollMax;
			t++;
		}

		scrollPos = scrollPos - 1;
		if (scrollPos <= 0) {
			scrollPos = scrollMax;
		}
		horizontalScrollview.scrollTo(scrollPos, 0);
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	public class myAsyncTask extends AsyncTask<Void, Void, Void> {

		myAsyncTask() {
			System.out.println("hereasyntask");
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("Onpostexecute");
			right();
			left();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("Onpreexecute");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			System.out.println("doinbackground");
			try {
				StringBuilder sb = new StringBuilder();
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(
						"http://fancyclone.net/dev/api/slideshow");
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null
						&& response.getStatusLine().getStatusCode() == 200) {
					InputStream is = entity.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String str;
					while ((str = br.readLine()) != null) {
						sb.append(str);
					}
					String result = sb.toString();
					jsonObject = new JSONObject(result);
					is.close();
				}
				System.out.println(jsonObject);
				JSONArray arr = jsonObject.getJSONArray("result");
				int n = arr.length();
				imageNameArray = new String[n];
				for (int i = 0; i < n; i++) {
					JSONObject obj = arr.getJSONObject(i);
					System.out.println(obj);
					String s = obj.getString("item_url_main_150");
					System.out.println(s);
					imageNameArray[i] = s;
					System.out.println(s);
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
	}

	public void geturl() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

					}
				});
				try {
					StringBuilder sb = new StringBuilder();
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(
							"http://fancyclone.net/dev/api/slideshow");
					HttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null
							&& response.getStatusLine().getStatusCode() == 200) {
						InputStream is = entity.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						String str;
						while ((str = br.readLine()) != null) {
							sb.append(str);
						}
						String result = sb.toString();
						jsonObject = new JSONObject(result);
						is.close();
					}
					System.out.println(jsonObject);
					JSONArray arr = jsonObject.getJSONArray("result");
					int n = arr.length();
					imageNameArray = new String[n];
					for (int i = 0; i < n; i++) {
						JSONObject obj = arr.getJSONObject(i);
						System.out.println(obj);
						String s = obj.getString("item_url_main_150");
						System.out.println(s);
						imageNameArray[i] = s;
						System.out.println(s);
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
		}).start();
	}
}
