package com.radioaypfm.aypfm.util;

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
    textView.setTypeface(tf);
  }

  public static void setTextviewTypeface(String fontName, TextView textView, Context c) {
    Typeface tf = Typeface.createFromAsset(c.getAssets(), fontName);
    textView.setTypeface(tf);
  }

  public static void setButtonTypeface(String fontName, Button button, Context c) {
    Typeface tf = Typeface.createFromAsset(c.getAssets(), fontName);
    button.setTypeface(tf);
  }

  public static boolean isNetworkAvailable(Context context) {
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
    return false;
  }

  public static void phoneCall(String number, Context context) {
    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" + number));
    context.startActivity(callIntent);
  }

  public static void sendEmail(Context c) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
    intent.putExtra(Intent.EXTRA_SUBJECT, "Contact AYP FM");
    c.startActivity(Intent.createChooser(intent, "Send Email"));
  }

}
