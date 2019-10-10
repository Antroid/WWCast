package app.globe.com.weatherglobe.di.modules

import app.globe.com.weatherglobe.ui.fragments.forecast.ForecastFragment
import app.globe.com.weatherglobe.ui.fragments.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun provideWeatherFragment(): HomeFragment

    @ContributesAndroidInjector
    internal abstract fun provideForecastFragment(): ForecastFragment


}