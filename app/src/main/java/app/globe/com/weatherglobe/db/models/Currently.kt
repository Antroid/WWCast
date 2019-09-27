package app.globe.com.weatherglobe.db.models

import androidx.room.Ignore
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

	@Ignore
	var windUnits : String=""

)