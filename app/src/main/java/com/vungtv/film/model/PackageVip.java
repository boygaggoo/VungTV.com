package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by pc on 2/21/2017.
 */

public class PackageVip {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("money")
    @Expose
    private int money;
    @SerializedName("pro_id")
    @Expose
    private String proId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("create_at")
    @Expose
    private long createAt;
    @SerializedName("update_at")
    @Expose
    private long updateAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }
}
