package app.globe.com.weatherglobe.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject



class AboutViewModel @Inject
constructor() : ViewModel() {

    private val appVersion = MutableLiveData<String>()

    internal fun getVersion(): LiveData<String> {
        return appVersion
    }

    fun loadAppVersion(con : Context)
    {
        try {
            val pInfo = con.packageManager.getPackageInfo(con.packageName, 0)
            appVersion.value = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}