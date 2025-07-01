package com.example.b3tempoapp2425;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TempoNotifications {
    private final static String LOG_TAG = TempoNotifications.class.getSimpleName();
    // Define notification channel ids
    public static final String RED_TEMPO_ALERT_CHANNEL_ID = "red_tempo_alert_channel_id";
    public static final String WHITE_TEMPO_ALERT_CHANNEL_ID = "white_tempo_alert_channel_id";
    public static final String BLUE_TEMPO_ALERT_CHANNEL_ID = "blue_tempo_alert_channel_id";

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

}
