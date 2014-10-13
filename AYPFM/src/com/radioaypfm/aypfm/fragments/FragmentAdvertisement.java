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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class FragmentAdvertisement extends Fragment implements OnClickListener {
  ImageView banner;
  String link = null;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View fragment = inflater.inflate(R.layout.fragment_adver, container, false);
    Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
        ((TextView) fragment.findViewById(R.id.text1)), getActivity());
    banner = (ImageView) fragment.findViewById(R.id.banner);
    banner.setOnClickListener(this);

    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          HttpClient client = new DefaultHttpClient();
          HttpPost httpPost = new HttpPost(Constants.API_URL);

          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          nameValuePairs.add(new BasicNameValuePair("auth_key", Constants.AUTH_KEY));
          nameValuePairs.add(new BasicNameValuePair("action", "banner"));
          httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

          HttpResponse response = client.execute(httpPost);
          String res = EntityUtils.toString(response.getEntity());

          JSONObject obj = new JSONObject(res);
          JSONObject data = obj.optJSONObject("data");

          final String bannerUrl = data.optString("img");
          link = data.optString("link");

          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              AQuery aq = new AQuery(getActivity());
              aq.id(R.id.banner).image(bannerUrl);
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

    return fragment;
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.banner:
        try {
          openWebsite(getActivity());
        } catch (NullPointerException e) {
          e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;

      default:
        break;
    }

  }

  public void openWebsite(Context c) throws NullPointerException {
    if (link != null && !link.isEmpty()) {
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
      c.startActivity(browserIntent);
    }
  }
}
