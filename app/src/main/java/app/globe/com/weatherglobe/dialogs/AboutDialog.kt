package app.globe.com.weatherglobe.dialogs

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.viewmodels.AboutViewModel
import app.globe.com.weatherglobe.viewmodels.ViewModelFactory
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import javax.inject.Inject


open class AboutDialog private constructor() : BottomSheetDialogFragment() {

    @BindView(R.id.app_version)
    lateinit var appVersion : TextView

    @BindView(R.id.contact_me)
    lateinit var contactMeBtn : MaterialButton

    @set:Inject
    internal var viewModelFactory: ViewModelFactory? = null


    private var viewModel: AboutViewModel? = null

    companion object{
        fun newInstance() : AboutDialog{
            return AboutDialog()
        }
    }

    private var unbinder : Unbinder?=null

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.about_dialog, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutViewModel::class.java)

        contactMeBtn.setOnClickListener {

            composeEmail()

        }

        observableViewModel()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        return dialog
    }

    private fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:casterapps@gmail.com") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_feedback_subject))
        startActivity(Intent.createChooser(intent, getString(R.string.send_feedback)))
    }

    private fun observableViewModel() {

        viewModel!!.getVersion().observe(this, Observer<String> { version ->
           appVersion.text = version
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel!!.loadAppVersion(context!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

}