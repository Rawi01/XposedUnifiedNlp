package de.r3w6.xposedunifiednlp;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
* Created on 13.02.2015.
*/
class PlayServiceLocationCheckStep extends CheckStep implements GoogleApiClient.ConnectionCallbacks {
    private Context context;
    private GoogleApiClient client;
    private Location location;
    private final Object lock = new Object();
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Location found", location.toString());
            setLocation(location);
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    };

    public PlayServiceLocationCheckStep(Context context) {
        super("PlayServices location");
        this.context = context;
    }

    @Override
    public void runStep() {
        client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        client.connect();

        synchronized (lock) {
            try {
                lock.wait(10000);
            } catch (InterruptedException e) {
            }
        }

        if(client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, locationListener);
        }

        if(location != null) {
            Bundle extras = location.getExtras();
            if (extras != null) {
                if (extras.containsKey("SERVICE_BACKEND_PROVIDER")) {
                    setState(StepState.SUCCESS);
                    return;
                }
            }
            setState(StepState.FAIL);
            setSolution("Your location were found but it looks not like an UnifiedNlp location. Check if you have activated this module.");
        } else {
            setState(StepState.FAIL);
            setSolution("Your location were not found. Make sure you added at least one backend.");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, locationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
