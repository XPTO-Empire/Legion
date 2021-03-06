package com.xpto.legion.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.xpto.legion.R;
import com.xpto.legion.data.DB;

public class LActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener,
		LocationListener, com.google.android.gms.location.LocationListener {
	// Global data
	private Global global;

	public Global getGlobal() {
		return this.global;
	}

	public final static int TRANSITION_DOWN = 0;
	public final static int TRANSITION_SIDE = 1;
	public final static int TRANSITION_FADE = 2;

	// Location variables - GPlay services
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private boolean keepTracking = false;
	private LCallback locationCallback;
	private LCallback noLocationCallback;

	// Location variables - GPlay services
	private boolean lcRequested = false;
	private LocationClient locationClient;

	// Location variables - Manager
	private LocationManager locationManager;

	private boolean actionBarVisible;
	private int enterTransition = TRANSITION_DOWN;

	private ArrayList<View> dialogs;

	public ArrayList<View> getDialogs() {
		if (dialogs == null)
			dialogs = new ArrayList<View>();

		return this.dialogs;
	}

	private boolean isActivityVisible;

	public boolean isActivityVisible() {
		return isActivityVisible;
	}

	public LActivity(boolean useActionBar, int startTransition) {
		super();

		this.actionBarVisible = useActionBar;
		this.enterTransition = startTransition;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Start DB
		DB.startDB(getApplicationContext());

		// Control if action bar is visible
		if (!actionBarVisible)
			getSupportActionBar().hide();
		else {
			setActionBarTitle(getString(R.string.app_name));
			getSupportActionBar().setIcon(R.drawable.logo_simple);
			getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bg));
			getSupportActionBar().setDisplayShowCustomEnabled(true);
		}

		// Get global object
		this.global = (Global) getApplication();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		new LAsyncTask(this) {
			@Override
			protected void doInBackground() {
				try {
					Thread.sleep(1);
				} catch (Exception ex) {

				}
			}

			@Override
			protected void onPostExecute() {
				// Set transition animation
				switch (enterTransition) {
				default:
				case TRANSITION_DOWN:
					overridePendingTransition(R.anim.transition_down_in, R.anim.transition_down_out);
					break;
				case TRANSITION_SIDE:
					overridePendingTransition(R.anim.transition_side_in, R.anim.transition_side_out);
					break;
				case TRANSITION_FADE:
					overridePendingTransition(R.anim.transition_fade_in, R.anim.transition_fade_out);
					break;
				}

				// TODO: ...

				// Set default font for the activity
				Util.loadFonts(findViewById(android.R.id.content).getRootView());

				onPostLoad();
			}
		}.start();
	}

	protected void onPostLoad() {
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Restart tracking if it is
		if (this.keepTracking && this.locationCallback != null)
			this.getLocation(this.locationCallback, this.noLocationCallback);
	}

	@Override
	protected void onStop() {
		cancelLocationUpdates();
		if (locationClient != null && locationClient.isConnected())
			locationClient.disconnect();

		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();

		isActivityVisible = true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		isActivityVisible = false;
	}

	public void setActionBarTitle(String _title) {
		getSupportActionBar().setTitle(Html.fromHtml("<font color=\"" + getString(R.string.title_color) + "\">" + _title + "</font>"));
	}

	// Control use of loading overlay
	private View viwLoading;

	public void startLoading(int resText) {
		startLoading(getString(resText));
	}

	public void startLoading(String text) {
		boolean animate = false;

		if (viwLoading == null) {
			viwLoading = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_loading, null);
			Util.loadFonts(viwLoading);

			ViewGroup window = (ViewGroup) findViewById(android.R.id.content).getRootView();
			window.addView(viwLoading, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			animate = true;
		} else
			animate = (viwLoading.getVisibility() != View.VISIBLE);

		((TextView) viwLoading.findViewById(R.id.txtLoading)).setText(text);

		if (animate) {
			Animation moveUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition_down_in);
			moveUp.setDuration(200);
			viwLoading.findViewById(R.id.layLoading).startAnimation(moveUp);
			viwLoading.setVisibility(View.VISIBLE);
		}
	}

	public void endLoading() {
		if (viwLoading != null) {
			boolean animate = (viwLoading.getVisibility() != View.GONE);

			if (animate) {
				Animation moveDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition_up_out);
				moveDown.setDuration(200);
				moveDown.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						viwLoading.setVisibility(View.GONE);
					}
				});
				viwLoading.findViewById(R.id.layLoading).startAnimation(moveDown);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				break;
			}
		}
	}

	public boolean isTracking() {
		return this.keepTracking;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void stopTracking() {
		// Set to stop tracking
		this.keepTracking = false;

		cancelLocationUpdates();
	}

	private LDialog.DialogResult noLocationDialog = new LDialog.DialogResult() {
		@Override
		public void result(int result, String info) {
			switch (result) {
			case LDialog.BUTTON2:
				Intent iLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(iLocation);
				return;

			default:
				if (noLocationCallback != null)
					noLocationCallback.finished(null);
				break;
			}
		}
	};

	private static boolean noGMSDialogNotification = false;

	private LDialog.DialogResult noGMSDialog = new LDialog.DialogResult() {
		@Override
		public void result(int result, String info) {
			switch (result) {
			case LDialog.BUTTON2:
				Intent iGMS = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
				startActivity(iGMS);
				return;
			}
		}
	};

	public void getLocation(LCallback callback, LCallback noCallback, boolean stayTracking) {
		// Set track type
		this.keepTracking = stayTracking;

		// Ask for location
		this.getLocation(callback, noCallback);
	}

	@SuppressWarnings("deprecation")
	public void getLocation(LCallback callback, LCallback noCallback) {
		// Hold callback
		this.locationCallback = callback;
		this.noLocationCallback = noCallback;

		String provs = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provs == null || provs.length() == 0) {
			// No access to location
			LDialog.openDialog(this, R.string.f_no_location, R.string.f_may_enable_location, R.string.f_no, true, R.string.f_yes, false, noLocationDialog);
			return;
		}

		// Start location objects
		if (this.isGooglePlayServicesAvailable()) {
			// Connect to Google service
			if (this.locationClient == null)
				this.locationClient = new LocationClient(this, this, this);
			this.locationClient.connect();
		} else {
			if (!noGMSDialogNotification) {
				noGMSDialogNotification = true;

				LDialog.openDialog(this, R.string.f_to_update, R.string.f_no_gms, R.string.f_no, true, R.string.f_yes, false, noGMSDialog);
			}

			// Ask location to location manager
			this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			Location lPas;
			Location lGPS = null;
			Location lNet = null;

			// Set updates to passive updates
			this.locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 0, this);
			lPas = this.locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

			// Set updates to GPS updates, if enabled
			if (this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
				lGPS = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}

			// Set updates to network updates, if enabled
			if (this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
				lNet = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			Location best = lPas;
			if (lGPS != null) {
				if (best == null || best.getTime() < lGPS.getTime() && best.hasAccuracy() && lGPS.hasAccuracy() && best.getAccuracy() > lGPS.getAccuracy())
					best = lGPS;
			}

			if (lNet != null) {
				if (best == null || best.getTime() < lNet.getTime() && best.hasAccuracy() && lNet.hasAccuracy() && best.getAccuracy() > lNet.getAccuracy())
					best = lNet;
			}

			if (best != null && callback != null)
				callback.finished(best);
		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		return (ConnectionResult.SUCCESS == resultCode);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error has a resolution, try sending an Intent to start a Google Play services
		 * activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else
			this.getLocation(this.locationCallback, this.noLocationCallback);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (this.locationCallback != null && this.locationClient != null && !this.lcRequested) {
			this.locationCallback.finished(this.locationClient.getLastLocation());

			LocationRequest locationRequest = LocationRequest.create();
			// Use high accuracy
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// Set the update interval to 5 seconds
			locationRequest.setInterval(5000);
			// Set the fastest update interval to 1 second
			locationRequest.setFastestInterval(1000);

			this.lcRequested = true;
			this.locationClient.requestLocationUpdates(locationRequest, this);
		}
	}

	@Override
	public void onDisconnected() {
		// Do nothing
	}

	@Override
	public void onLocationChanged(Location location) {
		if (this.locationCallback != null && location != null)
			this.locationCallback.finished(location);

		if (!keepTracking)
			cancelLocationUpdates();
	}

	private void cancelLocationUpdates() {
		if (this.locationClient != null && this.locationClient.isConnected()) {
			this.lcRequested = false;
			this.locationClient.removeLocationUpdates(this);
		}

		if (this.locationManager != null)
			this.locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// Do nothing
	}

	@Override
	public void onProviderEnabled(String provider) {
		// Do nothing
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do nothing
	}

	@Override
	public void finish() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				reallyFinish();
			}
		});
	}

	private void reallyFinish() {
		super.finish();
	}

	@Override
	public void onBackPressed() {
		if (getDialogs().size() > 0) {
			try {
				((LDialog) getDialogs().get(0)).back();
				return;
			} catch (Exception e) {
			}
		}

		super.onBackPressed();

		// Set transition animation
		switch (enterTransition) {
		default:
		case TRANSITION_DOWN:
			overridePendingTransition(R.anim.transition_up_in, R.anim.transition_up_out);
			break;
		case TRANSITION_SIDE:
			overridePendingTransition(R.anim.transition_side_back_in, R.anim.transition_side_back_out);
			break;
		case TRANSITION_FADE:
			overridePendingTransition(R.anim.transition_fade_in, R.anim.transition_fade_out);
			break;
		}
	}
}
