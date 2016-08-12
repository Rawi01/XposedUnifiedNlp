package de.r3w6.xposedunifiednlp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XResources;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;

import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class UnifiedNlp implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    private static final String TAG = "XposedUnifiedNlp";

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        XResources.setSystemWideReplacement("android", "bool", "config_enableNetworkLocationOverlay", false);
        XResources.setSystemWideReplacement("android", "string", "config_networkLocationProviderPackageName", "org.microg.gms");
        XResources.setSystemWideReplacement("android", "bool", "config_enableGeocoderOverlay", false);
        XResources.setSystemWideReplacement("android", "string", "config_geocoderProviderPackageName", "org.microg.gms");
        XposedBridge.log("UnifiedNlp config set");
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        hookIntent(lpparam);

        if(!lpparam.packageName.equals("com.android.settings"))
            return;

        XC_MethodHook methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                ResolveInfo ri = (ResolveInfo) param.args[0];
                ri.serviceInfo.applicationInfo.flags |= ApplicationInfo.FLAG_SYSTEM;
            }
        };

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            XposedHelpers.findAndHookMethod("com.android.settings.location.SettingsInjector", lpparam.classLoader, "parseServiceInfo", ResolveInfo.class, PackageManager.class, methodHook);
        } else {
            XposedHelpers.findAndHookMethod("com.android.settings.location.SettingsInjector", lpparam.classLoader, "parseServiceInfo", ResolveInfo.class, UserHandle.class, PackageManager.class, methodHook);
        }
    }

    private static HashMap<String,String> replaceMap = new HashMap<>();

    static {
        replaceMap.put("com.google.android.location.internal.GoogleLocationManagerService.START", "org.microg.gms.location.GoogleLocationManagerService");
        replaceMap.put("com.google.android.location.reporting.service.START", "org.microg.gms.location.ReportingAndroidService");
        replaceMap.put("com.google.android.gms.location.reporting.service.START", "org.microg.gms.location.ReportingAndroidService");
        replaceMap.put("com.google.android.gms.location.places.ui.PICK_PLACE", "org.microg.gms.ui.PlacePickerActivity");
        replaceMap.put("com.google.android.gms.location.places.GeoDataApi","org.microg.gms.places.GeoDataService");
        replaceMap.put("com.google.android.gms.location.places.PlacesApi","org.microg.gms.places.GeoDataService");
        replaceMap.put("com.google.android.gms.location.places.PlaceDetectionApi","org.microg.gms.places.PlaceDetectionService");
        //replaceMap.put("","");
    }

    private void hookIntent(final XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(Intent.class, "setPackage", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent i = (Intent)param.thisObject;
                if(replaceMap.containsKey(i.getAction())) {
                    Log.d(TAG, "Replacing package " + param.args[0] + " for action " + i.getAction() + " created by " + lpparam.packageName);
                    param.args[0] = "org.microg.gms";
                }
            }
        });

        XposedHelpers.findAndHookMethod(Intent.class, "setComponent", ComponentName.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent i = (Intent)param.thisObject;
                if(replaceMap.containsKey(i.getAction())) {
                    param.args[0] = new ComponentName("org.microg.gms", replaceMap.get(i.getAction()));
                }
            }
        });
    }

}
