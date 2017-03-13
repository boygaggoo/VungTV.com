package com.vungtv.film.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vungtv.film.data.source.remote.service.DeviceServices;
import com.vungtv.film.util.DeviceUtil;
import com.vungtv.film.util.LogUtils;

/**
 *
 * Created by Mr Cuong on 5/24/2016.
 */
public class VtvFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "VtvFirebaseInstanceIDService";

    private DeviceServices deviceServices;

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    @Override
    public void onDestroy() {
        if (deviceServices != null) {
            deviceServices.cancel();
        }
        super.onDestroy();
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        deviceServices = new DeviceServices(this);
        deviceServices.registerDevice(DeviceUtil.getId(this), token);
    }
}
