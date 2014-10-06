package com.radioaypfm.aypfm.fragments;

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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class FragmentInfo extends Fragment {



  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View fragment = inflater.inflate(R.layout.fragment_info, container, false);
    final Activity activity = getActivity();

    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          HttpClient client = new DefaultHttpClient();
          HttpPost httpPost = new HttpPost(Constants.API_URL);

          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          nameValuePairs.add(new BasicNameValuePair("auth_key", Constants.AUTH_KEY));
          nameValuePairs.add(new BasicNameValuePair("action", "qui-somme"));
          httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

          HttpResponse response = client.execute(httpPost);
          String res = EntityUtils.toString(response.getEntity());

          JSONObject obj = new JSONObject(res);
          JSONObject data = obj.optJSONObject("data");

          final String title = data.optString("title");
          final String desc = data.optString("desc");
          final String text = Html.fromHtml(desc).toString();


          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              ((ProgressBar) fragment.findViewById(R.id.progressBar)).setVisibility(View.GONE);

              TextView titleView = (TextView) fragment.findViewById(R.id.title);
              TextView textView = (TextView) fragment.findViewById(R.id.text);
              titleView.setText(title);
              textView.setText(text);
              Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN, titleView,
                  getActivity());
              Utilities
                  .setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN, textView, getActivity());
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
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
    }).start();

    return fragment;
  }

}
