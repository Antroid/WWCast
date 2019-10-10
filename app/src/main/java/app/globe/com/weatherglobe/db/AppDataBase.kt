package app.globe.com.weatherglobe.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.globe.com.weatherglobe.db.models.Weather


@Database(entities = [Weather::class],
    version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

}
