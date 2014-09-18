package com.radioaypfm.aypfm.util;

import android.content.Context;

public class DataHolder {

	private static DataHolder dataHolder;

	private DataHolder(Context c) {

	}

	public static DataHolder getInstance(Context context) {
		if (dataHolder == null) {
			dataHolder = new DataHolder(context.getApplicationContext());
		}
		return dataHolder;
	}

	public void emptyData() {

	}

}
