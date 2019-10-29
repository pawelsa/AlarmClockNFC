package com.helpfulapps.base.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<ITEM, DB : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<ITEM>) :
    ListAdapter<ITEM, BaseListAdapter.ItemViewHolder<ITEM, DB>>(diffCallback) {

    abstract val bind: DB.(item: ITEM, position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<ITEM, DB> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<DB>(layoutInflater, viewType, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder<ITEM, DB>, position: Int) {
        holder.bind(getItem(position), position, bind)
    }

    class ItemViewHolder<ITEM, DB : ViewDataBinding>(private val binding: DB) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ITEM, position: Int, bindAction: DB.(item: ITEM, position: Int) -> Unit) {
            bindAction(binding, item, position)
        }
    }
}