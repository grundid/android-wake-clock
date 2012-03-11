package de.grundid.wakeclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmHandler {

	public static final String WAKE_SCREEN = "WAKE_SCREEN";

	public static void setWakeUpAlarm(Context context, long targetTime, long currentTimerId, boolean wakeScreen) {
		PendingIntent pendingAlarmIntent = createPendingIntentForAlarmManager(context, currentTimerId, wakeScreen);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetTime, pendingAlarmIntent);
	}

	public static PendingIntent createPendingIntentForAlarmManager(Context context, long currentTimerId,
			boolean wakeScreen) {
		Intent alarmIntent = createAlarmIntent(currentTimerId, wakeScreen);
		PendingIntent pendingAlarmIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0);
		return pendingAlarmIntent;
	}

	public static Intent createAlarmIntent(long currentTimerId, boolean wakeScreen) {
		Intent alarmIntent = new Intent(Intent.ACTION_VIEW);
		alarmIntent.setData(Uri.parse("wakeId://" + currentTimerId));
		alarmIntent.putExtra(WAKE_SCREEN, wakeScreen);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
		return alarmIntent;
	}

}
