package com.helpfulapps.base.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<ITEM, DB : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<ITEM>) :
    ListAdapter<ITEM, BaseListAdapter<ITEM, DB>.ItemViewHolder>(diffCallback) {

    abstract fun bind(): DB.(item: ITEM, position: Int) -> Unit

    abstract val itemView: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: DB = DataBindingUtil.inflate(
            layoutInflater,
            itemView,
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), position, bind())
    }

    inner class ItemViewHolder(private val binding: DB) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ITEM, position: Int, bindAction: DB.(item: ITEM, position: Int) -> Unit) {
            bindAction(binding, item, position)
        }
    }
}