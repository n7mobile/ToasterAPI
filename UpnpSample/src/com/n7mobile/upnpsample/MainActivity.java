package com.n7mobile.upnpsample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.n7mobile.upnplibrary.PublicApi;
import com.n7mobile.upnplibrary.PublicApi.DeviceResult;
import com.n7mobile.upnplibrary.PublicApi.OnActionCompleteListener;

public class MainActivity extends Activity {

	PublicApi mApi;
	DeviceResult mDevice;
	private ImageButton btnPlay;
	private SeekBar seekBarPosition;

	private boolean mIsCurrentlyPlaying = false;
	private boolean mIsTrackChosen = false;

	private final static int SELECT_RENDERER_REQ = 783;
	private final static int REGISTER_APP_REQ = 784;
	private final static int UNREGISTER_APP_REQ = 785;
	private final static int PLAY_ITEM_REQ = 786;
	private final static int PAUSE_REQ = 787;
	private final static int PLAY_REQ = 788;
	private final static int SEEK_REQ = 789;
	private final static int STOP_REQ = 790;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.lv);
		btnPlay = (ImageButton) findViewById(R.id.btn_play);
		seekBarPosition = (SeekBar) findViewById(R.id.seekBarPosition);
		seekBarPosition.setMax(0);
		Button btnChooseRenderer = (Button) findViewById(R.id.btn_choose_renderer);

		mApi = new PublicApi(this);
		mApi.actionRegisterApplication(this, REGISTER_APP_REQ, new OnActionCompleteListener() {

			@Override
			public void onActionComplete(long reqCode, int statusCode, String description) {
				Toast.makeText(MainActivity.this, description + " | code: " + statusCode, Toast.LENGTH_SHORT).show();
			}
		});

		mApi.setPlaybackListener(new PublicApi.PlaybackListener() {

			@Override
			public void onVolumeChanged(Long reqCode, int value) {

			}

			@Override
			public void onStateChanged(int stateChanged) {
				switch (stateChanged) {
				case PublicApi.STATE_PAUSED:
					setButtonPlaying(false);
					break;
				case PublicApi.STATE_PLAYING:
					setButtonPlaying(true);
					break;
				default:
					resetUItoDefault();
				}

			}

			@Override
			public void onPositionChanged(long position, long mediaDuration) {
				seekBarPosition.setMax((int) mediaDuration);
				seekBarPosition.setProgress((int) position);
				setButtonPlaying(true);

			}

			@Override
			public void onPlayerDisconnected(int disconnectedCode, String description) {
				resetUItoDefault();
				mDevice = null;

			}
		});

		String order = MediaStore.Audio.Media.YEAR + " ASC Limit 25";
		String[] projection = new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION };
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		Cursor cur = getContentResolver().query(uri, projection, null, null, order);
		ArrayList<Track> trackList = new ArrayList<Track>();
		try {
			if (cur != null && cur.moveToFirst()) {
				int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
				int pathColumn = cur.getColumnIndex(MediaStore.Audio.Media.DATA);
				int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
				do {
					Track temp = new Track();
					temp.title = cur.getString(titleColumn);
					temp.filepath = cur.getString(pathColumn);
					temp.duration = cur.getLong(durationColumn);
					trackList.add(temp);
				} while (cur.moveToNext());
			}

		} finally {
			cur.close();
		}

		TracksAdapter adapter = new TracksAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, trackList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Track track = (Track) parent.getAdapter().getItem(position);
				if (mDevice != null) {
					mApi.actionPlayItem((Context) MainActivity.this, PLAY_ITEM_REQ, track.filepath, track.title, null, null, null,
							track.duration, 0l, 0l, false, new OnActionCompleteListener() {

								@Override
								public void onActionComplete(long reqCode, int statusCode, String description) {
									if (statusCode == PublicApi.INFO_OK) {
										setButtonPlaying(true);
										mIsTrackChosen = true;
									}
								}

							});
					seekBarPosition.setProgress(0);
				} else {
					Toast.makeText(MainActivity.this, "Choose a device", Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		btnChooseRenderer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mApi.selectRenderer(MainActivity.this, SELECT_RENDERER_REQ);
			}
		});

		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDevice == null) {
					Toast.makeText(MainActivity.this, "Choose a device", Toast.LENGTH_SHORT).show();
					return;
				}

				if (mIsCurrentlyPlaying) {
					mApi.actionPause(MainActivity.this, PAUSE_REQ, new OnActionCompleteListener() {

						@Override
						public void onActionComplete(long reqCode, int statusCode, String description) {
							if (statusCode == PublicApi.INFO_OK) {
								setButtonPlaying(false);
							}

						}
					});
				} else {
					if(mIsTrackChosen){
						mApi.actionPlay(MainActivity.this, PLAY_REQ, new OnActionCompleteListener() {

							@Override
							public void onActionComplete(long reqCode, int statusCode, String description) {
								if (statusCode == PublicApi.INFO_OK) {
									setButtonPlaying(true);
								} else if (statusCode == PublicApi.INFO_PLAY_NOT_HANDLE)
									Toast.makeText(MainActivity.this, "Choose a device", Toast.LENGTH_SHORT).show();

							}
						});
					} else {
						Toast.makeText(MainActivity.this, "Choose a track", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});

		seekBarPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					mApi.actionSeek(MainActivity.this, SEEK_REQ, progress, new OnActionCompleteListener() {

						@Override
						public void onActionComplete(long reqCode, int statusCode, String description) {

						}
					});
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mDevice = PublicApi.parseSelectRendererResult(data);
		if (mDevice != null) {
			Toast.makeText(this, "Device set to: " + mDevice.deviceName, Toast.LENGTH_SHORT).show();
			resetUItoDefault();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		mApi.actionStop(this, STOP_REQ, null);
		PublicApi.actionUnregisterApplication(this, UNREGISTER_APP_REQ);
		mApi.destroy(this);
		super.onDestroy();
	}

	private void setButtonPlaying(boolean isPlaying) {
		if (btnPlay != null) {
			if (isPlaying) {
				btnPlay.setImageResource(R.drawable.ic_pause);
				mIsCurrentlyPlaying = true;
			} else {
				btnPlay.setImageResource(R.drawable.ic_play);
				mIsCurrentlyPlaying = false;
			}
		}

	}

	private void resetUItoDefault() {
		mIsTrackChosen = false;
		seekBarPosition.setProgress(0);
		seekBarPosition.setMax(0);
		setButtonPlaying(false);
	}

	public class Track {
		public String title;
		public String filepath;
		public long duration;
	}

}
