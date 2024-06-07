package com.example.dream_ias.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.apiResponse.FAQListRes
import com.example.dream_ias.databinding.FaqItemBinding

class FAQAdapter(val con: Context, var list: ArrayList<FAQListRes.Data>) :
    RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: FaqItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FaqItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.ivForword.setOnClickListener {
            if (holder.mView.ivForword.visibility == View.VISIBLE) {
                holder.mView.ivForword.visibility = View.GONE
                holder.mView.ivDown.visibility = View.VISIBLE
                holder.mView.childLay.visibility = View.VISIBLE
            }
        }
        holder.mView.ivDown.setOnClickListener {
            if (holder.mView.ivDown.visibility == View.VISIBLE) {
                holder.mView.ivForword.visibility = View.VISIBLE
                holder.mView.ivDown.visibility = View.GONE
                holder.mView.childLay.visibility = View.GONE

            }
        }
        holder.mView.textView44.text =list[position].faqName?:""
        holder.mView.tvDes.text =list[position].description?:""
    }

    override fun getItemCount(): Int {
        return list.size
    }
}