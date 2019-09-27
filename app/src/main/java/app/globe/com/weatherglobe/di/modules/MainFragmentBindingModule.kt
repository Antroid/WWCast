package app.globe.com.weatherglobe.di.modules

import app.globe.com.weatherglobe.ui.fragments.weather.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun provideWeatherFragment(): WeatherFragment



}