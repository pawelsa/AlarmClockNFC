package com.helpfulapps.alarmclock.views.clock_fragment

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildEditTitleDialog
import com.helpfulapps.base.base.BaseAdapter


class ClockListAdapter(private val recyclerView: RecyclerView) :
    BaseAdapter<AlarmData, ItemAlarmBinding>() {

    private val TAG = ClockListAdapter::class.java.simpleName

    private var expandedPosition = -1
    private val alarmItemList: ArrayList<AlarmData> = getMyData()

    override val itemView: Int
        get() = R.layout.item_alarm

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

    override fun getItemCount(): Int = alarmItemList.size

    override fun View.setData(itemBinding: ItemAlarmBinding, item: AlarmData, position: Int) {

        val isExpanded = position == expandedPosition

        with(itemBinding) {

            if (!isExpanded && clAlarmItemContainer.isActivated) {
                setCollapsed(this)
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
                buildEditTitleDialog(
                    clAlarmItemContainer.context,
                    tvAlarmItemTitleRename.text.toString()
                ) { newLabel ->
                    tvAlarmItemTitleRename.text = newLabel
                    tvAlarmItemTitle.text = newLabel
                }.show()
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

}