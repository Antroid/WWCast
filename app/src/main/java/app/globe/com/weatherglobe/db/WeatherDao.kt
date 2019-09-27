package app.globe.com.weatherglobe.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.globe.com.weatherglobe.db.models.Weather
import io.reactivex.Maybe

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather where latitude = :lat and longitude = :lng")
    fun getWeatherForecastByLocation(lat: Double, lng: Double): Maybe<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)

    @Query("SELECT count(*) FROM weather where latitude = :lat and longitude = :lng")
    fun hasWeatherForecastByLocation(lat: Double, lng: Double) : Int

}