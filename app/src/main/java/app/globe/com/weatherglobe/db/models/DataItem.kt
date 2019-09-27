package app.globe.com.weatherglobe.db.models

import com.squareup.moshi.Json


data class DataItem(

	@Json(name="icon")
	val icon: String = "",

	@Json(name="apparentTemperatureMin")
	var apparentTemperatureMin: Double = 0.0,

	@Json(name="precipProbability")
	val precipProbability: Double = 0.0,

	@Json(name="apparentTemperatureMax")
	var apparentTemperatureMax: Double = 0.0,

	@Json(name="time")
	val time: Long = 0

)
