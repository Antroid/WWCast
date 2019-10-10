package app.globe.com.weatherglobe.viewmodels

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.preference.SharedPrefs
import app.globe.com.weatherglobe.repositories.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class WeatherViewModel @Inject
constructor() : ViewModel() {
    private var disposable: CompositeDisposable? = null

    private val weatherHomeData = MutableLiveData<Weather>()
    private val weatherForecastData = MutableLiveData<Weather>()
    private val errorLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var prefs : SharedPrefs

    init {
        disposable = CompositeDisposable()
    }

    internal fun getHomeWeather(): LiveData<Weather> {
        return weatherHomeData
    }

    internal fun getForecastWeather() : LiveData<Weather>
    {
        return weatherForecastData
    }

    internal fun getLoading(): LiveData<Boolean> {
        return loading
    }

    internal fun getError(): LiveData<Boolean> {
        return errorLoadError
    }

    fun loadWeatherForecast(geoCoder : Geocoder,
                            lat: Double, lng: Double) {

        disposable!!.add(
            weatherRepository.getWeather(geoCoder,lat, lng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(WeatherDisposableObserver())
        )
    }

    fun reloadWeatherForecast(geoCoder : Geocoder,
                              lat: Double, lng: Double)
    {
        disposable!!.add(weatherRepository.reloadWeather(geoCoder,lat,lng)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(WeatherDisposableObserver()))
    }


    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

    private inner class WeatherDisposableObserver : DisposableObserver<Weather>(){
        override fun onComplete() {
        }

        override fun onNext(t: Weather) {
            weatherHomeData.value = t
            weatherForecastData.value = t


            errorLoadError.value = false
            loading.value = false
        }

        override fun onError(e: Throwable) {
            errorLoadError.value = true
            loading.value = false
        }
    }

}