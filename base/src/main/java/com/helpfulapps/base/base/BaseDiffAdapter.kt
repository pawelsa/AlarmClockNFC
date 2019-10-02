package com.helpfulapps.base.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDiffAdapter<ITEM, DB : ViewDataBinding>(callback: DiffUtil.ItemCallback<ITEM>) :
    ListAdapter<ITEM, BaseDiffAdapter<ITEM, DB>.BaseViewHolder<DB>>(callback) {

    abstract val itemView: Int

    override fun onBindViewHolder(holder: BaseViewHolder<DB>, position: Int) {
        bind(holder.binding, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DB> =
        BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemView,
                parent,
                false
            )
        )

    protected abstract fun bind(binding: DB, position: Int)

    open inner class BaseViewHolder<out T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root)
}