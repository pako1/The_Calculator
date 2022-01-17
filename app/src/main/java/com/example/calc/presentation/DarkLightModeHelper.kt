package com.example.calc.presentation

import androidx.appcompat.app.AppCompatDelegate

interface DarkLightPicker {
    /**
     * Tip: Changing from light to dark mode is enabled from SDK 29 and above.
     * Checks if it is possible to switch from dark to light mode.
     */
    fun isManuallyHandlingOfDarkModePossible(): Boolean

    /**
     * Enables the dark mode
     */
    fun enableDarkMode()

    /**
     * Enables the light mode
     */
    fun enableLightMode()
}

class DarkLightModePicker : DarkLightPicker {
    override fun isManuallyHandlingOfDarkModePossible(): Boolean =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    override fun enableDarkMode() =
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    override fun enableLightMode() =
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


}