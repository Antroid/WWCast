package app.globe.com.weatherglobe.data.models

import app.globe.com.weatherglobe.db.models.Currently
import app.globe.com.weatherglobe.db.models.Daily
import app.globe.com.weatherglobe.db.models.DataItem
import app.globe.com.weatherglobe.db.models.Weather
import com.squareup.moshi.Json

data class WeatherRes(

	@Json(name="alerts")
	val alerts: List<AlertsItemRes?>? = null,

	@Json(name="currently")
	val currently: CurrentlyRes? = null,

	@Json(name="offset")
	val offset: Int = 0,

	@Json(name="timezone")
	val timezone: String = "",

	@Json(name="latitude")
	val latitude: Double = 0.0,

	@Json(name="daily")
	val daily: DailyRes? = null,

	@Json(name="flags")
	val flags: FlagsRes? = null,

	@Json(name="hourly")
	val hourly: HourlyRes? = null,

	@Json(name="minutely")
	val minutely: MinutelyRes? = null,

	@Json(name="longitude")
	val longitude: Double = 0.0
){
	fun buildWeather() : Weather {
		val longitude = this.longitude
		val latitude = this.latitude


		val currentlySummary = currently!!.summary
		val currentlyTemperature = currently.temperature
		val currentlyHumidity = currently.humidity
		val currentlyTime = currently.time
		val currentlyWindSpeed = currently.windSpeed

		val currently = Currently(currentlySummary,currentlyTemperature,currentlyHumidity,currentlyTime,currentlyWindSpeed)

		val weatherDataItems = ArrayList<DataItem>()
		val dataItemResList = daily!!.data
		for(i in dataItemResList!!.indices)
		{
			val dataItemRes = dataItemResList[i]

			val icon = dataItemRes!!.icon
			val apparentTemperatureMin = dataItemRes.apparentTemperatureMin
			val precipProbability = dataItemRes.precipProbability
			val apparentTemperatureMax = dataItemRes.apparentTemperatureMax
			val time = dataItemRes.time

			val dataItem = DataItem(icon,apparentTemperatureMin,precipProbability,apparentTemperatureMax,time)
			weatherDataItems.add(dataItem)
		}
		val daily = Daily(weatherDataItems)
		return Weather(longitude,latitude,currently,daily)
	}
}