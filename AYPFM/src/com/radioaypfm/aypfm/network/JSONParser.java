package com.radioaypfm.aypfm.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.radioaypfm.aypfm.model.Video;

public class JSONParser {

	public static ArrayList<Video> parseChannelData(Context context,
			JSONObject json) throws JSONException, NullPointerException {

		ArrayList<Video> videos = new ArrayList<Video>();
		JSONObject feed = (JSONObject) json.optJSONObject("feed");
		JSONArray entries = (JSONArray) feed.optJSONArray("entry");

		if (entries != null)
			for (int i = 0; i < entries.length(); i++) {
				JSONObject e = (JSONObject) entries.opt(i);
				Video v = new Video();
				JSONObject title = (JSONObject) e.optJSONObject("title");
				v.setTitle(title.optString("$t"));

				JSONArray links = (JSONArray) e.optJSONArray("link");
				JSONObject hrefs = (JSONObject) links.opt(0);
				String href = (String) hrefs.optString("href");
				v.setUrl(href);

				JSONObject mediagroup = (JSONObject) e
						.optJSONObject("media$group");
				JSONArray thumbs = (JSONArray) mediagroup
						.optJSONArray("media$thumbnail");
				JSONObject thumb = (JSONObject) thumbs.opt(0);
				String url = (String) thumb.optString("url");
				v.setThumbnail(url);

				JSONObject content = (JSONObject) e.optJSONObject("content");
				String desc = (String) content.optString("$t");
				v.setDesc(desc);

				JSONObject pubDate = (JSONObject) e.optJSONObject("published");
				String date = (String) pubDate.optString("$t");
				v.setDate(date);

				videos.add(v);
			}

		return videos;
	}

}
