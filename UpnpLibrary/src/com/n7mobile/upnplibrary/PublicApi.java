package com.n7mobile.upnplibrary;

import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class PublicApi extends BroadcastReceiver {
	public static final String RESPONSE_INFO = "com.n7mobile.upnpplayer.RESPONSE_INFO";
	public static final String RECEIVE_POSITION_CHANGE = "com.n7mobile.upnpplayer.RECEIVE_POSITION_CHANGE";
	public static final String RECEIVE_STATE_CHANGE = "com.n7mobile.upnpplayer.RECEIVE_STATE_CHANGE";
	public static final String RECEIVE_ERROR = "com.n7mobile.upnpplayer.RECEIVE_ERROR";
	public static final String RECEIVE_DEVICE_DISCONNECTED = "com.n7mobile.upnpplayer.RECEIVE_DEVICE_DISCONNECTED";
	public static final String RECEIVE_VOLUME_CHANGED = "com.n7mobile.upnpplayer.RECEIVE_VOLUME_CHANGED";

	public static final String ACTIVITY_SELECT_RENDERER = "com.n7mobile.upnpplayer.ACTIVITY_SELECT_RENDERER";
	public static final String ACTION_SET_RENDERER = "com.n7mobile.upnpplayer.ACTION_SET_RENDERER";
	public static final String ACTION_AV_PLAY = "com.n7mobile.upnpplayer.ACTION_AV_PLAY";
	public static final String ACTION_AV_PAUSE = "com.n7mobile.upnpplayer.ACTION_AV_PAUSE";
	public static final String ACTION_AV_STOP = "com.n7mobile.upnpplayer.ACTION_AV_STOP";
	public static final String ACTION_SET_VOLUME = "com.n7mobile.upnpplayer.ACTION_SET_VOLUME";
	public static final String ACTION_SET_MUTE = "com.n7mobile.upnpplayer.ACTION_SET_MUTE";
	public static final String ACTION_RISE_VOLUME = "com.n7mobile.upnpplayer.ACTION_RISE_VOLUME";
	public static final String ACTION_SEEK = "com.n7mobile.upnpplayer.ACTION_SEEK";
	public static final String ACTION_PLAY_ITEM = "com.n7mobile.upnpplayer.ACTION_PLAY_ITEM";
	public static final String ACTION_REGISTER_APLICATION = "com.n7mobile.upnpplayer.ACTION_REGISTER_APPLICATION";
	public static final String ACTION_UNREGISTER_APLICATION = "com.n7mobile.upnpplayer.ACTION_UNREGISTER_APPLICATION";
	public static final String ACTION_GET_VOLUME = "com.n7mobile.upnpplayer.ACTION_GET_VOLUME";

	public static final String KEY_REQ_CODE = "REQ_CODE";
	public static final String KEY_CLIENT_PACKAGE = "CLIENT_PACKAGE";
	public static final String KEY_DEVICE_UDN = "DEVICE_UDN";
	public static final String KEY_DEVICE_NAME = "DEVICE_NAME";
	public static final String KEY_DEVICE_MANUFACTURER = "DEVICE_MANUFACTURER";
	public static final String KEY_REQ_STATUS_CODE = "REQ_STATUS_CODE";
	public static final String KEY_INFO_DESCRIPTION = "INFO_DESCRIPTION";

	public static final String KEY_POSITION = "POSITION";
	public static final String KEY_MEDIA_DURATION = "MEDIA_DURATION";
	public static final String KEY_STATE_CODE = "STATE_CODE";
	public static final String KEY_ERROR_CODE = "ERROR_CODE";
	public static final String KEY_ERROR_DESCRIPTION = "ERROR_DESCRIPTION";
	public static final String KEY_DISCONNECT_CODE = "DISCONNECT_CODE";
	public static final String KEY_DISCONNECT_DESCRIPTION = "DISCONNECT_DESCRIPTION";

	public static final int STATE_PLAYING = 1;
	public static final int STATE_PAUSED = 4;
	public static final int STATE_PREPARING = 5;
	public static final int STATE_IDLE = 0;
	public static final int STATE_END_OF_DATA = -1;
	public static final int STATE_ERROR = 2;

	// Keys for ACTION_PLAY_MODE
	/** String type, required */
	public static final String KEY_ITEM_PATH = "ITEM_PATH";
	/** String type */
	public static final String KEY_ITEM_TITLE = "ITEM_TITLE";
	/** String type */
	public static final String KEY_ITEM_ALBUM = "ITEM_ALBUM";
	/** String type */
	public static final String KEY_ITEM_CREATOR = "ITEM_CREATOR";
	/** String type */
	public static final String KEY_ITEM_COVERART_PATH = "ITEM_COVERART_PATH";
	/** Long type */
	public static final String KEY_ITEM_DURATION = "ITEM_DURATION";
	/** String type, required */
	public static final String KEY_ITEM_MIMETYPE = "ITEM_MIMETYPE";
	/** Long type */
	public static final String KEY_ITEM_SIZE = "ITEM_SIZE";
	/** Long type */
	public static final String KEY_ITEM_BITRATE = "ITEM_BITRATE";
	/** float type, required */
	public static final String KEY_VOLUME_LVL = "VOLUME_LVL";
	/** long type, required */
	public static final String KEY_SEEK_MILIS = "SEEK_MILIS";
	/** int type, required */
	public static final String KEY_VOLUME = "VOLUME";
	/** show playback notification */
	public static final String KEY_NOTIFICATION_ENABLED = "NOTIFICATION_ENABLED";

	public static final int ERROR_ACTION_NOT_SUPPORTED = 100;
	public static final int ERROR_UNSUPPORTED_FORMAT = 103;
	public static final int ERROR_DEVICE_UNAVAILABLE = 104;
	public static final int ERROR_WRONG_ARGUMENT = 105;
	public static final int ERROR_WRONG_DEVICE_TYPE = 106;
	public static final int ERROR_ARGUMENT_IS_REQUIRED = 107;
	public static final int ERROR_WRONG_FORMAT = 108;
	public static final int ERROR_UNREGISTERED_APPLICATION = 109;

	public static final int INFO_OK = 200;
	public static final int INFO_PAUSE_NOT_HANDLE = 201;
	public static final int INFO_PLAY_NOT_HANDLE = 202;
	public static final int INFO_STOP_NOT_HANDLE = 203;
	public static final int DEVICE_IS_REGISTRED = 204;

	public PublicApi(Context context) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.n7mobile.simpleupnpplayer", "com.n7mobile.simpleupnpplayer.service.NServiceUpnp"));
		context.startService(intent);

		IntentFilter intentFilter = new IntentFilter(RESPONSE_INFO);
		intentFilter.addAction(RECEIVE_POSITION_CHANGE);
		intentFilter.addAction(RECEIVE_STATE_CHANGE);
		intentFilter.addAction(RECEIVE_DEVICE_DISCONNECTED);
		intentFilter.addAction(RECEIVE_VOLUME_CHANGED);
		context.registerReceiver(this, intentFilter);
	}
	/**
	 * Clean receivers from system
	 * @param context
	 */
	public void destroy(Context context) {
		context.unregisterReceiver(this);
	}
	/**
	 * Play item on remote device. This method work only when application is registered in Toaster Cast otherwise 
	 * callback function return ERROR_UNREGISTERED_APPLICATION
	 * @param context
	 * @param reqId - unique number for recognize remote method invocation.
	 * @param itemPath - URL or local path of media item
	 * @param itemTitle - title of media item
	 * @param itemAlbum - album name 
	 * @param itemCreator - creator of media file
	 * @param itemCoverart - albumart URL or local path
	 * @param duration - music or movie item length [s]
	 * @param size - media item size
	 * @param bitrate - media item bitrate
	 * @param showNotification - if true then show notification with control button
	 * @param callback - callback with method invocation status
	 */
	public void actionPlayItem(Context context, long reqId, String itemPath, String itemTitle, String itemAlbum, String itemCreator,
			String itemCoverart, long duration, long size, long bitrate, boolean showNotification, OnActionCompleteListener callback) {
		Intent intent = new Intent(ACTION_PLAY_ITEM);
		if (itemPath == null || itemPath.length() < 0)
			throw new IllegalArgumentException("Item path can not be null or empty");
		intent.putExtra(KEY_ITEM_PATH, itemPath);
		intent.putExtra(KEY_REQ_CODE, reqId);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (itemTitle != null)
			intent.putExtra(KEY_ITEM_TITLE, itemTitle);
		if (itemAlbum != null)
			intent.putExtra(KEY_ITEM_ALBUM, itemAlbum);
		if (itemCoverart != null)
			intent.putExtra(KEY_ITEM_COVERART_PATH, itemCoverart);
		if (itemCreator != null)
			intent.putExtra(KEY_ITEM_CREATOR, itemCreator);
		intent.putExtra(KEY_ITEM_DURATION, duration);
		intent.putExtra(KEY_NOTIFICATION_ENABLED, showNotification);

		intent.putExtra(KEY_ITEM_SIZE, size);
		intent.putExtra(KEY_ITEM_BITRATE, bitrate);
		if (callback != null)
			listenerMap.put(reqId, callback);
		context.startService(intent);
	}

	/**
	 * Register application in Toaster Cast. Other application registered in Toaster Cast will unregister.
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionRegisterApplication(Context context, long reqCode, OnActionCompleteListener callback) {
		Intent intent = new Intent();
		intent.setAction(ACTION_REGISTER_APLICATION);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		intent.putExtra(KEY_REQ_CODE, reqCode);
		listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Register application in Toaster Cast. Other application registered in Toaster Cast will unregister.
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 */
	public static void actionRegisterApplication(Context context, long reqCode) {
		Intent intent = new Intent();
		intent.setAction(ACTION_REGISTER_APLICATION);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		intent.putExtra(KEY_REQ_CODE, reqCode);
		context.startService(intent);
	}
