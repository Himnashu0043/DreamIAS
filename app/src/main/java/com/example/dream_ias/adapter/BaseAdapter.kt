package com.example.dream_ias.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseAdapter<itemType> : RecyclerView.Adapter<ViewHolder>() {
    private val itemList = mutableListOf<itemType>()
    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun get(position: Int): itemType {
        return itemList[position]
    }


    fun set(items: List<itemType>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun add(items: List<itemType>) {
        val count = itemList.count()
        itemList.addAll(items)
        notifyItemRangeInserted(count,items.count())
    }


    fun add(item: itemType) {
        val addingPosition = itemList.count()
        itemList.add(item)
        notifyItemInserted(addingPosition)
    }

}