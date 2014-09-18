package com.radioaypfm.aypfm.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.PreferenceUtilities;

public class JSONParser {

	public static void parseLoginData(Context context, JSONObject json) {
		try {
			if (json.has(Constants.KEY_CONTENT)
					&& json.get(Constants.KEY_CONTENT) != null) {
				JSONObject content = json.getJSONObject(Constants.KEY_CONTENT);

				if (content.has(Constants.KEY_ACCESS_TOKEN)
						&& content.get(Constants.KEY_ACCESS_TOKEN) != null) {
					String token = content.get(Constants.KEY_ACCESS_TOKEN)
							.toString();
					PreferenceUtilities.writeString(context,
							Constants.KEY_ACCESS_TOKEN, token);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
