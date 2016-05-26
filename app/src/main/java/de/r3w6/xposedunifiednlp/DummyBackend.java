package de.r3w6.xposedunifiednlp;

import android.location.Location;

import org.microg.nlp.api.LocationBackendService;
import org.microg.nlp.api.LocationHelper;

/**
 * Created by raul on 26.05.16.
 */
public class DummyBackend extends LocationBackendService {
    @Override
    protected Location update() {
        return LocationHelper.create("DummyBackend",42,42,10);
    }
}
