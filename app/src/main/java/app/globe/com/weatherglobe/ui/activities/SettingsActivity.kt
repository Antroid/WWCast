package app.globe.com.weatherglobe.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.ui.base.BaseActivity
import app.globe.com.weatherglobe.utils.DateTimeUtil
import app.globe.com.weatherglobe.utils.THEME_DAY_KEY
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : BaseActivity()
{

    override fun initUI() {
        toolbar.navigationIcon!!.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP)
    }

    override fun layoutRes(): Int {
        return R.layout.activity_settings
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeDay = intent.getIntExtra(THEME_DAY_KEY,Calendar.SUNDAY)
        val theme = DateTimeUtil.getCustomTheme(themeDay)

        initTheme(container,theme)

        toolbar.setNavigationOnClickListener {
            finish()
        }

    }



}