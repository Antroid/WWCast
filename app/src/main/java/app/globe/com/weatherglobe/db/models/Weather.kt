package app.globe.com.weatherglobe.db.models

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "weather",primaryKeys = ["place", "state", "countryName"])
data class Weather(

	@Json(name="place")
	var place: String ="",

	@Json(name="state")
	var state: String = "",

	@Json(name="countryName")
	var countryName: String = "",

	@Json(name="longitude")
	var longitude: Double = 0.0,

	@Json(name="latitude")
	var latitude: Double = 0.0,

	@Json(name="currently")
	var currently: Currently? = null,

	@Json(name="daily")
	var daily: Daily? = null,

	@Json(name="hourly")
	var hourly : Hourly?=null
)