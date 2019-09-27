package app.globe.com.weatherglobe.data.models

import com.squareup.moshi.Json

data class CurrentlyRes(

	@Json(name="summary")
	val summary: String="",

	@Json(name="precipProbability")
	val precipProbability: Double = 0.0,

	@Json(name="visibility")
	val visibility: Double = 0.0,

	@Json(name="windGust")
	val windGust: Double =0.0,

	@Json(name="precipIntensity")
	val precipIntensity: Double =0.0,

	@Json(name="icon")
	val icon: String="",

	@Json(name="cloudCover")
	val cloudCover: Double=0.0,

	@Json(name="windBearing")
	val windBearing: Double=0.0,

	@Json(name="apparentTemperature")
	val apparentTemperature: Double=0.0,

	@Json(name="pressure")
	val pressure: Double=0.0,

	@Json(name="dewPoint")
	val dewPoint: Double=0.0,

	@Json(name="ozone")
	val ozone: Double = 0.0,

	@Json(name="nearestStormBearing")
	val nearestStormBearing: Double = 0.0,

	@Json(name="nearestStormDistance")
	val nearestStormDistance: Double = 0.0,

	@Json(name="temperature")
	val temperature: Double = 0.0,

	@Json(name="humidity")
	val humidity: Double = 0.0,

	@Json(name="time")
	val time: Long = 0,

	@Json(name="windSpeed")
	val windSpeed: Double = 0.0,

	@Json(name="uvIndex")
	val uvIndex: Int = 0
)