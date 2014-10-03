package com.radioaypfm.aypfm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.radioaypfm.aypfm.R;

public class FragmentMain extends Fragment implements OnClickListener {

	public FragmentMain() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		rootView.findViewById(R.id.mainContactButton).setOnClickListener(this);
		rootView.findViewById(R.id.mainVideoButton).setOnClickListener(this);
		rootView.findViewById(R.id.mainInfoButton).setOnClickListener(this);
		rootView.findViewById(R.id.mainPlayButton).setOnClickListener(this);
		rootView.findViewById(R.id.mainSoundButton).setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mainPlayButton:
			displayView(0);
			break;
		case R.id.mainVideoButton:
			displayView(1);
			break;
		case R.id.mainInfoButton:
			displayView(2);
			break;
		case R.id.mainContactButton:
			displayView(3);
			break;
		case R.id.mainSoundButton:
			displayView(4);
			break;
		default:
			break;
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		String TAG = "";
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new FragmentPlayer();
			TAG = "HOME_FRAGMENT";
			break;
		case 1:
			fragment = new FragmentVideo();
			TAG = "HOME_FRAGMENT";
			break;
		case 2:
			fragment = new FragmentInfo();
			TAG = "HOME_FRAGMENT";
			break;
		case 3:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
			intent.putExtra(Intent.EXTRA_SUBJECT, "AYP Contact");
			startActivity(Intent.createChooser(intent, "Send Email"));
			break;
		case 4:
			fragment = new FragmentVideo();
			TAG = "HOME_FRAGMENT";
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.frame_container, fragment, TAG);
			transaction.commit();

		} else {
			// Log.e("MainActivity", "Error in creating fragment");
		}
	}
}