package com.helpfulapps.alarmclock.views.clock_fragment

import androidx.recyclerview.widget.DiffUtil
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.alarmclock.helpers.Time
import com.helpfulapps.alarmclock.helpers.extensions.toDayArray
import com.helpfulapps.base.base.BaseListAdapter

class NewClockListAdapter(
    val removeAlarm: (AlarmData) -> Unit,
    val updateAlarm: (AlarmData) -> Unit,
    val switchAlarm: (AlarmData) -> Unit,
    val changeTime: (Time) -> Time,
    val changeTitle: (String) -> String,
    val changeRingtone: (String) -> Pair<String, String>
) : BaseListAdapter<AlarmData, ItemAlarmBinding>(AlarmDataDiffCallback()) {

    private val TAG = NewClockListAdapter::class.java.simpleName

    override val itemView: Int = R.layout.item_alarm

    override fun bind(): ItemAlarmBinding.(item: AlarmData, position: Int) -> Unit =
        { item, position ->
            alarmData = item
            clItemAlarmBase.isActivated = item.isExpanded

            if (item.isExpanded) mvItemAlarmExpand.showAvdFirst() else mvItemAlarmExpand.showAvdSecond()

            mvItemAlarmExpand.setOnClickListener {
                item.isExpanded = !item.isExpanded
                notifyItemChanged(position)
            }

            cbAlarmItemRepeating.setOnCheckedChangeListener { checkBox, isChecked ->
                if (checkBox.isPressed && item.isRepeating != isChecked) {
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
                item.repetitionDays = dayList.toDayArray()
                updateAlarm(item)
            }

            btItemAlarmSound.setOnClickListener {
                val newRingtone = changeRingtone(item.ringtoneTitle)
                item.ringtoneTitle = newRingtone.first
                item.ringtoneUrl = newRingtone.second
                updateAlarm(item)
            }

            swItemAlarm.setOnCheckedChangeListener { checkBox, isChecked ->
                if (checkBox.isPressed) {
                    item.isTurnedOn = isChecked
                    switchAlarm(item)
                }
            }

            tvItemAlarmTime.setOnClickListener {
                val newTime = changeTime(Time(item.hour, item.minute))
                item.hour = newTime.first
                item.minute = newTime.second
                updateAlarm(item)
            }

            cbAlarmItemVibrations.setOnCheckedChangeListener { checkBox, isChecked ->
                if (checkBox.isPressed && item.isVibrationOn != isChecked) {
                    item.isVibrationOn = isChecked
                    updateAlarm(item)
                }
            }
        }


    class AlarmDataDiffCallback : DiffUtil.ItemCallback<AlarmData>() {
        override fun areItemsTheSame(oldItem: AlarmData, newItem: AlarmData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmData, newItem: AlarmData): Boolean {
            return oldItem == newItem
        }
    }
}
