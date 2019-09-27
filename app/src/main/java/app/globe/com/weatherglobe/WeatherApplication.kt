package app.globe.com.weatherglobe

import app.globe.com.weatherglobe.BuildConfig.DEBUG
import app.globe.com.weatherglobe.di.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.acra.ACRA
import org.acra.annotation.AcraMailSender
import org.acra.config.CoreConfigurationBuilder
import org.acra.data.StringFormat
import timber.log.Timber


/**
 * We create a custom Application class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @AppScoped Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
@AcraMailSender(mailTo = "casterapps@gmail.com")
class WeatherApplication : DaggerApplication()
{
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
       return DaggerApplicationComponent.builder().application(this).build()

    }

    override fun onCreate() {
        super.onCreate()

        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val builder = CoreConfigurationBuilder(this)
        builder.setBuildConfigClass(BuildConfig::class.java).setReportFormat(StringFormat.JSON)
        ACRA.init(this,builder)
    }

}