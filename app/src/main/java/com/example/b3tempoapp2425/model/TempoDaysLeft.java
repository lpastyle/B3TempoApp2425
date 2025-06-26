package com.example.b3tempoapp2425.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TempoDaysLeft {

    @SerializedName("errors")
    @Expose
    public List<Object> errors;
    @SerializedName("content")
    @Expose
    public List<Content> content;

}
