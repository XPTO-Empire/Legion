package com.xpto.legion;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.xpto.legion.adapters.AdpAllTypes;
import com.xpto.legion.data.Caller;
import com.xpto.legion.models.Place;
import com.xpto.legion.models.Subject;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LEditText;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Like;

public class FrgEvent extends LFragment {
	private Place place;
	private long lastListUpdate;

	private AdpAllTypes adpSubjects;
	private ListView lst;
	private int marginBottom;

	private LEditText txt;
	private Button btn;

	private View layRefresh;

	private Subject sendingSubject;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_list, null);

		lst = (ListView) view.findViewById(R.id.lst);
		adpSubjects = new AdpAllTypes((LActivity) getActivity(), onClickEventLike, onClickEventDislike, onClickSubjectLike, onClickSubjectDislike, true);
		adpSubjects.addItem(place);
		lst.setAdapter(adpSubjects);
		lst.setOnItemClickListener(onItemClick);

		marginBottom = ((FrameLayout.LayoutParams) lst.getLayoutParams()).bottomMargin;

		txt = (LEditText) view.findViewById(R.id.txt);
		txt.setOnFocusChange(onFocusChange);

		int maxLength = 255;
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		txt.setFilters(fArray);

		btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(onClickSend);

		layRefresh = view.findViewById(R.id.layRefresh);
		layRefresh.setOnClickListener(onClickRefresh);

		return view;
	}

	@Override
	public Animation getInAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_down_in);
	}

	@Override
	public Animation getOutAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_up_out);
	}

	@Override
	public boolean canBack() {
		((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_EVENT);
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();

		updateList();
	}

	private void updateList() {
		if (place != null && getActivity() != null && System.currentTimeMillis() - lastListUpdate > 5000) {
			lastListUpdate = System.currentTimeMillis();

			if (getActivity() != null)
				Caller.getSubjects(getActivity(), getSubjectsSuccess, null, getSubjectsFail, place.getId());

			if (place.getName() == null || place.getName().length() == 0)
				Caller.getPlace(getActivity(), getPlaceSuccess, null, null, place.getId());
		}
	}

	private LCallback getSubjectsSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					// Convert json to objects
					ArrayList<Subject> subjects = new ArrayList<Subject>();
					JSONArray jarray = json.getJSONArray("Content");
					for (int i = 0; i < jarray.length(); i++) {
						Subject subject = new Subject();
						if (subject.loadFromJSon(jarray.getJSONObject(i)))
							subjects.add(subject);
					}

					// Add objects to adapter
					for (int i = 0; i < subjects.size(); i++)
						adpSubjects.addItem(subjects.get(i));
					adpSubjects.notifyDataSetChanged();

					// Update list after 15 sec.s
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							updateList();
						}
					}, 15000);
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	private LCallback getSubjectsFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_down_in);
			layRefresh.setVisibility(View.VISIBLE);
			layRefresh.startAnimation(cameIn);
		}
	};

	private LCallback getPlaceSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					place.loadFromJSon(json.getJSONObject("Content"));
					adpSubjects.notifyDataSetChanged();
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	public void setEvent(Place _place) {
		place = _place;

		if (adpSubjects != null && adpSubjects.getCount() == 0)
			adpSubjects.addItem(place);

		updateList();
	}

	private OnClickListener onClickEventLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_EVENT, true);
		}
	};

	private OnClickListener onClickEventDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_EVENT, false);
		}
	};

	private OnClickListener onClickSubjectLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_SUBJECT, true);
		}
	};

	private OnClickListener onClickSubjectDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_SUBJECT, false);
		}
	};

	private LEditText.OnFocusChange onFocusChange = new LEditText.OnFocusChange() {
		@Override
		public void onFocusChange(boolean _hasFocus) {
			if (_hasFocus) {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) lst.getLayoutParams();
				lp.setMargins(0, 0, 0, marginBottom * 2);
				lst.setLayoutParams(lp);

				lp = (FrameLayout.LayoutParams) ((FrameLayout) txt.getParent()).getLayoutParams();
				lp.height = marginBottom * 2;
				((FrameLayout) txt.getParent()).setLayoutParams(lp);
			} else {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) lst.getLayoutParams();
				lp.setMargins(0, 0, 0, marginBottom);
				lst.setLayoutParams(lp);

				lp = (FrameLayout.LayoutParams) ((FrameLayout) txt.getParent()).getLayoutParams();
				lp.height = marginBottom;
				((FrameLayout) txt.getParent()).setLayoutParams(lp);

				lst.requestFocus();
			}
		}
	};

	private OnClickListener onClickRefresh = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Animation cameOut = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_up_out);
			layRefresh.startAnimation(cameOut);
			layRefresh.setVisibility(View.GONE);

			lastListUpdate = 0;
			updateList();
		}
	};

	private OnClickListener onClickSend = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (getGlobal().getLogged() == null) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else {
				String content = txt.getText().toString();
				if (content.length() < 4) {
					LDialog.openDialog((LActivity) getActivity(), R.string.f_event_fill_name_title, R.string.f_event_fill_name_subtitle, R.string.f_ok, false);
					return;
				}

				sendingSubject = new Subject();
				sendingSubject.setContent(content);
				adpSubjects.addItem(sendingSubject, 1);

				subjectRetry.finished(null);
				Caller.newSubject(getActivity(), subjectSuccess, subjectRetry, subjectFail, getGlobal().getLogged().getId(), place.getId(), content);
			}
		}
	};

	private LCallback subjectSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).endLoading();

			txt.setEnabled(true);
			btn.setEnabled(true);

			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					txt.setText(null);

					sendingSubject.setId(json.getLong("Content"));
					sendingSubject = null;

					updateList();
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	private LCallback subjectRetry = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).startLoading(R.string.f_sending);

			txt.setEnabled(false);
			btn.setEnabled(false);
			lst.requestFocus();
		}
	};

	private LCallback subjectFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			LDialog.openDialog((LActivity) getActivity(), R.string.f_no_connection, R.string.f_event_fill_cant_send, R.string.f_ok, false);
		}
	};

	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {
			if (_position > 0) {
				FrgSubject frgSubject = new FrgSubject();
				frgSubject.setSubject((Subject) adpSubjects.getItem(_position));
				((ActMain) getActivity()).setFragment(frgSubject, ActMain.LEVEL_SUBJECT);
			}
		}
	};
}
