package app.globe.com.weatherglobe.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module


/**
 * This is the app's Dagger module. We use this to bind our Application class as a Context in the AppComponent.
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 * through Dagger.Android our Application & Activities are provided into your graph for us.
 * {@link
 * AppComponent}.
 */

@Module
abstract class ContextModule {

    @Binds
    internal abstract fun provideContext(application: Application): Context
}