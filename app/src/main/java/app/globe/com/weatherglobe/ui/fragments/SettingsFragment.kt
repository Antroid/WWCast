package app.globe.com.weatherglobe.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.dialogs.AboutDialog
import app.globe.com.weatherglobe.viewmodels.SettingsViewModel
import app.globe.com.weatherglobe.viewmodels.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject



class SettingsFragment : PreferenceFragmentCompat(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    @set:Inject
    internal var viewModelFactory: ViewModelFactory? = null

    var viewModel : SettingsViewModel?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)

    }

    override fun onCreatePreferences(bundle: Bundle?, string: String?) {
        addPreferencesFromResource(R.xml.preferences)


        val tempPrefs : ListPreference? = findPreference(getString(R.string.temperature_unit_pref_key))
        val tempIndexUnit = preferenceScreen.sharedPreferences.getString(getString(R.string.temperature_unit_pref_key), "0")
        val arrTemps = resources.getStringArray(R.array.temperature)
        tempPrefs!!.summary = arrTemps[tempIndexUnit!!.toInt()]

        tempPrefs.setOnPreferenceChangeListener { preference, newValue ->

            val newTempUnitIndex = newValue.toString().toInt()

            preference.summary = arrTemps[newTempUnitIndex]
            true
        }

        val distancePrefs : ListPreference? = findPreference(getString(R.string.distance_unit_pref_key))
        val distanceIndexUnit = preferenceScreen.sharedPreferences.getString(getString(R.string.distance_unit_pref_key), "0")
        val arrDistances = resources.getStringArray(R.array.distance)
        distancePrefs!!.summary = arrDistances[distanceIndexUnit!!.toInt()]

        distancePrefs.setOnPreferenceChangeListener { preference, newValue ->

            val newDistanceUnitIndex = newValue.toString().toInt()

            preference.summary = arrDistances[newDistanceUnitIndex]
            true
        }

        val aboutPreference : Preference? = findPreference(getString(R.string.about))
        aboutPreference!!.setOnPreferenceClickListener {
            showAboutDialog()
            true
        }

    }

    private fun showAboutDialog() {
        val aboutDialogFragment = AboutDialog.newInstance()
        aboutDialogFragment.show(
            childFragmentManager,
            "AboutDialog"
        )
    }


}