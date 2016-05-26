package de.r3w6.xposedunifiednlp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XResources;
import android.os.Build;
import android.os.UserHandle;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class UnifiedNlp implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        XResources.setSystemWideReplacement("android", "bool", "config_enableNetworkLocationOverlay", false);
        XResources.setSystemWideReplacement("android", "string", "config_networkLocationProviderPackageName", "org.microg.nlp");
        XResources.setSystemWideReplacement("android", "bool", "config_enableGeocoderOverlay", false);
        XResources.setSystemWideReplacement("android", "string", "config_geocoderProviderPackageName", "org.microg.nlp");
        XposedBridge.log("UnifiedNlp config set");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
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
}
