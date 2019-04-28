package com.helpfulapps.alarmclock.clock_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.alarmclock.layout_helpers.OnLabelChangedListener
import com.helpfulapps.alarmclock.layout_helpers.buildDialog


class ClockListAdapter(private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<ClockListAdapter.ClockListViewHolder>() {

    private val TAG = ClockListAdapter::class.java.simpleName

    private var expandedPosition = -1
    private val alarmItemList: ArrayList<AlarmData> = getMyData()


    private fun getMyData(): ArrayList<AlarmData> = ArrayList<AlarmData>().also {
        it.add(
            AlarmData(
                "Hello1",
                "Content1",
                R.drawable.ic_add
            )
        )
        it.add(
            AlarmData(
                "Hello2",
                "Content2",
                R.drawable.ic_alarm
            )
        )
        it.add(
            AlarmData(
                "Hello3",
                "Content3",
                R.drawable.ic_hourglass
            )
        )
        it.add(
            AlarmData(
                "Hello4",
                "Content4",
                R.drawable.ic_menu
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockListViewHolder =
        ClockListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_alarm,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = alarmItemList.size

    override fun onBindViewHolder(holder: ClockListViewHolder, position: Int) {

        val isExpanded = position == expandedPosition

        with(holder.view) {

            if (!isExpanded && clAlarmItemContainer.isActivated) {
                setCollapsed(holder.view)
            }
            alarmItemList[position].isExpanded = isExpanded
            alarmData = alarmItemList[position]
            clAlarmItemContainer.isActivated = isExpanded

            ivAlarmItemExpandingIcon.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                val shouldBeExpanded = !isExpanded
                val constraintSet = getConstrainedSet(shouldBeExpanded)
                TransitionManager.beginDelayedTransition(recyclerView)
                vsAlarmItemTitleSwitcher.displayedChild = if (isExpanded) 0 else 1
                clAlarmItemContainer.setConstraintSet(constraintSet)
                notifyDataSetChanged()
            }

            incAlarmItemExtension.cbAlarmItemRepeat.setOnCheckedChangeListener { _, isChecked ->
                incAlarmItemExtension.wpAlarmItemPicker.visibility =
                    if (isChecked) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(recyclerView)
                notifyDataSetChanged()
            }

            tvAlarmItemTitleRename.setOnClickListener {
                buildDialog(
                    clAlarmItemContainer.context,
                    tvAlarmItemTitleRename.text.toString(),
                    object : OnLabelChangedListener {
                        override fun onLabelChanged(newLabel: String) {
                            tvAlarmItemTitleRename.text = newLabel
                            tvAlarmItemTitle.text = newLabel
                        }
                    })
            }
        }
    }

    private fun setCollapsed(binding: ItemAlarmBinding) {
        with(binding) {
            ivAlarmItemExpandingIcon.showAvdFirst()
            val constraintSet = getConstrainedSet()
            TransitionManager.beginDelayedTransition(recyclerView)
            vsAlarmItemTitleSwitcher.displayedChild = 0
            clAlarmItemContainer.setConstraintSet(constraintSet)
        }
    }

    private fun getConstrainedSet(shouldBeExpanded: Boolean = false) = ConstraintSet().also {
        it.load(
            recyclerView.context,
            if (shouldBeExpanded) R.layout.item_alarm_edit else R.layout.item_alarm
        )
    }

    class ClockListViewHolder(val view: ItemAlarmBinding) : RecyclerView.ViewHolder(view.root)

}