package com.helpfulapps.alarmclock.helpers.layout_helpers

import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.akaita.android.morphview.MorphView


@BindingAdapter("app:iconExpanded")
fun setIconExpanded(morphView: MorphView, isExpanded: Boolean) {
    when {
        isExpanded -> morphView.showAvdFirst()
        else -> morphView.showAvdSecond()
    }
}

@BindingAdapter("app:adapter")
fun <T : RecyclerView.ViewHolder> setRecyclerViewAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<T>?
) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
    (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    val itemDecor = DividerItemDecoration(recyclerView.context, LinearLayoutCompat.VERTICAL)
    recyclerView.addItemDecoration(itemDecor)
}