package app.globe.com.weatherglobe.data

import app.globe.com.weatherglobe.data.models.WeatherRes
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    //https://api.darksky.net/forecast/[key]/[latitude],[longitude]/?currently

    @GET("/forecast/{apiKey}/{locationPoint}")
    fun getWeatherForecastResponse(@Path("apiKey") apiKey : String, @Path("locationPoint") locationPoint : String) : Single<WeatherRes>

}