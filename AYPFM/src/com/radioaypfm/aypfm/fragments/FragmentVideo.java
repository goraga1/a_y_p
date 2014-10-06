package com.radioaypfm.aypfm.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.adapter.VideoAdapter;
import com.radioaypfm.aypfm.model.Video;
import com.radioaypfm.aypfm.network.JSONParser;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class FragmentVideo extends Fragment {
  ListView listView;
  VideoAdapter adapter;
  AQuery aq;
  ArrayList<Video> videos;
  int index = 15;
  int count = 15;
  String url = "";
  ProgressBar progressBar;

  public FragmentVideo() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_video, container, false);

    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarVideo);
    listView = (ListView) rootView.findViewById(R.id.listViewVideos);
    setupLoadVideosButton((Button) rootView.findViewById(R.id.loadMoreVideos));

    videos = new ArrayList<Video>();
    aq = new AQuery(getActivity());
    getChannelData();

    return rootView;
  }

  public void getChannelData() {

    url =
        "https://gdata.youtube.com/feeds/api/users/UCEJ0AWCTu7mWqjVjX8CTiAg/uploads?&alt=json&max-results="
            + String.valueOf(index);

    aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
      @Override
      public void callback(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
          try {
            videos = JSONParser.parseChannelData(getActivity(), json);
            adapter = new VideoAdapter(getActivity(), videos);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videos.get(position)
                    .getUrl())));
              }
            });
          } catch (JSONException e) {
            e.printStackTrace();
          } catch (NullPointerException e) {
            e.printStackTrace();
          } finally {
            hideProgressBar();
          }
        }
      }
    });
  }

  public void loadMoreVideos() {
    showProgressBar();
    String newUrl = url + "&start-index=" + String.valueOf(index + 1);

    aq.ajax(newUrl, JSONObject.class, new AjaxCallback<JSONObject>() {
      @Override
      public void callback(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
          try {
            ArrayList<Video> newVideos = JSONParser.parseChannelData(getActivity(), json);
            videos.addAll(newVideos);
            adapter.notifyDataSetChanged();
            newVideos.clear();
            newVideos = null;
            index += count;
          } catch (JSONException e) {
            e.printStackTrace();
          } catch (NullPointerException e) {
            e.printStackTrace();
          } finally {
            hideProgressBar();
          }
        }
      }
    });
  }

  protected void setupLoadVideosButton(Button btn) {
    Utilities.setButtonTypeface(Constants.FONT_HELVETICA_ROMAN, btn, getActivity());
    btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        loadMoreVideos();
      }
    });
  }

  protected void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  protected void hideProgressBar() {
    progressBar.setVisibility(View.INVISIBLE);
  }

}
