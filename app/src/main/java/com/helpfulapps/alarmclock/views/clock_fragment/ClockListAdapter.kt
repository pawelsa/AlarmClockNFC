package com.helpfulapps.alarmclock.views.clock_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding


class ClockListAdapter : RecyclerView.Adapter<ClockListAdapter.ClockItemHolder>() {

    lateinit var itemList: List<AlarmData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAlarmBinding.inflate(layoutInflater, parent, false)
        return ClockItemHolder(itemBinding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ClockItemHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    inner class ClockItemHolder(var binding: ItemAlarmBinding) :
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
            }
        }

    }

}