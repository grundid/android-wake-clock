package de.grundid.wakeclock;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		setContentView(R.layout.wake);
		timeLabel = (TextView)findViewById(R.id.timeLabel);
		timeLabel.setText(df.format(new Date()));
		stopButton = (Button)findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				WakeupActivity.this.finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		aquireWakeLock();
		String wakeId = getIntent().getData().toString();
		addTime = Integer.parseInt(wakeId.substring("wakeId://".length()));
		countdownUpdater = new CountdownUpdater(this, MAX_AWAKE, timeLabel);
		countdownUpdater.execute(new Long(0));

	}

	@Override
	protected void onPause() {
		if (countdownUpdater != null)
			countdownUpdater.cancel(true);
		releaseWakeLock();
		try {
			powerManager.goToSleep(200);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		super.onPause();
	}

	public void finishWithAlarm() {
		long startTime = System.currentTimeMillis();
		AlarmHandler.setWakeUpAlarm(this, startTime + (addTime * 1000), addTime);
		finish();
	}

	private void aquireWakeLock() {
		if (!powerManager.isScreenOn()) {
			wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
					"Timer");
			wakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld())
			wakeLock.release();
	}

}
