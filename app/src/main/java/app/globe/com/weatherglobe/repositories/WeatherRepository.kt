package app.globe.com.weatherglobe.repositories

import android.location.Geocoder
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.preference.SharedPrefs
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepository @Inject constructor(
    private var lWeatherRepo: LocalWeatherRepo,
    private var rWeatherRepo: RemoteWeatherRepo
)
{

    @Inject
    lateinit var prefs : SharedPrefs


    fun getWeather(
        geoCoder: Geocoder,
        lat: Double, lng: Double
    ): Observable<Weather> {

        val dbSingle = lWeatherRepo.getWeather(lat, lng)
            .switchIfEmpty(
                rWeatherRepo.getWeather(
                    lat,
                    lng
                ).flatMapMaybe {weather->

                    Maybe.just(weather.buildWeather(geoCoder)) }
                    .doOnSuccess(lWeatherRepo::addWeatherForecast))

        return dbSingle.toObservable()

    }

    fun reloadWeather(
        geoCoder: Geocoder,
        lat: Double, lng: Double
    ): Observable<Weather> {
        return rWeatherRepo.getWeather(lat, lng)
            .flatMapMaybe {weather->
                Maybe.just(weather.buildWeather(geoCoder)) }
            .doOnSuccess(lWeatherRepo::addWeatherForecast).toObservable()
    }
}