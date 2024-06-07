package com.example.dream_ias.activity.testInfo

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.DetailsQuestionList
import com.example.dream_ias.databinding.ActivityTestInstructionBinding
import com.example.dream_ias.databinding.ItemInstructionsBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import java.text.DecimalFormat
import java.text.NumberFormat

class InstructionActivity : BaseActivity<ActivityTestInstructionBinding>() {
    private val viewModel: AuthViewModel by viewModels()
    private var list = ArrayList<DetailsQuestionList.Data.Result>()
    private lateinit var adapter: InstructionsAdapter
    private var timer: CountDownTimer? = null
    private var timer1: CountDownTimer? = null
    override fun getLayout(): ActivityTestInstructionBinding {
        return ActivityTestInstructionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
        binding.btnContinue.setOnClickListener {
            startActivity(Intent(this, VideoTipsActivity::class.java))
            finish()
        }
        binding.ivBack.setOnClickListener {
            finish()
        }

        if (intent.hasExtra("fromPractice"))
            binding.btnContinue.visibility = View.GONE

    }

    private fun detailsQuestionAPI() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._detailsQuestion(token, App.app.prefManager.contestId)
    }

    private fun observer() {
        viewModel._detailsQuestionLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result!!)

                        if (list.isNotEmpty()) {
                            binding.tvTittle.text = list[0].testData?.testTitle ?: ""
                            binding.tvTitle.text = list[0].testData?.testTitle ?: ""
                            if (list[0].crosscutOff?.get(0)?.prizeAmount != null) {
                                binding.tvPrize.text =
                                    (list[0].crosscutOff?.get(0)?.prizeAmount ?: 0).toString()
                            } else {
                                binding.tvPrize.text =
                                    (list[0].dreamCandidateAmount ?: 0).toString()
                            }

                            binding.tvQuestion.text =
                                "${(list[0].testData?.questions ?: 0)} Question"
                            binding.tvMarks.text = "${(list[0].testData?.totalMarks ?: 0)} Marks"
                            binding.tvNegativeMarks.text =
                                "Negative ${(list[0].questionsData?.negativeMarking?.marking ?: 0)} Marks"
                            binding.tvTime.text = "${(list[0].testData?.duration ?: 0)}"
                            binding.tvSubTitle.text =
                                "Play the game and get all general knowledge about ${list[0]?.testData?.testTitle ?: ""}"
                            adapter = InstructionsAdapter()
                            binding.rvInstuction.adapter = adapter
                            adapter.set(list)

                            val time = convertTimeStringToMillis(
                                list[0].testData?.conteststartTime ?: ""
                            ) + convertMinutesStringToMillis(
                                list[0].liveStreamingTime ?: ""
                            ) - getCurrentMillisSinceStartOfDay()
                            println("============current${getCurrentMillisSinceStartOfDay()}")
                            println("============time${time}")
                            var time1 = convertTimeStringToMillis(
                                list[0].testData?.conteststartTime ?: ""
                            ) - getCurrentMillisSinceStartOfDay()
                            if (time > 0){
                                startTimer(time1, (time - time1))
                            }

                            //App.app.prefManager.contesntTime = ((time - time1).toString())
                            App.app.prefManager.liveStreamLink = list[0].liveStreamingLink ?: ""
                            App.app.prefManager.contestName = list[0].testTitle ?: ""
                        } else {
                            shortToast("Data Not Found!!")
                        }


                        /*var contestTime =
                            convertTimeToMillis(list[0].testData?.conteststartTime ?: "")
                        var liveStremTime =
                            convertMinutesToMilliseconds(list[0].liveStreamingTime ?: "").toInt()
                        var curentTime = convertTimeToMillis(getCurrentTime())*/


                        //binding.btnContinue.visibility = View.VISIBLE

                    } else {
                        CommonUtil.showSnackBar(
                            this,
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        this,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        this,
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    class InstructionsAdapter : BaseAdapter<DetailsQuestionList.Data.Result>() {


        inner class InstructionViewHolder(val binding: ItemInstructionsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return InstructionViewHolder(
                ItemInstructionsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as InstructionViewHolder).apply {
                val htmlAsString =get(absoluteAdapterPosition).Instructions ?: ""
                val htmlAsSpanned = Html.fromHtml(htmlAsString)
                binding.tvInstruction.text = htmlAsSpanned
            }
        }


    }

    private fun startTimer(timeValue1: Long, time: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timeValue1, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    /*App.app.prefManager.liveStreamTime = millisUntilFinished.toString()*/
                    Log.d("TIMER_QUIZ", "onTick: ${millisUntilFinished / 1000}")
                    val f: NumberFormat = DecimalFormat("00")
                    val hours = millisUntilFinished / 3600000
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    if (hours > 0) {
                        binding.tvTimeLeftIntro.text = binding.root.context.getString(
                            R.string.hour_min_sec,
                            f.format(hours),
                            f.format(min),
                            f.format(sec)
                        )

                    } else {
                        binding.tvTimeLeftIntro.text = binding.root.context.getString(
                            R.string.min_sec,
                            f.format(min),
                            f.format(sec)
                        )
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onFinish() {
                timer?.cancel()
                timer = null
                Log.d("TIMER_QUIZ", "fiished: ")
                try {
                    binding.tvTimeLeftIntro.text = "00:00"
                    startTimerLiveStream(time)
                    if (timeValue1 > 0) {
                        binding.btnContinue.visibility = View.VISIBLE
                    } else {
                        binding.btnContinue.visibility = View.GONE
                    }

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        timer?.start()
    }

    private fun startTimerLiveStream(timeValue: Long) {
        timer1?.cancel()
        timer1 = object : CountDownTimer(timeValue, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    Log.d("TIMER_QUIZ111", "onTick: ${millisUntilFinished / 1000}")
                    App.app.prefManager.liveStreamTime = millisUntilFinished.toString()
                    App.app.prefManager.contesntTime = millisUntilFinished.toString()
                    val f: NumberFormat = DecimalFormat("00")
                    val hours = millisUntilFinished / 3600000
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    binding.btnContinue.visibility = View.VISIBLE
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onFinish() {
                timer1?.cancel()
                timer1 = null
                Log.d("TIMER_QUIZLivew", "fiished: ")
                try {
                     binding.tvTimeLeftIntro.text = "00:00"
                    if (timeValue < 0) {
                        binding.btnContinue.visibility = View.GONE
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        timer1?.start()
    }

    override fun onResume() {
        super.onResume()
        if (!App.app.prefManager.contestId.isNullOrEmpty()) {
            detailsQuestionAPI()
            
        }


    }

}