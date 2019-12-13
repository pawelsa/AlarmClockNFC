package com.helpfulapps.alarmclock.views.stopwatch_fragment

import androidx.recyclerview.widget.DiffUtil
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemStopwatchLapBinding
import com.helpfulapps.base.base.BaseListAdapter

class StopwatchTimesAdapter :
    BaseListAdapter<LapModel, ItemStopwatchLapBinding>(LapTimeDiffCallback()) {

    override val itemView: Int = R.layout.item_stopwatch_lap

    override fun bind(): ItemStopwatchLapBinding.(item: LapModel, position: Int) -> Unit =
        { lapModel: LapModel, _: Int ->
            lapTime = lapModel
        }

    class LapTimeDiffCallback : DiffUtil.ItemCallback<LapModel>() {
        override fun areItemsTheSame(oldItem: LapModel, newItem: LapModel): Boolean {
            return oldItem.lapNo == newItem.lapNo
        }

        override fun areContentsTheSame(oldItem: LapModel, newItem: LapModel): Boolean {
            return oldItem == newItem
        }
    }

}