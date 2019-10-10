package app.globe.com.weatherglobe.ui.fragments.home

import android.location.Geocoder
import android.location.Location
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.eventbus.LocationFailureMessageEvent
import app.globe.com.weatherglobe.eventbus.LocationMessageEvent
import app.globe.com.weatherglobe.preference.SharedPrefs
import app.globe.com.weatherglobe.ui.activities.MainActivity
import app.globe.com.weatherglobe.ui.base.BaseFragment
import app.globe.com.weatherglobe.utils.*
import app.globe.com.weatherglobe.viewmodels.HomeViewModel
import app.globe.com.weatherglobe.viewmodels.TimeViewModel
import app.globe.com.weatherglobe.viewmodels.ViewModelFactory
import app.globe.com.weatherglobe.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import mumayank.com.airlocationlibrary.AirLocation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    @set:Inject
    internal var viewModelFactory: ViewModelFactory? = null


    private var homeViewModel: HomeViewModel? = null


    private var currentWeather: Weather? = null

    private var tempUnits: Int = 0
    private var distanceUnits: Int = 0
    private var dateFormat = 0
    private var timeFormat = 0

    @Inject
    lateinit var prefs: SharedPrefs

    private lateinit var geoCoder: Geocoder

    private var needToRefreshWeather = false

    override fun onRefresh() {

        val currentLocation = (activity as MainActivity).getCurrentLocation()

        if (currentLocation == null) {
            Toast.makeText(
                context,
                getString(app.globe.com.weatherglobe.R.string.set_location_before_loading_weather),
                Toast.LENGTH_LONG
            ).show()
        } else {
            initUI()
            (activity as MainActivity).enableLocationMenuItem(false)
            swipe_to_refresh.isEnabled = false


            //we don't want to show the date and time without the weather data
            currentWeather = null


            loading_message.visibility = View.VISIBLE
            loading_message.text = getString(app.globe.com.weatherglobe.R.string.fetching_weather)
            reloadWeather(currentLocation)
        }
    }


    private fun reloadWeather(location: Location) {
        (activity as MainActivity).enableLocationMenuItem(false)
        val lat = String.format("%.4f", location.latitude).toDouble()
        val lng = String.format("%.4f", location.longitude).toDouble()
        homeViewModel!!.reloadWeatherForecast(geoCoder, lat, lng)
    }

    private fun loadWeather(location: Location) {
        (activity as MainActivity).enableLocationMenuItem(false)
        val lat = String.format("%.4f", location.latitude).toDouble()
        val lng = String.format("%.4f", location.longitude).toDouble()

        homeViewModel!!.loadWeatherForecast(geoCoder, lat, lng)
    }

    override fun layoutRes(): Int {
        return app.globe.com.weatherglobe.R.layout.home_fragment
    }


    override fun initLogic() {
        geoCoder = Geocoder(activity, Locale.getDefault())

        tempUnits = prefs.getTempUnit()
        distanceUnits = prefs.getDistanceUnit()
        dateFormat = prefs.getDateFormat()
        timeFormat = prefs.getTimeFormat()
        loadGPS()
    }

    override fun initObservables() {

        val subWeatherViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(WeatherViewModel::class.java)

        val subTimeViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(TimeViewModel::class.java)

        homeViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(HomeViewModel::class.java)

        homeViewModel!!.setViewModels(subWeatherViewModel, subTimeViewModel)


        homeViewModel!!.getTime().observe(this, Observer<Long> { time ->

            showCurrentDate()

            val lastTimeAppOpened = prefs.getLastTimeAppWasOpened()
            needToRefreshWeather = DateTimeUtil.needToReloadWeather(lastTimeAppOpened, time)

            val currentLocation = (activity as MainActivity).getCurrentLocation()

            if (needToRefreshWeather && currentLocation!=null) {
                (activity as MainActivity).transition()
                prefs.setLastTimeAppWasOpened(Calendar.getInstance().timeInMillis)
                reloadWeather(currentLocation)
            }
        })

        prefs.getDateFormatLivePreference()
            .observe(this, Observer<String> { dateFormatIndex ->
                if (dateFormat != dateFormatIndex.toInt()) {
                    current_date.text =
                        homeViewModel!!.getDate(dateFormatIndex.toInt() == DATE_FORMAT_MONTH_FIRST)
                    dateFormat = dateFormatIndex.toInt()
                }
            })

        prefs.getTimeFormatLivePreference()
            .observe(this, Observer<String> { timeFormatIndex ->
                if (timeFormat != timeFormatIndex.toInt()) {
                    current_time.text =
                        homeViewModel!!.getTime(timeFormatIndex.toInt() == TIME_FORMAT_24_HOUR)
                    timeFormat = timeFormatIndex.toInt()
                }
            })


        prefs.getTempUnitsLivePreference().observe(this, Observer<String> { tempIndexStr ->
            if (currentWeather != null && tempIndexStr.toInt() != tempUnits) {
                changeTempUnit(tempIndexStr.toInt(), false)
                tempUnits = tempIndexStr.toInt()
            }
        })

        prefs.getDistanceUnitsLivePreference()
            .observe(this, Observer<String> { distanceIndexStr ->
                if (currentWeather != null && distanceIndexStr.toInt() != distanceUnits) {
                    changeDistanceUnit(distanceIndexStr.toInt(), false)
                    distanceUnits = distanceIndexStr.toInt()
                }
            })

        homeViewModel!!.getWeather().observe(this, Observer<Weather> { weather ->


            (activity as MainActivity).enableLocationMenuItem(true)

            currentWeather = weather

            changeTempUnit(prefs.getTempUnit(), true)
            changeDistanceUnit(prefs.getDistanceUnit(), true)

            current_temperature_icon.setImageResource(DataItemUtil.getDayIcon(weather.currently!!.icon))

            showCurrentDate()

            val sign = if (tempUnits == TEMPERATURE_C) "C" else "F"
            showTemperature(sign)

            current_temperature_description.text = currentWeather!!.currently!!.summary

            location.text = getLocationDetails()

            showHumidityWindSpeed()

            day_time_divider.visibility = View.VISIBLE
            vertical_separator.visibility = View.VISIBLE
            vertical_separator2.visibility = View.VISIBLE

        })

        homeViewModel!!.getWeatherError().observe(this, Observer<Boolean> { isError ->
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

        homeViewModel!!.getWeatherLoading().observe(this, Observer<Boolean> { isLoading ->
            swipe_to_refresh.isRefreshing = false
            if (isLoading != null) {
                loading_view.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    loading_message.visibility = View.GONE
//                    weatherRecycler.visibility = View.GONE
                }
            }
        })
    }


    private fun showCurrentDate() {
        if (currentWeather != null) {
            val is24TimeFormat = prefs.getTimeFormat() == TIME_FORMAT_24_HOUR
            current_time.text = homeViewModel!!.getTime(is24TimeFormat)

            val isDayFirstDateFormat =
                prefs.getDateFormat() == DATE_FORMAT_MONTH_FIRST
            current_date.text = homeViewModel!!.getDate(isDayFirstDateFormat)


            current_day.text = homeViewModel!!.getDay()
            time_format.text = if (!is24TimeFormat) homeViewModel!!.getAMPM() else ""
        }
    }

    override fun initUI() {
        swipe_to_refresh.setOnRefreshListener(this)
        initFields()
    }

    private fun initFields() {
        current_temperature.text = ""
        current_temperature_description.text = ""
        humidity.text = ""
        wind_speed.text = ""
        location.text = ""
        loading_message.text = ""

        humidity_title.text = ""
        wind_speed_title.text = ""
        current_temperature_icon.setImageResource(0)
        current_time.text = ""
        current_date.text = ""
        time_format.text = ""
        temperature_units.text = ""
        current_day.text = ""

        day_time_divider.visibility = View.GONE
        vertical_separator.visibility = View.GONE
        vertical_separator2.visibility = View.GONE

        loading_view.visibility = View.VISIBLE


    }


    private fun loadGPS() {
        initUI()

        swipe_to_refresh.isEnabled = false
        loading_message.text = getString(app.globe.com.weatherglobe.R.string.loading_gps_position)

        (activity as MainActivity).initLocation()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(locationEvent: LocationMessageEvent) {

        loading_message.text = getString(app.globe.com.weatherglobe.R.string.fetching_weather)

        val currentTime = Calendar.getInstance().timeInMillis
        val lastTimeAppOpened = prefs.getLastTimeAppWasOpened()

        val needRefreshWeather = DateTimeUtil.needToReloadWeather(lastTimeAppOpened, currentTime)

        if(!needRefreshWeather) {
            loadWeather(locationEvent.location)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(locationFailed: LocationFailureMessageEvent) {


        val currentLocation = (activity as MainActivity).getCurrentLocation()
        if (currentLocation != null)
            return

        val errorStr = when (locationFailed.locationFailed) {
            AirLocation.LocationFailedEnum.DeviceInFlightMode -> {
                getString(app.globe.com.weatherglobe.R.string.device_is_in_flight_mode)
            }
            AirLocation.LocationFailedEnum.HighPrecisionNA_TryAgainPreferablyWithInternet -> {
                getString(app.globe.com.weatherglobe.R.string.high_precision_gps_na)
            }
            AirLocation.LocationFailedEnum.LocationOptimizationPermissionNotGranted -> {
                getString(app.globe.com.weatherglobe.R.string.location_optimization_permission_not_granted)
            }
            AirLocation.LocationFailedEnum.LocationPermissionNotGranted -> {
                getString(app.globe.com.weatherglobe.R.string.location_permission_not_granted)
            }
        }

        loading_message.visibility = View.VISIBLE
        loading_message.text = errorStr
        loading_view.visibility = View.GONE
        swipe_to_refresh.isRefreshing = false

        swipe_to_refresh.isEnabled = false

        (activity as MainActivity).enableLocationMenuItem(true)

    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onPause() {
        super.onPause()
        homeViewModel!!.pauseTime()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel!!.resumeTime()
    }



    private fun showTemperature(sign: String) {
        temperature_units.text = sign
        current_temperature.text = String.format(
            getString(app.globe.com.weatherglobe.R.string.temp_holder),
            currentWeather!!.currently!!.temperature.toInt()
        )
    }

    private fun showHumidityWindSpeed() {
        humidity_title.text = getString(app.globe.com.weatherglobe.R.string.humidity)
        wind_speed_title.text = getString(app.globe.com.weatherglobe.R.string.wind_speed)

        val distanceUnitsPostfix = if (prefs.getDistanceUnit() == DISTANCE_KILOMETER)
            getString(app.globe.com.weatherglobe.R.string.kph) else getString(app.globe.com.weatherglobe.R.string.mph)

        humidity.text = String.format(
            getString(app.globe.com.weatherglobe.R.string.humidity_holder),
            (currentWeather!!.currently!!.humidity * 100).toInt()
        )
        wind_speed.text = String.format(
            getString(app.globe.com.weatherglobe.R.string.wind_speed_holder),
            currentWeather!!.currently!!.windSpeed.toInt(),
            distanceUnitsPostfix
        )

    }

    private fun getLocationDetails(): String {

        return if (currentWeather!!.countryName == "United States") String.format(
            "%s, %s",
            currentWeather!!.place,
            currentWeather!!.state
        ) else String.format("%s, %s", currentWeather!!.place, currentWeather!!.countryName)
    }


    private fun changeTempUnit(tempUnits: Int, loadingResults: Boolean) {
        if (tempUnits == TEMPERATURE_C) {
            currentWeather!!.currently!!.temperature =
                DataItemUtil.toCelsius(currentWeather!!.currently!!.temperature)
            showTemperature("C")
        }
        //change the units only if user change it manually, otherwise the temperature will be in fahrenheit already
        else if (tempUnits == TEMPERATURE_F && !loadingResults) {
            currentWeather!!.currently!!.temperature =
                DataItemUtil.toFahrenheit(currentWeather!!.currently!!.temperature)
            showTemperature("F")
        }


    }

    private fun changeDistanceUnit(distanceUnit: Int, loadingResults: Boolean) {
        if (distanceUnit == DISTANCE_MILE) {
            currentWeather!!.currently!!.windSpeed =
                DataItemUtil.toMillle(currentWeather!!.currently!!.windSpeed)
        }
        //change the units only if user change it manually, otherwise the distance will be in kilometer already
        else if (distanceUnit == DISTANCE_KILOMETER && !loadingResults) {
            currentWeather!!.currently!!.windSpeed =
                DataItemUtil.toKilometer(currentWeather!!.currently!!.windSpeed)
        }

        showHumidityWindSpeed()

    }


}