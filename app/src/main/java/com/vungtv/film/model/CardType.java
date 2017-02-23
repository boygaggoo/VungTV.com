package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by pc on 2/22/2017.
 */

public class CardType {

    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("label")
    @Expose
    public String label;

    public CardType() {
    }

    public CardType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
