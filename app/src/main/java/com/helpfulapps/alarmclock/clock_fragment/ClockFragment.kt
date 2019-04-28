package com.helpfulapps.alarmclock.clock_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
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
    }
}