package com.tuyou.tsd.voice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuyou.tsd.common.TSDEvent;

public class RecognitionFragment extends Fragment {
	private Activity mParentActivity;
	private ImageButton mCloseBtn;
	private TextView mResultView, mStatusView;
	private ImageView mThinkingView;
	private Timer mTimerAnim = null;

	@Override
	public void onAttach(Activity activity) {
		mParentActivity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recog_fragment, container, false);

		mResultView = (TextView) view.findViewById(R.id.recog_result_view);
		mStatusView = (TextView) view.findViewById(R.id.recog_status_view);

		mThinkingView = (ImageView) view.findViewById(R.id.recog_thinking_view);
		mThinkingView.setBackgroundResource(R.drawable.thinking_anim);
		
		TimerTask timeoutTask = new TimerTask() {

			@Override
			public void run() {
				Activity activity = getActivity();
				if(activity != null){
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(mThinkingView != null){
								mThinkingView.setBackgroundResource(R.drawable.thinking_deep_anim);
								AnimationDrawable rocketAnimation = (AnimationDrawable) mThinkingView.getBackground();
								rocketAnimation.start();
							}	
						}
					});
				}
			}
		};

		mTimerAnim = new Timer("TimeoutTask", true);
		mTimerAnim.schedule(timeoutTask, 3000);

		mCloseBtn = (ImageButton) view.findViewById(R.id.recog_close_btn);
		mCloseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mParentActivity.sendBroadcast(new Intent(TSDEvent.Interaction.CANCEL_INTERACTION_BY_TP));
			}
		});
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		mThinkingView.setVisibility(View.VISIBLE);
		AnimationDrawable anim = (AnimationDrawable) mThinkingView.getBackground();
		anim.start();
		super.onResume();
	}

	void setResultText(String text) {
		if (mTimerAnim != null) {
			mTimerAnim.cancel();
			mTimerAnim = null;
		}
		
		if (mThinkingView != null) {
			mThinkingView.setBackgroundResource(R.drawable.thinking_anim);
			AnimationDrawable rocketAnimation = (AnimationDrawable) mThinkingView.getBackground();
			rocketAnimation.start();
			TimerTask timeoutTask = new TimerTask() {

				@Override
				public void run() {
					Activity activity = getActivity();
					if(activity != null){
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(mThinkingView != null){
									mThinkingView.setBackgroundResource(R.drawable.thinking_deep_anim);
									AnimationDrawable rocketAnimation = (AnimationDrawable) mThinkingView.getBackground();
									rocketAnimation.start();
								}	
							}
						});
					}
				}
			};

			mTimerAnim = new Timer("TimeoutTask", true);
			mTimerAnim.schedule(timeoutTask, 3000);
		}
		if (mResultView != null) {
			mResultView.setText(text);
		}

	}

	void setStatusText(String text) {
		if (mStatusView != null) {
			mStatusView.setText(text);
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (mTimerAnim != null) {
			mTimerAnim.cancel();
			mTimerAnim = null;
		}
	}
}
