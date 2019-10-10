package app.globe.com.weatherglobe.db.models

import com.squareup.moshi.Json

data class Currently(

	@Json(name="summary")
	val summary: String = "",

	@Json(name="temperature")
	var temperature: Double = 0.0,

	@Json(name="humidity")
	val humidity: Double = 0.0,

	@Json(name="time")
	val time: Long = 0,

	@Json(name="windSpeed")
	var windSpeed: Double = 0.0,

	@Json(name="icon")
	var icon :String ="",

	@Json(name="cloudCover")
	var cloudCover : Double=0.0,

	@Json(name="windGust")
	var windGust : Double = 0.0,

	@Json(name="uvIndex")
	var uvIndex : Int = 0

)