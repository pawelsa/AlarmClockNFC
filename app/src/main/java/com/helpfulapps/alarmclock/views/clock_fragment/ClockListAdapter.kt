package com.helpfulapps.alarmclock.views.clock_fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ItemAlarmBinding
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildEditTitleDialog
import com.helpfulapps.base.base.BaseAdapter


class ClockListAdapter :
    BaseAdapter<AlarmData, ItemAlarmBinding>() {

    private val TAG = ClockListAdapter::class.java.simpleName
    private lateinit var recyclerView: RecyclerView
    private var expandedPosition = -1

    override val itemView: Int
        get() = R.layout.item_alarm

    override fun getItemCount(): Int = items.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun View.setData(itemBinding: ItemAlarmBinding, item: AlarmData, position: Int) {

        val isExpanded = position == expandedPosition

        with(itemBinding) {

            if (!isExpanded && clItemAlarmBase.isActivated) {
                setCollapsed(this)
            }
            items[position].isExpanded = isExpanded
            alarmData = items[position]
            clItemAlarmBase.isActivated = isExpanded

            mvItemAlarmExpand.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                val shouldBeExpanded = !isExpanded
//                val constraintSet = getConstrainedSet(shouldBeExpanded)
//                TransitionManager.beginDelayedTransition(recyclerView)
//                vsAlarmItemTitleSwitcher.displayedChild = if (isExpanded) 0 else 1
//                clAlarmItemContainer.setConstraintSet(constraintSet)
                notifyDataSetChanged()
            }
/*
            incAlarmItemExtension.cbAlarmItemRepeat.setOnCheckedChangeListener { _, isChecked ->
                incAlarmItemExtension.wpAlarmItemPicker.visibility =
                    if (isChecked) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(recyclerView)
                notifyDataSetChanged()
            }
*/
            tvItemAlarmTitle.setOnClickListener {
                buildEditTitleDialog(
                    context,
                    tvItemAlarmTitle.text.toString()
                ) { newLabel ->
                    tvItemAlarmTitle.text = newLabel
                }.show()
            }
        }
    }

    private fun setCollapsed(binding: ItemAlarmBinding) {
        with(binding) {
            dpAlarmItemPicker.visibility = View.GONE
            btItemAlarmRemove.visibility = View.GONE
            btItemAlarmSound.visibility = View.GONE
            cbAlarmItemRepeating.visibility = View.GONE
        }
    }

/*
    private fun setCollapsed(binding: ItemAlarmBinding) {
        with(binding) {
            ivAlarmItemExpandingIcon.showAvdFirst()
            val constraintSet = getConstrainedSet()
            TransitionManager.beginDelayedTransition(recyclerView)
            vsAlarmItemTitleSwitcher.displayedChild = 0
            clAlarmItemContainer.setConstraintSet(constraintSet)
        }
    }*/
/*
    private fun getConstrainedSet(shouldBeExpanded: Boolean = false) = ConstraintSet().also {
        it.load(
            recyclerView.context,
            if (shouldBeExpanded) R.layout.item_alarm_edit else R.layout.item_alarm
        )
    }*/

}