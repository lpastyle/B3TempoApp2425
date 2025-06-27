package com.example.b3tempoapp2425;

import static com.example.b3tempoapp2425.MainActivity.TEMPO_CALENDAR_EXTRA_KEY;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.b3tempoapp2425.databinding.ActivityHistoryBinding;
import com.example.b3tempoapp2425.model.TempoDate;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();

    // Data model
    private final ArrayList<TempoDate> tempoCalendar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.b3tempoapp2425.databinding.ActivityHistoryBinding binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init RV
        binding.tempoHistoryRv.setHasFixedSize(true);
        binding.tempoHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        // RV adapter
        TempoDateAdapter tempoDateAdapter = new TempoDateAdapter(tempoCalendar, this);
        binding.tempoHistoryRv.setAdapter(tempoDateAdapter);

        // get tempo calendar data
        ArrayList<TempoDate> parcelableArrayListExtra = getIntent().getParcelableArrayListExtra(MainActivity.TEMPO_CALENDAR_EXTRA_KEY);
        if (parcelableArrayListExtra != null) {
            tempoCalendar.addAll(parcelableArrayListExtra);
            tempoDateAdapter.notifyDataSetChanged();
        } else {
            Log.d(LOG_TAG,"Empty tempo calendar was passed to " + LOG_TAG);
            finish();
        }
    }
}