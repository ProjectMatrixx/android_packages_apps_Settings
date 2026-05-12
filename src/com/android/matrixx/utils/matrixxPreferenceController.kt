/*
 * Copyright (C) 2025 Matrixx
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
package com.android.matrixx.utils

import android.content.Context
import android.os.Handler
import android.os.SystemProperties
import android.view.View
import android.widget.TextView
import androidx.preference.PreferenceScreen
import com.android.settings.R
import com.android.settingslib.core.AbstractPreferenceController
import com.android.settingslib.widget.LayoutPreference
import com.android.matrixx.utils.DeviceInfoUtil

class matrixxPreferenceController(context: Context) :
    AbstractPreferenceController(context) {

    private val defaultFallback =
        mContext.getString(R.string.device_info_default)

    private val handler = Handler()

    private fun getProp(propName: String): String {
        return SystemProperties
            .get(propName, defaultFallback)
            .replace("_", " ")
    }

    private fun getProp(propName: String, fallbackProp: String): String {
        val value = SystemProperties.get(propName)
            .ifEmpty { SystemProperties.get(fallbackProp, defaultFallback) }

        return value.replace("_", " ")
    }

    private fun getMatrixxProcessor(): String =
        getProp(PROP_MATRIXX_PROCESSOR, "ro.matrixx.processor")

    private fun getMatrixxVersion(): String =
        getProp(PROP_MATRIXX_VERSION, "ro.matrixx.display.version")

    private fun getMaintainer(): String =
        getProp(PROP_MATRIXX_MAINTAINER)

    override fun displayPreference(screen: PreferenceScreen) {
        super.displayPreference(screen)

        val deviceInfoPreference =
            screen.findPreference<LayoutPreference>(KEY_HW_INFO)

        deviceInfoPreference?.apply {

            findViewById<TextView>(R.id.processor)?.text =
                getMatrixxProcessor()

            findViewById<TextView>(R.id.matrixx_summary)?.text =
                getMatrixxVersion()

            findViewById<TextView>(R.id.ram)?.text =
                DeviceInfoUtil.getTotalRam()

            findViewById<TextView>(R.id.storage)?.text =
                        DeviceInfoUtil.getStorageTotal(mContext)

            findViewById<TextView>(R.id.battery_summary)?.text =
                DeviceInfoUtil.getBatteryCapacity(mContext)

            val maintainerRow = findViewById<View>(R.id.maintainer_top)
            val maintainerText =
                findViewById<TextView>(R.id.maintainer_summary)

            val maintainerValue = getMaintainer()

            if (maintainerValue == defaultFallback || maintainerValue.isBlank()) {
                maintainerRow?.visibility = View.GONE
            } else {
                maintainerRow?.visibility = View.VISIBLE
                maintainerText?.text = maintainerValue
            }
        }
    }

    override fun isAvailable(): Boolean = true

    override fun getPreferenceKey(): String = KEY_DEVICE_INFO

    companion object {
        private const val KEY_HW_INFO = "my_device_hw_header"
        private const val KEY_DEVICE_INFO = "my_device_info_header"
        private const val KEY_BUILD_BANNER = "banner_logo"

        private const val PROP_MATRIXX_DEVICE =
            "ro.matrixx.display.device"
        private const val PROP_MATRIXX_PROCESSOR =
            "ro.matrixx.processor"
        private const val PROP_MATRIXX_VERSION =
            "ro.matrixx.version.display"
        private const val PROP_MATRIXX_MAINTAINER =
            "ro.matrixx.maintainer"

        private const val MATRIXX_BUILD_TYPE =
            "ro.matrixx.buildtype"
    }
}
