package de.r3w6.xposedunifiednlp;

import android.content.res.XResources;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;

public class UnifiedNlp implements IXposedHookZygoteInit {

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        XResources.setSystemWideReplacement("android", "bool", "config_enableNetworkLocationOverlay", false);
        XResources.setSystemWideReplacement("android", "string", "config_networkLocationProviderPackageName", "org.microg.nlp");
        XposedBridge.log("UnifiedNlp config set");
    }

}
