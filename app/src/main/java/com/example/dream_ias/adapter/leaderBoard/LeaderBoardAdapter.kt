package com.example.dream_ias.adapter.leaderBoard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dream_ias.R
import com.example.dream_ias.apiResponse.leaderBoard.ContestMainLeaderBoardRes
import com.example.dream_ias.databinding.LeaderBoardItemBinding

class LeaderBoardAdapter(val con: Context, val list: ArrayList<ContestMainLeaderBoardRes.Data>) :
    RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: LeaderBoardItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(LeaderBoardItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            holder.mView.main.background =
                ContextCompat.getDrawable(con, R.drawable.dark_orange_stroke_light_yellow_bg)
        }

            Glide.with(con).load(list[position].userData?.image ?: "")
                .into(holder.mView.imageView31)
            holder.mView.textView68.text = "${list[position].userData?.firstName ?: ""} ${
                list[position].userData?.lastName ?: ""
            }"
            holder.mView.textView69.text = "${list[position].totalScore ?: 0} Score"
            // holder.mView.textView70.text = "${list[position].totalScore?:0} Score"

    }

    override fun getItemCount(): Int {
        return list.size
    }
}