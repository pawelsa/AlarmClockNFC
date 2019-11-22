package com.helpfulapps.alarmclock.views.clock_fragment

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.base.base.BaseListAdapter
import com.helpfulapps.domain.models.alarm.Alarm

class ClockListAdapter(
    val switchAlarm: (Alarm) -> Unit,
    val openEditMode: (Alarm) -> Unit,
    val removeAlarm: (Alarm) -> Unit
) : BaseListAdapter<AlarmData, ItemAlarmBinding>(AlarmDataDiffCallback()) {

    private val TAG = ClockListAdapter::class.java.simpleName

    override val itemView: Int = R.layout.item_alarm

    override fun bind(): ItemAlarmBinding.(item: AlarmData, position: Int) -> Unit =
        { item, position ->
            alarmData = item

            val context = this.root.context
            setupChips(context)

            (clItemAlarmBase.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
                if (position == itemCount - 1) 450 else 0

            clItemAlarmBase.setOnClickListener {
                openEditMode(item.toDomain())
            }

            clItemAlarmBase.setOnLongClickListener {
                removeAlarm(item.toDomain())
                true
            }

            swItemAlarm.setOnCheckedChangeListener { checkBox, isChecked ->
                if (checkBox.isPressed) {
                    item.isTurnedOn = isChecked
                    switchAlarm(item.toDomain())
                }
            }
        }

    private fun ItemAlarmBinding.setupChips(context: Context) {
        setupTemperatureChip(this, context)
        setupRainChip(this, context)
        setupSnowChip(this, context)
        setupWindChip(this, context)
    }

    private fun setupWindChip(
        itemAlarmBinding: ItemAlarmBinding,
        context: Context
    ) {
        itemAlarmBinding.chipAlarmItemWind.text =
            when (itemAlarmBinding.alarmData?.weatherShort?.wind) {
                1 -> context.getString(R.string.item_alarm_wind_windy)
                2 -> context.getString(R.string.item_alarm_wind_very_windy)
                else -> context.getString(R.string.item_alarm_wind_no)
            }
    }

    private fun setupSnowChip(
        itemAlarmBinding: ItemAlarmBinding,
        context: Context
    ) {
        itemAlarmBinding.chipAlarmItemSnow.text =
            when (itemAlarmBinding.alarmData?.weatherShort?.snow) {
                1 -> context.getString(R.string.item_alarm_snow_snowy)
                2 -> context.getString(R.string.item_alarm_snow_heavy_snow)
                else -> context.getString(R.string.item_alarm_snow_no)
            }
    }

    private fun setupRainChip(
        itemAlarmBinding: ItemAlarmBinding,
        context: Context
    ) {
        itemAlarmBinding.chipAlarmItemRain.text =
            when (itemAlarmBinding.alarmData?.weatherShort?.rain) {
                1 -> context.getString(R.string.item_alarm_rain_may_rain)
                2 -> context.getString(R.string.item_alarm_rain_will_rain)
                3 -> context.getString(R.string.item_alarm_rain_heavy_rain)
                else -> context.getString(R.string.item_alarm_rain_no)
            }
    }

    private fun setupTemperatureChip(
        itemAlarmBinding: ItemAlarmBinding,
        context: Context
    ) {
        itemAlarmBinding.chipAlarmItemTemperature.text =
            when (itemAlarmBinding.alarmData?.weatherShort?.temperature) {
                -2 -> context.getString(R.string.item_alarm_temperature_very_cold)
                -1 -> context.getString(R.string.item_alarm_temperature_cold)
                1 -> context.getString(R.string.item_alarm_temperature_hot)
                2 -> context.getString(R.string.item_alarm_temperature_very_hot)
                else -> context.getString(R.string.item_alarm_temperature_ok)
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
