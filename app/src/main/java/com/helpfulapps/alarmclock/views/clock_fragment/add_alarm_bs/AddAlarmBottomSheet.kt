package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.DialogAddAlarmBinding

class AddAlarmBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAddAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_alarm, container, false)
        return binding.root
    }

}