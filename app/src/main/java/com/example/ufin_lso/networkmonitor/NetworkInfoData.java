package com.example.ufin_lso.networkmonitor;

public class NetworkInfoData {

    private String workerId;
    private String loginId;
    private int lsoId;
    private int orgId;

    private NetworkData network;

    private LocationData location;

    private long timestamp;


    public NetworkInfoData(
            String workerId,
            String loginId,
            int lsoId,
            int orgId,
            NetworkData network,
            LocationData location,
            long timestamp
    ) {

        this.workerId = workerId;
        this.loginId = loginId;
        this.lsoId = lsoId;
        this.orgId = orgId;
        this.network = network;
        this.location = location;
        this.timestamp = timestamp;
    }

    public static class NetworkData {

        private String type;
        private String generation;
        private boolean connected;
        private boolean internetAvailable;
        private boolean isRoaming;
        private SignalData signal;

        public NetworkData(
                String type,
                String generation,
                boolean connected,
                boolean internetAvailable,
                boolean isRoaming,
                SignalData signal
        ) {

            this.type = type;
            this.generation = generation;
            this.connected = connected;
            this.internetAvailable = internetAvailable;
            this.isRoaming = isRoaming;
            this.signal = signal;
        }

        public String getType() {
            return type;
        }

        public String getGeneration() {
            return generation;
        }

        public boolean isConnected() {
            return connected;
        }

        public boolean isInternetAvailable() {
            return internetAvailable;
        }

        public boolean isRoaming() {
            return isRoaming;
        }

        public SignalData getSignal() {
            return signal;
        }
    }

    public static class SignalData {

        private int dbm;
        private int level;

        public SignalData(
                int dbm,
                int level
        ) {

            this.dbm = dbm;
            this.level = level;
        }

        public int getDbm() {
            return dbm;
        }


        public int getLevel() {
            return level;
        }
    }


    public static class LocationData {

        private double latitude;
        private double longitude;


        public LocationData(
                double latitude,
                double longitude
        ) {

            this.latitude = latitude;
            this.longitude = longitude;
        }


        public double getLatitude() {
            return latitude;
        }


        public double getLongitude() {
            return longitude;
        }
    }


    public String getWorkerId() {
        return workerId;
    }


    public String getLoginId() {
        return loginId;
    }


    public int getLsoId() {
        return lsoId;
    }


    public int getOrgId() {
        return orgId;
    }


    public NetworkData getNetwork() {
        return network;
    }


    public LocationData getLocation() {
        return location;
    }


    public long getTimestamp() {
        return timestamp;
    }
}