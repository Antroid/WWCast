package app.globe.com.weatherglobe.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.repositories.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class WeatherViewModel @Inject
constructor() : ViewModel() {
    private var disposable: CompositeDisposable? = null

    private val weatherData = MutableLiveData<Weather>()
    private val errorLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()


    @Inject
    lateinit var weatherRepository: WeatherRepository


    init {
        disposable = CompositeDisposable()
    }

    internal fun getWeather(): LiveData<Weather> {
        return weatherData
    }

    internal fun getLoading(): LiveData<Boolean> {
        return loading
    }

    internal fun getError(): LiveData<Boolean> {
        return errorLoadError
    }

//    internal fun reloadTemperature(tempUnitIndex : Int)
//    {
//        weatherData.value = changeTempUnit(tempUnitIndex,weatherData.value!!,false)
//    }
//
//    internal fun reloadDistance(distanceUnitIndex : Int)
//    {
//        weatherData.value = changeDistanceUnit(distanceUnitIndex,weatherData.value!!,false)
//    }

    fun loadWeatherForecast(lat: Double, lng: Double) {

        disposable!!.add(
            weatherRepository.getWeather(lat, lng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(WeatherDisposableObserver())
        )
    }

    fun reloadWeatherForecast(lat: Double, lng: Double)
    {
        disposable!!.add(weatherRepository.reloadWeather(lat,lng)
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
            weatherData.value = t

            errorLoadError.value = false
            loading.value = false
        }

        override fun onError(e: Throwable) {
            errorLoadError.value = true
            loading.value = false
        }
    }

}