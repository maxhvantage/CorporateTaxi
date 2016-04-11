package com.corporatetaxi.gcmclasses;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.util.Log;
import com.google.android.gcm.GCMRegistrar;
import com.corporatetaxi.R;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.corporatetaxi.beans.Allbeans;

/**
 * Created by Eyon on 11/23/2015.
 */
public class Controller extends Application {

	private final int MAX_ATTEMPTS = 5;
	private final int BACKOFF_MILLI_SECONDS = 2000;
	private final Random random = new Random();
	static Context context;

	private ArrayList<Allbeans> UserDataArr = new ArrayList<Allbeans>();

	@Override
	public void onCreate() {
		super.onCreate();

		// // UNIVERSAL IMAGE LOADER SETUP
//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//				.cacheOnDisc(true).cacheInMemory(true)
//				.imageScaleType(ImageScaleType.EXACTLY)
//				.displayer(new FadeInBitmapDisplayer(300)).build();
//
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				getApplicationContext())
//				.defaultDisplayImageOptions(defaultOptions)
//				.memoryCache(new WeakMemoryCache())
//				.discCacheSize(100 * 1024 * 1024).build();
//
//		ImageLoader.getInstance().init(config);
		// // END - UNIVERSAL IMAGE LOADER SETUP

		// ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
	}

	// Register this account with the server.
	public void register(final Context context, String name, String email,
			final String regId, final String IMEI) {
		this.context = context;
		Log.i(Config.TAG, "registering device (regId = " + regId + ")");

		// Server url to post gcm registration data
		String serverUrl = Config.YOUR_SERVER_URL + "register.php";

		Map<String, String> params = new HashMap<String, String>();
//		params.put("regId", regId);
//		params.put("custmerid", String.valueOf(AppPreferences
//				.getCustomerId(getApplicationContext())));
//		params.put("name", name);
//		params.put("email", email);
//		params.put("imei", IMEI);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

		for (int i = 1; i <= MAX_ATTEMPTS; i++) {

			Log.d(Config.TAG, "Attempt #" + i + " to register");

			try {
				// Send Broadcast to Show message on screen
				displayRegistrationMessageOnScreen(context, context.getString(
						R.string.server_registering, i, MAX_ATTEMPTS));

				// Post registration values to web server
				post(serverUrl, params);

				GCMRegistrar.setRegisteredOnServer(context, true);

				// Send Broadcast to Show message on screen
				String message = context.getString(R.string.server_registering);
				displayRegistrationMessageOnScreen(context, message);



				return;
			} catch (IOException e) {

				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).

				Log.e(Config.TAG, "Failed to register on attempt " + i + ":"
						+ e);

				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {

					Log.d(Config.TAG, "Sleeping for " + backoff
							+ " ms before retry");
					Thread.sleep(backoff);

				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(Config.TAG,
							"Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}

				// increase backoff exponentially
				backoff *= 2;
			}
		}

		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);

		// Send Broadcast to Show message on screen
		displayRegistrationMessageOnScreen(context, message);
	}

	// Unregister this account/device pair within the server.
	public void unregister(final Context context, final String regId,
			final String IMEI) {

		Log.i(Config.TAG, "unregisteringdevice(regId = " + regId + ")");

		String serverUrl = Config.YOUR_SERVER_URL + "unregister.php";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("imei", IMEI);

		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);

			String message = context.getString(R.string.server_unregistered);
			displayRegistrationMessageOnScreen(context, message);
		} catch (IOException e) {

			// At this point the device is unregistered from GCM, but still
			// registered in the our server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.

			String message = context.getString(R.string.server_register_error,
					e.getMessage());
			Log.i("GCM K", message);

			displayRegistrationMessageOnScreen(context, message);
		}
	}

	// Issue a POST request to the server.
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {

		URL url;
		try {

			url = new URL(endpoint);

		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = params.entrySet()
				.iterator();

		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Map.Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}

		String body = bodyBuilder.toString();

		Log.v(Config.TAG, "Posting '" + body + "' to " + url);

		byte[] bytes = body.getBytes();

		HttpURLConnection conn = null;
		try {

			Log.e("URL", "> " + url);

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();

			// handle the response
			int status = conn.getResponseCode();

			if (status == 200) {

				// AppPreferences.setEmei(context, params.get("imei"));
			}

			Log.d("status", status + " ?");
			// If response is not success
			if (status != 200) {

				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	// Checking for all possible internet providers
	public boolean isConnectingToInternet() {

		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	// Notifies UI to display a message.
	public void displayRegistrationMessageOnScreen(Context context,
			String message) {

		Intent intent = new Intent(Config.DISPLAY_REGISTRATION_MESSAGE_ACTION);
		intent.putExtra(Config.EXTRA_MESSAGE, message);

		// Send Broadcast to Broadcast receiver with message
		context.sendBroadcast(intent);

	}


	private PowerManager.WakeLock wakeLock;

	public void acquireWakeLock(Context context) {
		if (wakeLock != null)
			wakeLock.release();

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");

		wakeLock.acquire();
	}

	public void releaseWakeLock() {
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;
	}

	// Get UserData model object from UserDataArrlist at specified position
//	public ChatBean getUserData(int pPosition) {
//
//		return UserDataArr.get(pPosition);
//	}
//
//	// Add UserData model object to UserDataArrlist
//	public void setUserData(ChatBean Products) {
//
//		UserDataArr.add(Products);
//
//	}

	// Get Number of UserData model object contains by UserDataArrlist
	public int getUserDataSize() {

		return UserDataArr.size();
	}

	// Clear all user data from arraylist
	public void clearUserData() {

		UserDataArr.clear();
	}
}