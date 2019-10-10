package app.globe.com.weatherglobe.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.globe.com.weatherglobe.custom.SingleLiveEvent
import app.globe.com.weatherglobe.utils.DateTimeUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimeViewModel @Inject
constructor() : ViewModel() {

    private var disposable : CompositeDisposable?=null
    private var timeDisposable : Disposable?=null

    private var time : Long = Calendar.getInstance().timeInMillis
    private val timeData = SingleLiveEvent<Long>()
    init {
        disposable = CompositeDisposable()
    }



    fun getTime() : LiveData<Long>
    {
        return timeData
    }

    fun getDay() : String
    {
        return DateTimeUtil.getDayOfWeek(time,Calendar.SHORT)
    }

    fun getTime(is24TimeFormat: Boolean): String {

        return DateTimeUtil.getTime(time,is24TimeFormat)
    }

    fun getDate(isMonthFirst : Boolean) :String
    {
        return DateTimeUtil.getDate(time,isMonthFirst)
    }

    fun getAMPM() : String{
        return DateTimeUtil.getAMPM(time)
    }

    fun startTime() {

        timeDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap {
                    return@flatMap Observable.create<Long> { emitter ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.MILLISECOND,0)
                        time = cal.timeInMillis
                        emitter.onNext(cal.timeInMillis)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timeData.value = it
                }
        disposable!!.add(timeDisposable!!)
    }

    fun pauseTime() {
        disposable!!.dispose()
        disposable = CompositeDisposable()
    }

    fun resumeTime() {
        startTime()
    }

    override fun onCleared() {
        super.onCleared()
        if(disposable!=null) {
            disposable!!.dispose()
            disposable = null
        }
    }

}