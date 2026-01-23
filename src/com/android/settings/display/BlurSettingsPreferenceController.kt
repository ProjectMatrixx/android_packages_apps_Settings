/*
 * Copyright (C) 2026 AxionOS
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
package com.android.settings.display

import android.content.Context
import android.os.SystemProperties
import android.provider.Settings
import com.android.settings.core.BasePreferenceController
import android.view.CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED
import kotlin.math.roundToInt

class BlurSettingsPreferenceController(
    context: Context,
    preferenceKey: String
) : BasePreferenceController(context, preferenceKey) {

    override fun getAvailabilityStatus(): Int {
        return if (CROSS_WINDOW_BLUR_SUPPORTED) AVAILABLE else UNSUPPORTED_ON_DEVICE
    }

    override fun getSummary(): CharSequence {
        val blurEnabledByDefault = SystemProperties.getBoolean("ro.custom.blur.enable", false)
        val blursEnabled = Settings.Global.getInt(
            mContext.contentResolver,
            Settings.Global.DISABLE_WINDOW_BLURS,
            if (blurEnabledByDefault) 0 else 1
        ) == 0

        if (blursEnabled) {
            val radius = Settings.Secure.getFloat(
                mContext.contentResolver,
                "system_blur_radius",
                34f
            )
            val percent = (radius / 34f * 100).roundToInt()
            return "On ($percent%)"
        }
        return "Off"
    }
}
