package app.globe.com.weatherglobe.db.models

import com.squareup.moshi.Json

data class HourlyItem(
    @Json(name="time")
    val time: Long=0,

    @Json(name="icon")
    val icon: String="",

    @Json(name="temperature")
    var temperature: Double=0.0
)