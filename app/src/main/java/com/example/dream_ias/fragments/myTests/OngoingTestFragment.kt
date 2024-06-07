package com.example.dream_ias.fragments.myTests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.ContestsListActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.MyTestMainFragListRes
import com.example.dream_ias.databinding.FragmentOngoingTestBinding
import com.example.dream_ias.databinding.ItemOngoingTestBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class OngoingTestFragment : BaseFragment<FragmentOngoingTestBinding>() {
    private var examId: String = ""
    private var haveData = true
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private lateinit var adapter: OngoingAdapter
    override fun getLayout(): FragmentOngoingTestBinding {
        return FragmentOngoingTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        examId = arguments?.getString("test_ongoing_exam_id") ?: ""
        println("=====ongroing$examId")

        binding.btnExploreUpcomingContest.setOnClickListener {
            (requireParentFragment() as SubTestsFragment).moveToPosition(1)
        }
        observer()
    }

    private fun myContestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._getMyContest(token, "ongoing", examId,null)
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
                        adapter = OngoingAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(it.value.data?.result!!)
                        if (it.value.data?.result!!.isNotEmpty()) {
                            binding.llNoTest.visibility = View.GONE
                            binding.rvList.visibility = View.VISIBLE
                        } else {
                            binding.llNoTest.visibility = View.VISIBLE
                            binding.rvList.visibility = View.GONE
                        }
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

    override fun onResume() {
        super.onResume()
        Log.d("TEST_FRAGMENT", "onResume: $haveData")
       /* if (haveData) {
            binding.llNoTest.visibility = View.VISIBLE
            binding.rvList.visibility = View.GONE
        } else {
            binding.llNoTest.visibility = View.GONE
            binding.rvList.visibility = View.VISIBLE
        }
        haveData = !haveData*/
        myContestList()

    }

    class OngoingAdapter : BaseAdapter<MyTestMainFragListRes.Data.Result>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return OngoingViewHolder(
                ItemOngoingTestBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as OngoingAdapter.OngoingViewHolder).apply {
                binding.tvTitle.text = get(absoluteAdapterPosition).testData?.testTitle ?: ""
                binding.tvBottomTitle.text =
                    get(absoluteAdapterPosition).testData?.testsubTitle ?: ""
                binding.tvContestCount.text =
                    "${get(absoluteAdapterPosition).contestData?.contestCount ?: 0} Contests"
            }
        }

        inner class OngoingViewHolder(val binding: ItemOngoingTestBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                /* binding.btnContinue.setOnClickListener {
                     it.context.startActivity(Intent(it.context, InstructionActivity::class.java))
                 }*/

                /* binding.root.setOnClickListener {
                     it.context.startActivity(
                         Intent(
                             it.context,
                             TestInfoActivity::class.java
                         ).apply {
                             putExtra(BundleKey.CONTEST_JOINED.name, true)
                         }
                     )
                 }*/
                binding.btnContinue.setOnClickListener {
                    if (get(absoluteAdapterPosition).testData != null) {
                        it.context.startActivity(
                            Intent(
                                it.context,
                                ContestsListActivity::class.java
                            ).putExtra("from", "my_test")
                                .putExtra(
                                    "my_test_exam_id",
                                    get(absoluteAdapterPosition).examId ?: ""
                                )
                                .putExtra(
                                    "my_test_testId",
                                    get(absoluteAdapterPosition).testId ?: ""
                                )
                                .putExtra(
                                    "time_value",
                                    get(absoluteAdapterPosition).testData?.conteststartTime ?: ""
                                )
                        )
                    } else {
                        CommonUtil.showSnackBar(it.context, "contest startTime is not Found", R.color.red)
                    }

                }
            }
        }
    }

}