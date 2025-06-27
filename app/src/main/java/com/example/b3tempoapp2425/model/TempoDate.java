package com.example.b3tempoapp2425.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.b3tempoapp2425.TempoColor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempoDate implements Parcelable {
    @SerializedName("dateApplication")
    @Expose
    public String dateApplication;
    @SerializedName("statut")
    @Expose
    public TempoColor statut;

    // Ctors
    public TempoDate(String dateApplication, String statut) {
        this.dateApplication = dateApplication;
        this.statut = TempoColor.valueOf(statut);
    }

    protected TempoDate(Parcel in) {
        dateApplication = in.readString();
        statut = TempoColor.valueOf(in.readString());
    }

    public static final Creator<TempoDate> CREATOR = new Creator<TempoDate>() {
        @Override
        public TempoDate createFromParcel(Parcel in) {
            return new TempoDate(in);
        }

        @Override
        public TempoDate[] newArray(int size) {
            return new TempoDate[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dateApplication);
        dest.writeString(statut.toString());
    }

    // getters & setters
    public String getDateApplication() { return dateApplication; }
    public String getStatut() { return statut.toString(); }
}
