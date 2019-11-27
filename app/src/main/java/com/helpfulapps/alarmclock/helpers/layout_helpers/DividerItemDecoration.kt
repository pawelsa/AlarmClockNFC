package com.helpfulapps.alarmclock.helpers.layout_helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class DividerItemDecoration
    (
    context: Context,
    private val mIsShowInLastItem: Boolean
) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        if (mDivider == null) {
            Log.w(
                TAG,
                "@android:attr/listDivider was not set in the theme used for this " + "DividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }
        drawVertical(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount: Int = if (mIsShowInLastItem) {
            parent.childCount
        } else {
            parent.childCount - 1
        }
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val decoratedBottom = parent.layoutManager!!.getDecoratedBottom(child)
            val bottom = decoratedBottom + child.translationY.roundToInt()
            val top = bottom - mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mDivider == null) {
            outRect.setEmpty()
            return
        }

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val itemCount = state.itemCount

        when {
            mIsShowInLastItem -> outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
            itemPosition == itemCount - 1 -> // We didn't set the last item when mIsShowInLastItem's value is false.
                outRect.setEmpty()
            else -> outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
        }
    }

    companion object {
        private val TAG = "DividerItem"
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}