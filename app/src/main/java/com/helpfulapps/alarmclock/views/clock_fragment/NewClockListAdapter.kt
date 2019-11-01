package com.helpfulapps.alarmclock.views.clock_fragment

import androidx.recyclerview.widget.DiffUtil
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.base.base.BaseListAdapter
import com.helpfulapps.domain.models.alarm.Alarm

class NewClockListAdapter(
    val switchAlarm: (Alarm) -> Unit,
    val openEditMode: (Alarm) -> Unit
) : BaseListAdapter<AlarmData, ItemAlarmBinding>(AlarmDataDiffCallback()) {

    private val TAG = NewClockListAdapter::class.java.simpleName

    override val itemView: Int = R.layout.item_alarm

    override fun bind(): ItemAlarmBinding.(item: AlarmData, position: Int) -> Unit =
        { item, position ->
            alarmData = item

            clItemAlarmBase.setOnClickListener {
                openEditMode(item.toDomain())
            }

            mvItemAlarmExpand.setOnClickListener {
                item.isExpanded = !item.isExpanded
                notifyItemChanged(position)
            }

            swItemAlarm.setOnCheckedChangeListener { checkBox, isChecked ->
                if (checkBox.isPressed) {
                    item.isTurnedOn = isChecked
                    switchAlarm(item.toDomain())
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
