package com.example.mentalhealf;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class reminders {
    private final Context context;
    private final AlarmManager alarmManager;
    private static final int ONCE_DAILY = 1;
    private static final int TWICE_DAILY = 2;
    private static final int THRICE_DAILY = 3;

    public reminders(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
    public void setReminders(int frequency, String reminderTitle, String reminderMessage) {
        cancelReminder();
        switch (frequency) {
            case ONCE_DAILY:
                setSingleReminder(12, 00, reminderTitle, reminderMessage, 1);
                break;
            case TWICE_DAILY:
                setSingleReminder(9, 0, reminderTitle + " (Morning)", reminderMessage, 1);
                setSingleReminder(18, 0, reminderTitle + " (Evening)", reminderMessage, 2);
                break;
            case THRICE_DAILY:
                setSingleReminder(8, 0, reminderTitle + " (Morning)", reminderMessage, 1);
                setSingleReminder(13, 0, reminderTitle + " (Afternoon)", reminderMessage, 2);
                setSingleReminder(20, 0, reminderTitle + " (Evening)", reminderMessage, 3);
                break;
        }

        String frequencyText = frequency + " time" + (frequency > 1 ? "s" : "") + " daily";
        Toast.makeText(context, "Reminders set for " + frequencyText, Toast.LENGTH_SHORT).show();
    }

    private void setSingleReminder(int hour, int minute, String title, String message, int requestCode) {
        Intent intent = new Intent(context, ReminderMessage.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        setAlarm(calendar.getTimeInMillis(), pendingIntent);
    }

    private void setAlarm(long triggerAtMillis, PendingIntent pendingIntent) {
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent);

    }
    public void cancelReminder() {
        for (int i = 101; i <= 106; i++) {
            Intent intent = new Intent(context, ReminderMessage.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    i,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "All reminders canceled", Toast.LENGTH_SHORT).show();
    }
}
