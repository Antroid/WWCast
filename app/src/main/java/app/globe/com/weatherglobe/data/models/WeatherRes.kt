package app.globe.com.weatherglobe.data.models

import android.location.Geocoder
import app.globe.com.weatherglobe.db.models.*
import com.squareup.moshi.Json

data class WeatherRes(

    @Json(name = "alerts")
    val alerts: List<AlertsItemRes?>? = null,

    @Json(name = "currently")
    val currently: CurrentlyRes? = null,

    @Json(name = "offset")
    val offset: Int = 0,

    @Json(name = "timezone")
    val timezone: String = "",

    @Json(name = "latitude")
    val latitude: Double = 0.0,

    @Json(name = "daily")
    val daily: DailyRes? = null,

    @Json(name = "flags")
    val flags: FlagsRes? = null,

    @Json(name = "hourly")
    val hourly: HourlyRes? = null,

    @Json(name = "minutely")
    val minutely: MinutelyRes? = null,

    @Json(name = "longitude")
    val longitude: Double = 0.0
) {
    fun buildWeather(geoCoder: Geocoder): Weather {
        val longitude = this.longitude
        val latitude = this.latitude

        val addresses = geoCoder.getFromLocation(latitude, longitude, 10)
        var place = ""
        var state = ""
        var countryName = ""

        if (addresses != null && addresses.isNotEmpty()) {
            for (address in addresses) {

                if (state.isEmpty() && address.adminArea != null && address.adminArea.isNotEmpty()) {
                    state = address.adminArea
                }

                if (place.isEmpty() && address.locality != null && address.locality.isNotEmpty()) {
                    place = address.locality
                }

                if (place.isEmpty() && address.subLocality != null && address.subLocality.isNotEmpty()) {
                    place = address.subLocality
                }

                if (place.isEmpty() && address.featureName != null && address.featureName.isNotEmpty()) {
                    place = address.featureName
                }
            }
            countryName = addresses[0].countryName
        }

        val currentlySummary = currently!!.summary
        val currentlyTemperature = currently.temperature
        val currentlyHumidity = currently.humidity
        val currentlyTime = currently.time
        val currentlyWindSpeed = currently.windSpeed
        val currentlyIcon = currently.icon

        val cloudCover = currently.cloudCover
        val windGust = currently.windGust
        val uvIndex = currently.uvIndex


        val currently = Currently(
            currentlySummary,
            currentlyTemperature,
            currentlyHumidity,
            currentlyTime,
            currentlyWindSpeed,
            currentlyIcon,
            cloudCover,
            windGust,
            uvIndex
        )

        val dataItemResList = getDataItemList(daily!!.data!!)
        val daily = Daily(dataItemResList)

        val hourlySummary: String = hourly!!.summary
        val hourlyData: ArrayList<HourlyItem> = getHourlyItemList(hourly.data!!)
        val hourlyIcon: String = hourly.icon

        val hourly = Hourly(hourlySummary,hourlyData,hourlyIcon)

        return Weather(place, state, countryName, longitude, latitude, currently, daily,hourly)
    }

    private fun getHourlyItemList(data: List<HourlyItemRes>): ArrayList<HourlyItem> {
        val res = ArrayList<HourlyItem>()

        for(itemIndex in 0 until 25)
        {
            val item = data[itemIndex]
            val time = item.time
            val icon = item.icon
            val temperature = item.temperature
            res.add(HourlyItem(time,icon,temperature))
        }

        return res
    }

    private fun getDataItemList(dataItemRes : List<DataItemRes>) : ArrayList<DataItem>
    {
        val res : ArrayList<DataItem> = ArrayList()

        for(item in dataItemRes)
        {
            val icon = item.icon
            val apparentTemperatureMin = item.apparentTemperatureMin
            val precipProbability = item.precipProbability
            val apparentTemperatureMax = item.apparentTemperatureMax
            val time = item.time

            val dataItem = DataItem(
                icon,
                apparentTemperatureMin,
                precipProbability,
                apparentTemperatureMax,
                time
            )
            res.add(dataItem)
        }
        return res
    }

}