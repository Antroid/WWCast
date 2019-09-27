package app.globe.com.weatherglobe.preference

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import app.globe.com.weatherglobe.R
import javax.inject.Inject


class SharedPrefs @Inject constructor(val application : Application){

    private var prefsSettings : SharedPreferences?=null
    private var liveSharedPreferences : LiveSharedPreferences?=null


    init {
        prefsSettings = PreferenceManager.getDefaultSharedPreferences(application)
        liveSharedPreferences = LiveSharedPreferences(prefsSettings!!)
    }

    fun getTempUnitsLivePreference() : LivePreference<String>
    {
        return liveSharedPreferences!!.getString(application.getString(R.string.temperature_unit_pref_key), "0")
    }

    fun getDistanceUnitsLivePreference() : LivePreference<String>
    {
        return liveSharedPreferences!!.getString(application.getString(R.string.distance_unit_pref_key), "0")
    }

    fun getDistanceUnit() : Int
    {
        return prefsSettings!!.getString(application.getString(R.string.distance_unit_pref_key),"0")!!.toInt()
    }

    fun getTempUnit(): Int {
        return prefsSettings!!.getString(application.getString(R.string.temperature_unit_pref_key),"0")!!.toInt()
    }

    fun setTempUnit(unit : String)
    {
        prefsSettings!!.edit().putString(application.getString(R.string.temperature_unit_pref_key),
            unit
        ).apply()
    }

    fun setDistanceUnit(unit : String)
    {
        prefsSettings!!.edit().putString(application.getString(R.string.distance_unit_pref_key),unit).apply()
    }


//    override fun setDistanceUnit(unit: Int) {
//        distancePrefs.value = unit
//    }
//
//    override fun setTempUnit(unit: Int) {
//        temperaturePrefs.value = unit
//    }


//    override fun getDistanceUnit(): Int {
//        return  prefsSettings.getString(application.getString(R.string.distance_unit_pref_key),"0")!!.toInt()
//    }
//
//
//    override fun getTempUnit(): Int {
//        return prefsSettings.getString(application.getString(R.string.temperature_unit_pref_key),"0")!!.toInt()
//    }
//
//    fun getTempPrefsLiveData() : LiveData<Int>
//    {
//        return temperaturePrefs
//    }
//
//    fun getDistancePrefsLiveData() : LiveData<Int>
//    {
//        return distancePrefs
//    }



}