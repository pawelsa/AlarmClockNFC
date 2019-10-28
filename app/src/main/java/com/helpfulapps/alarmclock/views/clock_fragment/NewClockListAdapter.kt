package com.helpfulapps.alarmclock.views.clock_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.alarmclock.helpers.Time
import com.helpfulapps.alarmclock.helpers.extensions.toArray

class NewClockListAdapter(
    val removeAlarm: (AlarmData) -> Unit,
    val updateAlarm: (AlarmData) -> Unit,
    val switchAlarm: (AlarmData) -> Unit,
    val changeTime: (Time) -> Time,
    val changeTitle: (String) -> String,
    val changeRingtone: (String) -> Pair<String, String>
) : ListAdapter<AlarmData, NewClockListAdapter.ClockItemHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAlarmBinding.inflate(layoutInflater, parent, false)
        return ClockItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NewClockListAdapter.ClockItemHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ClockItemHolder(val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlarmData, position: Int) {

            with(binding) {

                alarmData = item
                clItemAlarmBase.isActivated = item.isExpanded

                if (item.isExpanded) mvItemAlarmExpand.showAvdFirst() else mvItemAlarmExpand.showAvdSecond()

                mvItemAlarmExpand.setOnClickListener {
                    item.isExpanded = !item.isExpanded
                    notifyItemChanged(position)
                }

                cbAlarmItemRepeating.setOnCheckedChangeListener { _, isChecked ->
                    if (item.isRepeating != isChecked) {
                        item.isRepeating = isChecked
                        updateAlarm(item)
                        notifyItemChanged(position)
                    }
                }

                btItemAlarmRemove.setOnClickListener {
                    removeAlarm(item)
                }

                tvItemAlarmTitle.setOnClickListener {
                    item.title = changeTitle(item.title)
                    updateAlarm(item)
                }

                dpAlarmItemPicker.setDaySelectionChangedListener { dayList ->
                    item.repetitionDays = dayList.toArray()
                    updateAlarm(item)
                }

                btItemAlarmSound.setOnClickListener {
                    val newRingtone = changeRingtone(item.ringtoneTitle)
                    item.ringtoneTitle = newRingtone.first
                    item.ringtoneUrl = newRingtone.second
                    updateAlarm(item)
                }

                swItemAlarm.setOnCheckedChangeListener { _, isChecked ->
                    item.isTurnedOn = isChecked
                    switchAlarm(item)
                }

                tvItemAlarmTime.setOnClickListener {
                    val newTime = changeTime(Time(item.hour, item.minute))
                    item.hour = newTime.first
                    item.minute = newTime.second
                    updateAlarm(item)
                }

                cbAlarmItemVibrations.setOnCheckedChangeListener { _, isChecked ->
                    if (item.isVibrationOn != isChecked) {
                        item.isVibrationOn = isChecked
                        updateAlarm(item)
                    }
                }
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<AlarmData>() {
        override fun areItemsTheSame(oldItem: AlarmData, newItem: AlarmData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmData, newItem: AlarmData): Boolean {
            return oldItem == newItem
        }
    }
}
