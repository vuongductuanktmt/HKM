package com.example.nghia.hkmai.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Trung Nguyen on 3/17/2017.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    public static final String UPDATE_UI_FROM_BROADCAST_CHANGE_NETWORK_STATE = "update_ui_change_network_state";
    public static final String IS_NETWORK_AVAILABLE = "network_available";

    public void onReceive(Context context, Intent intent) {
//        Log.d("app", "Network connectivity change");
        Intent iNetworkAvailable = new Intent(UPDATE_UI_FROM_BROADCAST_CHANGE_NETWORK_STATE);
        if (intent.getExtras() != null) {
            NetworkInfo info = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
//                Log.d("app", "Network " + info.getTypeName() + " connected");
                iNetworkAvailable.putExtra(IS_NETWORK_AVAILABLE, true);
                context.sendBroadcast(iNetworkAvailable);
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
//                Log.d("app", "There's no network connectivity");
                iNetworkAvailable.putExtra(IS_NETWORK_AVAILABLE, false);
                context.sendBroadcast(iNetworkAvailable);
            }
        }
    }
}
