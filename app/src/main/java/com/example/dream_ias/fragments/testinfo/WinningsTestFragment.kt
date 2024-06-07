package com.example.dream_ias.fragments.testinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.TestContestListRes
import com.example.dream_ias.databinding.FragmentTestWinningsBinding
import com.example.dream_ias.databinding.ItemWinningListBinding
import com.example.dream_ias.fragments.BaseFragment

class WinningsTestFragment : BaseFragment<FragmentTestWinningsBinding>() {
    private lateinit var adapter: WinningsAdapter
    private var list: TestContestListRes.Data.Result? = null
    override fun getLayout(): FragmentTestWinningsBinding {
        return FragmentTestWinningsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = arguments?.getSerializable("list_winning") as TestContestListRes.Data.Result?
        println("----------$list")

        adapter = WinningsAdapter()
        binding.rvWinnings.adapter = adapter
        //adapter.add((0..6).toList())
        adapter.set(list?.rangeAmount!!)
    }

    class WinningsAdapter : BaseAdapter<TestContestListRes.Data.Result.RangeAmount>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return WinningsViewHolder(
                ItemWinningListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as WinningsViewHolder).apply {
                if (position > 2) {
                    binding.viewStart.visibility = View.GONE
                    binding.ivPrize.visibility = View.GONE
                } else {
                    when (position) {
                        0 ->
                            binding.viewStart.setBackgroundColor(
                                getColor(
                                    binding.root.context,
                                    R.color.blue_100
                                )
                            )


                        1 ->
                            binding.viewStart.setBackgroundColor(
                                getColor(
                                    binding.root.context,
                                    R.color.blue_80
                                )
                            )


                        2 ->
                            binding.viewStart.setBackgroundColor(
                                getColor(
                                    binding.root.context,
                                    R.color.blue_60
                                )
                            )

                    }
                    /*val newColor = ColorUtils.setAlphaComponent(
                        getColor(binding.root.context, R.color.blue),
                        1 - (position / 2)
                    )*/
                    binding.viewStart.visibility = View.VISIBLE
                    binding.ivPrize.visibility = View.VISIBLE
                    //chnages

                }
                binding.tvRank.text = "${position + 1}"
                binding.tvPriceValue.text = "₹ ${get(absoluteAdapterPosition).amount?:0}"
            }
        }

        inner class WinningsViewHolder(val binding: ItemWinningListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }
    }
}