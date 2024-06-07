package com.example.dream_ias.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dream_ias.apiResponse.plan.HomePlanRes
import com.example.dream_ias.databinding.ChildSubscriptionItemBinding

class ChildSubscriptionAdapter(
    val con: Context,
    val list: ArrayList<HomePlanRes.Data.PlanFeature>
   /* val onPress: Click*/
) :
    RecyclerView.Adapter<ChildSubscriptionAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ChildSubscriptionItemBinding) :
        RecyclerView.ViewHolder(mView.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ChildSubscriptionItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       /* if (!list[position].icon.isNullOrEmpty()) {
            Glide.with(con).load(list[position].icon).into(holder.mView.imageView11)
        }*/
        holder.mView.textView26.text = list[position].feature ?: ""
        //holder.mView.textView27.text = list[position].description ?: ""
       /* holder.itemView.setOnClickListener {
            onPress.onClick(list[position].configurePrice!!, list[position].title ?: "", position)
        }*/

    }

    override fun getItemCount(): Int {
        return list.size
    }

  /*  interface Click {
        fun onClick(
            msg: ArrayList<HomePlanRes.Data.ConfigurePrice>,
            nameTitle: String,
            position: Int
        )
    }*/
}