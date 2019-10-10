package app.globe.com.weatherglobe.ui.fragments.forecast

import android.location.Geocoder
import android.location.Location
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.db.models.DataItem
import app.globe.com.weatherglobe.db.models.HourlyItem
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.eventbus.LocationFailureMessageEvent
import app.globe.com.weatherglobe.eventbus.LocationMessageEvent
import app.globe.com.weatherglobe.preference.SharedPrefs
import app.globe.com.weatherglobe.ui.activities.MainActivity
import app.globe.com.weatherglobe.ui.base.BaseFragment
import app.globe.com.weatherglobe.utils.*
import app.globe.com.weatherglobe.viewmodels.TimeViewModel
import app.globe.com.weatherglobe.viewmodels.ViewModelFactory
import app.globe.com.weatherglobe.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.forecast_fragment.*
import mumayank.com.airlocationlibrary.AirLocation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


class ForecastFragment : BaseFragment(), DaySelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private var weatherViewModel: WeatherViewModel? = null
    private var timeViewModel : TimeViewModel?=null

    private var hourlyForecastAdapter: HourlyForecastAdapter? = null
    private var dailyForecastAdapter : DailyForecastAdapter? = null
    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var prefs: SharedPrefs

    private var timeFormat : Int = 0
    private var tempUnits : Int = 0

    private var hourlyData : ArrayList<HourlyItem>?=null
    private var dailyData : ArrayList<DataItem>?=null

    private var currentWeather : Weather?=null

    override fun layoutRes(): Int {
        return R.layout.forecast_fragment
    }

    override fun onRefresh() {

        val currentLocation = (activity as MainActivity).getCurrentLocation()

        if (currentLocation == null) {
            Toast.makeText(
                context,
                getString(R.string.set_location_before_loading_weather),
                Toast.LENGTH_LONG
            ).show()
        } else {
            initUI()
            (activity as MainActivity).enableLocationMenuItem(false)
            swipe_to_refresh.isEnabled = false
            loading_view.visibility = View.VISIBLE

            //we don't want to show the date and time without the weather data
            currentWeather = null


            loading_message.visibility = View.VISIBLE
            loading_message.text = getString(R.string.fetching_weather)
            reloadWeather(currentLocation)
        }
    }


    override fun initLogic() {

        swipe_to_refresh.isEnabled = false
        loading_message.text = getString(R.string.loading_gps_position)

        loading_message.visibility = View.VISIBLE
        loading_view.visibility = View.VISIBLE

        (activity as MainActivity).initLocation()
    }

    override fun initUI() {


        initFields()

        swipe_to_refresh.setOnRefreshListener(this)

        val layoutManager1 = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager2 = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)

        hourly_forecast_recycle_view.layoutManager = layoutManager1
        daily_forecast_recycle_view.layoutManager = layoutManager2

        tempUnits = prefs.getTempUnit()
        timeFormat = prefs.getTimeFormat()

        hourlyForecastAdapter =
            HourlyForecastAdapter(tempUnits == TEMPERATURE_C,timeFormat== TIME_FORMAT_24_HOUR)
        hourly_forecast_recycle_view.adapter = hourlyForecastAdapter

        dailyForecastAdapter = DailyForecastAdapter(tempUnits== TEMPERATURE_C,this)
        daily_forecast_recycle_view.adapter = dailyForecastAdapter
    }

    private fun reloadWeather(location: Location) {
        val geoCoder = Geocoder(context)
        (activity as MainActivity).enableLocationMenuItem(false)
        val lat = String.format("%.4f", location.latitude).toDouble()
        val lng = String.format("%.4f", location.longitude).toDouble()
        weatherViewModel!!.reloadWeatherForecast(geoCoder, lat, lng)
    }

    private fun loadWeather(location: Location) {
        val geoCoder = Geocoder(context)
        (activity as MainActivity).enableLocationMenuItem(false)
        val lat = String.format("%.4f", location.latitude).toDouble()
        val lng = String.format("%.4f", location.longitude).toDouble()
        weatherViewModel!!.loadWeatherForecast(geoCoder, lat, lng)
    }

    override fun initObservables() {
        weatherViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(WeatherViewModel::class.java)

        timeViewModel = ViewModelProviders.of(this,viewModelFactory).get(TimeViewModel::class.java)


        prefs.getTimeFormatLivePreference()
            .observe(this, Observer<String> { timeFormatIndex ->
                if (timeFormat != timeFormatIndex.toInt()) {

                    hourlyForecastAdapter = HourlyForecastAdapter(tempUnits == TEMPERATURE_C,timeFormatIndex.toInt()== TIME_FORMAT_24_HOUR)
                    hourlyForecastAdapter!!.setData(hourlyData!!)
                    hourly_forecast_recycle_view.adapter = hourlyForecastAdapter

                    timeFormat = timeFormatIndex.toInt()
                }
            })

        timeViewModel!!.getTime().observe(this,Observer<Long>{ time->

            val lastTimeAppOpened = prefs.getLastTimeAppWasOpened()
            val needToRefreshWeather = DateTimeUtil.needToReloadWeather(lastTimeAppOpened, time)

            val currentLocation = (activity as MainActivity).getCurrentLocation()

            if (needToRefreshWeather && currentLocation!=null) {
                (activity as MainActivity).transition()
                prefs.setLastTimeAppWasOpened(Calendar.getInstance().timeInMillis)
                reloadWeather(currentLocation)
            }
        })

        weatherViewModel!!.getForecastWeather().observe(this, Observer<Weather> { weather ->

            daily_hourly_forecast_separator.visibility = View.VISIBLE
            hourly_forecast_separator.visibility = View.VISIBLE

            wind_speed_txt.text = getString(R.string.wind_speed)
            wind_gust_txt.text = getString(R.string.wind_gust)
            cloud_cover_txt.text = getString(R.string.cloud_cover)
            uv_index_txt.text = getString(R.string.uv_index)
            summery_txt.text = getString(R.string.summery)

            currentWeather = weather

            hourlyData = weather.hourly!!.data!!
            hourlyForecastAdapter!!.setData(hourlyData!!)

            dailyData = weather.daily!!.data!!
            dailyForecastAdapter!!.setData(dailyData!!)


            summery.text = weather.currently!!.summary

            cloud_cover.text = String.format(getString(R.string.cloud_cover_holder),(100*weather.currently!!.cloudCover).toInt())

            uv_index.text = String.format("${weather.currently!!.uvIndex}")

            showWindSpecs(prefs.getDistanceUnit())
        })

        weatherViewModel!!.getError().observe(this, Observer<Boolean> { isError ->
            if (isError != null) {
                swipe_to_refresh.isRefreshing = false

                (activity as MainActivity).enableLocationMenuItem(true)

//                reloadLocationBtn.isEnabled = true
                swipe_to_refresh.isEnabled = true

                if (isError) {
                    initFields()

                    loading_message.visibility = View.VISIBLE
                    loading_message.text =
                        getString(app.globe.com.weatherglobe.R.string.error_loading_data)
                } else {

                    loading_message.visibility = View.GONE
                    loading_message.text = null
                }
            }
        })

        weatherViewModel!!.getLoading().observe(this, Observer<Boolean> { isLoading ->
            swipe_to_refresh.isRefreshing = false
            if (isLoading != null) {
                loading_view.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    loading_message.visibility = View.GONE
                }
            }
        })

        prefs.getTempUnitsLivePreference().observe(this, Observer<String> { tempIndexStr ->

            if(tempIndexStr.toInt()!=tempUnits && currentWeather!=null) {

                val tempInCelsius = tempIndexStr.toInt() == TEMPERATURE_C

                hourlyForecastAdapter = HourlyForecastAdapter(tempInCelsius,timeFormat== TIME_FORMAT_24_HOUR)
                hourlyForecastAdapter!!.setData(hourlyData!!)
                hourly_forecast_recycle_view.adapter = hourlyForecastAdapter

                dailyForecastAdapter = DailyForecastAdapter(tempInCelsius,this)
                dailyForecastAdapter!!.setData(dailyData!!)
                daily_forecast_recycle_view.adapter = dailyForecastAdapter


                tempUnits = tempIndexStr.toInt()
            }
        })

        prefs.getDistanceUnitsLivePreference().observe(this,Observer<String>{ distanceIndex->

            if(currentWeather!=null) {
                showWindSpecs(distanceIndex.toInt())
            }
        })

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(locationEvent: LocationMessageEvent) {

        loading_message.text = getString(R.string.fetching_weather)

        val currentTime = Calendar.getInstance().timeInMillis
        val lastTimeAppOpened = prefs.getLastTimeAppWasOpened()

        val needRefreshWeather = DateTimeUtil.needToReloadWeather(lastTimeAppOpened, currentTime)

        if(!needRefreshWeather) {
            loadWeather(locationEvent.location)
        }

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        timeViewModel!!.resumeTime()
    }

    override fun onPause() {
        super.onPause()
        timeViewModel!!.pauseTime()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(locationFailed: LocationFailureMessageEvent) {


        val currentLocation = (activity as MainActivity).getCurrentLocation()
        if (currentLocation != null)
            return

        val errorStr = when (locationFailed.locationFailed) {
            AirLocation.LocationFailedEnum.DeviceInFlightMode -> {
                getString(R.string.device_is_in_flight_mode)
            }
            AirLocation.LocationFailedEnum.HighPrecisionNA_TryAgainPreferablyWithInternet -> {
                getString(R.string.high_precision_gps_na)
            }
            AirLocation.LocationFailedEnum.LocationOptimizationPermissionNotGranted -> {
                getString(R.string.location_optimization_permission_not_granted)
            }
            AirLocation.LocationFailedEnum.LocationPermissionNotGranted -> {
                getString(R.string.location_permission_not_granted)
            }
        }

        loading_message.visibility = View.VISIBLE
        loading_message.text = errorStr
        loading_view.visibility = View.GONE
        swipe_to_refresh.isRefreshing = false

        swipe_to_refresh.isEnabled = false

        (activity as MainActivity).enableLocationMenuItem(true)

    }

    private fun initFields()
    {
        wind_gust_txt.text = ""
        wind_speed_txt.text = ""
        uv_index_txt.text = ""
        summery_txt.text = ""
        cloud_cover_txt.text =""

        daily_hourly_forecast_separator.visibility = View.GONE
        hourly_forecast_separator.visibility = View.GONE

        wind_gust.text = ""
        wind_speed.text = ""
        uv_index.text = ""
        summery.text = ""
        cloud_cover.text =""
    }

    private fun showWindSpecs(distanceUnit : Int) {
        var windPostfix = "kph"
        var windGust = currentWeather!!.currently!!.windGust
        var windSpeed = currentWeather!!.currently!!.windSpeed

        if(distanceUnit== DISTANCE_MILE)
        {
            windPostfix = "mph"
            windGust =  DataItemUtil.toMillle(currentWeather!!.currently!!.windGust)
            windSpeed = DataItemUtil.toMillle(currentWeather!!.currently!!.windSpeed)
        }
        wind_gust.text = String.format(getString(R.string.wind_speed_holder),windGust.toInt(),windPostfix)
        wind_speed.text = String.format(getString(R.string.wind_speed_holder),windSpeed.toInt(),windPostfix)
    }

    override fun onDaySelect(item: DataItem) {

    }




}