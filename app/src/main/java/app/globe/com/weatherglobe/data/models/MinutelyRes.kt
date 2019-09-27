package app.globe.com.weatherglobe.data.models

import com.squareup.moshi.Json

data class MinutelyRes(

	@Json(name="summary")
	val summary: String = "",

	@Json(name="data")
	val data: List<DataItemRes?>? = null,

	@Json(name="icon")
	val icon: String = ""
)