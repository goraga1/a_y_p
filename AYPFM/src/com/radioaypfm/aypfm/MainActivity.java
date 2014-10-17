package com.radioaypfm.aypfm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.radioaypfm.aypfm.fragments.FragmentAdvertisement;
import com.radioaypfm.aypfm.fragments.FragmentInfo;
import com.radioaypfm.aypfm.fragments.FragmentMain;
import com.radioaypfm.aypfm.fragments.FragmentPlayer;
import com.radioaypfm.aypfm.fragments.FragmentVideo;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class MainActivity extends FragmentActivity implements OnClickListener {
  private DrawerLayout mDrawerLayout;
  private RelativeLayout mDrawerList;
  private AQuery aq;
  GoogleCloudMessaging gcm;
  String regid;
  String PROJECT_NUMBER = "968974903106";

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
    getRegId();

  }

  public void getRegId() {
    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground(Void... params) {
        String msg = "";
        try {
          if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
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
        sendRegId();
      }
    }.execute(null, null, null);
  }

  private void sendRegId() {
    String url = "http://radio-aypfm.com/_mob_api/device.php?new=" + regid;
    aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
      @Override
      public void callback(String url, JSONObject json, AjaxStatus status) {
        if (json != null)
          System.out.println("AYP GCM Response: " + json.toString());
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
    ImageButton menuButton = (ImageButton) mCustomView.findViewById(R.id.imageButtonMenu);
    menuButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
          mDrawerLayout.openDrawer(Gravity.LEFT);
        else
          mDrawerLayout.closeDrawers();
      }
    });
    ImageButton infoButton = (ImageButton) mCustomView.findViewById(R.id.imageButtonInfo);
    infoButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openInfoDialog(MainActivity.this);
      }
    });

    mActionBar.setCustomView(mCustomView);
    mActionBar.setDisplayShowCustomEnabled(true);
  }

  protected void initNavigationDrawer() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (RelativeLayout) findViewById(R.id.list_slidermenu);
    ((LinearLayout) findViewById(R.id.slidermenu1)).setOnClickListener(this);
    ((LinearLayout) findViewById(R.id.slidermenu2)).setOnClickListener(this);
    ((LinearLayout) findViewById(R.id.slidermenu3)).setOnClickListener(this);
    ((LinearLayout) findViewById(R.id.slidermenu4)).setOnClickListener(this);
    ((LinearLayout) findViewById(R.id.slidermenu5)).setOnClickListener(this);
    ((LinearLayout) findViewById(R.id.slidermenu6)).setOnClickListener(this);
    ((LinearLayout) findViewById(R.id.slidermenu7)).setOnClickListener(this);
  }

  protected void openInfoDialog(Context context) {
    final Dialog dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    dialog.setContentView(R.layout.dialog);

    if (!dialog.isShowing())
      dialog.show();
    else
      dialog.dismiss();

    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    Window window = dialog.getWindow();
    lp.copyFrom(window.getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    window.setAttributes(lp);

    dialog.findViewById(R.id.dialogLayout).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    if (!Utilities.isNetworkAvailable(MainActivity.this))
      return;

    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          HttpClient client = new DefaultHttpClient();
          HttpPost httpPost = new HttpPost(Constants.API_URL);

          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          nameValuePairs.add(new BasicNameValuePair("auth_key", Constants.AUTH_KEY));
          nameValuePairs.add(new BasicNameValuePair("action", "mention-legales"));
          httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

          HttpResponse response = client.execute(httpPost);
          String res = EntityUtils.toString(response.getEntity());

          JSONObject obj = new JSONObject(res);
          JSONObject data = obj.optJSONObject("data");

          final String title = data.optString("title");
          final String desc = data.optString("desc");
          final String text = Html.fromHtml(desc).toString();


          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              ((ProgressBar) dialog.findViewById(R.id.progressBar)).setVisibility(View.GONE);

              TextView titleView = (TextView) dialog.findViewById(R.id.title);
              TextView textView = (TextView) dialog.findViewById(R.id.text);
              titleView.setText(title);
              textView.setText(text);
              Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN, titleView,
                  MainActivity.this);
              Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN, textView,
                  MainActivity.this);
            }
          });


        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }).start();


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
        Utilities.openWebsite(MainActivity.this);
        break;
      case R.id.slidermenu7:
        Utilities.sendEmail(MainActivity.this);
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
        fragment = new FragmentInfo();
        TAG = "HOME_FRAGMENT";
        break;
      case 2:
        fragment = new FragmentPlayer();
        TAG = "HOME_FRAGMENT";
        break;
      case 3:
        fragment = new FragmentVideo();
        TAG = "HOME_FRAGMENT";
        break;
      case 4:
        fragment = new FragmentAdvertisement();
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
      FragmentTransaction transaction = fragmentManager.beginTransaction();
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
