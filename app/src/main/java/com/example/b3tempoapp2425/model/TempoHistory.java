package com.example.b3tempoapp2425.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
   This class was created with a POJO generator (http://www.jsonschema2pojo.org/)
   from the following JSON sample:

   {
     "errors": [],
     "content": {
       "dateApplicationBorneInf": "2024-06-19",
       "dateApplicationBorneSup": "2025-06-19",
       "dateHeureTraitementActivET": "2025-06-17T22:26:43Z",
       "options": [
         {
           "option": "TEMPO",
           "calendrier": [
             {
               "dateApplication": "2024-06-19",
               "statut": "TEMPO_BLEU"
             }
           ]
         }
       ]
     }
   }

   NB: Calendrier class was refactored to TempoDate for clarity

*/
public class TempoHistory {

    @SerializedName("errors")
    @Expose
    public List<Object> errors;
    @SerializedName("content")
    @Expose
    public Content content;

    public class Content {

        @SerializedName("dateApplicationBorneInf")
        @Expose
        public String dateApplicationBorneInf;
        @SerializedName("dateApplicationBorneSup")
        @Expose
        public String dateApplicationBorneSup;
        @SerializedName("dateHeureTraitementActivET")
        @Expose
        public String dateHeureTraitementActivET;
        @SerializedName("options")
        @Expose
        public List<Option> options;

    }

    public class Option {

        @SerializedName("option")
        @Expose
        public String option;
        @SerializedName("calendrier")
        @Expose
        public List<TempoDate> calendrier;

    }

}
