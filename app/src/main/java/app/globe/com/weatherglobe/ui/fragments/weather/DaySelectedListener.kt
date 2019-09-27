package app.globe.com.weatherglobe.ui.fragments.weather

import app.globe.com.weatherglobe.db.models.DataItem

interface DaySelectedListener
{
    fun onDaySelect(item : DataItem)
}