package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.ba.zavrsnirad_esiljak1.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mListPreference: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        mListPreference = preferenceManager.findPreference("saving_battery")!!
        

        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}