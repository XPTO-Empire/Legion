package com.xpto.legion.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class Util {
	private static Typeface tfDefaultThin;
	private static Typeface tfDefaultLight;
	private static Typeface tfDefaultRegular;
	private static Typeface tfDefaultBold;

	// Returns/Load the app's default font
	public static Typeface getDefaultFont(Context context, int type) {
		if (tfDefaultThin == null) {
			tfDefaultThin = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Thin.ttf");
			tfDefaultLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Light.ttf");
			tfDefaultRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Regular.ttf");
			tfDefaultBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Bold.ttf");
		}

		switch (type) {
		case 0:
			return tfDefaultThin;
		case 1:
			return tfDefaultLight;
		default:
		case 2:
			return tfDefaultRegular;
		case 3:
			return tfDefaultBold;
		}
	}

	// Set the default font for view and its children
	public static void loadFonts(View view) {
		if (view instanceof TextView) {
			int type = 2;

			if (view.getTag() != null) {
				try {
					if ("t".equals(view.getTag()))
						type = 0;
					else if ("l".equals(view.getTag()))
						type = 1;
					else if ("b".equals(view.getTag()))
						type = 3;
				} catch (Exception ex) {
				}
			}

			// Set font to components
			((TextView) view).setTypeface(getDefaultFont(view.getContext(), type));
		} else if (view instanceof ViewGroup) {
			// Search for children to set font recursively
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
				loadFonts(((ViewGroup) view).getChildAt(i));
		}
	}

	public static float distance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (float) (dist * meterConversion);
	}

	public static void showKeyboard(View view) {
		if (view == null)
			return;

		((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void hideKeyboard(View view) {
		if (view == null)
			return;

		((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	// Validate if string is email
	public static boolean isEmail(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	@SuppressLint("SimpleDateFormat")
	public static Date parseJSONDate(String jsonDate) {
		try {
			jsonDate = jsonDate.trim();

			if (jsonDate.contains("(")) {
				String timeString = jsonDate.substring(jsonDate.indexOf("(") + 1, jsonDate.indexOf(")"));
				String[] timeSegments;
				int timeZoneOffSet;
				if (timeString.contains("+")) {
					timeSegments = timeString.split("\\+");
					timeZoneOffSet = Integer.valueOf(timeSegments[1]) * 36000; // (("0100" / 100) * 3600 * 1000)
				} else {
					timeSegments = timeString.split("\\-");
					timeZoneOffSet = Integer.valueOf(timeSegments[1]) * -36000; // (("0100" / 100) * 3600 * 1000)
				}
				long millis = Long.valueOf(timeSegments[0]);
				return new Date(millis + timeZoneOffSet);
			} else {
				if (jsonDate.indexOf(' ') - jsonDate.lastIndexOf('/') == 3)
					return new SimpleDateFormat("dd/MM/yy HH:mm").parse(jsonDate);
				else
					return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(jsonDate);
			}
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String formatToLongDateTime(Date value) {
		return new SimpleDateFormat("dd/MM-HH:mm", Locale.getDefault()).format(value);
	}

	public static float convertDpToPx(Context context, int dp) {
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
	}

	public static float convertPxToDp(Context context, float px) {
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics());
	}

	public static void vibrate(Context context, long milisegundos) {
		Vibrator rr = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		rr.vibrate(milisegundos);
	}

	public static void openPlayStore(Activity activity) {
		final String appPackageName = activity.getPackageName();
		try {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (Exception e) {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}
}
