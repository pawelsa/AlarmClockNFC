package com.helpfulapps.alarmclock.views.ringing_alarm

import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityRingingAlarmBinding
import com.helpfulapps.base.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ringing_alarm.*
import org.koin.android.viewmodel.ext.android.viewModel

class RingingAlarmActivity : BaseActivity<RingingAlarmViewModel, ActivityRingingAlarmBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_ringing_alarm

    override val viewModel: RingingAlarmViewModel by viewModel()


    override fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ringing_base, text, Snackbar.LENGTH_SHORT).show()
    }

}