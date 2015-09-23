package feliperrm.reversiandroid.Uteis;


        import android.app.AlertDialog;
        import android.app.Service;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.provider.Settings;
        import android.util.Log;

        import feliperrm.reversiandroid.Interfaces.GPSCallback;

public class GPS extends Service implements LocationListener {

    public static final String tag=GPS.class.getSimpleName();
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 metro

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 100;//0 * 10; // 10 segundos

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private GPSCallback callback;

    public GPSCallback getCallback() {
        return callback;
    }

    public void setCallback(GPSCallback callback) {
        this.callback = callback;
    }

    public GPS(Context context, GPSCallback callback) {
        this.mContext = context;
        this.callback = callback;
        startGPS();
    }


    public void startGPS(){
        getLocation();
        onLocationChanged(location);
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.d(tag, "None GPS");
            } else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d(tag, "GPS Network enable");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(tag, "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPS.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     *
     * @param context*/
    public void showSettingsAlert(final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if(callback!=null)
                callback.onLocationChanged(location);
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(tag, "Provide " + provider + " disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(tag, "Provider " + provider + " enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(tag, "Provider " + provider + " change to " + status + " - " + extras);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}