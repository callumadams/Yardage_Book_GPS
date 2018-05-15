package greengrid.example.android.yardagebookgps;

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

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;


    //Flags for GPS & Network status
    boolean isGPSEneabled = false;
    boolean isNetworkEneabled = false;
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    //The minimum distance to change Updates in memters
    private static final long MIN_DISTANCE_CHANGES_FOR_UPDATES = 10; //10 meters

    //The minimum time between updates in Milliseconds
    private static final long MIN_TIME_BETWEEN_BW_UPDATES = 1000 * 60 * 1; // 1000 millisecnonds * 60 seconds * 1 minute

    protected LocationManager locationManager;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    //Tries to get location from Network Provider, then GPS, and returns location
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //getting GPS Status
            isGPSEneabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting Network status
            isNetworkEneabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEneabled && !isNetworkEneabled) {
                //TODO Create Error for No network or GPS
            } else {
                this.canGetLocation = true;
                //First get location from network Provider
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_BW_UPDATES, MIN_DISTANCE_CHANGES_FOR_UPDATES, this);

                Log.d("Network", "Network");

                if (locationManager != null) {

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            // if GPS Enabled get lat/long using GPS Service
            if (isGPSEneabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_BW_UPDATES, MIN_DISTANCE_CHANGES_FOR_UPDATES, this);

                    Log.d("GPS Enabled", "GPS enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    } //stop location service

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    } //returns latitude as double

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    } //returns longitude as double

    public boolean CanGetLocation() {
        return this.canGetLocation;
    } //returns true if GPS and Network location are available

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Set dialog title
        alertDialog.setTitle("GPS is settings");

        //Set dialog message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to the settings menu?");

        //Set incon to dialog ???
        //alertDialog.setIcon(R.drawable.delete);

        //On Pressing settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();


    } //Prompt to turn on GPS if disabled


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}

