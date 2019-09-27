package app.globe.com.weatherglobe.ui.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.eventbus.LocationFailureMessageEvent
import app.globe.com.weatherglobe.eventbus.LocationMessageEvent
import app.globe.com.weatherglobe.ui.base.BaseActivity
import app.globe.com.weatherglobe.ui.fragments.weather.WeatherFragment
import mumayank.com.airlocationlibrary.AirLocation
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {

    private var airLocation: AirLocation? = null

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().add(
                R.id.screen_container,
                WeatherFragment()
            ).commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    fun initLocation() {
        airLocation = AirLocation(this,
            shouldWeRequestPermissions = true,
            shouldWeRequestOptimization = true,
            callbacks = object : AirLocation.Callbacks {
                override fun onSuccess(location: Location) {
                    EventBus.getDefault().post(LocationMessageEvent(location))
                }

                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                    EventBus.getDefault().post(LocationFailureMessageEvent(locationFailedEnum))

                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLocation?.onActivityResult(
            requestCode,
            resultCode,
            data
        ) // ADD THIS LINE INSIDE onActivityResult
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLocation?.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        ) // ADD THIS LINE INSIDE onRequestPermissionResult
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
