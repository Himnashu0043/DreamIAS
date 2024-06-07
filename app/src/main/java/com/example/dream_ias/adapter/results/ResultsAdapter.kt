package com.example.dream_ias.adapter.results

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.apiResponse.results.ResultsListRes
import com.example.dream_ias.databinding.ResultsItemsBinding
import com.example.dream_ias.model.ResultsModel

class ResultsAdapter(val con: Context, val list: ArrayList<ResultsModel>) :
    RecyclerView.Adapter<ResultsAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ResultsItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ResultsItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView63.text = list[position].name
        holder.mView.textView64.text = list[position].des
        holder.mView.imageView20.setImageResource(list[position].img)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}