package com.example.dream_ias.fragments.myTests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.activity.results.ResultsActivity
import com.example.dream_ias.activity.solution.SolutionForContestActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.MyTestMainFragListRes
import com.example.dream_ias.databinding.FragmentCompletedTestBinding
import com.example.dream_ias.databinding.ItemCompletedTestBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class CompletedTestFragment : BaseFragment<FragmentCompletedTestBinding>() {
    private lateinit var adapter: CompletedAdapter
    private var completed_examId: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): FragmentCompletedTestBinding {
        return FragmentCompletedTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        completed_examId = arguments?.getString("test_completed_exam_id") ?: ""
        println("=====completed$completed_examId")

    }

    private fun myContestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._getMyContest(token, "completed", completed_examId, null)
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
                        adapter = CompletedAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(it.value.data?.result!!)
                        if (!it.value.data?.result!!.isNullOrEmpty()) {
                            App.app.prefManager.contestId =
                                it.value.data?.result?.get(0)?.contestId ?: ""
                            App.app.prefManager.linkVideoSolution =
                                it.value.data?.result?.get(0)?.questionsData?.solution?.link ?: ""
                            App.app.prefManager.pdfSolution =
                                it.value.data?.result?.get(0)?.questionsData?.solution?.pdf ?: ""
                            binding.rvList.visibility = View.VISIBLE
                            binding.lottieEmpty.visibility= View.GONE
                        }else{
                            binding.rvList.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility= View.VISIBLE
                        }

                    } else {
                        Loaders.hide()
                        binding.rvList.visibility = View.INVISIBLE
                        binding.lottieEmpty.visibility= View.VISIBLE
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    binding.rvList.visibility = View.INVISIBLE
                    binding.lottieEmpty.visibility= View.VISIBLE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    Loaders.hide()
                    binding.rvList.visibility = View.INVISIBLE
                    binding.lottieEmpty.visibility= View.VISIBLE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }


    }

    class CompletedAdapter : BaseAdapter<MyTestMainFragListRes.Data.Result>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return OngoingViewHolder(
                ItemCompletedTestBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as CompletedAdapter.OngoingViewHolder).apply {
                binding.tvTitle.text = get(absoluteAdapterPosition).testData?.testTitle ?: ""
                binding.tvBottomTitle.text =
                    get(absoluteAdapterPosition).testData?.testsubTitle ?: ""
                binding.tvContestCount.text =
                    "${get(absoluteAdapterPosition).contestData?.contestCount ?: 0} Contests"
            }
        }

        inner class OngoingViewHolder(val binding: ItemCompletedTestBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                 binding.btnContinue.setOnClickListener {
                     /*it.context.startActivity(
                         Intent(
                             it.context,
                             SolutionForContestActivity::class.java
                         ).putExtra("from","my_test_main_completed")
                     )*/
                     it.context.startActivity(Intent(it.context, ResultsActivity::class.java).putExtra("from","contest"))
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
