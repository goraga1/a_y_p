package com.radioaypfm.aypfm.network;

import org.json.JSONObject;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.PreferenceUtilities;

public class RequestCreator {
	public static void getData(final Context context, final AQuery aq) {
		String token = PreferenceUtilities.readString(context,
				Constants.KEY_ACCESS_TOKEN, "");
		String url = Constants.API_URL + token + "/category";

		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				if (json != null) {
					JSONParser.parseLoginData(context, json);
				}
			}
		});

	}

}