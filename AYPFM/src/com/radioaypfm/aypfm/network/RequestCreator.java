package com.radioaypfm.aypfm.network;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.radioaypfm.aypfm.model.Video;

public class RequestCreator {
	public static ArrayList<Video> getChannelData(final Context context,
			final AQuery aq) {

		ArrayList<Video> videos = new ArrayList<Video>();
		String url = "https://gdata.youtube.com/feeds/api/users/UCaQpF4GxsqgUrSlEeOC96Pw/uploads?&max-results=50&alt=json";

		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				if (json != null) {
					try {
						JSONParser.parseChannelData(context, json);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return videos;

	}

}