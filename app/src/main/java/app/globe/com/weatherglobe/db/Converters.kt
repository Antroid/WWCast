package app.globe.com.weatherglobe.db

import androidx.room.TypeConverter
import app.globe.com.weatherglobe.db.models.Currently
import app.globe.com.weatherglobe.db.models.Daily
import app.globe.com.weatherglobe.db.models.DataItem
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun currentlyToJson(value: Currently?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToCurrently(value: String): Currently {

         return Gson().fromJson(value, Currently::class.java) as Currently
    }

    @TypeConverter
    fun dataItemListToJson(value : List<DataItem>) : String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToDataItemList(value : String) : List<DataItem>
    {
        val objects = Gson().fromJson(value, Array<DataItem>::class.java) as Array<DataItem>
        return  objects.toList()
    }

    @TypeConverter
    fun dailyToJson(value : Daily) : String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToDaily(value : String) : Daily
    {
        return Gson().fromJson(value, Daily::class.java) as Daily
    }

}