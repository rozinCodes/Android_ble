package com.waltonbd.smartscale.models.unassigned_model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


public class UnassignedData {

    private String date;
    private List<UnassignedDetails> details;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<UnassignedDetails> getDetails() {
        return details;
    }

    public void setDetails(List<UnassignedDetails> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(this);
    }
}
