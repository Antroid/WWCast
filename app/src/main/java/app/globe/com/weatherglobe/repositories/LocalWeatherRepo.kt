package app.globe.com.weatherglobe.repositories

import app.globe.com.weatherglobe.db.WeatherDao
import app.globe.com.weatherglobe.db.models.Weather
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class LocalWeatherRepo@Inject constructor(private var weatherDao : WeatherDao)
{
    fun getWeather(lat: Double, lng: Double): Maybe<Weather> {
        Timber.d("LocalWeatherRepo getWeather lat=$lat lng=$lng")
        return weatherDao.getWeatherForecastByLocation(lat, lng)
    }

    fun addWeatherForecast(weather : Weather)
    {
        Timber.d("adding new data lat=${weather.latitude} lng=${weather.longitude}")
        weatherDao.insertWeather(weather)
    }

}