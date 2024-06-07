package com.example.dream_ias.adapter.subScription

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.apiResponse.plan.SideSubscriptionRes
import com.example.dream_ias.databinding.ActiveSubscriptionItemBinding
import com.example.dream_ias.util.setFormatDate

class ActiveSubScriptionAdapter(
    val con: Context,
    val key: String,
    var list: ArrayList<SideSubscriptionRes.Data>
) :
    RecyclerView.Adapter<ActiveSubScriptionAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ActiveSubscriptionItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ActiveSubscriptionItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (key == "expired") {
            holder.mView.tvContestCount.text = "Expired"
            holder.mView.tvContestCount.setTextColor(ContextCompat.getColor(con, R.color.red))
            holder.mView.tvTitle.text = list[position].examName ?: ""
            holder.mView.textView54.text = setFormatDate(list[position].createdAt ?: "")
            holder.mView.textView541.text = setFormatDate(list[position].expiredAt ?: "")
            holder.mView.textView5411.text = "Rs. ${list[position].amount ?: 0}"
        } else {
            holder.mView.tvContestCount.text = "Active"
            holder.mView.tvContestCount.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.main_green
                )
            )
            holder.mView.tvTitle.text = list[position].examName ?: ""
            holder.mView.textView54.text = setFormatDate(list[position].createdAt ?: "")
            holder.mView.textView541.text = setFormatDate(list[position].expiredAt ?: "")
            holder.mView.textView5411.text = "Rs. ${list[position].amount ?: 0}"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}