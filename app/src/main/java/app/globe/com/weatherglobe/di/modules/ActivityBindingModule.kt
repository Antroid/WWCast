package app.globe.com.weatherglobe.di.modules

import app.globe.com.weatherglobe.ui.activities.MainActivity
import app.globe.com.weatherglobe.ui.activities.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of
 * whichever module ActivityBindingModule is on (AppComponent, here).
 * we never need to tell AppComponent that it is going to have all or any of these subcomponents
 * nor do we need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and
 * be aware of a scope annotation @ActivityScoped
 * In this case, when Dagger.Android annotation processor runs it will create 1 subcomponent for us
 */

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [(MainFragmentBindingModule::class)])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [(SettingsFragmentBindingModule::class)])
    internal abstract fun bindSettingsActivity(): SettingsActivity


}