XposedUnifiedNlp
-

Xposed module to integrate [UnifiedNlp](https://github.com/microg/android_packages_apps_UnifiedNlp) into the system even if GAPPS are installed.

Installation
---
1. Download and install `UnifiedNlp.apk` from the UnifiedNlp [release page](https://github.com/microg/android_packages_apps_UnifiedNlp/releases/latest)
2. Download and install one or more [backends](https://github.com/microg/android_packages_apps_UnifiedNlp#usage)
3. Download and install this module
4. Restart
5. Done


### How do I know it's running

If you use the experimental module you can open the module settings and press the "Check settings" button. It will check if everything works as intended.
Otherwise have a look at [#1](https://github.com/Rawi01/XposedUnifiedNlp/issues/1).

### It doesn't work

Check that you
* installed `UnifiedNlp.apk` and not `NetworkLocation.apk` or `LegacyNetworkLocation.apk`. If you installed UnifiedNlp vie F-Droid you have to replace it with the correct version.
* activated the backends
* activated network-based geolocation in Settings->Location

If you followed the instructions above and it still does not work, feel free to open an issue.
