package app.globe.com.weatherglobe.utils

import android.content.Context
import app.globe.com.weatherglobe.R
import java.text.SimpleDateFormat
import java.util.*


class DateTimeUtil {

    companion object {

        fun getDayOfWeekToday(con : Context, time : Long, dayFormat: Int) : String
        {

            val currentCal = GregorianCalendar()
            val cal = GregorianCalendar()
            cal.timeInMillis = time

            if(currentCal.get(Calendar.DAY_OF_YEAR)==cal.get(Calendar.DAY_OF_YEAR))
                return con.getString(R.string.today)

            return cal.getDisplayName(Calendar.DAY_OF_WEEK, dayFormat, Locale.getDefault())
        }

        fun getDayOfWeek(time: Long, dayFormat: Int): String {
            val cal = GregorianCalendar()
            cal.timeInMillis = time
            return cal.getDisplayName(Calendar.DAY_OF_WEEK, dayFormat, Locale.getDefault())
        }

        fun getTime(time: Long, is24TimeFormat: Boolean): String {

            val timeFormat = if (is24TimeFormat) "HH:mm:ss" else "hh:mm:ss"
            val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())
            return sdf.format(time)
        }

        fun getHourOfDay(time : Long, is24TimeFormat : Boolean) : String
        {
            val timeFormat = if (is24TimeFormat) "HH:00" else "hh:00 a"
            val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())
            return sdf.format(time).toLowerCase()
        }

        fun getDate(time: Long, isMonthFirst: Boolean): String {
            val dateFormat = if (isMonthFirst) "MM/dd/yyyy" else "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            return sdf.format(time)
        }

        fun getAMPM(time : Long) : String
        {
            val timeFormat = "aa"
            val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())
            return sdf.format(time)
        }

        fun getCustomTheme(day : Int): CustomTheme {

            when (day) {
                Calendar.SUNDAY -> {
                    return CustomTheme(
                        R.color.start_yellow,
                        R.color.end_yellow
                    )
                }
                Calendar.MONDAY -> {
                    return CustomTheme(
                        R.color.start_grey,
                        R.color.end_grey
                    )
                }
                Calendar.TUESDAY -> {
                    return CustomTheme(
                        R.color.start_green,
                        R.color.end_green
                    )
                }
                Calendar.WEDNESDAY -> {
                    return CustomTheme(
                        R.color.start_red,
                        R.color.end_red
                    )
                }
                Calendar.THURSDAY -> {
                    return CustomTheme(
                        R.color.start_purple,
                        R.color.end_purple
                    )
                }
                Calendar.FRIDAY -> {
                    return CustomTheme(
                        R.color.start_blue,
                        R.color.end_blue
                    )
                }
                Calendar.SATURDAY -> {
                    return CustomTheme(
                        R.color.start_turquoise,
                        R.color.end_turquoise
                    )
                }
            }
            return CustomTheme(
                R.color.start_yellow,
                R.color.end_yellow
            )

        }

        fun getDayFromTime(time : Long) : Int
        {
            val cal = Calendar.getInstance()
            cal.timeInMillis = time
            return cal.get(Calendar.DAY_OF_WEEK)
        }

        fun needToReloadWeather(lastTimeAppOpened: Long, newTime : Long): Boolean {

            val oldCal = Calendar.getInstance()
            oldCal.timeInMillis = lastTimeAppOpened

            val newCal = Calendar.getInstance()
            newCal.timeInMillis = newTime

            return oldCal.get(Calendar.DAY_OF_YEAR) != newCal.get(Calendar.DAY_OF_YEAR)

        }

    }



}
