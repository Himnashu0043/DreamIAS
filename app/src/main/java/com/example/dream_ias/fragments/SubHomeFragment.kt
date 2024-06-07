package com.example.dream_ias.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.ContestsListActivity
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.MyTestListRes
import com.example.dream_ias.databinding.FragmentSubHomeBinding
import com.example.dream_ias.databinding.ItemMyContestsBinding
import com.example.dream_ias.databinding.ItemUpcomingContestBinding
import com.example.dream_ias.databinding.ItemUpcomingPracticeBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel
import java.text.DecimalFormat
import java.text.NumberFormat

class SubHomeFragment : BaseFragment<FragmentSubHomeBinding>() {
    private var id: String = ""
    private var subscribed: Boolean = false
    private var dialog: Dialog? = null
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private val activity: HomeActivity by lazy {
        requireActivity() as HomeActivity
    }
    private var contestlist = ArrayList<MyTestListRes.Data.Mycontest>()
    private var upcomingContestlist = ArrayList<MyTestListRes.Data.UpComingContest>()

    private var upcomingPracticelist = ArrayList<MyTestListRes.Data.UpComingPractice>()
    private val myContestAdapter: MyContestsAdapter by lazy {
        MyContestsAdapter()
    }
    private val upcomingContestAdapter: UpcomingContestsAdapter by lazy {
        UpcomingContestsAdapter()
    }
    private val upcomingPracticeContestAdapter: UpcomingPracticeContestsAdapter by lazy {
        UpcomingPracticeContestsAdapter()
    }

    override fun getLayout(): FragmentSubHomeBinding {
        return FragmentSubHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString("id").toString()
        observer()
        println("=======$id")
        binding.rvMyContests.adapter = myContestAdapter
        binding.rvUpcomingContests.adapter = upcomingContestAdapter
        binding.rvUpcomingTests.adapter = upcomingPracticeContestAdapter
//        myContestAdapter.add((0..1).toList())
        // upcomingPracticeContestAdapter.add((0..1).toList())
    }

    override fun onResume() {
        super.onResume()
        myTestList()

    }

