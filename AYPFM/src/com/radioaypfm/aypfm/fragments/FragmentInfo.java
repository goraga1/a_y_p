package com.radioaypfm.aypfm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.radioaypfm.aypfm.R;

public class FragmentInfo extends Fragment {

	private WebView web;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_info, container,
				false);

		web = (WebView) fragment.findViewById(R.id.webviewInfo);
		web.loadUrl("http://www.google.com");
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		return fragment;
	}

}
