package com.radioaypfm.aypfm.fragments;

import java.io.IOException;

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

import com.radioaypfm.aypfm.R;

public class FragmentPlayer extends Fragment {

	public FragmentPlayer() {
	}

	private Button btn;
	private boolean isPlayingButton;
	private MediaPlayer mediaPlayer;
	private AudioManager audioManager;
	private boolean intialStage = true;
	SeekBar volumeCtrl;

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

		// Volume SeekBar
		volumeCtrl = (SeekBar) rootView.findViewById(R.id.volSeekBar);
		audioManager = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		int maxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		volumeCtrl.setMax(maxVolume);
		volumeCtrl.setProgress(curVolume);
		volumeCtrl
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
								progress, 0);
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

		rootView.findViewById(R.id.noSound).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (mediaPlayer.isPlaying()
								&& volumeCtrl.getProgress() != 0) {
							volumeCtrl.setProgress(0);
						}
					}
				});

		rootView.findViewById(R.id.maxSound).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (mediaPlayer.isPlaying()
								&& volumeCtrl.getProgress() != 100) {
							volumeCtrl.setProgress(100);
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