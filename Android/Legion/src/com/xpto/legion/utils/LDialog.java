package com.xpto.legion.utils;

import com.xpto.legion.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LDialog extends FrameLayout {
	public static final int DISMISS = 0;
	public static final int BUTTON1 = 1;
	public static final int BUTTON2 = 2;
	public static final int BUTTON3 = 3;

	private static final int selectedColor = 0xffe0e0e0;
	private static final int secondaryColor = 0xffa0a0a0;

	private LActivity wActivity;

	private View viwBackgroud;
	private View layDialog;

	private EditText txtEdit;

	private TextView txtTitle;
	private TextView txtSubTitle;

	private LinearLayout layTwo;
	private View viwDivider1;
	private Button btn1;
	private View viwDivider2;
	private Button btn2;
	private View viwDivider3;
	private Button btn3;

	private DialogResult dialogResult;

	public LDialog(Context context) {
		super(context);
	}

	public LDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LDialog(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private OnClickListener onClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			back();
			if (v != null) {
				if (v instanceof Button) {
					v.setBackgroundColor(selectedColor);
				}

				if (dialogResult != null && v.getTag() != null) {
					String info = (txtEdit.getVisibility() == View.VISIBLE ? txtEdit.getText().toString().trim() : null);
					dialogResult.result((Integer) v.getTag(), info);
				}
			}

		}
	};

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, null, false, null, false, null, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, DialogResult _dialogResult) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, _dialogResult);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, DialogResult _dialogResult) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, null, false, null, false, _dialogResult, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, null, false, null, false, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, null, false, null, false, null, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, DialogResult _dialogResult) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, _dialogResult);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, DialogResult _dialogResult) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, null, false, null, false, _dialogResult, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, DialogResult _dialogResult,
			boolean askForInfo) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary,
			DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, null, false, null, false, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, int resButton2, boolean isButton2Secondary) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2), isButton2Secondary);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, String strButton2, boolean isButton2Secondary) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, strButton2, isButton2Secondary, null, false, null, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, int resButton2,
			boolean isButton2Secondary) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2),
				isButton2Secondary);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, String strButton2,
			boolean isButton2Secondary) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, strButton2, isButton2Secondary, null, false, null, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, int resButton2, boolean isButton2Secondary,
			DialogResult _dialogResult) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2), isButton2Secondary, _dialogResult);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, String strButton2, boolean isButton2Secondary,
			DialogResult _dialogResult) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, strButton2, isButton2Secondary, null, false, _dialogResult, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, int resButton2, boolean isButton2Secondary,
			DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2), isButton2Secondary, _dialogResult,
				askForInfo);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, String strButton2, boolean isButton2Secondary,
			DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, strButton2, isButton2Secondary, null, false, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, int resButton2,
			boolean isButton2Secondary, DialogResult _dialogResult) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2),
				isButton2Secondary, _dialogResult);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, String strButton2,
			boolean isButton2Secondary, DialogResult _dialogResult) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, strButton2, isButton2Secondary, null, false, _dialogResult, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, int resButton2,
			boolean isButton2Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2),
				isButton2Secondary, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, String strButton2,
			boolean isButton2Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, strButton2, isButton2Secondary, null, false, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, int resButton2, boolean isButton2Secondary,
			int resButton3, boolean isButton3Secondary) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2), isButton2Secondary,
				wa.getString(resButton3), isButton3Secondary);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, String strButton2, boolean isButton2Secondary,
			String strButton3, boolean isButton3Secondary) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, strButton2, isButton2Secondary, strButton3, isButton3Secondary, null, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, int resButton2, boolean isButton2Secondary,
			int resButton3, boolean isButton3Secondary, DialogResult _dialogResult) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2), isButton2Secondary,
				wa.getString(resButton3), isButton3Secondary, _dialogResult);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, String strButton2, boolean isButton2Secondary,
			String strButton3, boolean isButton3Secondary, DialogResult _dialogResult) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, strButton2, isButton2Secondary, strButton3, isButton3Secondary, _dialogResult, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resButton1, boolean isButton1Secondary, int resButton2, boolean isButton2Secondary,
			int resButton3, boolean isButton3Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2), isButton2Secondary,
				wa.getString(resButton3), isButton3Secondary, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, String strTitle, String strButton1, boolean isButton1Secondary, String strButton2, boolean isButton2Secondary,
			String strButton3, boolean isButton3Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, strTitle, null, strButton1, isButton1Secondary, strButton2, isButton2Secondary, strButton3, isButton3Secondary, _dialogResult,
				askForInfo);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, int resButton2,
			boolean isButton2Secondary, int resButton3, boolean isButton3Secondary) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2),
				isButton2Secondary, wa.getString(resButton3), isButton3Secondary);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, String strButton2,
			boolean isButton2Secondary, String strButton3, boolean isButton3Secondary) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, strButton2, isButton2Secondary, strButton3, isButton3Secondary, null, false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, int resButton2,
			boolean isButton2Secondary, int resButton3, boolean isButton3Secondary, DialogResult _dialogResult) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2),
				isButton2Secondary, wa.getString(resButton3), isButton3Secondary, _dialogResult);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, String strButton2,
			boolean isButton2Secondary, String strButton3, boolean isButton3Secondary, DialogResult _dialogResult) {
		openDialog(wa, strTitle, strSubTitle, strButton1, isButton1Secondary, strButton2, isButton2Secondary, strButton3, isButton3Secondary, _dialogResult,
				false);
	}

	public static void openDialog(LActivity wa, int resTitle, int resSubTitle, int resButton1, boolean isButton1Secondary, int resButton2,
			boolean isButton2Secondary, int resButton3, boolean isButton3Secondary, DialogResult _dialogResult, boolean askForInfo) {
		openDialog(wa, wa.getString(resTitle), wa.getString(resSubTitle), wa.getString(resButton1), isButton1Secondary, wa.getString(resButton2),
				isButton2Secondary, wa.getString(resButton3), isButton3Secondary, _dialogResult, askForInfo);
	}

	public static void openDialog(LActivity wa, String strTitle, String strSubTitle, String strButton1, boolean isButton1Secondary, String strButton2,
			boolean isButton2Secondary, String strButton3, boolean isButton3Secondary, DialogResult _dialogResult, boolean askForInfo) {
		if (wa == null)
			return;

		final LDialog thisDialog = (LDialog) LayoutInflater.from(wa).inflate(R.layout.view_dialog, null);

		Util.loadFonts(thisDialog);
		Util.hideKeyboard(thisDialog);

		// Control properties
		thisDialog.wActivity = wa;
		thisDialog.dialogResult = _dialogResult;

		// Load layouts
		thisDialog.viwBackgroud = thisDialog.findViewById(R.id.viwBackgroud);
		thisDialog.viwBackgroud.setTag(DISMISS);
		thisDialog.viwBackgroud.setOnClickListener(thisDialog.onClick);

		thisDialog.layDialog = thisDialog.findViewById(R.id.layDialog);

		// Load and set title
		thisDialog.txtTitle = (TextView) thisDialog.findViewById(R.id.txtTitle);
		thisDialog.txtTitle.setText(strTitle);

		// Load and set sub-title
		thisDialog.txtSubTitle = (TextView) thisDialog.findViewById(R.id.txtSubTitle);
		if (strSubTitle != null && strSubTitle.length() > 0)
			thisDialog.txtSubTitle.setText(strSubTitle);
		else
			thisDialog.txtSubTitle.setVisibility(View.GONE);

		// Load and set textEdit
		thisDialog.txtEdit = (EditText) thisDialog.findViewById(R.id.txtEdit);
		if (!askForInfo)
			thisDialog.txtEdit.setVisibility(View.GONE);

		// Load and set button1
		thisDialog.viwDivider1 = thisDialog.findViewById(R.id.viwDivider1);
		thisDialog.btn1 = (Button) thisDialog.findViewById(R.id.btn1);
		if (strButton1 != null && strButton1.length() != 0) {
			thisDialog.btn1.setText(strButton1);
			thisDialog.btn1.setTag(BUTTON1);
			thisDialog.btn1.setOnClickListener(thisDialog.onClick);
			if (isButton1Secondary)
				thisDialog.btn1.setTextColor(secondaryColor);
		} else {
			thisDialog.viwDivider1.setVisibility(View.GONE);
			thisDialog.btn1.setVisibility(View.GONE);
		}

		// Load and set button2
		thisDialog.viwDivider2 = thisDialog.findViewById(R.id.viwDivider2);
		thisDialog.btn2 = (Button) thisDialog.findViewById(R.id.btn2);
		if (strButton2 != null && strButton2.length() > 0) {
			thisDialog.btn2.setText(strButton2);
			thisDialog.btn2.setTag(BUTTON2);
			thisDialog.btn2.setOnClickListener(thisDialog.onClick);
			if (isButton2Secondary)
				thisDialog.btn2.setTextColor(secondaryColor);
		} else {
			thisDialog.viwDivider2.setVisibility(View.GONE);
			thisDialog.btn2.setVisibility(View.GONE);
		}

		// Load and set button3
		thisDialog.viwDivider3 = thisDialog.findViewById(R.id.viwDivider3);
		thisDialog.btn3 = (Button) thisDialog.findViewById(R.id.btn3);
		if (strButton3 != null && strButton3.length() > 0) {
			thisDialog.btn3.setText(strButton3);
			thisDialog.btn3.setTag(BUTTON3);
			thisDialog.btn3.setOnClickListener(thisDialog.onClick);
			if (isButton3Secondary)
				thisDialog.btn3.setTextColor(secondaryColor);
		} else {
			thisDialog.viwDivider3.setVisibility(View.GONE);
			thisDialog.btn3.setVisibility(View.GONE);
		}

		// Set buttons 1 and 2 to be horizontal
		if (strButton1 != null && strButton1.length() > 0 && strButton2 != null && strButton2.length() > 0 && (strButton3 == null || strButton3.length() == 0)) {
			thisDialog.layTwo = (LinearLayout) thisDialog.findViewById(R.id.layTwo);
			thisDialog.layTwo.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) thisDialog.btn1.getLayoutParams();
			lp1.width = 0;
			lp1.weight = 1;

			thisDialog.btn1.setLayoutParams(lp1);
			thisDialog.btn2.setLayoutParams(lp1);

			LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) thisDialog.viwDivider2.getLayoutParams();
			lp2.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, wa.getResources().getDisplayMetrics());
			lp2.height = LinearLayout.LayoutParams.MATCH_PARENT;
			thisDialog.viwDivider2.setLayoutParams(lp2);

		}

		// Attach to window
		ViewGroup window = (ViewGroup) wa.findViewById(android.R.id.content).getRootView();
		window.addView(thisDialog, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		wa.getDialogs().add(thisDialog);

		// Animate
		Animation fadeIn = AnimationUtils.loadAnimation(wa, R.anim.transition_fade_in);
		thisDialog.viwBackgroud.startAnimation(fadeIn);

		Animation dialogIn = AnimationUtils.loadAnimation(wa, R.anim.transition_dialog_in);
		if (askForInfo && dialogIn != null)
			dialogIn.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					thisDialog.txtEdit.requestFocus();
					Util.showKeyboard(thisDialog.txtEdit);
				}
			});
		thisDialog.layDialog.startAnimation(dialogIn);
	}

	public void back() {
		viwBackgroud.setEnabled(false);
		btn1.setEnabled(false);
		btn2.setEnabled(false);
		btn3.setEnabled(false);

		// Animate
		Animation fadeOut = AnimationUtils.loadAnimation(wActivity, R.anim.transition_fade_out);
		if (fadeOut != null) {
			fadeOut.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							((ViewGroup) LDialog.this.getParent()).removeView(LDialog.this);
							wActivity.getDialogs().remove(LDialog.this);
						}
					});
				}
			});
		}

		viwBackgroud.startAnimation(fadeOut);
		viwBackgroud.setVisibility(View.GONE);

		Animation dialogOut = AnimationUtils.loadAnimation(wActivity, R.anim.transition_dialog_out);
		layDialog.startAnimation(dialogOut);
		layDialog.setVisibility(View.GONE);
	}

	public interface DialogResult {
		public void result(int result, String info);
	}
}
