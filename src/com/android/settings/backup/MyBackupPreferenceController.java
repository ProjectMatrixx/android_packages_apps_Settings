package com.android.settings.backup;

import android.content.Context;
import android.content.Intent;
import com.android.settings.core.BasePreferenceController;

public class MyBackupPreferenceController extends BasePreferenceController {

    public MyBackupPreferenceController(Context ctx, String key) {
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
            "com.google.android.gms.backup.component.BackupOrRestoreSettingsActivity"
        );
        mContext.startActivity(i);
        return true;
    }
}
