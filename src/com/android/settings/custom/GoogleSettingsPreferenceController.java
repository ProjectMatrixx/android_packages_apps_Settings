package com.android.settings.custom;

import android.content.Context;
import android.content.Intent;
import com.android.settings.core.BasePreferenceController;

public class GoogleSettingsPreferenceController extends BasePreferenceController {

    public GoogleSettingsPreferenceController(Context ctx, String key) {
        super(ctx, key);
    }

    @Override
    public int getAvailabilityStatus() {
    	try {
   	     mContext.getPackageManager().getPackageInfo("com.google.android.gms", 0);
   	     return AVAILABLE;
   	 } catch (Exception e) {
   	     return UNSUPPORTED_ON_DEVICE;
   	 }
    }

    @Override
    public boolean handlePreferenceTreeClick(androidx.preference.Preference pref) {
        if (!getPreferenceKey().equals(pref.getKey())) return false;

        Intent i = new Intent();
        i.setClassName(
            "com.google.android.gms",
            "com.google.android.gms.app.settings.GoogleSettingsIALink"
        );
        mContext.startActivity(i);
        return true;
    }
}
