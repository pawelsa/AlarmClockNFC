package com.helpfulapps.alarmclock.helpers.layout_helpers

import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.button.MaterialButton
import com.helpfulapps.alarmclock.helpers.extensions.notEqual
import com.helpfulapps.alarmclock.helpers.extensions.toDayArray
import com.helpfulapps.alarmclock.helpers.extensions.toDayList


@BindingAdapter("app:adapter")
fun <T : RecyclerView.ViewHolder> setRecyclerViewAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<T>?
) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
    val itemDecor = DividerItemDecoration(recyclerView.context, LinearLayoutCompat.VERTICAL)
    recyclerView.addItemDecoration(itemDecor)
}

@BindingAdapter("app:selectedDays")
fun setSelectedDays(dayPicker: MaterialDayPicker, days: Array<Boolean>) {
    if (dayPicker.selectedDays.notEqual(days.toDayList())) {
        dayPicker.setSelectedDays(days.toDayList())
    }
}

@InverseBindingAdapter(attribute = "app:selectedDays")
fun getSelectedDays(dayPicker: MaterialDayPicker): Array<Boolean> {
    return dayPicker.selectedDays.toDayArray()
}

@BindingAdapter("app:selectedDaysAttrChanged")
fun setDayPickerListener(dayPicker: MaterialDayPicker, listener: InverseBindingListener?) {
    dayPicker.setDaySelectionChangedListener {
        listener?.onChange()
    }
}

@BindingAdapter("app:hintText", "app:currentText")
fun setTemplateText(button: MaterialButton, hintText: Int, currentText: String?) {
    button.text =
        if (currentText?.isBlank() != false) button.context.getString(hintText) else currentText
    button.setTextColor(
        ContextCompat.getColor(
            button.context,
            if (currentText?.isBlank() != false) android.R.color.darker_gray else android.R.color.black
        )
    )
}

