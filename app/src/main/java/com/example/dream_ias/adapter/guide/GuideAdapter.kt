package com.example.dream_ias.adapter.guide

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.apiResponse.TestContestListRes
import com.example.dream_ias.databinding.GuideItemBinding

class GuideAdapter(
    val con: Context,
    val list: ArrayList<TestContestListRes.Data.Result.ConfigureGuide>
) : RecyclerView.Adapter<GuideAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: GuideItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(GuideItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val htmlAsString =
            list[position].paragraph
        val htmlAsSpanned = Html.fromHtml(htmlAsString)
        holder.mView.textView719.text = htmlAsSpanned
        holder.mView.textView79.text = list[position].title
    }

    override fun getItemCount(): Int {
        return list.size
    }
}