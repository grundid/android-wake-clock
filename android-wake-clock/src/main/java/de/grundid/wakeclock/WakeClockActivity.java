package de.grundid.wakeclock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WakeClockActivity extends Activity {

	private EditText wakeEvery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		wakeEvery = (EditText)findViewById(R.id.wakeEvery);
		Button startWakeButton = (Button)findViewById(R.id.startWakeButton);

		startWakeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long startTime = System.currentTimeMillis();
				long addTime = Integer.parseInt(wakeEvery.getText().toString());
				AlarmHandler.setWakeUpAlarm(WakeClockActivity.this, startTime + (addTime * 1000), addTime);
				Toast.makeText(WakeClockActivity.this, "Wake clock activated", Toast.LENGTH_LONG);
				WakeClockActivity.this.finish();
			}
		});
	}
}
