package app.globe.com.weatherglobe.viewmodels

import androidx.lifecycle.ViewModel
import app.globe.com.weatherglobe.preference.SharedPrefs
import javax.inject.Inject

class SettingsViewModel @Inject
constructor() : ViewModel() {

    @Inject
    lateinit var prefs : SharedPrefs

}