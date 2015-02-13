package de.r3w6.xposedunifiednlp;

import android.content.Context;
import android.content.pm.PackageManager;

/**
* Created on 13.02.2015.
*/
class PackageCheckStep extends CheckStep {
    private Context context;

    public PackageCheckStep(Context context) {
        super("UnifiedNlp package");
        this.context = context;
    }

    @Override
    public void runStep() {
        try {
            context.getPackageManager().getPackageInfo("org.microg.nlp", PackageManager.GET_ACTIVITIES);
            setState(StepState.SUCCESS);
        } catch (PackageManager.NameNotFoundException e) {
            setState(StepState.FAIL);
        }
    }

    @Override
    public String getSolution() {
        return "Please download and install the latest UnifiedNlp.apk from the UnifiedNlp release page.";
    }
}
