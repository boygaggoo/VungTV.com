package com.vungtv.film.model;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ParserException;

import java.util.UUID;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/5/2017.
 * Email: vancuong2941989@gmail.com
 */

public class DrmSession {
    String drmSchemeUUIDExtra;
    String drmLicenseUrl = null;
    String[] drmKeyRequestProperties = null;
    boolean preferExtensionDecoders = false;

    public UUID getDrmUuid(String typeString) throws ParserException {
        switch (typeString.toLowerCase()) {
            case "widevine":
                return C.WIDEVINE_UUID;
            case "playready":
                return C.PLAYREADY_UUID;
            default:
                try {
                    return UUID.fromString(typeString);
                } catch (RuntimeException e) {
                    throw new ParserException("Unsupported drm type: " + typeString);
                }
        }
    }

    public String getDrmSchemeUUIDExtra() {
        return drmSchemeUUIDExtra;
    }

    public void setDrmSchemeUUIDExtra(String drmSchemeUUIDExtra) {
        this.drmSchemeUUIDExtra = drmSchemeUUIDExtra;
    }

    public String getDrmLicenseUrl() {
        return drmLicenseUrl;
    }

    public void setDrmLicenseUrl(String drmLicenseUrl) {
        this.drmLicenseUrl = drmLicenseUrl;
    }

    public String[] getDrmKeyRequestProperties() {
        return drmKeyRequestProperties;
    }

    public void setDrmKeyRequestProperties(String[] drmKeyRequestProperties) {
        this.drmKeyRequestProperties = drmKeyRequestProperties;
    }

    public boolean isPreferExtensionDecoders() {
        return preferExtensionDecoders;
    }

    public void setPreferExtensionDecoders(boolean preferExtensionDecoders) {
        this.preferExtensionDecoders = preferExtensionDecoders;
    }
}
