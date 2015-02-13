package de.r3w6.xposedunifiednlp;

import android.content.Context;
import android.location.LocationManager;

/**
* Created on 13.02.2015.
*/
class NetworkLocationCheckStep extends CheckStep {
    private Context context;

    public NetworkLocationCheckStep(Context context) {
        super("Network-based geolocation");
        this.context = context;
    }

    @Override
    public void runStep() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            setState(StepState.SUCCESS);
        } else {
            setState(StepState.FAIL);
        }
    }

    @Override
    public String getSolution() {
        return "Please activate network-based geolocation in Settings->Location. Since KitKat, you need to select any mode but \"device only\", on older Android version this setting is called \"Wi-Fi & mobile network location\" (ignore any misleading texts saying this is for Google's location service)";
    }
}
