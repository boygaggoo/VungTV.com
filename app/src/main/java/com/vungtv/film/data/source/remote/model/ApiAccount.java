package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.User;

public class ApiAccount extends ApiModel {

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
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("refresh_token")
        @Expose
        private String refreshToken;

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
