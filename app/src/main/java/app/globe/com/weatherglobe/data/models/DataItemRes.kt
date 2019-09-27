package app.globe.com.weatherglobe.data.models

import com.squareup.moshi.Json

data class DataItemRes(

    @Json(name = "windGust")
    val windGust: Double = 0.0,

    @Json(name = "apparentTemperatureMinTime")
    val apparentTemperatureMinTime: Int = 0,

    @Json(name = "temperatureMax")
    val temperatureMax: Double = 0.0,

    @Json(name = "icon")
    val icon: String = "",

    @Json(name = "precipIntensityMax")
    val precipIntensityMax: Double = 0.0,

    @Json(name = "windBearing")
    val windBearing: Int = 0,

    @Json(name = "ozone")
    val ozone: Double = 0.0,

    @Json(name = "temperatureMaxTime")
    val temperatureMaxTime: Int = 0,

    @Json(name = "apparentTemperatureMin")
    val apparentTemperatureMin: Double = 0.0,

    @Json(name = "sunsetTime")
    val sunsetTime: Int = 0,

    @Json(name = "temperatureLow")
    val temperatureLow: Double = 0.0,

    @Json(name = "precipType")
    val precipType: String = "",

    @Json(name = "humidity")
    val humidity: Double = 0.0,

    @Json(name = "moonPhase")
    val moonPhase: Double = 0.0,

    @Json(name = "windSpeed")
    val windSpeed: Double = 0.0,

    @Json(name = "apparentTemperatureLowTime")
    val apparentTemperatureLowTime: Int = 0,

    @Json(name = "sunriseTime")
    val sunriseTime: Int = 0,

    @Json(name = "apparentTemperatureLow")
    val apparentTemperatureLow: Double = 0.0,

    @Json(name = "summary")
    val summary: String = "",

    @Json(name = "precipProbability")
    val precipProbability: Double = 0.0,

    @Json(name = "temperatureHighTime")
    val temperatureHighTime: Int = 0,

    @Json(name = "visibility")
    val visibility: Double = 0.0,

    @Json(name = "precipIntensity")
    val precipIntensity: Double = 0.0,

    @Json(name = "cloudCover")
    val cloudCover: Double = 0.0,

    @Json(name = "temperatureMin")
    val temperatureMin: Double = 0.0,

    @Json(name = "apparentTemperatureHighTime")
    val apparentTemperatureHighTime: Int = 0,

    @Json(name = "pressure")
    val pressure: Double = 0.0,

    @Json(name = "dewPoint")
    val dewPoint: Double = 0.0,

    @Json(name = "temperatureMinTime")
    val temperatureMinTime: Int = 0,

    @Json(name = "uvIndexTime")
    val uvIndexTime: Int = 0,

    @Json(name = "apparentTemperatureMax")
    val apparentTemperatureMax: Double = 0.0,

    @Json(name = "temperatureHigh")
    val temperatureHigh: Double = 0.0,

    @Json(name = "temperatureLowTime")
    val temperatureLowTime: Int = 0,

    @Json(name = "apparentTemperatureHigh")
    val apparentTemperatureHigh: Double = 0.0,

    @Json(name = "time")
    val time: Long = 0,

    @Json(name = "precipIntensityMaxTime")
    val precipIntensityMaxTime: Int = 0,

    @Json(name = "windGustTime")
    val windGustTime: Int = 0,

    @Json(name = "uvIndex")
    val uvIndex: Int = 0,

    @Json(name = "apparentTemperatureMaxTime")
    val apparentTemperatureMaxTime: Int = 0

)

