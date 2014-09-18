package com.radioaypfm.aypfm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.adapter.VideoAdapter;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class FragmentVideo extends Fragment {
	ListView listView;
	VideoAdapter adapter;

	public FragmentVideo() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_video, container,
				true);

		listView = (ListView) rootView.findViewById(R.id.listViewVideos);
		adapter = new VideoAdapter(getActivity());
		listView.setAdapter(adapter);
		setupLoadVideosButton((Button) rootView
				.findViewById(R.id.loadMoreVideos));

		return rootView;
	}

	protected void setupLoadVideosButton(Button btn) {
		Utilities.setButtonTypeface(Constants.FONT_HELVETICA_ROMAN, btn,
				getActivity());
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

}