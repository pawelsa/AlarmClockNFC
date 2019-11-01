package com.helpfulapps.alarmclock.helpers.layout_helpers

import android.util.Log
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.akaita.android.morphview.MorphView
import com.helpfulapps.alarmclock.helpers.extensions.toDayArray
import com.helpfulapps.alarmclock.helpers.extensions.toDayList


@BindingAdapter("app:iconExpanded")
fun setIconExpanded(morphView: MorphView, isExpanded: Boolean) {
    when {
        isExpanded -> morphView.showAvdFirst()
        else -> morphView.showAvdSecond()
    }
}

@BindingAdapter("app:adapter")
fun <T : RecyclerView.ViewHolder> setRecyclerViewAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<T>?
) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
    (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    val itemDecor = DividerItemDecoration(recyclerView.context, LinearLayoutCompat.VERTICAL)
    recyclerView.addItemDecoration(itemDecor)
}

@BindingAdapter("app:selectedDays")
fun setSelectedDays(dayPicker: MaterialDayPicker, days: Array<Boolean>) {
    Log.d("Binding", "set Days : $days")
    if (dayPicker.selectedDays != days.toDayList()) {
        dayPicker.setSelectedDays(days.toDayList())
    }
}

@InverseBindingAdapter(attribute = "app:selectedDays")
fun getSelectedDays(dayPicker: MaterialDayPicker): Array<Boolean> {
    Log.d("Binding", "get Days : ${dayPicker.selectedDays.toDayArray()}")
    return dayPicker.selectedDays.toDayArray()
}

@BindingAdapter("app:selectedDaysAttrChanged")
fun setDayPickerListener(dayPicker: MaterialDayPicker, listener: InverseBindingListener?) {
    dayPicker.setDaySelectionChangedListener {
        listener?.onChange()
    }
}
