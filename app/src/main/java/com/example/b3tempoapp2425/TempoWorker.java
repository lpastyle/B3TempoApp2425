package com.example.b3tempoapp2425;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.b3tempoapp2425.model.TempoDate;
import com.example.b3tempoapp2425.model.TempoHistory;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TempoWorker extends Worker {
    private final String LOG_TAG = TempoWorker.class.getSimpleName();

    public TempoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(LOG_TAG,"doWork()");

        IEdfApi edfApi = ApiClient.get().create(IEdfApi.class);
        Call<TempoHistory> call = edfApi.getTempoHistory(
                IEdfApi.API_OPTION_PARAM_VALUE,
                Tools.getNowDate(),
                Tools.getTomorrowDate(),
                IEdfApi.API_CONSUMER_ID_PARAM_VALUE);

        try {
            // Synchronous call
            Response<TempoHistory> response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().content.options != null) {
                // Seek Tempo calendar in API response
                for (TempoHistory.Option option : response.body().content.options) {
                    if (option.option.equals(IEdfApi.API_OPTION_PARAM_VALUE)) {
                        List<TempoDate> tempoCalendar = option.calendrier;
                        int calendarSize = tempoCalendar.size();
                        if (calendarSize > 1) {
                            // send notification
                            TempoColor tomorrowColor = tempoCalendar.get(calendarSize - 1).statut;
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
                            TempoNotifications.sendColorNotification(
                                    getApplicationContext(),
                                    sharedPreferences,
                                    tomorrowColor);
                            return Result.success();
                        }
                    }
                }
            }
            Log.w(LOG_TAG,"Tempo calendar not found or empty");
            return Result.retry();

        } catch (IOException e) {
            Log.e(LOG_TAG,"synchronous call to getTempoDaysColor() failed");
            return Result.retry();
        }
    }
}
