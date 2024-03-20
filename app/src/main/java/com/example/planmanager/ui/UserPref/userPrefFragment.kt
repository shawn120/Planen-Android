package com.example.planmanager.ui.UserPref

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.planmanager.R


class userPrefFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.user_prefrence, rootKey)
    }
}