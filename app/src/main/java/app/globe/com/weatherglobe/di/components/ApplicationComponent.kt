package app.globe.com.weatherglobe.di.components

import android.app.Application
import app.globe.com.weatherglobe.di.modules.ActivityBindingModule
import app.globe.com.weatherglobe.di.modules.ApplicationModule
import app.globe.com.weatherglobe.di.modules.ContextModule
import app.globe.com.weatherglobe.di.modules.WeatherRepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * This is the root Dagger component.
 * {@link AndroidSupportInjectionModule}
 * is the module from Dagger.Android that helps with the generation
 * and location of subcomponents, which will be in our case, activities
 */

@Singleton
@Component(modules = [ContextModule::class, ApplicationModule::class,
                      AndroidSupportInjectionModule::class, ActivityBindingModule::class,
                      WeatherRepositoryModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: Application)

}