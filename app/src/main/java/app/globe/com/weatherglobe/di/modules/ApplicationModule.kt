package app.globe.com.weatherglobe.di.modules

import android.app.Application
import androidx.room.Room
import app.globe.com.weatherglobe.BuildConfig
import app.globe.com.weatherglobe.data.WeatherService
import app.globe.com.weatherglobe.db.AppDatabase
import app.globe.com.weatherglobe.db.WeatherDao
import app.globe.com.weatherglobe.preference.SharedPrefs
import app.globe.com.weatherglobe.utils.DB_NAME
import app.globe.com.weatherglobe.utils.WEATHER_BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(application: Application): SharedPrefs {
        return SharedPrefs(application)
    }

    @Provides
    @Singleton
    fun provideApplicationDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(dataBase: AppDatabase): WeatherDao {
        return dataBase.weatherDao()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(): Retrofit {

        if(BuildConfig.DEBUG) {
            return Retrofit.Builder().baseUrl(WEATHER_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        }
        return Retrofit.Builder().baseUrl(WEATHER_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

}