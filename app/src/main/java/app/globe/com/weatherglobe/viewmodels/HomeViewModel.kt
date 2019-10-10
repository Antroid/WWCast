package app.globe.com.weatherglobe.viewmodels

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.preference.SharedPrefs
import javax.inject.Inject

class HomeViewModel @Inject
constructor() : ViewModel() {

    private lateinit var timeViewModel: TimeViewModel
    private lateinit var weatherViewModel : WeatherViewModel

    @Inject
    lateinit var prefs: SharedPrefs


    fun setViewModels(weatherViewModel: WeatherViewModel,timeViewModel: TimeViewModel)
    {
        this.weatherViewModel = weatherViewModel
        this.timeViewModel = timeViewModel
    }

    fun getDate(isMonthFirst : Boolean) : String
    {
        return timeViewModel.getDate(isMonthFirst)
    }

    fun getTime(is24TimeFormat: Boolean) : String
    {
        return timeViewModel.getTime(is24TimeFormat)
    }

    fun getWeather() : LiveData<Weather>
    {
        timeViewModel.pauseTime()
        return weatherViewModel.getHomeWeather()
    }

    fun reloadWeatherForecast(geoCoder : Geocoder,
                              lat: Double, lng: Double)
    {
        weatherViewModel.reloadWeatherForecast(geoCoder,lat,lng)
    }

    fun loadWeatherForecast(geoCoder : Geocoder,
                              lat: Double, lng: Double)
    {
        weatherViewModel.loadWeatherForecast(geoCoder,lat,lng)
    }

    fun getTime() : LiveData<Long>
    {
        return timeViewModel.getTime()
    }

    fun getWeatherError() : LiveData<Boolean>
    {
        return weatherViewModel.getError()
    }

    fun getWeatherLoading() : LiveData<Boolean>
    {
        return weatherViewModel.getLoading()
    }

    fun getDay() : String
    {
        return timeViewModel.getDay()
    }

    fun getAMPM() : String
    {
        return timeViewModel.getAMPM()
    }

    fun pauseTime()
    {
        timeViewModel.pauseTime()
    }

    fun resumeTime()
    {
        timeViewModel.resumeTime()
    }
}