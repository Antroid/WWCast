package app.globe.com.weatherglobe.utils

import app.globe.com.weatherglobe.R
import java.util.*

class DataItemUtil
{
    companion object{
        fun getDayIcon(icon : String) : Int
        {
            var res : Int = R.drawable.ic_wi_clear_day
            when(icon)
            {
                "clear-day"->{
                    res = R.drawable.ic_wi_clear_day
                }
                "clear-night"->{
                    res = R.drawable.ic_wi_night_clear
                }
                "rain"->{
                    res = R.drawable.ic_wi_rain
                }

                "snow"->{
                    res = R.drawable.ic_wi_snow
                }
                "sleet"->{
                    res = R.drawable.ic_wi_sleet
                }
                "wind"->{
                    res = R.drawable.ic_wi_strong_wind
                }
                "fog"->{
                    res = R.drawable.ic_wi_fog
                }
                "cloudy"->{
                    res = R.drawable.ic_wi_cloudy
                }
                "partly-cloudy-day"->{
                    res = R.drawable.ic_wi_day_partly_cloudy
                }
                "partly-cloudy-night"->{
                    res = R.drawable.ic_wi_night_partly_cloudy
                }
                "hail"->{
                    res = R.drawable.ic_wi_hail
                }
                "thunderstorm"->{
                    res = R.drawable.ic_wi_thunderstorm
                }
                "tornado"->{
                    res = R.drawable.ic_wi_tornado
                }
            }
            return res
        }

        fun getDayOfWeek(time : Long, dayFormat : Int) : String
        {
            val cal = GregorianCalendar()
            cal.timeInMillis = time * 1000
            return cal.getDisplayName(Calendar.DAY_OF_WEEK, dayFormat, Locale.getDefault())
        }

        fun toCelsius(fahrenheit : Double) : Double{
            return (fahrenheit - 32 ) * 5 / 9
        }

        fun toFahrenheit(celsius : Double) : Double{
            return celsius * 9/5 + 32
        }

        fun toMillle(windSpeed: Double): Double {
            return windSpeed * 0.621371
        }

        fun toKilometer(windSpeed : Double) : Double{
            return windSpeed * 1.60934
        }

    }



}