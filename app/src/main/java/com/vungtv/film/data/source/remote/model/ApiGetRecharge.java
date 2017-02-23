package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.CardType;
import com.vungtv.film.model.CardValue;

import java.util.ArrayList;

/**
 *
 * Created by pc on 2/23/2017.
 */

public class ApiGetRecharge extends ApiModel{

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("limit")
        @Expose
        private int limit;
        @SerializedName("offset")
        @Expose
        private int offset;
        @SerializedName("cards")
        @Expose
        private ArrayList<CardType> cardTypes = null;
        @SerializedName("values")
        @Expose
        private ArrayList<CardValue> cardValues = null;

        public ArrayList<CardType> getCardTypes() {
            return cardTypes;
        }

        public ArrayList<CardValue> getCardValues() {
            return cardValues;
        }
    }
}
