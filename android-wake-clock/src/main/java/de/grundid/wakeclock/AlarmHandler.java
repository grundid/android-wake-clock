package de.grundid.wakeclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmHandler {

	public static void setWakeUpAlarm(Context context, long targetTime, long currentTimerId) {
		PendingIntent pendingAlarmIntent = createPendingIntentForAlarmManager(context, currentTimerId);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetTime, pendingAlarmIntent);
	}

	public static PendingIntent createPendingIntentForAlarmManager(Context context, long currentTimerId) {
		Intent alarmIntent = createAlarmIntent(currentTimerId);
		PendingIntent pendingAlarmIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0);
		return pendingAlarmIntent;
	}

	public static Intent createAlarmIntent(long currentTimerId) {
		Intent alarmIntent = new Intent(Intent.ACTION_VIEW);
		alarmIntent.setData(Uri.parse("wakeId://" + currentTimerId));
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
		return alarmIntent;
	}

	public static PendingIntent recreatePendingIntentForAlarmManager(Context context, long currentTimerId) {
		Intent alarmIntent = createAlarmIntent(currentTimerId);
		PendingIntent pendingAlarmIntent = PendingIntent.getActivity(context, 0, alarmIntent,
				PendingIntent.FLAG_NO_CREATE);
		return pendingAlarmIntent;
	}

}
