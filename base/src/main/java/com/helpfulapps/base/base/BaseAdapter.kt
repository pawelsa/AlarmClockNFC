package com.helpfulapps.base.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ITEM> : RecyclerView.Adapter<BaseAdapter<ITEM>.ViewHolder>() {

    abstract val itemView: Int

    var onPositionClick: (position: Int) -> Unit = {}
    var onItemClick: (item: ITEM) -> Unit = {}

    var items: List<ITEM> = listOf()
        set(value) {
            field = value
            onNewDataProvided()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemView, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    abstract fun View.setData(item: ITEM, position: Int)

    protected open fun View.setupListeners(item: ITEM, position: Int) = Unit

    protected open fun onNewDataProvided() = Unit

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ITEM) {
            itemView.setData(item, adapterPosition)
            itemView.setOnClickListener {
                onPositionClick(adapterPosition)
                onItemClick(items[adapterPosition])
            }
            itemView.setupListeners(items[adapterPosition], adapterPosition)
        }
    }
}