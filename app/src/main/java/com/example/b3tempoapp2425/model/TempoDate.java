package com.example.b3tempoapp2425.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempoDate {
    @SerializedName("dateApplication")
    @Expose
    public String dateApplication;
    @SerializedName("statut")
    @Expose
    public String statut;
}
