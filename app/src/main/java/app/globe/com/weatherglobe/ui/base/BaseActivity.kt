package app.globe.com.weatherglobe.ui.base

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import app.globe.com.weatherglobe.utils.CustomTheme
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {


    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        initUI()
    }

    abstract fun initUI()

    open fun initTheme(view : View, theme : CustomTheme) {
        view.background = getGradientByTheme(ContextCompat.getColor(this, theme.startColor),
            ContextCompat.getColor(this, theme.endColor))
        setStatusBarBackground(theme.startColor)
    }

    private fun getGradientByTheme(startColor : Int, endColor : Int): GradientDrawable {
        val colors = intArrayOf(startColor,endColor)

        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
    }

    private fun setStatusBarBackground(color : Int)
    {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

}