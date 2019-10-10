package app.globe.com.weatherglobe.preference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.utils.PREF_FILE_NAME
import java.util.*
import javax.inject.Inject


class SharedPrefs @Inject constructor(val application : Application){

    private var mPrefs: SharedPreferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    private var prefsSettings : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private var liveSharedPreferences : LiveSharedPreferences = LiveSharedPreferences(prefsSettings)

    //----------------------------Shared Preferences--------------------------------------

    fun setLastTimeAppWasOpened(time : Long) {
        mPrefs.edit().putLong(application.getString(R.string.last_time_app_was_opened_key), time).apply()
    }

    fun getLastTimeAppWasOpened(): Long {
        val lastTimeAppWasOpened = Calendar.getInstance().timeInMillis
        return mPrefs.getLong(application.getString(R.string.last_time_app_was_opened_key), lastTimeAppWasOpened)
    }

    //---------------------------SETTINGS-------------------------------------------------
    fun getTempUnitsLivePreference() : LivePreference<String>
    {
        return liveSharedPreferences.getString(application.getString(R.string.temperature_unit_pref_key), "0")
    }

    fun getDistanceUnitsLivePreference() : LivePreference<String>
    {
        return liveSharedPreferences.getString(application.getString(R.string.distance_unit_pref_key), "0")
    }

    fun getDateFormatLivePreference() : LivePreference<String>
    {
        return liveSharedPreferences.getString(application.getString(R.string.date_format_pref_key),"0")
    }

    fun getTimeFormatLivePreference() : LivePreference<String>
    {
        return liveSharedPreferences.getString(application.getString(R.string.time_format_pref_key),"0")
    }

    fun getDistanceUnit() : Int
    {
        return prefsSettings.getString(application.getString(R.string.distance_unit_pref_key),"0")!!.toInt()
    }

    fun getTempUnit(): Int {
        return prefsSettings.getString(application.getString(R.string.temperature_unit_pref_key),"0")!!.toInt()
    }

    fun getDateFormat() : Int{
        return prefsSettings.getString(application.getString(R.string.date_format_pref_key),"0")!!.toInt()
    }

    fun getTimeFormat() : Int{
        return prefsSettings.getString(application.getString(R.string.time_format_pref_key),"0")!!.toInt()
    }

}