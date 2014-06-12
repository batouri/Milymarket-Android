package com.hitasoft.apps.milymarket;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.actionbarsherlock.app.SherlockFragment;

@SuppressWarnings("deprecation")
public class ProfileTabholderfragment extends SherlockFragment {
	private LocalActivityManager mLocalActivityManager;

	protected LocalActivityManager getLocalActivityManager() {
		return mLocalActivityManager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle state = null;
		mLocalActivityManager = new LocalActivityManager(getActivity(), true);
		mLocalActivityManager.dispatchCreate(state);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int colorBlack = getResources().getColor(R.color.black);
		String text = "Profile";
		SpannableString spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(colorBlack), 0,
				text.length(), 0);
		((FragmentChangeActivity) getActivity()).getSupportActionBar()
				.setTitle(spannable);
		Intent i = new Intent(getActivity(), ProfileTabHolder.class);
		Window w = mLocalActivityManager.startActivity("tag", i);
		View currentView = w.getDecorView();
		currentView.setVisibility(View.VISIBLE);
		currentView.setFocusableInTouchMode(true);
		((ViewGroup) currentView)
				.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		return currentView;
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
	}

}
