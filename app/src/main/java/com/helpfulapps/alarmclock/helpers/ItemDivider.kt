package com.helpfulapps.alarmclock.helpers

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.helpfulapps.base.helpers.whenNotNull


class ItemDivider(private val mDivider: Drawable?) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + (mDivider?.intrinsicHeight ?: 0)

            whenNotNull(mDivider) { divider ->
                divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                divider.draw(canvas)
            }
        }
    }
}