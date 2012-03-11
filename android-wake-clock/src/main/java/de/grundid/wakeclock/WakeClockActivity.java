package de.grundid.wakeclock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class WakeClockActivity extends Activity {

	private EditText wakeEvery;
	private CheckBox checkBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		wakeEvery = (EditText)findViewById(R.id.wakeEvery);
		checkBox = (CheckBox)findViewById(R.id.wakeScreen);
		Button startWakeButton = (Button)findViewById(R.id.startWakeButton);

		startWakeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long startTime = System.currentTimeMillis();
				long addTime = Integer.parseInt(wakeEvery.getText().toString());
				AlarmHandler.setWakeUpAlarm(WakeClockActivity.this, startTime + (addTime * 1000), addTime,
						checkBox.isChecked());
				WakeClockActivity.this.finish();
			}
		});
	}
}
