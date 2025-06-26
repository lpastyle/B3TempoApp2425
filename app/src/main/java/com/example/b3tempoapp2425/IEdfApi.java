package com.example.b3tempoapp2425;

import com.example.b3tempoapp2425.model.TempoDaysLeft;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IEdfApi {
    String API_OPTION_PARAM_VALUE = "TEMPO";
    String API_CONSUMER_ID_PARAM_VALUE = "src";

    // https://api-commerce.edf.fr/commerce/activet/v1/calendrier-jours-effacement?option=TEMPO&dateApplicationBorneInf=2024-6-27&dateApplicationBorneSup=2025-6-27&identifiantConsommateur=src
    @GET("commerce/activet/v1/calendrier-jours-effacement")
    Call<TempoDaysLeft> getTempoDaysLeft(
            @Query("option") String option,
            @Query("dateApplicationBorneInf") String dateInf,
            @Query("dateApplicationBorneSup") String dateSup,
            @Query("identifiantConsommateur") String consumerId
    );
}
