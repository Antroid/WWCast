package app.globe.com.weatherglobe.ui.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.ui.base.BaseActivity
import butterknife.BindView

class SettingsActivity : BaseActivity()
{

    @BindView(R.id.toolbar)
    lateinit var toolbar : Toolbar


    override fun layoutRes(): Int {
        return R.layout.activity_settings
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        toolbar.setNavigationOnClickListener {
            finish()
        }

    }



}