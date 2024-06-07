package com.example.dream_ias.fragments.practiceTests

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.practiceTest.OngoingPracticeTestRes
import com.example.dream_ias.databinding.FragmentUpcomingTestBinding
import com.example.dream_ias.databinding.UpcomingPracticeItemBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.fragments.SubHomeFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import java.text.DecimalFormat
import java.text.NumberFormat

class UpcommingPracticeFragment : BaseFragment<FragmentUpcomingTestBinding>() {
    private var examId: String = ""
    private lateinit var adapter: UpcomingAdapter
    private var list = ArrayList<OngoingPracticeTestRes.Data.Result>()
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): FragmentUpcomingTestBinding {
        return FragmentUpcomingTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examId = arguments?.getString("upcoming_examId").toString()
        println("========upcoming_examId$examId")
        observer()

    }

    private fun ongoingList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.practiceTestListList(token, examId, "upcoming")
    }

    private fun observer() {
        viewModel._practiceTestliveData.observe(this) {
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
                        adapter = UpcomingAdapter()
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
                is Resource.Error -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        it.throwable?.getError(requireContext())?.message.toString(),
                        R.color.red
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

    class UpcomingAdapter : BaseAdapter<OngoingPracticeTestRes.Data.Result>() {
        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)

            val time = convertTimeStringToMillis(
                get(holder.absoluteAdapterPosition).testData?.teststartTime ?: ""
            ) - getCurrentMillisSinceStartOfDay()
            if (isTodayDate(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")) {
                (holder as OngoingViewHolder).startTimer(time)
            } else if (isTomorrowdaysDate(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")) {
                (holder as OngoingViewHolder).apply {
                    binding.tvTimeLeft.text = "Tomorrow"
                }
            } else {
                (holder as OngoingViewHolder).apply {
                    binding.tvTimeLeft.text =
                        setFormatDateNew(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")
                }
            }
//            println("-------timr$time")
//            (holder as OngoingViewHolder).startTimer(time)

            val anim =
                AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
            holder.itemView.startAnimation(anim)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.itemView.clearAnimation()
            (holder as OngoingViewHolder).cancelTimer()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return OngoingViewHolder(
                UpcomingPracticeItemBinding.inflate(
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
                binding.tvTime.text =
                    get(absoluteAdapterPosition).testData?.teststartTime ?: ""
            }
        }

        inner class OngoingViewHolder(val binding: UpcomingPracticeItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            private var timer: CountDownTimer? = null

            init {
                /*binding.root.setOnClickListener {
                    val intent = Intent(it.context, InstructionActivity::class.java)
                    intent.putExtra("fromPractice", true)
                    it.context.startActivity(intent)
                    *//*val intent = Intent(it.context, TestInfoActivity::class.java)
                    if (adapterPosition % 2 == 0)
                        intent.putExtra(BundleKey.TYPE.name, BundleKey.CONTEST_JOINED.name)
                    else intent.putExtra(BundleKey.TYPE.name, BundleKey.NOT_JOINED.name)
                    it.context.startActivity(intent)*//*
                }*/
            }

            fun startTimer(time: Long) {
                timer?.cancel()
                timer = object : CountDownTimer(time, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val f: NumberFormat = DecimalFormat("00")
                        val hours = millisUntilFinished / 3600000
                        val min = millisUntilFinished / 60000 % 60
                        val sec = millisUntilFinished / 1000 % 60
                        if (hours > 0) {
                            binding.tvTimeLeft.post {
                                binding.tvTimeLeft.text = binding.root.context.getString(
                                    R.string.hour_min_sec,
                                    f.format(hours),
                                    f.format(min),
                                    f.format(sec)
                                )
                            }
                        } else {
                            binding.tvTimeLeft.post {
                                binding.tvTimeLeft.text = binding.root.context.getString(
                                    R.string.min_sec,
                                    f.format(min),
                                    f.format(sec)
                                )
                            }
                        }
                    }

                    override fun onFinish() {
                        try {
                            binding.tvTimeLeft.post {
                                binding.tvTimeLeft.text = "Live"
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }.start()
            }

            fun cancelTimer() {
                timer?.cancel()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ongoingList()
    }

}