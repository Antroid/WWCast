package app.globe.com.weatherglobe.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes(), container, false)
    }

    //for init viewmodels, shared prefs defaults etc..
    abstract fun initLogic()

    //for init ui elements
    abstract fun initUI()

    //for observables that will be used for populating fields from internet/database calls
    abstract fun initObservables()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        initLogic()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObservables()
    }

}