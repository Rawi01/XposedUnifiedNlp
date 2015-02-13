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

License
---
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
