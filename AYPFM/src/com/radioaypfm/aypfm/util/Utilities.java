package com.radioaypfm.aypfm.util;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;


public class Utilities {
  private static ProgressDialog mDialog;

  public static String getDeviceId(Context c) {
    final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
    return tm.getDeviceId();
  }

  public static void showOrHideActivityIndicator(Context ctx, final int hide, final String message) {
    if (mDialog != null && hide == 1) {
      if (mDialog.isShowing())
        mDialog.dismiss();
    } else {
      mDialog = new ProgressDialog(ctx);
      mDialog.setMessage(message);
      mDialog.setCancelable(false);
      mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      mDialog.show();
    }
  }

  public static void setDialogMessage(String mes) {
    if (mDialog != null && mDialog.isShowing())
      mDialog.setMessage(mes);
  }

  public static void showAlertDialog(Context context, String title, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setCancelable(true);
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  public static void setListviewItemTypeface(Typeface tf, TextView textView, Context c) {
    try {
      textView.setTypeface(tf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setTextviewTypeface(String fontName, TextView textView, Context c) {
    try {
      Typeface tf = Typeface.createFromAsset(c.getAssets(), fontName);
      textView.setTypeface(tf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setButtonTypeface(String fontName, Button button, Context c) {
    try {
      Typeface tf = Typeface.createFromAsset(c.getAssets(), fontName);
      button.setTypeface(tf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isNetworkAvailable(Context context) {
    try {
      ConnectivityManager connectivity =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

      if (connectivity == null) {
        return false;
      } else {
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
          for (int i = 0; i < info.length; i++) {
            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
              return true;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void phoneCall(Context context) {
    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" + "123"));
    context.startActivity(callIntent);
  }

  public static void sendEmail(final Context c) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          HttpClient client = new DefaultHttpClient();
          HttpPost httpPost = new HttpPost(Constants.API_URL);

          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          nameValuePairs.add(new BasicNameValuePair("auth_key", Constants.AUTH_KEY));
          nameValuePairs.add(new BasicNameValuePair("action", "email"));
          httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

          HttpResponse response = client.execute(httpPost);
          String res = EntityUtils.toString(response.getEntity());

          JSONObject obj = new JSONObject(res);
          JSONObject data = obj.optJSONObject("data");
          final String email = data.optString("email");

          Intent intent = new Intent(Intent.ACTION_SEND);
          intent.setType("text/plain");
          intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
          intent.putExtra(Intent.EXTRA_SUBJECT, "Contact AYP FM");
          c.startActivity(Intent.createChooser(intent, "Send Email"));

        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (JSONException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static void openWebsite(final Context c) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          HttpClient client = new DefaultHttpClient();
          HttpPost httpPost = new HttpPost(Constants.API_URL);

          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          nameValuePairs.add(new BasicNameValuePair("auth_key", Constants.AUTH_KEY));
          nameValuePairs.add(new BasicNameValuePair("action", "site-url"));
          httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

          HttpResponse response = client.execute(httpPost);
          String res = EntityUtils.toString(response.getEntity());

          JSONObject obj = new JSONObject(res);
          JSONObject data = obj.optJSONObject("data");
          final String url = data.optString("url");

          Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
          c.startActivity(browserIntent1);

        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (JSONException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

}
