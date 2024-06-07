package com.example.dream_ias.fragments.practiceTests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.solution.SolutionForContestActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.practiceTest.CompletedPracticeTestRes
import com.example.dream_ias.databinding.CompletedPracticeItemBinding
import com.example.dream_ias.databinding.FragmentCompletedTestBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class CompletedPracticeFragment : BaseFragment<FragmentCompletedTestBinding>() {
    private var examId: String = ""
    private lateinit var adapter: CompletedAdapter
    private var list = ArrayList<CompletedPracticeTestRes.Data.Result>()
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): FragmentCompletedTestBinding {
        return FragmentCompletedTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examId = arguments?.getString("completed_examId").toString()
        println("========completed_examId$examId")
       /* adapter = CompletedAdapter()
        binding.rvList.adapter = adapter
        adapter.add((0..6).toList())*/
        observer()

    }

    private fun ongoingList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.practiceCompletedTestList(token, examId)
    }

    private fun observer() {
        viewModel._practiceCompletedTestliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result!!)
                        adapter = CompletedAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(list)
                        if (list.isEmpty()) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvList.visibility = View.INVISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.GONE
                            binding.rvList.visibility = View.VISIBLE
                        }

                    } else {
                        Loaders.hide()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rvList.visibility = View.INVISIBLE
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
                    binding.rvList.visibility = View.INVISIBLE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error ->{
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        it.throwable?.getError(requireContext())?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                }
            }
        }
    }

    class CompletedAdapter : BaseAdapter<CompletedPracticeTestRes.Data.Result>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return OngoingViewHolder(
                CompletedPracticeItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as OngoingViewHolder).apply {
                binding.tvTitle.text = get(absoluteAdapterPosition).testData?.testTitle ?: ""
                binding.tvBottomTitle.text =
                    get(absoluteAdapterPosition).testData?.testsubTitle ?: ""
                binding.tvtotalMarks.text =
                    "${get(absoluteAdapterPosition).testData?.totalMarks ?: 0} Marks"
                binding.tvBottomQuestion.text =
                    "${get(absoluteAdapterPosition).testData?.questions ?: ""} Question"
                binding.tvBottomTime.text =
                    get(absoluteAdapterPosition).testData?.duration ?: ""
                binding.tvBottomNegativeMark.text =
                    "${get(absoluteAdapterPosition).negativeMarking?.marking ?: 0} Marks "
                App.app.prefManager.linkVideoSolution =
                    get(absoluteAdapterPosition).solution?.link ?: ""
                App.app.prefManager.pdfSolution = get(absoluteAdapterPosition).solution?.pdf ?: ""
                binding.textView72.text
                if (get(absoluteAdapterPosition).isAttempted != true) {
                    binding.tvTimeLeft.text = "Non-Attempted"
                    binding.tvTimeLeft.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.red
                        )
                    )
                    binding.layBottom.visibility = View.GONE
                    binding.divider14.visibility = View.GONE
                } else {
                    binding.tvTimeLeft.text = "Attempted"
                    binding.tvTimeLeft.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.text_green
                        )
                    )
                    binding.layBottom.visibility = View.VISIBLE
                    binding.divider14.visibility = View.VISIBLE
                    binding.textView72.text =
                        "${get(absoluteAdapterPosition).userEntry?.get(absoluteAdapterPosition)?.totalScore ?: 0.0}"
                    binding.textView73.text =
                        "${get(absoluteAdapterPosition).userEntry?.get(absoluteAdapterPosition)?.ranking ?: 0}"
                }
            }
        }

        inner class OngoingViewHolder(val binding: CompletedPracticeItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.btnContinue.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            SolutionForContestActivity::class.java
                        ).putExtra("from", "my_practice_test_completed")
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ongoingList()
    }

}