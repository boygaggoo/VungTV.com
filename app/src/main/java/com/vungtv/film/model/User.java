package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_password")
    @Expose
    private String userPassword;
    @SerializedName("user_salt")
    @Expose
    private String userSalt;
    @SerializedName("user_display_name")
    @Expose
    private String userDisplayName;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_personal_id")
    @Expose
    private String userPersonalId;
    @SerializedName("user_balance")
    @Expose
    private int userBalance;
    @SerializedName("user_otp")
    @Expose
    private String userOtp;
    @SerializedName("user_created_at")
    @Expose
    private String userCreatedAt;
    @SerializedName("user_lastlog_at")
    @Expose
    private String userLastlogAt;
    @SerializedName("user_updated_at")
    @Expose
    private String userUpdatedAt;
    @SerializedName("user_is_partner")
    @Expose
    private String userIsPartner;
    @SerializedName("user_level")
    @Expose
    private int userLevel;
    @SerializedName("user_status")
    @Expose
    private int userStatus;
    @SerializedName("user_vip_date")
    @Expose
    private String userVipDate;
    @SerializedName("user_status_message")
    @Expose
    private String userStatusMessage;
    @SerializedName("user_type")
    @Expose
    private String provider;
    @SerializedName("user_flag_pass_social")
    @Expose
    private int userFlagPassSocial;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPersonalId() {
        return userPersonalId;
    }

    public void setUserPersonalId(String userPersonalId) {
        this.userPersonalId = userPersonalId;
    }

    public String getUserOtp() {
        return userOtp;
    }

    public void setUserOtp(String userOtp) {
        this.userOtp = userOtp;
    }

    public String getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(String userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

    public String getUserLastlogAt() {
        return userLastlogAt;
    }

    public void setUserLastlogAt(String userLastlogAt) {
        this.userLastlogAt = userLastlogAt;
    }

    public String getUserUpdatedAt() {
        return userUpdatedAt;
    }

    public void setUserUpdatedAt(String userUpdatedAt) {
        this.userUpdatedAt = userUpdatedAt;
    }

    public String getUserIsPartner() {
        return userIsPartner;
    }

    public void setUserIsPartner(String userIsPartner) {
        this.userIsPartner = userIsPartner;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserVipDate() {
        return userVipDate;
    }

    public void setUserVipDate(String userVipDate) {
        this.userVipDate = userVipDate;
    }

    public String getUserStatusMessage() {
        return userStatusMessage;
    }

    public void setUserStatusMessage(String userStatusMessage) {
        this.userStatusMessage = userStatusMessage;
    }

    public int getUserFlagPassSocial() {
        return userFlagPassSocial;
    }

    public void setUserFlagPassSocial(int userFlagPassSocial) {
        this.userFlagPassSocial = userFlagPassSocial;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(int userBalance) {
        this.userBalance = userBalance;
    }
}
