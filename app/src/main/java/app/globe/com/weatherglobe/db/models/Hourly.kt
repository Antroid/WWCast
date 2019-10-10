package app.globe.com.weatherglobe.db.models

import com.squareup.moshi.Json

data class Hourly(
    @Json(name="summary")
    val summary: String = "",

    @Json(name="data")
    val data: ArrayList<HourlyItem>? = null,

    @Json(name="icon")
    val icon: String = ""
)