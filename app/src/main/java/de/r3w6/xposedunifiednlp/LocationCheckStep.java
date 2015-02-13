package de.r3w6.xposedunifiednlp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
* Created on 13.02.2015.
*/
class LocationCheckStep extends CheckStep {
    private Context context;
    private Location location;

    public LocationCheckStep(Context context) {
        super("UnifiedNlp location");
        this.context = context;
    }

    @Override
    public void runStep() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location found", location.toString());
                setLocation(location);
                Looper.myLooper().quit();
            }
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        };
        Looper.prepare();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Looper.myLooper().quit();
            }
        }, 10000);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
        Looper.loop();
        if(location != null) {
            Bundle extras = location.getExtras();
            if (extras != null) {
                if (extras.containsKey("SERVICE_BACKEND_PROVIDER")) {
                    setState(StepState.SUCCESS);
                }
            }
            setState(StepState.FAIL);
            setSolution("Your location were found but it looks not like an UnifiedNlp location. Check if you have activated this module.");
        } else {
            locationManager.removeUpdates(listener);
            setState(StepState.FAIL);
            setSolution("Your location were not found. Make sure you added at least one backend.");
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
