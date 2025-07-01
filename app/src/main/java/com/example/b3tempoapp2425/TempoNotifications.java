package com.example.b3tempoapp2425;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class TempoNotifications {
    private final static String LOG_TAG = TempoNotifications.class.getSimpleName();
    // Define notification channel ids
    public static final String RED_TEMPO_ALERT_CHANNEL_ID = "red_tempo_alert_channel_id";
    public static final String WHITE_TEMPO_ALERT_CHANNEL_ID = "white_tempo_alert_channel_id";
    public static final String BLUE_TEMPO_ALERT_CHANNEL_ID = "blue_tempo_alert_channel_id";
    public static final String LAST_TEMPO_NOTIFICATION_DATE_KEY = "last_tempo_notification_date";

    // Making Ctor private prevents from making instances of this class
    private TempoNotifications() {}

    // Create a notification channel for each tempo color
    // This function shall be called once in the application class
    public static void createNotificationChannels(Context context) {
        Log.d(LOG_TAG,"createNotificationChannels()");
        String[] channelIds = {
                BLUE_TEMPO_ALERT_CHANNEL_ID,
                WHITE_TEMPO_ALERT_CHANNEL_ID,
                RED_TEMPO_ALERT_CHANNEL_ID
        };

        int[] channelNames = {
                R.string.blue_channel_name,
                R.string.white_channel_name,
                R.string.red_channel_name
        };

        int[] channelDescriptions = {
                R.string.blue_channel_description,
                R.string.white_channel_description,
                R.string.red_channel_description
        };

        // create the channel list
        List<NotificationChannel> channels = new ArrayList<>();

        for (int i = 0; i < channelIds.length; i++) {
            NotificationChannel channel = new NotificationChannel(
                    channelIds[i],
                    context.getString(channelNames[i]),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(context.getString(channelDescriptions[i]));
            channels.add(channel);
        }

        // Register the channels with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannels(channels);
    }

    public static void sendColorNotification(Context context, SharedPreferences sharedPreferences, TempoColor color) {
        Log.d(LOG_TAG, "sendColorNotification(" + color + ")");
        if (! wasUserAlreadyNotifiedToday(sharedPreferences)) {

            String notificationChannelId;
            switch (color) {
                case RED:
                    notificationChannelId = RED_TEMPO_ALERT_CHANNEL_ID;
                    break;
                case WHITE:
                    notificationChannelId = WHITE_TEMPO_ALERT_CHANNEL_ID;
                    break;
                case BLUE:
                    notificationChannelId = BLUE_TEMPO_ALERT_CHANNEL_ID;
                    break;
                default:
                    Log.w(LOG_TAG,"Today tempo color not yet known");
                    return;
            }

            // get notification manager
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // check if notifications are not blocked
            if (notificationManager.areNotificationsEnabled()) {
                // create notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,notificationChannelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.tempo_notif_title))
                        .setContentText(context.getString(R.string.tempo_notif_text,context.getString(color.getStringResId())))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // show notification (Id is a unique int for each notification)
                notificationManager.notify(Tools.getNextNotifId(), builder.build());

                // save the notification date in shared preference
                sharedPreferences.edit().putString(LAST_TEMPO_NOTIFICATION_DATE_KEY, Tools.getNowDate()).apply();

            } else {
                Log.w(LOG_TAG, "Notifications are blocked");
            }
        } else {
            Log.i(LOG_TAG,"Tempo notification was already sent today");
        }

    }

    private static boolean wasUserAlreadyNotifiedToday(SharedPreferences sharedPreferences) {
        String lastTempoNotificationDate = sharedPreferences.getString(LAST_TEMPO_NOTIFICATION_DATE_KEY,"unknown");
        Log.d(LOG_TAG, "lastTempoNotificationDate=" + lastTempoNotificationDate);
        return lastTempoNotificationDate.equals(Tools.getNowDate());
    }

}
