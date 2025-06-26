package com.example.b3tempoapp2425;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.b3tempoapp2425.model.TempoDaysLeft;
import com.example.b3tempoapp2425.model.TempoHistory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private IEdfApi edfApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            edfApi = retrofitClient.create(IEdfApi.class);
        } else {
            Log.e(LOG_TAG, "Unable to initialize Retrofit client");
            finish();
        }

        updateNbTempoDaysLeft();
        updateTempoHistory();
    }
    /*
     *  ----------------------- Helper Methods -------------------------
     */

    void updateNbTempoDaysLeft() {
        Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft(
                IEdfApi.API_OPTION_PARAM_VALUE,
                "2025-06-26"
        );

        call.enqueue(new Callback<TempoDaysLeft>() {
            @Override
            public void onResponse(@NonNull Call<TempoDaysLeft> call, @NonNull Response<TempoDaysLeft> response) {
                Log.i(LOG_TAG, "call to getTempoDaysLeft() succeeded ");
            }

            @Override
            public void onFailure(@NonNull Call<TempoDaysLeft> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, "call to getTempoDaysLeft() failed ");
            }
        });
    }

    void updateTempoHistory() {
        // Create call to getTempoHistory
        Call<TempoHistory> call = edfApi.getTempoHistory(
                IEdfApi.API_OPTION_PARAM_VALUE,
                "2024-06-27",
                "2025-06-27",
                IEdfApi.API_CONSUMER_ID_PARAM_VALUE);

        call.enqueue(new Callback<TempoHistory>() {
            @Override
            public void onResponse(@NonNull Call<TempoHistory> call, @NonNull Response<TempoHistory> response) {
                Log.e(LOG_TAG,"Call to getTempoHistory() succeeded");
            }

            @Override
            public void onFailure(@NonNull Call<TempoHistory> call, @NonNull Throwable t) {
                Log.e(LOG_TAG,"Call to getTempoHistory() failed");
            }
        });

    }


}