package app.globe.com.weatherglobe.ui.activities

import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.eventbus.LocationFailureMessageEvent
import app.globe.com.weatherglobe.eventbus.LocationMessageEvent
import app.globe.com.weatherglobe.preference.SharedPrefs
import app.globe.com.weatherglobe.ui.base.BaseActivity
import app.globe.com.weatherglobe.utils.CustomTheme
import app.globe.com.weatherglobe.utils.DateTimeUtil
import app.globe.com.weatherglobe.utils.THEME_DAY_KEY
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import mumayank.com.airlocationlibrary.AirLocation
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var prefs: SharedPrefs

    private var airLocation: AirLocation? = null

    private var currentLocation : Location?=null


    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    fun getCurrentLocation() : Location?{
        return currentLocation
    }

    fun initLocation() {
        airLocation = AirLocation(this,
            shouldWeRequestPermissions = true,
            shouldWeRequestOptimization = true,
            callbacks = object : AirLocation.Callbacks {
                override fun onSuccess(location: Location) {
                    currentLocation = location
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

    override fun initUI() {

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)


        val lastDayAppOpened = DateTimeUtil.getDayFromTime(prefs.getLastTimeAppWasOpened())
        val oldThemeInit = DateTimeUtil.getCustomTheme(lastDayAppOpened)
        initTheme(container, oldThemeInit)

        tool_bar.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.action_settings -> {
                    val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                    val lastDayOpened = DateTimeUtil.getDayFromTime(prefs.getLastTimeAppWasOpened())
                    settingsIntent.putExtra(THEME_DAY_KEY, lastDayOpened)
                    startActivity(settingsIntent)

                }
                R.id.action_location -> {
                    initLocation()
                }
            }

            true
        }
    }

    fun enableLocationMenuItem(state: Boolean) {
        tool_bar.menu.findItem(R.id.action_location).isEnabled = state
    }

    fun transition() {

        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val lastTimeAppOpened = prefs.getLastTimeAppWasOpened()
        val lastDayAppOpened = DateTimeUtil.getDayFromTime(lastTimeAppOpened)

        val newTheme = DateTimeUtil.getCustomTheme(currentDay)
        val oldTheme = DateTimeUtil.getCustomTheme(lastDayAppOpened)
        transition(oldTheme, newTheme)
    }

    private fun transition(oldTheme: CustomTheme, newTheme: CustomTheme) {
        animateTheme(
            oldTheme.startColor, oldTheme.endColor, newTheme.startColor, newTheme.endColor
        )
    }

    private fun animateTheme(
        startColorOldRes: Int,
        endColorOldRes: Int,
        startColorNewRes: Int,
        endColorNewRes: Int
    ) {

        val startOld = ContextCompat.getColor(this, startColorOldRes)
        val endOld = ContextCompat.getColor(this, endColorOldRes)

        val startNew = ContextCompat.getColor(this, startColorNewRes)
        val endNew = ContextCompat.getColor(this, endColorNewRes)

        val evaluator = ArgbEvaluator()
        val gradient = container.background as GradientDrawable

        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.duration = 3000
        animator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction
            val newStart = evaluator.evaluate(fraction, startOld, startNew) as Int
            val newEnd = evaluator.evaluate(fraction, endOld, endNew) as Int
            val newArray = intArrayOf(newStart, newEnd)
            gradient.colors = newArray

            window.statusBarColor = newStart

            val colorList = colorStateListOf(
                intArrayOf(android.R.attr.state_checked) to newStart,
                intArrayOf(android.R.attr.state_enabled) to Color.WHITE
            )
            nav_view.itemIconTintList = colorList
            nav_view.itemTextColor = colorList
        }

        animator.start()
    }

    private fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
        val (states, colors) = mapping.unzip()
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    override fun initTheme(view: View, theme: CustomTheme) {
        super.initTheme(view, theme)

        val colorList = colorStateListOf(
            intArrayOf(android.R.attr.state_checked) to ContextCompat.getColor(
                this,
                theme.startColor
            ),
            intArrayOf(android.R.attr.state_enabled) to Color.WHITE
        )

        nav_view.itemIconTintList = colorList
        nav_view.itemTextColor = colorList
    }

    override fun onStop() {
        super.onStop()
        val lastTimeAppWasOpened = Calendar.getInstance().timeInMillis
        prefs.setLastTimeAppWasOpened(lastTimeAppWasOpened)
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
