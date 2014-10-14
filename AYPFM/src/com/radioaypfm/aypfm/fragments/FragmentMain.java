package com.radioaypfm.aypfm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class FragmentMain extends Fragment implements OnClickListener {

  public FragmentMain() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_main, container, false);

    Button mainContactButton = (Button) rootView.findViewById(R.id.mainContactButton);
    Button mainVideoButton = (Button) rootView.findViewById(R.id.mainVideoButton);
    Button mainInfoButton = (Button) rootView.findViewById(R.id.mainInfoButton);
    Button mainPlayButton = (Button) rootView.findViewById(R.id.mainPlayButton);
    Button mainSoundButton = (Button) rootView.findViewById(R.id.mainSoundButton);

    mainContactButton.setOnClickListener(this);
    mainVideoButton.setOnClickListener(this);
    mainInfoButton.setOnClickListener(this);
    mainPlayButton.setOnClickListener(this);
    mainSoundButton.setOnClickListener(this);

    Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
        ((TextView) rootView.findViewById(R.id.mainDesc)), getActivity());

    return rootView;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      case R.id.mainPlayButton:
        displayView(0);
        break;
      case R.id.mainVideoButton:
        displayView(1);
        break;
      case R.id.mainInfoButton:
        displayView(2);
        break;
      case R.id.mainContactButton:
        displayView(3);
        break;
      case R.id.mainSoundButton:
        displayView(4);
        break;
      default:
        break;
    }
  }

  private void displayView(int position) {
    String TAG = "";
    Fragment fragment = null;
    switch (position) {
      case 0:
        fragment = new FragmentPlayer();
        break;
      case 1:
        fragment = new FragmentVideo();
        break;
      case 2:
        fragment = new FragmentInfo();
        break;
      case 3:
        Utilities.sendEmail(getActivity());
        break;
      case 4:
        fragment = new FragmentAdvertisement();
        break;
      default:
        break;
    }

    if (fragment != null) {
      FragmentManager fragmentManager = getFragmentManager();
      FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.replace(R.id.frame_container, fragment, TAG);
      transaction.commit();
    } else {
    }
  }
}
