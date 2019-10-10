package app.globe.com.weatherglobe.dialogs

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.globe.com.weatherglobe.viewmodels.AboutViewModel
import app.globe.com.weatherglobe.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.about_dialog.*
import javax.inject.Inject


open class AboutDialog private constructor() : BottomSheetDialogFragment() {


    @set:Inject
    internal var viewModelFactory: ViewModelFactory? = null


    private var viewModel: AboutViewModel? = null

    companion object {
        fun newInstance(): AboutDialog {
            return AboutDialog()
        }
    }


    override fun getTheme(): Int = app.globe.com.weatherglobe.R.style.BaseBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(app.globe.com.weatherglobe.R.layout.about_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutViewModel::class.java)

        rate_this_app.setOnClickListener {

            rateThisApp()

        }

        observableViewModel()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        return dialog
    }


    private fun rateThisApp() {
        val uri = Uri.parse("market://details?id=" + context!!.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.packageName)
                )
            )
        }

    }

    private fun observableViewModel() {

        viewModel!!.getVersion().observe(this, Observer<String> { version ->
            app_version.text = version
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel!!.loadAppVersion(context!!)
    }

}