package com.radioaypfm.aypfm.fragments;

import java.io.IOException;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.radioaypfm.aypfm.MainActivity;
import com.radioaypfm.aypfm.R;

public class FragmentPlayer extends Fragment {

	public FragmentPlayer() {
	}

	private Button btn;
	private boolean isPlayingButton;
	private MediaPlayer mediaPlayer;

	private boolean intialStage = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_player, container,
				false);

		btn = (Button) rootView.findViewById(R.id.playButton);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!isPlayingButton) {
					btn.setBackgroundResource(R.drawable.pause_radio);// pause
					if (intialStage)
						new Player()
								.execute("http://stric6.streamakaci.com/radioayp.mp3");
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

		return rootView;
	}

	@Override
	public void onResume() {

		super.onResume();
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
			progress = new ProgressDialog(getActivity());
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
		btn.setBackgroundResource(R.drawable.play_radio);// play
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();

		mediaPlayer.reset();
		isPlayingButton = false;
		intialStage = true;

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