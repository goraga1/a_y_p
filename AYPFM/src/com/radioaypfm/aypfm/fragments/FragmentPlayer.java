package com.radioaypfm.aypfm.fragments;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

public class FragmentPlayer extends Fragment {

  public FragmentPlayer() {}

  private Button btn;
  private boolean isPlayingButton;
  private MediaPlayer mediaPlayer;
  private AudioManager audioManager;
  private boolean intialStage = true;
  private SeekBar volumeCtrl;
  private String RADIO_URL = "";
  private Activity activity;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    activity = getActivity();
    View rootView = inflater.inflate(R.layout.fragment_player, container, false);
    Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
        ((TextView) rootView.findViewById(R.id.playerTitle)), activity);
    Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
        ((TextView) rootView.findViewById(R.id.radioDesc)), activity);
    Utilities.setTextviewTypeface(Constants.FONT_HELVETICA_ROMAN,
        ((TextView) rootView.findViewById(R.id.radioDesc2)), activity);


    btn = (Button) rootView.findViewById(R.id.playButton);
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    btn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        if (RADIO_URL.isEmpty() || !Utilities.isNetworkAvailable(activity)) {
          Toast.makeText(activity, "Channel not found", Toast.LENGTH_SHORT).show();
          return;
        }

        if (!isPlayingButton) {
          btn.setBackgroundResource(R.drawable.pause_radio);// pause
          if (intialStage)
            new Player().execute(RADIO_URL);
          else {
            if (!mediaPlayer.isPlaying())
              mediaPlayer.pause();
          }
          isPlayingButton = true;
        } else {
          btn.setBackgroundResource(R.drawable.play_radio);// play
          if (mediaPlayer.isPlaying())
            mediaPlayer.stop();

          mediaPlayer.reset();
          isPlayingButton = false;
          intialStage = true;
        }
      }

    });

    // Volume SeekBar
    volumeCtrl = (SeekBar) rootView.findViewById(R.id.volSeekBar);
    audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

    volumeCtrl.setMax(maxVolume);
    volumeCtrl.setProgress(curVolume);
    volumeCtrl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      public void onStopTrackingTouch(SeekBar seekBar) {}

      public void onStartTrackingTouch(SeekBar seekBar) {}

      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
      }
    });

    rootView.setOnKeyListener(new OnKeyListener() {

      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
          int index = volumeCtrl.getProgress();
          volumeCtrl.setProgress(index + 1);
          return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
          int index = volumeCtrl.getProgress();
          volumeCtrl.setProgress(index - 1);
          return true;
        }
        return false;
      }
    });

    rootView.findViewById(R.id.noSound).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        if (mediaPlayer.isPlaying() && volumeCtrl.getProgress() != 0) {
          volumeCtrl.setProgress(0);
        }
      }
    });

    rootView.findViewById(R.id.maxSound).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        if (mediaPlayer.isPlaying() && volumeCtrl.getProgress() != 100) {
          volumeCtrl.setProgress(100);
        }
      }
    });

    new Thread(new Runnable() {
      @Override
      public void run() {
        getRadioUrl(activity);
      }
    }).start();

    return rootView;
  }

  @Override
  public void onResume() {

    super.onResume();
  }

  public void getRadioUrl(Activity a) {


    try {
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(Constants.API_URL);

      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
      nameValuePairs.add(new BasicNameValuePair("auth_key", Constants.AUTH_KEY));
      nameValuePairs.add(new BasicNameValuePair("action", "radio-url"));
      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      HttpResponse response = client.execute(httpPost);
      String res = EntityUtils.toString(response.getEntity());

      JSONObject obj = new JSONObject(res);
      JSONObject data = obj.optJSONObject("data");
      RADIO_URL = data.optString("radiourl");

      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          btn.setEnabled(true);
        }
      });


    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  class Player extends AsyncTask<String, Void, Boolean> {
    private ProgressDialog progress;

    @Override
    protected Boolean doInBackground(String... params) {
      // TODO Auto-generated method stub
      Boolean prepared;
      try {

        mediaPlayer.setDataSource(params[0]);

        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

          @Override
          public void onCompletion(MediaPlayer mp) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            intialStage = true;
            isPlayingButton = false;
            btn.setBackgroundResource(R.drawable.play_radio);

          }
        });
        mediaPlayer.prepare();
        prepared = true;
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        Log.d("IllegarArgument", e.getMessage());
        prepared = false;
        e.printStackTrace();
      } catch (SecurityException e) {
        // TODO Auto-generated catch block
        prepared = false;
        e.printStackTrace();
      } catch (IllegalStateException e) {
        // TODO Auto-generated catch block
        prepared = false;
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        prepared = false;
        e.printStackTrace();
      }
      return prepared;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      if (progress.isShowing()) {
        progress.cancel();
      }
      Log.d("Prepared", "//" + result);
      mediaPlayer.start();

      intialStage = false;
    }

    public Player() {
      progress = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
      // TODO Auto-generated method stub
      super.onPreExecute();
      this.progress.setMessage("Buffering...");
      this.progress.show();

    }
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  public void onDestroy() {
    super.onDestroy();

    if (mediaPlayer != null) {
      mediaPlayer.reset();
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

}
