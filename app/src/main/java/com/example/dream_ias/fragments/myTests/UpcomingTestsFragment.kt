package com.example.dream_ias.fragments.myTests

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.MyTestMainFragListRes
import com.example.dream_ias.databinding.FragmentUpcomingTestBinding
import com.example.dream_ias.databinding.ItemUpcomingTestBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel
import java.text.DecimalFormat
import java.text.NumberFormat

class UpcomingTestsFragment : BaseFragment<FragmentUpcomingTestBinding>() {
    private var upcoming_examId: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private lateinit var adapter: UpcomingAdapter
    override fun getLayout(): FragmentUpcomingTestBinding {
        return FragmentUpcomingTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upcoming_examId = arguments?.getString("test_upcoming_exam_id") ?: ""
        println("=====upcoming$upcoming_examId")

    }

    private fun myContestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._getMyContest(token, "upcoming", upcoming_examId,null)
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
                        adapter = UpcomingAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(it.value.data?.result!!)
                        if (it.value.data?.result!!.isEmpty()){
                            binding.lottieEmpty.visibility=View.VISIBLE
                            binding.rvList.visibility= View.GONE
                        }else{
                            binding.lottieEmpty.visibility=View.GONE
                            binding.rvList.visibility= View.VISIBLE
                        }
                    } else {
                        binding.lottieEmpty.visibility=View.VISIBLE
                        binding.rvList.visibility= View.GONE
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility=View.VISIBLE
                    binding.rvList.visibility= View.GONE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility=View.VISIBLE
                    binding.rvList.visibility= View.GONE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }


    }

    class UpcomingAdapter : BaseAdapter<MyTestMainFragListRes.Data.Result>() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            if (get(holder.absoluteAdapterPosition).testData != null) {
                val time = convertTimeStringToMillis(
                    get(holder.absoluteAdapterPosition).testData?.conteststartTime ?: ""
                ) - getCurrentMillisSinceStartOfDay()
                println("-------timr$time")
                if (isTodayDate(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")) {
                    (holder as UpcomingAdapter.OngoingViewHolder).startTimer(
                        time
                    )
                } else if (isTomorrowdaysDate(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")) {
                    (holder as UpcomingAdapter.OngoingViewHolder).apply {
                        binding.tvTimeLeft.text = "Tomorrow"
                    }
                } else {
                    (holder as UpcomingAdapter.OngoingViewHolder).apply {
                        binding.tvTimeLeft.text =
                            setFormatDateNew(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")
                    }
                }
            }


            val anim =
                AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
            holder.itemView.startAnimation(anim)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.itemView.clearAnimation()
            (holder as UpcomingAdapter.OngoingViewHolder).cancelTimer()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return OngoingViewHolder(
                ItemUpcomingTestBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as UpcomingAdapter.OngoingViewHolder).apply {
                binding.tvTitle.text = get(absoluteAdapterPosition).testData?.testTitle ?: ""
                binding.tvBottomTitle.text =
                    get(absoluteAdapterPosition).testData?.testsubTitle ?: ""
                //binding.tvTimeLeft.text = get(absoluteAdapterPosition).testData?.duration ?: ""
                binding.tvTime.text = get(absoluteAdapterPosition).testData?.conteststartTime ?: ""
                binding.tvContestCount.text =
                    "${get(absoluteAdapterPosition).contestData?.contestCount ?: 0} Contests"
                /*if (get(absoluteAdapterPosition).testData != null) {
                    val time = convertTimeStringToMillis(
                        get(holder.absoluteAdapterPosition).testData?.conteststartTime ?: ""
                    ) - getCurrentMillisSinceStartOfDay()
                    startTimer(time)
                }*/

            }
        }

        inner class OngoingViewHolder(val binding: ItemUpcomingTestBinding) :
            RecyclerView.ViewHolder(binding.root) {
            private var timer: CountDownTimer? = null
            init {
                /*binding.root.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            TestInfoActivity::class.java
                        ).apply {
                            if (adapterPosition % 2 == 0)
                                putExtra(BundleKey.TYPE.name, BundleKey.CONTEST_JOINED.name)
                            else putExtra(BundleKey.TYPE.name, BundleKey.NOT_JOINED.name)
                        }
                    )
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
        myContestList()
        observer()
    }

}