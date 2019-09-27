package app.globe.com.weatherglobe.data.models

import com.squareup.moshi.Json

data class FlagsRes(

	@Json(name="nearest-station")
	val nearestStation: Double = 0.0,

	@Json(name="sources")
	val sources: List<String?>? = null,

	@Json(name="units")
	val units: String = ""
)