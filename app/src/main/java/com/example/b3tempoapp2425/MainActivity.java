package com.example.b3tempoapp2425;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.b3tempoapp2425.TempoColor.BLUE;
import static com.example.b3tempoapp2425.TempoColor.RED;
import static com.example.b3tempoapp2425.TempoColor.WHITE;
import static com.example.b3tempoapp2425.TempoNotifications.sendColorNotification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.b3tempoapp2425.databinding.ActivityMainBinding;
import com.example.b3tempoapp2425.model.TempoDate;
import com.example.b3tempoapp2425.model.TempoDaysLeft;
import com.example.b3tempoapp2425.model.TempoHistory;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String TEMPO_CALENDAR_EXTRA_KEY = "tempo_calendar_ek";
    private static final String TEMPO_WORKER_REQUEST_TAG = "tempo-work-request";

    private ActivityMainBinding binding;
    private IEdfApi edfApi;
    ArrayList<TempoDate> tempoCalendar = new ArrayList<>();
    // Number of running progress wheels
    private int nbRunningWheels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init views
        binding.historyBt.setOnClickListener(this);

        // request permission workflow
        check4NotificationPermission();

        // Init work manager
        initWorkManager();

        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            edfApi = retrofitClient.create(IEdfApi.class);
        } else {
            Log.e(LOG_TAG, "Unable to initialize Retrofit client");
            finish();
        }
    }
    /*
     *  ----------------------- Helper Methods -------------------------
     */

    void updateNbTempoDaysLeft() {
        Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft(
                IEdfApi.API_OPTION_PARAM_VALUE,
                Tools.getNowDate()
        );

        showProgressWheel();
        call.enqueue(new Callback<TempoDaysLeft>() {
            @Override
            public void onResponse(@NonNull Call<TempoDaysLeft> call, @NonNull Response<TempoDaysLeft> response) {
                TempoDaysLeft tempoDaysLeft = response.body();
                if (response.code() == HttpURLConnection.HTTP_OK && tempoDaysLeft != null) {
                    for (int i = 0; i < tempoDaysLeft.content.size(); i++) {
                        Log.d(LOG_TAG, "typeJourEff[" + i + "] = " + tempoDaysLeft.content.get(i).typeJourEff);
                        Log.d(LOG_TAG, "nombreJours[" + i + "] = " + tempoDaysLeft.content.get(i).nombreJours);
                        Log.d(LOG_TAG, "nombreJoursTirés[" + i + "] = " + tempoDaysLeft.content.get(i).nombreJoursTires);
                        setTempoDaysLeft(tempoDaysLeft.content);
                    }
                } else {
                    Log.w(LOG_TAG, "call to getTempoDaysLeft() failed with error code " + response.code());
                }
                hideProgressWheel();
            }

            @Override
            public void onFailure(@NonNull Call<TempoDaysLeft> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, "call to getTempoDaysLeft() failed ");
                hideProgressWheel();
            }
        });
    }

    void setTempoDaysLeft(List<TempoDaysLeft.Content> contents) {
        for(TempoDaysLeft.Content item : contents) {
            switch (item.typeJourEff) {
                case RED : binding.redDaysTv.setText(Tools.getDaysLeftFromContent(item));
                    break;
                case WHITE : binding.whiteDaysTv.setText(Tools.getDaysLeftFromContent(item));
                    break;
                case BLUE : binding.blueDaysTv.setText(Tools.getDaysLeftFromContent(item));
                    break;
            }
        }
    }

    void updateTempoHistory() {
        // Create call to getTempoHistory
        Call<TempoHistory> call = edfApi.getTempoHistory(
                IEdfApi.API_OPTION_PARAM_VALUE,
                Tools.getLastYearDate(),
                Tools.getTomorrowDate(),
                IEdfApi.API_CONSUMER_ID_PARAM_VALUE);

        showProgressWheel();
        call.enqueue(new Callback<TempoHistory>() {
            @Override
            public void onResponse(@NonNull Call<TempoHistory> call, @NonNull Response<TempoHistory> response) {
                tempoCalendar.clear();
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    Log.d(LOG_TAG,"Got tempo history for " + response.body().content.options.size() + " option(s)");
                    setTempoCalendar(response.body());
                } else {
                    Log.e(LOG_TAG,"Call to getTempoHistory() returned error code " + response.code());
                }
                hideProgressWheel();
            }

            @Override
            public void onFailure(@NonNull Call<TempoHistory> call, @NonNull Throwable t) {
                Log.e(LOG_TAG,"Call to getTempoHistory() failed");
                hideProgressWheel();
            }
        });

    }

    void setTempoCalendar(TempoHistory tempoHistory) {
        for(TempoHistory.Option item : tempoHistory.content.options) {
            if (item.option.equals(IEdfApi.API_OPTION_PARAM_VALUE)) {
                tempoCalendar.addAll(item.calendrier);
                break;
            }
        }
        if (tempoCalendar.isEmpty()) {
            Log.w(LOG_TAG,"No data found for option "+ IEdfApi.API_OPTION_PARAM_VALUE);
        } else {
            for(TempoDate date : tempoCalendar) {
                Log.d(LOG_TAG, date.dateApplication + " = " + date.statut);
            }
            // update custom views
            int historySize = tempoCalendar.size();
            if (historySize > 1) {
                binding.todayDcv.setDayCircleColor(tempoCalendar.get(historySize - 2).statut);
                binding.tomorrowDcv.setDayCircleColor(tempoCalendar.get(historySize - 1).statut);
                /*sendColorNotification(this,
                        getPreferences(MODE_PRIVATE),
                        tempoCalendar.get(historySize - 1).statut);*/
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNbTempoDaysLeft();
        updateTempoHistory();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.history_bt) {
            Intent intent = new Intent();
            intent.setClass(this, HistoryActivity.class);
            intent.putExtra(TEMPO_CALENDAR_EXTRA_KEY, tempoCalendar);
            startActivity(intent);
        } else {
            Log.w(LOG_TAG, "unhandled click event");
        }
    }

    /* // deprecated way to handle button click based on the 'onClick' XML Button attribute
    public void showHistory(View view) {
        Intent intent = new Intent();
        intent.setClass(this,HistoryActivity.class);
        startActivity(intent);
    } */

    void showProgressWheel(){
        nbRunningWheels++;
        binding.progressWheelPb.setVisibility(VISIBLE);
    }

    void hideProgressWheel() {
        nbRunningWheels--;
        if (nbRunningWheels < 1) binding.progressWheelPb.setVisibility(GONE);
    }

    private void initWorkManager() {
        // compute delay between now and wished work request start
        Calendar currentTime = Calendar.getInstance();
        Calendar scheduledTime = Calendar.getInstance();

        scheduledTime.set(Calendar.HOUR_OF_DAY, 12);
        scheduledTime.set(Calendar.MINUTE, 0);
        if (scheduledTime.before(currentTime)) {
            scheduledTime.add(Calendar.DATE, 1);
        }

        long initialDelay = scheduledTime.getTimeInMillis() - currentTime.getTimeInMillis();

        // only need to be connected to any network
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Build work request
        PeriodicWorkRequest tempoWorkRequest = new PeriodicWorkRequest.Builder(TempoWorker.class,1, TimeUnit.DAYS)
                .addTag(TEMPO_WORKER_REQUEST_TAG)
                .setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria( // wait 10 minutes before retrying)
                        BackoffPolicy.LINEAR,
                        30,
                        TimeUnit.MINUTES
                )
                .build();

        Log.d(LOG_TAG, "initial delay=" + initialDelay);

        // enqueue request
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                TEMPO_WORKER_REQUEST_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                tempoWorkRequest);
    }

    /*
     * -------------------- Permission Requests Workflow Management ------------------
     */
    // see documentation at https://developer.android.com/training/permissions/requesting?hl=fr#request-permission

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i(LOG_TAG,"Notifications permission granted");
                } else {
                    Log.i(LOG_TAG, "Notifications permission denied");
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private void check4NotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.POST_NOTIFICATIONS)) {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected, and what
                    // features are disabled if it's declined. In this UI, include a
                    // "cancel" or "no thanks" button that lets the user continue
                    // using your app without granting the permission.
                    Log.w(LOG_TAG,"Android asked to call shouldShowRequestPermissionRationale()");
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
    }
}