package com.example.dream_ias.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.activity.ContestsListActivity
import com.example.dream_ias.activity.testInfo.InstructionActivity
import com.example.dream_ias.activity.testInfo.TestInfoActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.MyTestMainFragListRes
import com.example.dream_ias.databinding.FragmentMyContestsListBinding
import com.example.dream_ias.databinding.ItemMyContestListBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.showTooltip
import com.example.dream_ias.viewModel.AuthViewModel

class MyContestsFragment : BaseFragment<FragmentMyContestsListBinding>() {
    private lateinit var adapter: ContestsListAdapter
    private var examId: String = ""
    private var contestId: String = ""
    private val activity: ContestsListActivity by lazy {
        requireActivity() as ContestsListActivity
    }
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): FragmentMyContestsListBinding {
        return FragmentMyContestsListBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examId = arguments?.getString("examId").toString()
        contestId = arguments?.getString("contestId").toString()
        println("=======examId$examId")

    }

    private fun myContestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._getMyContest(token, null, examId, contestId)
    }

    private fun observer() {
        viewModel._getMyContestLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        adapter = ContestsListAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(it.value.data?.result!!)
                        /* App.app.prefManager.myContesntSize =
                             (it.value.data?.result?.size).toString()*/

                        activity.binding.tabLayout.getTabAt(1)
                            ?.setText("My Contest (${it.value.data?.result?.size ?: 0})")
                        if (it.value.data?.result!!.isEmpty()) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvList.visibility = View.GONE
                        } else {
                            binding.lottieEmpty.visibility = View.GONE
                            binding.rvList.visibility = View.VISIBLE
                        }

                    } else {
                        /* App.app.prefManager.myContesntSize ="0"*/
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
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
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.GONE
                    /*  App.app.prefManager.myContesntSize ="0"*/
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    /* App.app.prefManager.myContesntSize ="0"*/
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.GONE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }


    }

    class ContestsListAdapter : BaseAdapter<MyTestMainFragListRes.Data.Result>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                ItemMyContestListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ViewHolder).apply {
                /*if (position % 2 == 0) {
                    binding.btnStartQuiz.visibility = View.GONE
                } else {
                    binding.btnStartQuiz.visibility = View.VISIBLE
                }*/
                if (!get(position).contestData?.rangeAmount.isNullOrEmpty()) {
                    if (get(position).contestData?.rangeAmount?.get(0)?.amount != null) {
                        var amount: Int = 0
                        for (amt in get(position).contestData?.rangeAmount!!) {
                            amount += amt.amount ?: 0
                        }
                        binding.tvTotalAmount.text = "₹ $amount"
                    } else {
                        binding.tvTotalAmount.text =
                            "₹ ${get(absoluteAdapterPosition).contestData?.dreamCandidateAmount ?: 0}"
                    }
                }
                if (get(absoluteAdapterPosition).contestData?.studentEntrySpotsNo != null) {
                    if (get(absoluteAdapterPosition).contestData?.spots?.totalNoOfSpots == null) {
                        binding.indicator.visibility = View.GONE
                        binding.tvSpotLeft.visibility = View.GONE
                        binding.tvSpotCount.visibility = View.GONE
                    } else {
                        binding.indicator.visibility = View.VISIBLE
                        binding.tvSpotLeft.visibility = View.VISIBLE
                        binding.tvSpotCount.visibility = View.VISIBLE
                        val totalSport =
                            get(absoluteAdapterPosition).contestData?.spots?.totalNoOfSpots ?: 0
                        val fillSport =
                            get(absoluteAdapterPosition).contestData?.studentEntrySpotsNo ?: 0
                        val leftSport = totalSport - fillSport
                        binding.tvSpotLeft.text = "$leftSport Spots Left"
                        binding.indicator.progress =
                            get(absoluteAdapterPosition).contestData?.studentEntrySpotsNo ?: 0
                        binding.tvSpotCount.text =
                            "${get(absoluteAdapterPosition).contestData?.spots?.totalNoOfSpots ?: 0} Sports"
                    }

                } else {
                    binding.tvSpotLeft.text =
                        "${get(absoluteAdapterPosition).contestData?.spots?.totalNoOfSpots ?: 0} Sports Left"
                    binding.indicator.progress = 0
                    binding.tvSpotCount.text =
                        "${get(absoluteAdapterPosition).contestData?.spots?.totalNoOfSpots ?: 0} Sports"
                }
                binding.tvTitle.text = get(absoluteAdapterPosition).testData?.testTitle ?: ""
                binding.tvOfferPrice.text = "₹${get(absoluteAdapterPosition).amount ?: 0}"
                binding.tvBottomQuestion.text =
                    "${get(absoluteAdapterPosition).testData?.questions ?: 0} Questions"
                binding.tvBottomTime.text =
                    "${get(absoluteAdapterPosition).testData?.duration ?: ""}"
                binding.tvBottomPrice.text =
                    "₹${get(absoluteAdapterPosition).contestData?.dreamCandidateAmount ?: 0}"

            }
        }

        inner class ViewHolder(val binding: ItemMyContestListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

                binding.tvWinPercentage.setOnClickListener {
                    it.showTooltip("Chances of winning is 60 %")
                }
                binding.btnStartQuiz.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            InstructionActivity::class.java
                        )
                    )
                    println("========${get(absoluteAdapterPosition).contestId ?: ""}")
                    App.app.prefManager.contestId = get(absoluteAdapterPosition).contestId ?: ""
                    App.app.prefManager.testId = get(absoluteAdapterPosition).testId ?: ""
                    App.app.prefManager.questionId = get(absoluteAdapterPosition).questionId ?: ""
                    App.app.prefManager.userId = get(absoluteAdapterPosition).userId ?: ""
                }

                binding.root.setOnClickListener {
                    /*if (adapterPosition == 0) {
                       *//* DeadlinePassedBottomSheet {}.apply {
                            show((it.context as FragmentActivity).supportFragmentManager, "")
                        }*//*
                    } else {
                        it.context.startActivity(Intent(
                            it.context, TestInfoActivity::class.java
                        ).apply {
                            putExtra(BundleKey.TYPE.name, BundleKey.CONTEST_JOINED.name)
                        })
                    }*/
                    it.context.startActivity(Intent(
                        it.context, TestInfoActivity::class.java
                    ).apply {
                        putExtra(BundleKey.TYPE.name, BundleKey.CONTEST_JOINED.name)
                    })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myContestList()
        observer()
    }
}