    private fun myTestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._myTest(token, id)
    }

    private fun observer() {
        viewModel._myTestLiveData.observe(requireActivity()) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        contestlist.clear()
                        contestlist.addAll(it.value.data?.mycontest!!)
                        upcomingContestlist.clear()
                        upcomingContestlist.addAll(it.value.data?.upComingContest!!)
                        upcomingPracticelist.clear()
                        upcomingPracticelist.addAll(it.value.data?.upComingPractice!!)
                        myContestAdapter.set(contestlist)
                        upcomingContestAdapter.set(upcomingContestlist)
                        upcomingPracticeContestAdapter.set(upcomingPracticelist)
                        subscribed = it.value.data?.subscribed == true
                        App.app.prefManager.isPracticeSubS = it.value.data?.subscribed == true
                        println("======subscribed$subscribed")
                        if (contestlist.isEmpty()) {
                            binding.tvMyContests.visibility = View.GONE
                            binding.lottieEmpty.visibility = View.GONE
                        } else {
                            binding.lottieEmpty.visibility = View.GONE
                            binding.tvMyContests.visibility = View.VISIBLE
                        }
                        if (upcomingContestlist.isEmpty()) {
                            binding.lottieEmptyUpcoming.visibility = View.VISIBLE
                        } else {
                            binding.lottieEmptyUpcoming.visibility = View.GONE
                        }
                        if (upcomingPracticelist.isEmpty()) {
                            binding.lottieEmptyPractice.visibility = View.VISIBLE
                        } else {
                            binding.lottieEmptyPractice.visibility = View.GONE
                        }

                    } else {
                        Loaders.hide()
                         binding.lottieEmpty.visibility = View.VISIBLE
                         binding.lottieEmptyUpcoming.visibility = View.VISIBLE
                         binding.lottieEmptyPractice.visibility = View.VISIBLE
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
                    binding.lottieEmptyUpcoming.visibility = View.VISIBLE
                    binding.lottieEmptyPractice.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    Loaders.hide()
                      binding.lottieEmpty.visibility = View.VISIBLE
                      binding.lottieEmptyUpcoming.visibility = View.VISIBLE
                      binding.lottieEmptyPractice.visibility = View.VISIBLE
                    "Something Went Wrong".show(binding, R.color.red)
//                    CommonUtil.showSnackBar(
//                        requireContext(),
//                        "Something Went Wrong",
//                        android.R.color.holo_red_light
//                    )
                }
            }
        }
    }

    inner class MyContestsAdapter : BaseAdapter<MyTestListRes.Data.Mycontest>() {
        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            val time = convertTimeStringToMillis(
                get(holder.absoluteAdapterPosition).testData?.conteststartTime ?: ""
            ) - getCurrentMillisSinceStartOfDay()
            println("-------MyContestTimer$time")
            if (isTodayDate(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")) {
                (holder as ContestsViewHolder).startTimer1(time)
            } else if (isTomorrowdaysDate(
                    get(holder.absoluteAdapterPosition).testData?.startDate ?: ""
                 )
             ) {
                 (holder as ContestsViewHolder).apply {
                     binding.tvTimeLeft.text = "Tomorrow"
                 }
             } else {
                 (holder as ContestsViewHolder).apply {
                     binding.tvTimeLeft.text =
                         setFormatDateNew(
                             get(holder.absoluteAdapterPosition).testData?.startDate ?: ""
                         )
                 }
             }
             val anim =
                 AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
             holder.itemView.startAnimation(anim)
         }

         override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
             super.onViewDetachedFromWindow(holder)
             holder.itemView.clearAnimation()
             (holder as ContestsViewHolder).cancelTimer()
         }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContestsViewHolder(
                ItemMyContestsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ContestsViewHolder).apply {
                println("=======test")
                binding.tvTitle.text = get(position).testData?.testTitle ?: ""
                binding.tvContestCount.text = "${get(position).conData?.contestCount ?: 0} Contests"
                binding.tvDesi.text = get(position).testData?.testsubTitle ?: ""
                binding.tvTime.text = get(position).testData?.conteststartTime ?: ""
                //binding.tvTimeLeft.text = "Joined"
                binding.tvTitle.text = get(position).testData?.testTitle ?: ""
            }
        }

        inner class ContestsViewHolder(val binding: ItemMyContestsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            private var timer: CountDownTimer? = null
            init {
                binding.root.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            ContestsListActivity::class.java
                        ).putExtra(
                            "id", get(absoluteAdapterPosition).testData?._id
                        ).putExtra("list_size", itemCount)
                            .putExtra(
                                "time_value",
                                get(absoluteAdapterPosition).testData?.conteststartTime ?: ""
                            )

                    )
                }
            }

            fun startTimer1(time: Long) {
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
                            if (min < 50000) {
                                println("=================$min")
                                contestStartPopup()
                                var test = dialog?.findViewById<TextView>(R.id.tvLiveStream1)
                                test?.text = binding.root.context.getString(
                                    R.string.min_sec,
                                    f.format(min),
                                    f.format(sec)
                                )
                            }
                            binding.tvTimeLeft.post {
                                println("=================mmm$min")
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

    inner class UpcomingContestsAdapter : BaseAdapter<MyTestListRes.Data.UpComingContest>() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)

            val time = convertTimeStringToMillis(
                get(holder.absoluteAdapterPosition).conteststartTime ?: ""
            ) - getCurrentMillisSinceStartOfDay()
            println("-------timr$time")
            if (isTodayDate(get(holder.absoluteAdapterPosition).startDate ?: "")) {
                (holder as ContestsViewHolder).startTimer(time)
            } else if (isTomorrowdaysDate(get(holder.absoluteAdapterPosition).startDate ?: "")) {
                (holder as ContestsViewHolder).apply {
                    binding.tvTimeLeft.text = "Tomorrow"
                }
            } else {
                (holder as ContestsViewHolder).apply {
                    binding.tvTimeLeft.text =
                        setFormatDateNew(get(holder.absoluteAdapterPosition).startDate ?: "")
                }
            }
            val anim =
                AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
            holder.itemView.startAnimation(anim)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.itemView.clearAnimation()
            (holder as ContestsViewHolder).cancelTimer()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContestsViewHolder(
                ItemUpcomingContestBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


        private fun sumDreamAmountAndRangeAmount(data: MyTestListRes.Data.UpComingContest): Int {
            var dreamTotal = 0
            var rangeAmount = 0
            for (contest in data.contestData ?: emptyList()) {
                dreamTotal += contest.dreamCandidateAmount ?: 0
                for (range in contest.rangeAmount ?: emptyList()) {
                    rangeAmount += range.amount ?: 0
                }
            }
            return dreamTotal + rangeAmount
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Log.d("TIMER_ISSUE", "onBindViewHolder: $position")
            (holder as UpcomingContestsAdapter.ContestsViewHolder).apply {

                /* if (get(position).contestData?.get(absoluteAdapterPosition)?.rangeAmount?.get(
                         absoluteAdapterPosition
                     )?.amount != null
                 ) {

                     for (amt in get(position).contestData?.get(absoluteAdapterPosition)?.rangeAmount!!) {
                         amount += amt?.amount ?: 0

                     }

                 } else {

                     for (dream in get(position).contestData!!) {
                         dreamAmount += dream.dreamCandidateAmount ?: 0
                     }
                     //binding.textView80.text = "MEGA ₹ ${get(position).dreamCandidateAmount ?: 0}"
                 }*/

                binding.textView80.text = "MEGA ₹ ${sumDreamAmountAndRangeAmount(get(position))}"
                binding.tvContestCount.text = "${get(position).addedContest ?: 0} Contests"
                binding.tvTitle.text = get(position).testTitle ?: ""
                binding.tvBottomTitle.text = get(position).testsubTitle ?: ""
                binding.tvTime.text = get(position).conteststartTime ?: ""
                binding.tvTitle.text = get(position).testTitle ?: ""


            }
        }


        inner class ContestsViewHolder(val binding: ItemUpcomingContestBinding) :
            RecyclerView.ViewHolder(binding.root) {


            private var timer: CountDownTimer? = null

            init {
                binding.root.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            ContestsListActivity::class.java
                        ).putExtra(
                            "id", get(absoluteAdapterPosition)._id ?: ""
                        ).putExtra("list_size", if (position == 0) position + 1 else position)
                            .putExtra("examId", get(absoluteAdapterPosition).examId ?: "")
                            .putExtra(
                                "time_value",
                                get(absoluteAdapterPosition).conteststartTime ?: ""
                            )
                    )
                }
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

    inner class UpcomingPracticeContestsAdapter :
        BaseAdapter<MyTestListRes.Data.UpComingPractice>() {

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            val time = convertTimeStringToMillis(
                get(holder.absoluteAdapterPosition).testData?.teststartTime ?: ""
            ) - getCurrentMillisSinceStartOfDay()
            println("-------timrUp$time")
            if (isTodayDate(get(holder.absoluteAdapterPosition).testData?.startDate ?: "")) {
                (holder as UpcomingPracticeContestsAdapter.ContestsViewHolder).startTimer(time)
            } else if (isTomorrowdaysDate(
                    get(holder.absoluteAdapterPosition).testData?.startDate ?: ""
                )
            ) {
                (holder as UpcomingPracticeContestsAdapter.ContestsViewHolder).apply {
                    binding.tvTimeLeft.text = "Tomorrow"
                }
            } else {
                (holder as UpcomingPracticeContestsAdapter.ContestsViewHolder).apply {
                    binding.tvTimeLeft.text =
                        setFormatDateNew(
                            get(holder.absoluteAdapterPosition).testData?.startDate ?: ""
                        )
                }
            }
            val anim = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
            holder.itemView.startAnimation(anim)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.itemView.clearAnimation()
            (holder as UpcomingPracticeContestsAdapter.ContestsViewHolder).cancelTimer()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContestsViewHolder(
                ItemUpcomingPracticeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as UpcomingPracticeContestsAdapter.ContestsViewHolder).apply {
                binding.tvTitle.text = get(position).testData?.testTitle ?: ""
                binding.tvBottomTitle.text = get(position).testData?.testsubTitle ?: ""
                binding.tvTime.text = get(position).testData?.teststartTime ?: ""
                //binding.tvTimeLeft.text = get(position).testData?.conteststartTime ?: ""
                binding.tvTitle.text = get(position).testData?.testTitle ?: ""
                binding.tvBottomQuestion.text = "${get(position).testData?.questions ?: 0} Question"
                binding.tvBottomTime.text = "${get(position).testData?.duration ?: ""}"
                if (App.app.prefManager.userDetail?.Subscription == true) {
                    binding.flPremium.setBackgroundResource(0)
                    binding.tvPremium.visibility = View.INVISIBLE
                } else if (subscribed) {
                    binding.flPremium.setBackgroundResource(0)
                    binding.tvPremium.visibility = View.INVISIBLE
                } else {
                    binding.flPremium.background = ContextCompat.getDrawable(requireContext(), R.drawable.blur_drawable)
                    binding.tvPremium.visibility = View.VISIBLE
                }
            }
        }

        inner class ContestsViewHolder(val binding: ItemUpcomingPracticeBinding) :
            RecyclerView.ViewHolder(binding.root) {
            private var timer: CountDownTimer? = null
            init {
                binding.tvPremium.setOnClickListener {
                    activity.binding.viewPagerHome.setCurrentItem(2, false)
                    activity.binding.bottomNavigation.selectedItemId = R.id.menuNewSub
                }
                /* binding.root.setOnClickListener {
                     *//*it.context.startActivity(
                        Intent(
                            it.context,
                            ContestsListActivity::class.java
                        ).putExtra(
                            "id", get(absoluteAdapterPosition).testData?._id
                        ).putExtra("list_size", if (position == 0) position + 1 else position)
                    )*//*
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
                            println("=======timerr$millisUntilFinished")
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

    private fun contestStartPopup() {
        if (dialog == null) {
            dialog = Dialog(requireContext())
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.video_timer_hide_popup)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.show()
            var crossImg = dialog?.findViewById<ImageView>(R.id.imageView22)
            crossImg?.setOnClickListener {
                dialog?.dismiss()
            }
            val window = dialog?.window
            if (window != null) {
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }
}