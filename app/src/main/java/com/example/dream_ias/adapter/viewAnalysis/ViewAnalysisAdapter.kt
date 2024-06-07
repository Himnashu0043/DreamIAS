package com.example.dream_ias.adapter.viewAnalysis

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.apiResponse.practiceTest.ResultsPracticeRes
import com.example.dream_ias.apiResponse.results.ResultsListRes
import com.example.dream_ias.databinding.ViewAnalysisItemBinding

class ViewAnalysisAdapter(
    val con: Context,
    val list: ArrayList<ResultsListRes.Data.Result.Answer>,
    val from: String,
    val list1: ArrayList<ResultsPracticeRes.Data.Result.Answer>
) :
    RecyclerView.Adapter<ViewAnalysisAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ViewAnalysisItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ViewAnalysisItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*when (position) {
            0 -> {
                holder.mView.ivCorrect.visibility = View.VISIBLE
                holder.mView.ivWrong.visibility = View.INVISIBLE
            }
            1 -> {
                holder.mView.ivCorrect.visibility = View.INVISIBLE
                holder.mView.ivWrong.visibility = View.VISIBLE
            }

        }*/

        if (from == "contest") {
            if (list[position].iscorrect == "true") {
                holder.mView.ivCorrect.visibility = View.VISIBLE
                holder.mView.ivWrong.visibility = View.INVISIBLE
            } else {
                holder.mView.ivCorrect.visibility = View.INVISIBLE
                holder.mView.ivWrong.visibility = View.VISIBLE
            }
            val htmlAsString =
                list[position].question ?: ""
            val htmlAsSpanned = Html.fromHtml(htmlAsString)
            holder.mView.textView57.text = htmlAsSpanned
            holder.mView.textView58.text = list[position].queAns ?: ""

        } else {
            if (list1[position].iscorrect == "true") {
                holder.mView.ivCorrect.visibility = View.VISIBLE
                holder.mView.ivWrong.visibility = View.INVISIBLE
            } else {
                holder.mView.ivCorrect.visibility = View.INVISIBLE
                holder.mView.ivWrong.visibility = View.VISIBLE
            }
            val htmlAsString =
                list1[position].question ?: ""
            val htmlAsSpanned = Html.fromHtml(htmlAsString)
            holder.mView.textView57.text = htmlAsSpanned
            holder.mView.textView58.text = list1[position].queAns ?: ""
        }
    }

    override fun getItemCount(): Int {
        if (from == "contest") {
            return list.size
        } else {
            return list1.size
        }

    }
}