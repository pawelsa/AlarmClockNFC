package com.helpfulapps.base.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ITEM, DB : ViewDataBinding> :
    RecyclerView.Adapter<BaseAdapter<ITEM, DB>.ViewHolder>() {

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
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemView,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    abstract fun View.setData(itemBinding: DB, item: ITEM, position: Int)

    protected open fun View.setupListeners(item: ITEM, position: Int) = Unit

    protected open fun onNewDataProvided() = Unit

    inner class ViewHolder(private val itemBinding: DB) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ITEM) {
            itemView.setData(itemBinding, item, adapterPosition)
            itemView.setOnClickListener {
                onPositionClick(adapterPosition)
                onItemClick(items[adapterPosition])
            }
            itemView.setupListeners(items[adapterPosition], adapterPosition)
        }
    }
}