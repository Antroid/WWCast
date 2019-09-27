package app.globe.com.weatherglobe.repositories

import app.globe.com.weatherglobe.data.WeatherService
import app.globe.com.weatherglobe.data.models.WeatherRes
import app.globe.com.weatherglobe.utils.API_KEY
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class RemoteWeatherRepo@Inject constructor(private var weatherService: WeatherService)
{
    fun getWeather(lat: Double, lng: Double): Single<WeatherRes> {
        Timber.d("RemoteWeatherRepo getWeather lat=$lat lng=$lng")
        val locationStr = String.format("%f,%f",lat,lng)
        return weatherService.getWeatherForecastResponse(API_KEY,locationStr)
    }

}