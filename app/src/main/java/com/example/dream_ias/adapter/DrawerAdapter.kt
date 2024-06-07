package com.example.dream_ias.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.databinding.DrawerItemBinding
import com.example.dream_ias.model.DrawerModel

class DrawerAdapter(var con: Context, var list: ArrayList<DrawerModel>, val onPress: Click) :
    RecyclerView.Adapter<DrawerAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: DrawerItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DrawerItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvName.text = list[position].name
        holder.mView.iv.setImageResource(list[position].img)
        holder.itemView.setOnClickListener {
            onPress.onClickSideItem(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onClickSideItem(position: Int)
    }
}