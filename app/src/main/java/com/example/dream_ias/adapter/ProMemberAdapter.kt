package com.example.dream_ias.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.apiResponse.plan.HomePlanRes
import com.example.dream_ias.databinding.ProMemberItemBinding

class ProMemberAdapter(val con: Context, val list: ArrayList<HomePlanRes.Data.PlanFeature>) :
    RecyclerView.Adapter<ProMemberAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ProMemberItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ProMemberItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView26.text = list[position].feature ?: ""
    }

    override fun getItemCount(): Int {
        return list.size
    }
}