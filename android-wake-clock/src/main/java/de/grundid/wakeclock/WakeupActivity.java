package de.grundid.wakeclock;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class WakeupActivity extends Activity {

	private static final int MAX_AWAKE = 5;
	private TextView timeLabel;
	private CountdownUpdater countdownUpdater;
	private DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
	private int addTime;
	private PowerManager.WakeLock wakeLock;
	private PowerManager powerManager;
	private Button stopButton;
	private Vibrator vibrator;
	private boolean finishWithAlarm = true;
	private boolean alarmSet = false;
	private boolean wakeScreen = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAddTime();
		powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeScreen = getIntent().getBooleanExtra("WAKE_SCREEN", true);

		if (wakeScreen) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

			setContentView(R.layout.wake);
			timeLabel = (TextView)findViewById(R.id.timeLabel);
			timeLabel.setText(df.format(new Date()));
			stopButton = (Button)findViewById(R.id.stopButton);
			wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
					"WakeClock");

			stopButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					WakeupActivity.this.finishWithAlarm = false;
					WakeupActivity.this.finish();
				}
			});
		}
		else {
			vibrate();
			finishWithAlarm();
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		finishWithAlarm = true;
		alarmSet = false;
		initAddTime();
		vibrate();
		aquireWakeLock();
		startCountdownUpdater();
	}

	private void startCountdownUpdater() {
		countdownUpdater = new CountdownUpdater(this, MAX_AWAKE, timeLabel);
		countdownUpdater.execute(new Long(0));
	}

	private void initAddTime() {
		String wakeId = getIntent().getData().toString();
		addTime = Integer.parseInt(wakeId.substring("wakeId://".length()));
	}

	private void vibrate() {
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(100);
	}

	@Override
	protected void onPause() {
		finishCountdownUpdater();
		if (finishWithAlarm)
			finishWithAlarm();
		super.onPause();
	}

	private void finishCountdownUpdater() {
		if (countdownUpdater != null) {
			countdownUpdater.cancel(true);
			countdownUpdater = null;
		}
	}

	private synchronized void finishWithAlarm() {
		if (!alarmSet) {
			alarmSet = true;
			long startTime = System.currentTimeMillis();
			AlarmHandler.setWakeUpAlarm(this, startTime + (addTime * 1000), addTime, wakeScreen);
		}
	}

	private void aquireWakeLock() {
		wakeLock.acquire((MAX_AWAKE + 1) * 1000);
	}
}
