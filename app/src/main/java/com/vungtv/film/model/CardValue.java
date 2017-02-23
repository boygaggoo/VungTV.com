package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by pc on 2/22/2017.
 */

public class CardValue {

    @SerializedName("money")
    @Expose
    public String money;
    @SerializedName("vung")
    @Expose
    public String vung;

    public CardValue() {
    }

    public CardValue(String money, String vung) {
        this.money = money;
        this.vung = vung;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getVung() {
        return vung;
    }

    public void setVung(String vung) {
        this.vung = vung;
    }
}
