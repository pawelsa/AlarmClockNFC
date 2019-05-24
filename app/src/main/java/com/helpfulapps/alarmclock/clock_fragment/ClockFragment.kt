package com.helpfulapps.alarmclock.clock_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
import com.helpfulapps.data.db.alarm.AlarmRepositoryImpl
import com.helpfulapps.domain.model.Alarm
import com.helpfulapps.domain.use_cases.alarm.AddAlarmUseCase
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_clock.*


class ClockFragment : Fragment() {

    private val binding by lazy {
        DataBindingUtil.inflate<FragmentClockBinding>(
            this.layoutInflater,
            R.layout.fragment_clock,
            this.cl_clock_container,
            false
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ClockViewModel::class.java)
        viewModel.setAdapter(ClockListAdapter(rv_clock_list))

        binding.viewModel = viewModel

        /*val fab : FloatingActionButton? = activity?.findViewById(R.id.fab_main_fab)
        fab?.setOnClickListener {
            val alarmRepositoryImpl = AlarmRepositoryImpl(context!!)
            val alarm = Alarm(0,"",false,true,false,15,0L,15L, IntArray(0))
            AddAlarmUseCase(alarmRepositoryImpl).execute(alarm).subscribe(
                { Log.d("onActivityCreated", "alarmSaved") },
                { t -> Log.d("onActivityCreated", t.message) })
            alarmRepositoryImpl.getAlarms().subscribe ( { list -> Log.d("onActivityCreated", "list : $list") },{ t-> Log.d("onActivityCreated", t.message) })
        }*/
    }
}