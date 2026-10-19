package com.example.ufin_lso.networkmonitor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;
import java.util.List;
public class NetworkMonitorManager {
    private final Context context;
    private final ConnectivityManager connectivityManager;
    private final TelephonyManager telephonyManager;
    private final WifiManager wifiManager;
    public NetworkMonitorManager(Context context) {
        this.context = context.getApplicationContext();

        connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    public String getNetworkType() {

        if (connectivityManager == null) {
            return "NO_NETWORK";
        }

        Network network = connectivityManager.getActiveNetwork();

        if (network == null) {
            return "NO_NETWORK";
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        if (capabilities == null) {
            return "NO_NETWORK";
        }

        if (capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
        )) {
            return "WIFI";
        }

        if (capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
        )) {
            return "MOBILE";
        }

        return "OTHER";
    }
    public boolean isNetworkConnected() {

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
    public boolean isInternetAvailable() {

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        if (capabilities == null) {
            return false;
        }
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
    public String getNetworkGeneration() {

        if (!"MOBILE".equals(getNetworkType())) {
            return "N/A";
        }

        if (telephonyManager == null) {
            return "UNKNOWN";
        }

        try {

            int networkType = telephonyManager.getDataNetworkType();

            switch (networkType) {

                // 2G
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:

                    return "2G";

                // 3G

                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSPAP:

                    return "3G";

                // 4G

                case TelephonyManager.NETWORK_TYPE_LTE:

                    return "4G";

                // 5G

                case TelephonyManager.NETWORK_TYPE_NR:
                    return "5G";

                default:
                    return "UNKNOWN";
            }

        } catch (SecurityException e) {
            return "UNKNOWN";
        }
    }

    public boolean isRoaming() {

        if (telephonyManager == null) {
            return false;
        }

        try {
            return telephonyManager.isNetworkRoaming();
        } catch (SecurityException e) {
            return false;
        }
    }
    public int getSignalDbm() {

        String networkType = getNetworkType();

        // WIFI

        if ("WIFI".equals(networkType)) {
            return getWifiSignalDbm();
        }

        // MOBILE

        if ("MOBILE".equals(networkType)) {
            return getMobileSignalDbm();
        }
        return 0;
    }

    private int getWifiSignalDbm() {

        if (wifiManager == null) {
            return 0;
        }

        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo == null) {
                return 0;
            }

            int rssi = wifiInfo.getRssi();

            if (rssi == -127) {
                return 0;
            }
            return rssi;

        } catch (SecurityException e) {
            return 0;
        }
    }
    private int getMobileSignalDbm() {

        if (telephonyManager == null) {
            return 0;
        }

        try {

            List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

            if (cellInfoList == null
                    || cellInfoList.isEmpty()) {
                return 0;
            }

            for (CellInfo cellInfo :
                    cellInfoList) {

                if (cellInfo == null) {
                    continue;
                }

                if (!cellInfo.isRegistered()) {
                    continue;
                }

                CellSignalStrength signalStrength = getSignalStrength(cellInfo);

                if (signalStrength == null) {
                    continue;
                }

                int dbm = signalStrength.getDbm();

                if (dbm != Integer.MAX_VALUE && dbm != 0)
                {
                    return dbm;
                }
            }

        } catch (SecurityException e) {
            return 0;
        }
        return 0;
    }

    // CELL SIGNAL STRENGTH
    private CellSignalStrength getSignalStrength(
            CellInfo cellInfo
    ) {

        if (Build.VERSION.SDK_INT >= 29) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return cellInfo.getCellSignalStrength();
            }
        }

        return null;
    }

    // SIGNAL LEVEL
    // WIFI   -> 0 to 4
    // MOBILE -> Android CellSignalStrength level
    public int getSignalLevel() {

        String networkType = getNetworkType();
        // WIFI

        if ("WIFI".equals(networkType)) {
            return getWifiSignalLevel();
        }

        // MOBILE

        if ("MOBILE".equals(networkType)) {
            return getMobileSignalLevel();
        }
        return 0;
    }

    // WIFI SIGNAL LEVEL
    // Returns:
    // 0 = very weak
    // 1 = weak
    // 2 = average
    // 3 = good
    // 4 = excellent
    private int getWifiSignalLevel() {
        if (wifiManager == null) {
            return 0;
        }
        try {
            WifiInfo wifiInfo =
                    wifiManager.getConnectionInfo();

            if (wifiInfo == null) {
                return 0;
            }

            int rssi = wifiInfo.getRssi();

            if (rssi ==  -127) {
                return 0;
            }

            /*
             * Android standard Wi-Fi signal level.
             *
             * 5 levels:
             *
             * 0 = very weak
             * 1 = weak
             * 2 = fair
             * 3 = good
             * 4 = excellent
             */

            return WifiManager.calculateSignalLevel(rssi, 5);

        } catch (SecurityException e) {
            return 0;
        }
    }

    private int getMobileSignalLevel() {

        if (telephonyManager == null) {
            return 0;
        }

        try {

            List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

            if (cellInfoList == null || cellInfoList.isEmpty()) {
                return 0;
            }

            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo == null) {
                    continue;
                }

                if (!cellInfo.isRegistered()) {
                    continue;
                }

                CellSignalStrength signalStrength = getSignalStrength(cellInfo);

                if (signalStrength == null) {
                    continue;
                }
                return signalStrength.getLevel();
            }
        } catch (SecurityException e) {
            return 0;
        }
        return 0;
    }

    public String getWifiSsid() {

        if (!"WIFI".equals(getNetworkType())) {
            return "N/A";
        }

        if (wifiManager == null) {
            return "UNKNOWN";
        }

        try {

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo == null) {
                return "UNKNOWN";
            }

            String ssid = wifiInfo.getSSID();

            if (ssid == null) {
                return "UNKNOWN";
            }

            return ssid;

        } catch (SecurityException e) {
            return "UNKNOWN";
        }
    }

    // OPTIONAL: WIFI LINK SPEED
    public int getWifiLinkSpeedMbps() {

        if (!"WIFI".equals(getNetworkType())) {
            return 0;
        }

        if (wifiManager == null) {
            return 0;
        }

        try {

            WifiInfo wifiInfo =
                    wifiManager.getConnectionInfo();

            if (wifiInfo == null) {
                return 0;
            }

            return wifiInfo.getLinkSpeed();

        } catch (SecurityException e) {

            return 0;
        }
    }
}