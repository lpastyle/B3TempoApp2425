package com.example.b3tempoapp2425;

import com.example.b3tempoapp2425.model.TempoDaysLeft;
import com.example.b3tempoapp2425.model.TempoHistory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IEdfApi {
    String API_OPTION_PARAM_VALUE = "TEMPO";
    String API_CONSUMER_ID_PARAM_VALUE = "src";

    // https://api-commerce.edf.fr/commerce/activet/v1/saisons/search?option=TEMPO&dateReference=2025-06-26
    @GET("commerce/activet/v1/saisons/search")
    Call<TempoDaysLeft> getTempoDaysLeft(
            @Query("option") String option,
            @Query("dateReference") String dateReference
    );

    // https://api-commerce.edf.fr/commerce/activet/v1/calendrier-jours-effacement?option=TEMPO&dateApplicationBorneInf=2024-6-27&dateApplicationBorneSup=2025-6-27&identifiantConsommateur=src
   @GET("commerce/activet/v1/calendrier-jours-effacement")
    Call<TempoHistory> getTempoHistory(
            @Query("option") String option,
            @Query("dateApplicationBorneInf") String dateInf,
            @Query("dateApplicationBorneSup") String dateSup,
            @Query("identifiantConsommateur") String consumerId
    );
}
