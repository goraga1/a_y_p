package com.radioaypfm.aypfm;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.radioaypfm.aypfm.fragments.FragmentMain;
import com.radioaypfm.aypfm.fragments.FragmentPlayer;
import com.radioaypfm.aypfm.fragments.FragmentVideo;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.PreferenceUtilities;
import com.radioaypfm.aypfm.util.Utilities;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerList;
	private AQuery aq;
	GoogleCloudMessaging gcm;
	String regid;
	String PROJECT_NUMBER = "00000000000";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initActionBar();
		initNavigationDrawer();
		setupFonts();
		if (savedInstanceState == null) {
			displayView(0);
		}

		aq = new AQuery(MainActivity.this);
		// / getRegId();

		initFragment(new FragmentPlayer());
	}

	protected void initFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(android.R.id.content, fragment);
		fragmentTransaction.commit();
	}

	public void getRegId() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					msg = "Device registered, registration ID=" + regid;
					Log.i("GCM", msg);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.e("GCM", msg);
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				sendRegId(regid);
			}
		}.execute(null, null, null);
	}

	private void sendRegId(String regId) {
		String token = PreferenceUtilities.readString(MainActivity.this,
				Constants.KEY_ACCESS_TOKEN, "");
		String url = Constants.API_URL + token + "/gcm/" + token;
		JSONObject input = new JSONObject();
		try {
			input.put("gcmId", regId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		aq.put(url, input, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void initActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		ImageButton menuButton = (ImageButton) mCustomView
				.findViewById(R.id.imageButtonMenu);
		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
					mDrawerLayout.openDrawer(Gravity.LEFT);
				else
					mDrawerLayout.closeDrawers();
			}
		});
		ImageButton infoButton = (ImageButton) mCustomView
				.findViewById(R.id.imageButtonInfo);
		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	protected void initNavigationDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (RelativeLayout) findViewById(R.id.list_slidermenu);
		((LinearLayout) findViewById(R.id.slidermenu1))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.slidermenu2))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.slidermenu3))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.slidermenu4))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.slidermenu5))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.slidermenu6))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.slidermenu7))
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slidermenu1:
			displayView(0);
			break;
		case R.id.slidermenu2:
			displayView(1);
			break;
		case R.id.slidermenu3:
			displayView(2);
			break;
		case R.id.slidermenu4:
			displayView(3);
			break;
		case R.id.slidermenu5:
			displayView(4);
			break;
		case R.id.slidermenu6:
			displayView(5);
			break;
		case R.id.slidermenu7:
			displayView(5);
			break;
		default:
			break;
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		String TAG = "";
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new FragmentMain();
			TAG = "HOME_FRAGMENT";
			break;
		case 1:
			fragment = new FragmentMain();
			TAG = "HOME_FRAGMENT";
			break;
		case 2:
			fragment = new FragmentMain();
			TAG = "HOME_FRAGMENT";
			break;
		case 3:
			fragment = new FragmentVideo();
			TAG = "HOME_FRAGMENT";
			break;
		case 4:
			fragment = new FragmentMain();
			TAG = "HOME_FRAGMENT";
			break;
		case 5:
			fragment = new FragmentMain();
			TAG = "HOME_FRAGMENT";
			break;
		case 6:
			fragment = new FragmentMain();
			TAG = "HOME_FRAGMENT";
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.frame_container, fragment, TAG);
			transaction.commit();

			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	protected void setupFonts() {
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu1), MainActivity.this);
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu2), MainActivity.this);
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu3), MainActivity.this);
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu4), MainActivity.this);
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu5), MainActivity.this);
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu6), MainActivity.this);
		Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
				(TextView) findViewById(R.id.menu7), MainActivity.this);
	}

}