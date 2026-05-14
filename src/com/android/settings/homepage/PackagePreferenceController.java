/*
 * Copyright (C) 2026 ProjectMatrixx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.preference.Preference;

import com.android.settings.core.BasePreferenceController;

public abstract class PackagePreferenceController extends BasePreferenceController {
    protected final Context mContext;

    public PackagePreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
        mContext = context;
    }

    @Override
    public int getAvailabilityStatus() {
        return isActivityAvailable()
                ? AVAILABLE
                : UNSUPPORTED_ON_DEVICE;
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (preference == null || !TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        
        final Intent intent = new Intent().setClassName(
            getPackageName(), 
            getSettingsActivity()
        );
        
        try {
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    protected boolean isPackageInstalled(@NonNull String packageName) {
        try {
            mContext.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    protected boolean isActivityAvailable() {
        try {
            Intent intent = new Intent().setClassName(
                    getPackageName(),
                    getSettingsActivity());

            return intent.resolveActivity(
                    mContext.getPackageManager()) != null;

        } catch (Exception e) {
            return false;
        }
    }

    protected abstract String getPackageName();
    protected abstract String getSettingsActivity();
}