/**
 * Unregister application from Toaster Cast. Notification disappear and playback will abort.
 * @param context
 * @param reqCode - unique number for recognize remote method invocation.
 * @param callback - callback with method invocation status
 */
	public void actionUnregisterApplication(Context context, long reqCode, OnActionCompleteListener callback) {
		Intent intent = new Intent();
		intent.setAction(ACTION_UNREGISTER_APLICATION);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		intent.putExtra(KEY_REQ_CODE, reqCode);
		listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Unregister application from Toaster Cast. Notification disappear and playback will abort.
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 */
	public static void actionUnregisterApplication(Context context, long reqCode) {
		Intent intent = new Intent();
		intent.setAction(ACTION_UNREGISTER_APLICATION);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		intent.putExtra(KEY_REQ_CODE, reqCode);
		context.startService(intent);
	}
	/**
	 * Method open activity form Toaster Cast application with device selection. To catch selected device info use PublicApi.parseSelectRendererResult(~Intent intent) method in onActivityResult method.
	 * @param activity
	 * @param reqCode
	 */
	public void selectRenderer(Activity activity, int reqCode) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.n7mobile.simpleupnpplayer",
				"com.n7mobile.simpleupnpplayer.simpleplayer.api.ActivityDeviceChooser"));
		intent.setAction("com.n7mobile.upnpplayer.ACTIVITY_SELECT_RENDERER");
		activity.startActivityForResult(intent, reqCode);
	}

	/**
	 * Set renderer without showing device selection dialog. 
	 * @param context
	 * @param udn - unique identifier of Device
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionSetRenderer(Context context, long reqCode, String udn, OnActionCompleteListener callback) {
		Intent intent = new Intent(ACTION_SET_RENDERER);
		if (udn == null || udn.length() < 0)
			throw new IllegalArgumentException("Item path can not be null or empty");
		intent.putExtra(KEY_DEVICE_UDN, udn);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Request Toaster Cast to send com.n7mobile.upnpplayer.RECEIVE_VOLUME_CHANGED broadcast.  
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionGetVolume(Context context, long reqCode, OnActionCompleteListener callback) {
		Intent intent = new Intent(ACTION_GET_VOLUME);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		intent.putExtra(KEY_REQ_CODE, reqCode);
		listenerMap.put(reqCode, callback);
		context.startService(intent);
	}

	/**
	 * Resume current playback.
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionPlay(Context context, long reqCode, OnActionCompleteListener callback) {
		Intent intent = new Intent(ACTION_AV_PLAY);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Pause current playback
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionPause(Context context, long reqCode, OnActionCompleteListener callback) {
		Intent intent = new Intent(ACTION_AV_PAUSE);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Stop current playback
	 * @param context
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionStop(Context context, long reqCode, OnActionCompleteListener callback) {
		Intent intent = new Intent(ACTION_AV_STOP);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}

	/**
	 * Set current volume
	 * @param context
	 * @param volume - playback volume, range 0-100
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionSetVolume(Context context, long reqCode, int volume, OnActionCompleteListener callback) {
		if (volume < 0 || volume > 100)
			throw new IllegalArgumentException("Value range must between 0 - 100");

		Intent intent = new Intent(ACTION_SET_VOLUME);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_VOLUME, volume);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Set current volume
	 * @param context
	 * @param volume - playback volume, range 0-100
	 * @param reqCode - unique number for recognize remote method invocation.
	 */
	public static void actionSetVolume(Context context, long reqCode, int volume) {
		if (volume < 0 || volume > 100)
			throw new IllegalArgumentException("Value range must between 0 - 100. Got: " + volume);

		Intent intent = new Intent(ACTION_SET_VOLUME);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_VOLUME, volume);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		context.startService(intent);
	}
	/**
	 * Adjust renderer volume in terms of max volume level. For example 0.1 will turn volume up by 10% maximum volume.
	 * @param context
	 * @param volume - volume rise level -1.0 - 1.0
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionRiseVolume(Context context, long reqCode, float volume, OnActionCompleteListener callback) {
		if (volume < -1.0 || volume > 1.0)
			throw new IllegalArgumentException("Value range must between -1.0 - 1.0. Got: " + volume);

		Intent intent = new Intent(ACTION_RISE_VOLUME);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_VOLUME_LVL, volume);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}
	/**
	 * Adjust renderer volume in terms of max volume level. For example 0.1 will turn volume up by 10% maximum volume.
	 * @param context
	 * @param volume - volume rise level -1.0 - 1.0
	 * @param reqCode - unique number for recognize remote method invocation.
	 */
	public static void actionRiseVolume(Context context, long reqCode, float volume) {
		if (volume < -1.0 || volume > 1.0)
			throw new IllegalArgumentException("Value range must between -1.0 - 1.0. Got: " + volume);

		Intent intent = new Intent(ACTION_RISE_VOLUME);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_VOLUME_LVL, volume);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		context.startService(intent);
	}

	/**
	 * Sets the playback position in currently playing file.
	 * @param context
	 * @param seekMilis - position in millisecond
	 * @param reqCode - unique number for recognize remote method invocation.
	 * @param callback - callback with method invocation status
	 */
	public void actionSeek(Context context, long reqCode, long seekMilis, OnActionCompleteListener callback) {
		if (seekMilis < 0)
			throw new IllegalArgumentException("Seek value must greater than 0");

		Intent intent = new Intent(ACTION_SEEK);
		intent.putExtra(KEY_REQ_CODE, reqCode);
		intent.putExtra(KEY_SEEK_MILIS, seekMilis);
		intent.putExtra(KEY_CLIENT_PACKAGE, context.getPackageName());
		listenerMap.put(reqCode, callback);
		if (callback != null)
			listenerMap.put(reqCode, callback);
		context.startService(intent);
	}

	/**
	 * Decode intent with device selection result and return object with device metadata. If intent isn't contain device data method return null
	 * @param intent
	 * @return Device metadata object
	 */
	public static DeviceResult parseSelectRendererResult(Intent intent) {
		if (intent != null && intent.getExtras() != null) {
			int reqStatusCode;
			String deviceUdn;
			String deviceName = null;
			String manufacturer = null;
			String infoDescription = null;

			if (intent.getExtras().containsKey(PublicApi.KEY_REQ_STATUS_CODE))
				reqStatusCode = intent.getIntExtra(PublicApi.KEY_REQ_STATUS_CODE, -1);
			else
				return null;

			if (intent.getExtras().containsKey(PublicApi.KEY_DEVICE_NAME))
				deviceName = intent.getStringExtra(PublicApi.KEY_DEVICE_NAME);

			if (intent.getExtras().containsKey(PublicApi.KEY_DEVICE_UDN))
				deviceUdn = intent.getStringExtra(PublicApi.KEY_DEVICE_UDN);
			else
				return null;

			if (intent.getExtras().containsKey(PublicApi.KEY_DEVICE_MANUFACTURER))
				manufacturer = intent.getStringExtra(PublicApi.KEY_DEVICE_MANUFACTURER);

			if (intent.getExtras().containsKey(PublicApi.KEY_INFO_DESCRIPTION))
				infoDescription = intent.getStringExtra(PublicApi.KEY_INFO_DESCRIPTION);

			DeviceResult devres = new DeviceResult();
			devres.deviceName = deviceName;
			devres.UDN = deviceUdn;
			devres.reqStatusCode = reqStatusCode;
			devres.manufacturer = manufacturer;
			devres.infoDescription = infoDescription;
			return devres;
		} else
			return null;
	}

	// Broadcast callback
	PlaybackListener mPlaybackListener;

	/**
	 * Set listener to get playback, volume and state changed infomation.
	 * @param listener
	 */
	public void setPlaybackListener(PlaybackListener listener) {
		mPlaybackListener = listener;
	}

	HashMap<Long, OnActionCompleteListener> listenerMap = new HashMap<Long, PublicApi.OnActionCompleteListener>();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(RESPONSE_INFO)) {
			Long reqCode = intent.getLongExtra(KEY_REQ_CODE, -1);
			int statusCode = intent.getIntExtra(KEY_REQ_STATUS_CODE, -1);
			String description = intent.getStringExtra(KEY_INFO_DESCRIPTION);
			OnActionCompleteListener listener = listenerMap.remove(reqCode);
			if (listener != null) {
				listener.onActionComplete(reqCode, statusCode, description);
			}
		} else if (intent.getAction().equalsIgnoreCase(RECEIVE_POSITION_CHANGE)) {
			long position = intent.getLongExtra(KEY_POSITION, 0);
			long duration = intent.getLongExtra(KEY_MEDIA_DURATION, 0);

			if (mPlaybackListener != null) {
				mPlaybackListener.onPositionChanged(position, duration);
			}
		} else if (intent.getAction().equalsIgnoreCase(RECEIVE_STATE_CHANGE)) {
			int state = intent.getIntExtra(KEY_STATE_CODE, -1);

			if (mPlaybackListener != null) {
				mPlaybackListener.onStateChanged(state);
			}
		} else if (intent.getAction().equalsIgnoreCase(RECEIVE_DEVICE_DISCONNECTED)) {
			int disconnectedCode = intent.getIntExtra(KEY_DISCONNECT_CODE, -1);
			String description = intent.getStringExtra(KEY_DISCONNECT_DESCRIPTION);
			String disconnectedPackage = intent.getStringExtra(KEY_CLIENT_PACKAGE);

			if (mPlaybackListener != null && context.getPackageName().equals(disconnectedPackage)) {
				mPlaybackListener.onPlayerDisconnected(disconnectedCode, description);
			}
		} else if (intent.getAction().equalsIgnoreCase(RECEIVE_VOLUME_CHANGED)) {
			Long reqCode = intent.getLongExtra(KEY_REQ_CODE, -1);
			int volume = intent.getIntExtra(KEY_VOLUME, 0);

			if (mPlaybackListener != null) {
				mPlaybackListener.onVolumeChanged(reqCode, volume);
			}
		}
	}

	public static interface OnActionCompleteListener {
		public void onActionComplete(long reqCode, int statusCode, String description);
	}

	public static class DeviceResult {
		public String deviceName;
		public String UDN;
		public String manufacturer;
		public String infoDescription;
		public int reqStatusCode;
	}


	public static interface PlaybackListener {
		public void onPositionChanged(long position, long mediaDuration);

		public void onStateChanged(int stateChanged);

		public void onPlayerDisconnected(int disconnectedCode, String description);

		public void onVolumeChanged(Long reqCode, int value);
	}
}