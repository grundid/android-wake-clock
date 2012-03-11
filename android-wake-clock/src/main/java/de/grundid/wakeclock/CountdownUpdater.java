package de.grundid.wakeclock;

import java.text.DateFormat;
import java.util.Date;

import android.os.AsyncTask;
import android.widget.TextView;

public class CountdownUpdater extends AsyncTask<Long, Long, Void> {

	private DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
	private WakeupActivity activity;
	private int maxRepeats;
	private long currentRun;
	private TextView textViewToUpdate;

	public CountdownUpdater(WakeupActivity activity, int maxRepeats, TextView textViewToUpdate) {
		this.activity = activity;
		this.maxRepeats = maxRepeats;
		this.textViewToUpdate = textViewToUpdate;
	}

	@Override
	protected Void doInBackground(Long... params) {
		while (!isCancelled() && currentRun <= maxRepeats) {
			currentRun++;
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				break;
			}
			publishProgress(currentRun);
		}
		if (!isCancelled())
			activity.finish();
		return null;
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		textViewToUpdate.setText(df.format(new Date()));
	}
}
