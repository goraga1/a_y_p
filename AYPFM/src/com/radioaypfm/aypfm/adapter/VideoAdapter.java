package com.radioaypfm.aypfm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

@SuppressLint("DefaultLocale")
public class VideoAdapter extends BaseAdapter {
	Context c;
	private final LayoutInflater mLayoutInflater;

	public VideoAdapter(Context context) {
		this.c = context;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.list_item_videos,
					viewGroup, false);
			holder.image = (ImageView) view.findViewById(R.id.videoImage);
			holder.title = (TextView) view.findViewById(R.id.videoTitle);
			holder.desc = (TextView) view.findViewById(R.id.videoDesc);
			holder.date = (TextView) view.findViewById(R.id.videoDate);
			Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_MD,
					holder.title, c);
			Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
					holder.desc, c);
			Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
					holder.date, c);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		return view;

	}

	private static class ViewHolder {
		protected ImageView image;
		protected TextView title;
		protected TextView desc;
		protected TextView date;

	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}