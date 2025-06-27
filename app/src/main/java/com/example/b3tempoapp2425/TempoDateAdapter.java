package com.example.b3tempoapp2425;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TempoDateAdapter extends RecyclerView.Adapter<TempoDateAdapter.TempoDateViewHolder>{


    TempoDateAdapter() {

    }

    @NonNull
    @Override
    public TempoDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TempoDateViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class TempoDateViewHolder extends RecyclerView.ViewHolder {

        public TempoDateViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
