package com.helpfulapps.alarmclock.views.ringing_alarm

import androidx.recyclerview.widget.DiffUtil
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemWeatherInfoBinding
import com.helpfulapps.base.base.BaseListAdapter

class WeatherInfoAdapter :
    BaseListAdapter<Pair<String, String>, ItemWeatherInfoBinding>(WeatherInfoDiffCallback()) {

    override val itemView: Int = R.layout.item_weather_info

    override fun bind(): ItemWeatherInfoBinding.(item: Pair<String, String>, position: Int) -> Unit =
        { item, _ ->
            this.info = item
        }


    class WeatherInfoDiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ): Boolean {
            return oldItem == newItem
        }
    }
}