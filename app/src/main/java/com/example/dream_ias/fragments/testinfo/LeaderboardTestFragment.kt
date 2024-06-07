package com.example.dream_ias.fragments.testinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.leaderBoard.ChildLeaderBoardList
import com.example.dream_ias.databinding.FragmentTestLeaderboardBinding
import com.example.dream_ias.databinding.ItemContestantBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class LeaderboardTestFragment : BaseFragment<FragmentTestLeaderboardBinding>() {
    private lateinit var adapter: LeaderBoardAdapter
    private var contestId: String = ""
    private var list = ArrayList<ChildLeaderBoardList.Data.Result>()
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): FragmentTestLeaderboardBinding {
        return FragmentTestLeaderboardBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contestId = App.app.prefManager.contestId
        observer()


    }

    private fun childLeaderBoard() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._childLeaderBoardList(token, contestId)
    }

    private fun observer() {
        viewModel._child_leader_board_liveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                    Loaders.show(requireContext())
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result!!)
                        adapter = LeaderBoardAdapter()
                        binding.rcy.adapter = adapter
                        adapter.set(list)
                        binding.tvContestantCount.text = "All Contestants (${list.size})"

                    } else {
                        Loaders.hide()
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    class LeaderBoardAdapter : BaseAdapter<ChildLeaderBoardList.Data.Result>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return WinningsViewHolder(
                ItemContestantBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as WinningsViewHolder).apply {
                if (!get(position).image.isNullOrEmpty()) {
                    Glide.with(binding.ivProfile).load(get(absoluteAdapterPosition).image)
                        .into(binding.ivProfile)
                }

                binding.tvName.text = "${get(absoluteAdapterPosition).firstName ?: ""} ${
                    get(
                        absoluteAdapterPosition
                    ).lastName ?: ""
                }"


                /*  if (position > 2) {
                      binding.viewStart.visibility = View.GONE
                      binding.ivPrize.visibility = View.GONE
                  } else {
                      val newColor = ColorUtils.setAlphaComponent(
                          ContextCompat.getColor(binding.root.context, R.color.blue),
                          1 - (position / 2)
                      )
                      binding.viewStart.setBackgroundColor(newColor)
                      binding.viewStart.visibility = View.VISIBLE
                      binding.ivPrize.visibility = View.VISIBLE
                  }*/
            }
        }

        inner class WinningsViewHolder(val binding: ItemContestantBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!contestId.isNullOrEmpty()){
            childLeaderBoard()
        }

    }
}