package com.example.b3tempoapp2425;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.b3tempoapp2425.model.TempoDaysLeft;

import retrofit2.Call;
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

    }
    /*
     *  ----------------------- Helper Methods -------------------------
     */

    void updateNbTempoDaysLeft() {
        Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft(
                IEdfApi.API_OPTION_PARAM_VALUE,

        )
    }


}