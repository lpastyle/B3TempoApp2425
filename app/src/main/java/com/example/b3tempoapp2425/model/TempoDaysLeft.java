package com.example.b3tempoapp2425.model;

import java.util.List;

import com.example.b3tempoapp2425.TempoColor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/*
    This class was created with a POJO generator (http://www.jsonschema2pojo.org/)
    from the following JSON sample:
    {
        "errors": [],
        "content": [
            {
                "typeJourEff": "TEMPO_ROUGE",
                "libelle": "TEMPO ROUGE 2024 2025",
                "nombreJours": 22,
                "premierJour": "2024-11-01",
                "dernierJour": "2025-03-31",
                "premierJourExclu": null,
                "dernierJourExclu": null,
                "nombreJoursTires": 22,
                "etat": "FERMEE"
            }
        ]
    }
 */
public class TempoDaysLeft {

    @SerializedName("errors")
    @Expose
    public List<Object> errors;
    @SerializedName("content")
    @Expose
    public List<Content> content;

    public class Content {

        @SerializedName("typeJourEff")
        @Expose
        public TempoColor typeJourEff;
        @SerializedName("libelle")
        @Expose
        public String libelle;
        @SerializedName("nombreJours")
        @Expose
        public Integer nombreJours;
        @SerializedName("premierJour")
        @Expose
        public String premierJour;
        @SerializedName("dernierJour")
        @Expose
        public String dernierJour;
        @SerializedName("premierJourExclu")
        @Expose
        public Object premierJourExclu;
        @SerializedName("dernierJourExclu")
        @Expose
        public Object dernierJourExclu;
        @SerializedName("nombreJoursTires")
        @Expose
        public Integer nombreJoursTires;
        @SerializedName("etat")
        @Expose
        public String etat;

    }
}
