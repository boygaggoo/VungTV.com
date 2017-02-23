package com.vungtv.film.eventbus;


public class ConfigurationChangedEvent {
    private boolean isOrientationLand;

    public ConfigurationChangedEvent(boolean isOrientationLand) {
        this.isOrientationLand = isOrientationLand;
    }

    public boolean isOrientationLand() {
        return isOrientationLand;
    }

    public void setOrientationLand(boolean orientationLand) {
        isOrientationLand = orientationLand;
    }
}
