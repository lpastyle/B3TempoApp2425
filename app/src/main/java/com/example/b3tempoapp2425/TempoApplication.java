package com.example.b3tempoapp2425;

import android.app.Application;

public class TempoApplication extends Application {
    // Do not confuse this with the 'onCreate()' method of an activity.
    // The below 'onCreate()' is only called once when the application starts.
    @Override
    public void onCreate() {
        super.onCreate();
        // Create notification channels
        TempoNotifications.createNotificationChannels(getApplicationContext());
    }

}
