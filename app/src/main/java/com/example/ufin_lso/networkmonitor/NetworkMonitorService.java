package com.example.ufin_lso.networkmonitor;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.ufin_lso.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class NetworkMonitorService extends Service {

    public static final String EXTRA_WORKER_ID = "EXTRA_WORKER_ID";
    public static final String EXTRA_LOGIN_ID = "EXTRA_LOGIN_ID";
    public static final String EXTRA_LSO_ID = "EXTRA_LSO_ID";
    public static final String EXTRA_ORG_ID = "EXTRA_ORG_ID";

    private static final long MONITOR_INTERVAL = 60_000L;

    private Handler handler;
    private Runnable monitoringRunnable;

    private NetworkMonitorManager networkMonitorManager;
    private FusedLocationProviderClient locationClient;

    private String workerId;
    private String loginId;
    private int lsoId;
    private int orgId;


    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();

        networkMonitorManager =
                new NetworkMonitorManager(this);

        locationClient =
                LocationServices.getFusedLocationProviderClient(this);

        createNotificationChannel();

        startForeground(
                1001,
                createNotification()
        );
    }


    @Override
    public int onStartCommand(
            Intent intent,
            int flags,
            int startId
    ) {

        if (intent != null) {

            workerId =
                    intent.getStringExtra(EXTRA_WORKER_ID);

            loginId =
                    intent.getStringExtra(EXTRA_LOGIN_ID);

            lsoId =
                    intent.getIntExtra(
                            EXTRA_LSO_ID,
                            0
                    );

            orgId =
                    intent.getIntExtra(
                            EXTRA_ORG_ID,
                            0
                    );
        }

        startMonitoring();

        return START_STICKY;
    }


    private void startMonitoring() {

        if (monitoringRunnable != null) {

            handler.removeCallbacks(
                    monitoringRunnable
            );
        }

        monitoringRunnable =
                new Runnable() {

                    @Override
                    public void run() {

                        collectAndSendData();

                        handler.postDelayed(
                                this,
                                MONITOR_INTERVAL
                        );
                    }
                };

        // First request immediately
        handler.post(monitoringRunnable);
    }


    private void collectAndSendData() {

        String networkType =
                networkMonitorManager.getNetworkType();

        boolean connected =
                networkMonitorManager.isNetworkConnected();

        boolean internetAvailable =
                networkMonitorManager.isInternetAvailable();

        String generation =
                networkMonitorManager.getNetworkGeneration();

        boolean isRoaming =
                networkMonitorManager.isRoaming();

        int signalDbm =
                networkMonitorManager.getSignalDbm();

        int signalLevel =
                networkMonitorManager.getSignalLevel();


        getLocationAndSendData(
                networkType,
                generation,
                connected,
                internetAvailable,
                isRoaming,
                signalDbm,
                signalLevel
        );
    }


    private void getLocationAndSendData(
            String networkType,
            String generation,
            boolean connected,
            boolean internetAvailable,
            boolean isRoaming,
            int signalDbm,
            int signalLevel
    ) {

        if (
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
        ) {

            sendData(
                    networkType,
                    generation,
                    connected,
                    internetAvailable,
                    isRoaming,
                    signalDbm,
                    signalLevel,
                    0.0,
                    0.0
            );

            return;
        }


        locationClient
                .getLastLocation()

                .addOnSuccessListener(
                        location -> {

                            double latitude = 0.0;
                            double longitude = 0.0;

                            if (location != null) {

                                latitude =
                                        location.getLatitude();

                                longitude =
                                        location.getLongitude();
                            }


                            sendData(
                                    networkType,
                                    generation,
                                    connected,
                                    internetAvailable,
                                    isRoaming,
                                    signalDbm,
                                    signalLevel,
                                    latitude,
                                    longitude
                            );
                        }
                )

                .addOnFailureListener(
                        e -> {

                            sendData(
                                    networkType,
                                    generation,
                                    connected,
                                    internetAvailable,
                                    isRoaming,
                                    signalDbm,
                                    signalLevel,
                                    0.0,
                                    0.0
                            );
                        }
                );
    }


    private void sendData(
            String networkType,
            String generation,
            boolean connected,
            boolean internetAvailable,
            boolean isRoaming,
            int signalDbm,
            int signalLevel,
            double latitude,
            double longitude
    ) {

        // SIGNAL
        NetworkInfoData.SignalData signal =
                new NetworkInfoData.SignalData(
                        signalDbm,
                        signalLevel
                );


        // NETWORK
        NetworkInfoData.NetworkData network =
                new NetworkInfoData.NetworkData(
                        networkType,
                        generation,
                        connected,
                        internetAvailable,
                        isRoaming,
                        signal
                );


        // LOCATION
        NetworkInfoData.LocationData location =
                new NetworkInfoData.LocationData(
                        latitude,
                        longitude
                );


        // COMPLETE REQUEST BODY
        NetworkInfoData data =
                new NetworkInfoData(
                        workerId,
                        loginId,
                        lsoId,
                        orgId,
                        network,
                        location,
                        getCurrentIndianTimestamp()
                );


        // API CALL
        NetworkApiService apiService =
                RetrofitClient.getApiService();


        apiService
                .sendNetworkData(data)
                .enqueue(
                        new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(
                                    Call<ResponseBody> call,
                                    Response<ResponseBody> response
                            ) {

                                try {

                                    String responseBody = "";

                                    if (response.body() != null) {
                                        responseBody = response.body().string();

                                    } else if (
                                            response.errorBody() != null
                                    ) {
                                        responseBody = response.errorBody().string();
                                    }

                                    Log.d("NETWORK_API", "RESPONSE: " + responseBody);

                                } catch (Exception e) {

                                    Log.e("NETWORK_API", "Response read error", e);
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<ResponseBody> call,
                                    Throwable t
                            ) {
                                Log.e("NETWORK_API", "REQUEST FAILED: " + t.getMessage());
                            }
                        }
                );
    }


    private void createNotificationChannel() {

        NotificationChannel channel =
                new NotificationChannel(
                        "network_monitor_channel",
                        "Network Monitoring",
                        NotificationManager.IMPORTANCE_LOW
                );


        NotificationManager manager =
                getSystemService(
                        NotificationManager.class
                );


        if (manager != null) {

            manager.createNotificationChannel(
                    channel
            );
        }
    }


    private Notification createNotification() {

        return new NotificationCompat.Builder(
                this,
                "network_monitor_channel"
        )

                .setContentTitle(
                        "Network Monitoring Active"
                )

                .setContentText(
                        "Monitoring network every minute"
                )

                .setSmallIcon(
                        R.mipmap.ic_launcher
                )

                .setOngoing(true)

                .build();
    }

/*
    private long getCurrentIndianTimestamp() {

        TimeZone indiaTimeZone =
                TimeZone.getTimeZone("Asia/Kolkata");

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.US
                );

        sdf.setTimeZone(indiaTimeZone);

        String indianTime =
                sdf.format(new Date());

        try {

            Date date =
                    new java.text.SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.US
                    ).parse(indianTime);

            return date != null
                    ? date.getTime()
                    : System.currentTimeMillis();

        } catch (Exception e) {

            return System.currentTimeMillis();
        }
    }
*/

    private long getCurrentIndianTimestamp() {
        return System.currentTimeMillis()
                + TimeZone.getTimeZone("Asia/Kolkata").getRawOffset();
    }

    private String getCurrentIndianTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "hh:mm:ss a",
                Locale.US
        );
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        return sdf.format(new Date());
    }


    @Override
    public void onDestroy() {

        if (
                handler != null
                        &&
                        monitoringRunnable != null
        ) {

            handler.removeCallbacks(
                    monitoringRunnable
            );
        }

        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(
            Intent intent
    ) {

        return null;
    }
}