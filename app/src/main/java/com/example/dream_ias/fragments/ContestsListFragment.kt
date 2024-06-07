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
import com.example.dream_ias.activity.testInfo.TestInfoActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.TestContestListRes
import com.example.dream_ias.databinding.FragmentContestsListBinding
import com.example.dream_ias.databinding.ItemContestsListBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class ContestsListFragment : BaseFragment<FragmentContestsListBinding>() {
    private var id: String = ""
    private var list = ArrayList<TestContestListRes.Data.Result>()

    private val activity: ContestsListActivity by lazy {
        requireActivity() as ContestsListActivity
    }

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private lateinit var adapter: ContestsListAdapter

    override fun getLayout(): FragmentContestsListBinding {
        return FragmentContestsListBinding.inflate(layoutInflater)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString("id").toString()
        println("=======conet$id")
        observer()

    }

    private fun myTestContestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._myTestContest(token, id)
    }
    private fun observer() {
        viewModel._myTestContestLiveData.observe(requireActivity()) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result!!)
                        adapter = ContestsListAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(list)

                        if (list.isEmpty()){
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvList.visibility = View.GONE
                        }else{
                            binding.lottieEmpty.visibility = View.GONE
                            binding.rvList.visibility = View.VISIBLE
                            activity.binding.tabLayout.getTabAt(0)
                                ?.setText("Contest (${list.size})")
                            activity.binding.tvTittle.text = list[0].testTitle ?: ""
                        }

                    } else {
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.GONE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
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
        viewModel._getContestEntryLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value.message.toString(),
                            android.R.color.holo_green_dark
                        )
                        myTestContestList()
                    } else {
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    inner class ContestsListAdapter : BaseAdapter<TestContestListRes.Data.Result>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                ItemContestsListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ContestsListAdapter.ViewHolder).apply {

                binding.tvTitle.text = get(position).testTitle ?: ""
                if (get(absoluteAdapterPosition).hasUserEntered) {
                    binding.tvEntry.visibility = View.INVISIBLE
                    binding.tvEntryFee.visibility = View.INVISIBLE
                    binding.divider6.visibility = View.INVISIBLE
                    binding.tvOfferPrice.text = "Joined"
                }else if (get(absoluteAdapterPosition).entryFee?.noEntryFee != "yes") {
                    binding.tvEntry.visibility = View.VISIBLE
                    binding.tvEntryFee.visibility = View.VISIBLE
                    binding.divider6.visibility = View.VISIBLE
                    binding.tvEntryFee.text = "₹${get(position).entryFee?.entryFee ?: ""}"
                    binding.tvOfferPrice.text =
                        "₹${(get(position).entryFee?.entryFee ?: 0) - (get(position).entryFee?.discount ?: 0)}"
                } else {
                    binding.tvEntryFee.text = "₹${0}"
                    binding.tvOfferPrice.text = "₹${0}"
                }
                if (!list.get(position).rangeAmount.isNullOrEmpty()){
                    if (list.get(position).rangeAmount?.get(0)?.amount != null) {
                        var amount: Int = 0
                        for (amt in list.get(position).rangeAmount!!) {
                            amount += amt.amount ?: 0
                        }
                        binding.tvTotalAmount.text = "₹ $amount"
                    } else {
                        binding.tvTotalAmount.text =
                            "₹ ${get(absoluteAdapterPosition).dreamCandidateAmount ?: 0}"
                    }
                }
                if (get(absoluteAdapterPosition).studentEntrySpotsNo != null) {
                    if (get(absoluteAdapterPosition).spots?.totalNoOfSpots == null) {
                        binding.indicator.visibility = View.GONE
                        binding.tvSpotLeft.visibility = View.GONE
                        binding.tvSpotCount.visibility = View.GONE
                    } else {
                        binding.indicator.visibility = View.VISIBLE
                        binding.tvSpotLeft.visibility = View.VISIBLE
                        binding.tvSpotCount.visibility = View.VISIBLE
                        val totalSport = get(absoluteAdapterPosition).spots?.totalNoOfSpots ?: 0
                        val fillSport = get(absoluteAdapterPosition).studentEntrySpotsNo ?: 0
                        val leftSport = totalSport - fillSport
                        binding.tvSpotLeft.text = "$leftSport Spots Left"
                        binding.indicator.progress =
                            get(absoluteAdapterPosition).studentEntrySpotsNo ?: 0
                        binding.tvSpotCount.text =
                            "${get(position).spots?.totalNoOfSpots ?: ""} Sports"
                    }
                } else {
                    binding.tvSpotLeft.text = "${get(position).spots?.totalNoOfSpots ?: ""}  Spots Left"
                    binding.indicator.progress = 0
                    binding.tvSpotCount.text =
                        "${get(position).spots?.totalNoOfSpots ?: ""} Sports"
                }
                binding.tvBottomPrice.text =
                    "₹${get(position).dreamCandidateAmount ?: 0}"
                binding.tvBottomQuestion.text = "${get(position).questions ?: 0} Question"
                binding.tvBottomTime.text = "${get(position).duration ?: ""}"


            }
        }

        inner class ViewHolder(val binding: ItemContestsListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            TestInfoActivity::class.java
                        ).apply {
                            /*if (adapterPosition % 2 != 0)
                                putExtra(BundleKey.TYPE.name, BundleKey.CONTEST_JOINED.name)
                            else putExtra(BundleKey.TYPE.name, BundleKey.NOT_JOINED.name)
                            putExtra("list", get(absoluteAdapterPosition))*/
                            if (get(absoluteAdapterPosition).hasUserEntered)
                                putExtra(BundleKey.TYPE.name, BundleKey.CONTEST_JOINED.name)
                            else putExtra(BundleKey.TYPE.name, BundleKey.NOT_JOINED.name)
                            putExtra("list", get(absoluteAdapterPosition))
                        }
                    )

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myTestContestList()

    }

}