package app.globe.com.weatherglobe.data.models

import com.squareup.moshi.Json

data class AlertsItemRes(

	@Json(name="severity")
	val severity: String = "",

	@Json(name="expires")
	val expires: Int = 0,

	@Json(name="regions")
	val regions: List<String?>? = null,

	@Json(name="description")
	val description: String = "",

	@Json(name="time")
	val time: Int = 0,

	@Json(name="title")
	val title: String = "",

	@Json(name="uri")
	val uri: String = ""
)