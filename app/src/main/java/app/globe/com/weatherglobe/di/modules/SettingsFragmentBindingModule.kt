package app.globe.com.weatherglobe.di.modules

import app.globe.com.weatherglobe.ui.fragments.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun provideSettingsFragment(): SettingsFragment

}