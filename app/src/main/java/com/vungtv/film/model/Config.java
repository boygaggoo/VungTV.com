package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/13/2017.
 * Email: vancuong2941989@gmail.com
 */

public class Config {
    @SerializedName("home_page")
    @Expose
    private String homePage;
    @SerializedName("fan_page")
    @Expose
    private String fanPage;
    @SerializedName("fan_page_id")
    @Expose
    private String fanPageId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("about_us_html")
    @Expose
    private String aboutUsHtml;
    @SerializedName("adv_contact_html")
    @Expose
    private String advContactHtml;
    @SerializedName("popup_update_html")
    @Expose
    private String popupUpdateHtml;
    @SerializedName("apk_url")
    @Expose
    private String apkUrl;
    @SerializedName("app_ver")
    @Expose
    private int appVer;
    @SerializedName("app_ver_name")
    @Expose
    private String appVerName;
    @SerializedName("adv_provider")
    @Expose
    private String advProvider;
    @SerializedName("force_update")
    @Expose
    private Boolean forceUpdate;
    @SerializedName("store_update")
    @Expose
    private Boolean storeUpdate;
    @SerializedName("admob_banner_key")
    @Expose
    private String admobBannerKey;
    @SerializedName("admob_interstitial_key")
    @Expose
    private String admobInterstitialKey;
    @SerializedName("admob_native_key")
    @Expose
    private String admobNativeKey;
    @SerializedName("face_banner_key")
    @Expose
    private String faceBannerKey;
    @SerializedName("face_interstitial_key")
    @Expose
    private String faceInterstitialKey;
    @SerializedName("face_native_key")
    @Expose
    private String faceNativeKey;

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getFanPage() {
        return fanPage;
    }

    public void setFanPage(String fanPage) {
        this.fanPage = fanPage;
    }

    public String getFanPageId() {
        return fanPageId;
    }

    public void setFanPageId(String fanPageId) {
        this.fanPageId = fanPageId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAboutUsHtml() {
        return aboutUsHtml;
    }

    public void setAboutUsHtml(String aboutUsHtml) {
        this.aboutUsHtml = aboutUsHtml;
    }

    public String getAdvContactHtml() {
        return advContactHtml;
    }

    public void setAdvContactHtml(String advContactHtml) {
        this.advContactHtml = advContactHtml;
    }

    public String getPopupUpdateHtml() {
        return popupUpdateHtml;
    }

    public void setPopupUpdateHtml(String popupUpdateHtml) {
        this.popupUpdateHtml = popupUpdateHtml;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public int getAppVer() {
        return appVer;
    }

    public void setAppVer(int appVer) {
        this.appVer = appVer;
    }

    public String getAppVerName() {
        return appVerName;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public String getAdvProvider() {
        return advProvider;
    }

    public void setAdvProvider(String advProvider) {
        this.advProvider = advProvider;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Boolean getStoreUpdate() {
        return storeUpdate;
    }

    public void setStoreUpdate(Boolean storeUpdate) {
        this.storeUpdate = storeUpdate;
    }

    public String getAdmobBannerKey() {
        return admobBannerKey;
    }

    public void setAdmobBannerKey(String admobBannerKey) {
        this.admobBannerKey = admobBannerKey;
    }

    public String getAdmobInterstitialKey() {
        return admobInterstitialKey;
    }

    public void setAdmobInterstitialKey(String admobInterstitialKey) {
        this.admobInterstitialKey = admobInterstitialKey;
    }

    public String getAdmobNativeKey() {
        return admobNativeKey;
    }

    public void setAdmobNativeKey(String admobNativeKey) {
        this.admobNativeKey = admobNativeKey;
    }

    public String getFaceBannerKey() {
        return faceBannerKey;
    }

    public void setFaceBannerKey(String faceBannerKey) {
        this.faceBannerKey = faceBannerKey;
    }

    public String getFaceInterstitialKey() {
        return faceInterstitialKey;
    }

    public void setFaceInterstitialKey(String faceInterstitialKey) {
        this.faceInterstitialKey = faceInterstitialKey;
    }

    public String getFaceNativeKey() {
        return faceNativeKey;
    }

    public void setFaceNativeKey(String faceNativeKey) {
        this.faceNativeKey = faceNativeKey;
    }
}
