package app.globe.com.weatherglobe.ui.fragments.weather

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.db.models.DataItem
import app.globe.com.weatherglobe.db.models.Weather
import app.globe.com.weatherglobe.eventbus.LocationFailureMessageEvent
import app.globe.com.weatherglobe.eventbus.LocationMessageEvent
import app.globe.com.weatherglobe.preference.SharedPrefs
import app.globe.com.weatherglobe.ui.activities.MainActivity
import app.globe.com.weatherglobe.ui.base.BaseFragment
import app.globe.com.weatherglobe.utils.DISTANCE
import app.globe.com.weatherglobe.utils.DataItemUtil
import app.globe.com.weatherglobe.utils.TEMPERATURE
import app.globe.com.weatherglobe.viewmodels.ViewModelFactory
import app.globe.com.weatherglobe.viewmodels.WeatherViewModel
import butterknife.BindView
import com.google.android.material.button.MaterialButton
import mumayank.com.airlocationlibrary.AirLocation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


class WeatherFragment : BaseFragment(),
    DaySelectedListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.load_my_location)
    lateinit var reloadLocationBtn: MaterialButton

    @BindView(R.id.swipe_to_refresh)
    lateinit var swipeToRefresh: SwipeRefreshLayout

    @BindView(R.id.current_temperature)
    lateinit var currentTemperatureText: TextView

    @BindView(R.id.current_temperature_description)
    lateinit var currentTemperatureDescriptionText: TextView

    @BindView(R.id.day_description)
    lateinit var dayDescriptionText: TextView

    @BindView(R.id.day_stats)
    lateinit var dayStatsText: TextView

    @BindView(R.id.forecast_recycle_view)
    lateinit var weatherRecycler: RecyclerView

    @BindView(R.id.loading_message)
    lateinit var loadingMessageText: TextView

    @BindView(R.id.loading_view)
    lateinit var loadingView: View

    @set:Inject
    internal var viewModelFactory: ViewModelFactory? = null


    private var viewModel: WeatherViewModel? = null

    private var currentLocation: Location? = null

    private var currentWeather : Weather?=null

    private var dailyWeatherAdapter : DailyForecastAdapter?=null

    private var tempUnits : Int = 0
    private var distanceUnits : Int = 0

    @Inject
    lateinit var prefs : SharedPrefs

    override fun onRefresh() {

        if (currentLocation == null) {
            Toast.makeText(
                context,
                getString(R.string.set_location_before_loading_weather),
                Toast.LENGTH_LONG
            ).show()
        } else {
            initUI()

            reloadLocationBtn.isEnabled = false
            swipeToRefresh.isEnabled = false


            loadingMessageText.visibility = View.VISIBLE
            loadingMessageText.text = getString(R.string.fetching_weather)
            reloadWeather(currentLocation!!)
        }
    }

    private fun initUI() {
        currentTemperatureText.text = ""
        currentTemperatureDescriptionText.text = ""
        dayDescriptionText.text = ""
        dayStatsText.text = ""
        loadingMessageText.text = ""

        weatherRecycler.visibility = View.GONE
        loadingView.visibility = View.VISIBLE

    }

    private fun reloadWeather(location: Location) {
        val lat = String.format("%.4f", location.latitude).toDouble()
        val lng = String.format("%.4f", location.longitude).toDouble()
        viewModel!!.reloadWeatherForecast(lat, lng)
    }

    private fun loadWeather(location: Location) {
        val lat = String.format("%.4f", location.latitude).toDouble()
        val lng = String.format("%.4f", location.longitude).toDouble()
        viewModel!!.loadWeatherForecast(lat, lng)
    }

    override fun layoutRes(): Int {
        return R.layout.weather_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(activity)

        tempUnits = prefs.getTempUnit()
        distanceUnits = prefs.getDistanceUnit()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel::class.java)

        swipeToRefresh.setOnRefreshListener(this)

        reloadLocationBtn.setOnClickListener {
            loadGPS()
        }

        dailyWeatherAdapter = DailyForecastAdapter(this)
        weatherRecycler.adapter = dailyWeatherAdapter


        weatherRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        observableViewModel()
    }

    private fun loadGPS() {
        initUI()

        reloadLocationBtn.isEnabled = false
        swipeToRefresh.isEnabled = false
        loadingMessageText.text = getString(R.string.loading_gps_position)

        (activity as MainActivity).initLocation()
    }

    override fun onDaySelect(item: DataItem) {
        //TODO future features
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(locationEvent: LocationMessageEvent) {
        currentLocation = locationEvent.location

        loadingMessageText.text = getString(R.string.fetching_weather)
        loadWeather(currentLocation!!)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(locationFailed: LocationFailureMessageEvent) {

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

        loadingMessageText.visibility = View.VISIBLE
        loadingMessageText.text = errorStr
        loadingView.visibility = View.GONE
        swipeToRefresh.isRefreshing = false

        reloadLocationBtn.isEnabled = true
        swipeToRefresh.isEnabled = true

    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onResume() {
        super.onResume()
        if (currentLocation == null) {
            loadGPS()
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)


    }

    private fun observableViewModel() {

        prefs.getTempUnitsLivePreference().observe(activity!!, Observer<String> {tempIndexStr->
            if(currentWeather!=null && tempIndexStr.toInt()!=tempUnits) {
                changeTempUnit(tempIndexStr.toInt(), false)
                tempUnits = tempIndexStr.toInt()
            }
        })

        prefs.getDistanceUnitsLivePreference().observe(activity!!, Observer<String> {distanceIndexStr->
            if(currentWeather!=null && distanceIndexStr.toInt()!=distanceUnits) {
                changeDistanceUnit(distanceIndexStr.toInt(), false)
                distanceUnits = distanceIndexStr.toInt()
            }
        })

        viewModel!!.getWeather().observe(this, Observer<Weather> { weather ->

            currentWeather = weather

            changeTempUnit(prefs.getTempUnit(),true)
            changeDistanceUnit(prefs.getDistanceUnit(),true)

            showTemperature()

            currentTemperatureDescriptionText.text = currentWeather!!.currently!!.summary

            val locationStr = getLocationDetails(weather.latitude, weather.longitude)

            dayDescriptionText.text = if (locationStr.isNotEmpty()) String.format(
                getString(
                    R.string.location_day_holder, locationStr,
                    DataItemUtil.getDayOfWeek(currentWeather!!.currently!!.time, Calendar.LONG)
                )
            ) else DataItemUtil.getDayOfWeek(currentWeather!!.currently!!.time, Calendar.LONG)


            showHumidityWindSpeed()

            if (weather.daily!!.data != null) weatherRecycler.visibility = View.VISIBLE

        })

        viewModel!!.getError().observe(this, Observer<Boolean> { isError ->
            if (isError != null) {
                swipeToRefresh.isRefreshing = false

                reloadLocationBtn.isEnabled = true
                swipeToRefresh.isEnabled = true

                if (isError) {
                    initUI()

                    loadingMessageText.visibility = View.VISIBLE
                    weatherRecycler.visibility = View.GONE
                    loadingMessageText.text =
                        getString(R.string.error_loading_data)
                } else {

                    loadingMessageText.visibility = View.GONE
                    loadingMessageText.text = null
                    weatherRecycler.visibility = View.VISIBLE
                }
            }
        })

        viewModel!!.getLoading().observe(this, Observer<Boolean> { isLoading ->
            swipeToRefresh.isRefreshing = false
            if (isLoading != null) {
                loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    loadingMessageText.visibility = View.GONE
                    weatherRecycler.visibility = View.GONE
                }
            }
        })
    }

    private fun showTemperature()
    {
        currentTemperatureText.text = String.format(
            getString(R.string.temp_holder),
            currentWeather!!.currently!!.temperature.toInt()
        )

        dailyWeatherAdapter!!.setData(currentWeather!!.daily!!.data!!)

    }

    private fun showHumidityWindSpeed()
    {
        val distanceUnitsPostfix = if(prefs.getDistanceUnit()== DISTANCE.KILOMETER.ordinal)
            getString(R.string.kph) else getString(R.string.mph)


        dayStatsText.text = String.format(
            getString(R.string.temp_stats_holder),
            (currentWeather!!.currently!!.humidity * 100).toInt(),
            currentWeather!!.currently!!.windSpeed.toInt(), distanceUnitsPostfix
        )
    }

    private fun getLocationDetails(latitude: Double, longitude: Double): String {
        var locationStr = ""

        val geoCoder = Geocoder(activity, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(latitude, longitude, 10)
        if (addresses != null && addresses.isNotEmpty()) {

            var place = ""
            var state = ""
            for (address in addresses) {

                if (state.isEmpty() && address.adminArea != null && address.adminArea.isNotEmpty()) {
                    state = address.adminArea
                }

                if (place.isEmpty() && address.locality != null && address.locality.isNotEmpty()) {
                    place = address.locality
                }

                if (place.isEmpty() && address.subLocality != null && address.subLocality.isNotEmpty()) {
                    place = address.subLocality
                }

                if (place.isEmpty() && address.featureName != null && address.featureName.isNotEmpty()) {
                    place = address.featureName
                }


            }

            val countryName = addresses[0].countryName
            val countryCode = addresses[0].countryCode
            locationStr = if (countryCode == "US") String.format(
                "%s, %s",
                place,
                state
            ) else String.format("%s, %s", place, countryName)
        }
        return locationStr
    }


    private fun changeTempUnit(tempUnits : Int,loadingResults : Boolean)
    {
        if(tempUnits == TEMPERATURE.C.ordinal)
        {
            currentWeather!!.currently!!.temperature = DataItemUtil.toCelsius(currentWeather!!.currently!!.temperature)
            for(dataItem in currentWeather!!.daily!!.data!!)
            {
                dataItem.apparentTemperatureMax = DataItemUtil.toCelsius(dataItem.apparentTemperatureMax)
                dataItem.apparentTemperatureMin = DataItemUtil.toCelsius(dataItem.apparentTemperatureMin)
            }

            showTemperature()

        }
        //change the units only if user change it manually, otherwise the temperature will be in fahrenheit already
        else if(tempUnits == TEMPERATURE.F.ordinal && !loadingResults)
        {
            currentWeather!!.currently!!.temperature = DataItemUtil.toFahrenheit(currentWeather!!.currently!!.temperature)
            for(dataItem in currentWeather!!.daily!!.data!!)
            {
                dataItem.apparentTemperatureMax = DataItemUtil.toFahrenheit(dataItem.apparentTemperatureMax)
                dataItem.apparentTemperatureMin = DataItemUtil.toFahrenheit(dataItem.apparentTemperatureMin)
            }

            showTemperature()

        }


    }

    private fun changeDistanceUnit(distanceUnit: Int, loadingResults : Boolean){
       if(distanceUnit == DISTANCE.MILE.ordinal)
        {
            currentWeather!!.currently!!.windSpeed = DataItemUtil.toMillle(currentWeather!!.currently!!.windSpeed)
        }
        //change the units only if user change it manually, otherwise the distance will be in kilometer already
        else if (distanceUnit == DISTANCE.KILOMETER.ordinal && !loadingResults)
        {
            currentWeather!!.currently!!.windSpeed = DataItemUtil.toKilometer(currentWeather!!.currently!!.windSpeed)
        }

        showHumidityWindSpeed()

    }





}