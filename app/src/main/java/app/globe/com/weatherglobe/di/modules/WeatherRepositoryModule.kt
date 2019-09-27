package app.globe.com.weatherglobe.di.modules

import app.globe.com.weatherglobe.data.WeatherService
import app.globe.com.weatherglobe.db.WeatherDao
import app.globe.com.weatherglobe.repositories.LocalWeatherRepo
import app.globe.com.weatherglobe.repositories.RemoteWeatherRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WeatherRepositoryModule {

    @Singleton
    @Provides
    fun provideLocalWeatherRep(weatherDao : WeatherDao) : LocalWeatherRepo
    {
        return LocalWeatherRepo(weatherDao)
    }

    @Singleton
    @Provides
    fun provideRemoteWeatherRep(weatherService : WeatherService) : RemoteWeatherRepo
    {
        return RemoteWeatherRepo(weatherService)
    }

}