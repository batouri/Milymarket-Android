package com.hitasoft.apps.milymarket;

import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ProfileTabHolder extends SherlockFragmentActivity implements
		OnClickListener {

	private static final String[] CONTENT = new String[] { "Profile",
			"Fancied", "List" };
	ProfilePageChanger mAdapter;
	protected static ViewPager mPager;
	protected static ImageButton smenu;
	String userId = null;
	private static ImageView tabIndictr;
	private ImageButton home, near, shop, alert, menu;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.profile_tabs);
		home = (ImageButton) findViewById(R.id.btn_home);
		near = (ImageButton) findViewById(R.id.btn_near);
		shop = (ImageButton) findViewById(R.id.btn_shop);
		alert = (ImageButton) findViewById(R.id.btn_alert);
		menu = (ImageButton) findViewById(R.id.btn_menu);

		home.setOnClickListener(this);
		near.setOnClickListener(this);
		shop.setOnClickListener(this);
		alert.setOnClickListener(this);
		menu.setOnClickListener(this);
		tabIndictr = (ImageView) findViewById(R.id.customTabIndicator);
		tabIndictr.setImageResource(R.drawable.profile);
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this,
				ProfileFragment.class.getName()));
		fragments.add(Fragment.instantiate(this,
				FantaciedFragment.class.getName()));
		fragments.add(Fragment.instantiate(this,
				ProfileListFragment.class.getName()));
		mAdapter = new ProfilePageChanger(getSupportFragmentManager(),
				fragments);
		mPager = (ViewPager) findViewById(R.id.pagerForProfile);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					tabIndictr.setImageResource(R.drawable.profile);
					break;
				case 1:
					tabIndictr.setImageResource(R.drawable.fantacyd);
					break;
				case 2:
					tabIndictr.setImageResource(R.drawable.lists);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	class ProfilePageChanger extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public ProfilePageChanger(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;

		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		public Fragment changeFragment(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position];
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
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
		case R.id.btn_shop:
			FragmentChangeActivity.rsshop = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;
		case R.id.btn_alert:
			FragmentChangeActivity.rsnote = true;
			startActivity(new Intent(this, FragmentChangeActivity.class));
			break;

		}
	}
}
