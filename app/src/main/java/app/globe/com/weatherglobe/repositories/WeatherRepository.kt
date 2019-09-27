package app.globe.com.weatherglobe.repositories

import app.globe.com.weatherglobe.db.models.Weather
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepository @Inject constructor(private var lWeatherRepo: LocalWeatherRepo,
                                            private var rWeatherRepo : RemoteWeatherRepo)
{
        fun getWeather(lat: Double, lng: Double): Observable<Weather> {

            val dbSingle = lWeatherRepo.getWeather(lat,lng)
                .switchIfEmpty(rWeatherRepo.getWeather(lat,lng).flatMapMaybe { Maybe.just(it.buildWeather()) }
                    .doOnSuccess(lWeatherRepo::addWeatherForecast))

            return dbSingle.toObservable()

    }

    fun reloadWeather(lat : Double, lng : Double) : Observable<Weather>
    {
        return rWeatherRepo.getWeather(lat,lng).flatMapMaybe { Maybe.just(it.buildWeather()) }
            .doOnSuccess(lWeatherRepo::addWeatherForecast).toObservable()
    }

}