package com.example.dream_ias.activity.quiz

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.results.ResultsActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.practiceTest.DetailPracticeTestRes
import com.example.dream_ias.databinding.ActivityQuizBinding
import com.example.dream_ias.databinding.ItemAnswerQuizBinding
import com.example.dream_ias.databinding.ItemPracticeQuestionQuizBinding
import com.example.dream_ias.databinding.ItemQuestionNumberIndicatorBinding
import com.example.dream_ias.model.QuestionAnswered
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import kotlin.math.roundToInt

class QuizActivity : BaseActivity<ActivityQuizBinding>() {
    private var currentPosition: Int = 0
    private var startTestId: String = ""
    private var practiceTestId: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var id: String = ""
    private var list = ArrayList<DetailPracticeTestRes.Data.Result.TestQue>()
    var isOFFON = false

    data class AnswerOption(val option: String = "Option", var isSelected: Boolean = false)
    data class Question(
        val question: String = "With over 222 million units sold, what is Apple's highest-selling iPhone model?",
        val options: List<AnswerOption> = arrayListOf(
            AnswerOption(),
            AnswerOption(),
            AnswerOption(),
            AnswerOption()
        )
    )

    override fun getLayout(): ActivityQuizBinding {
        return ActivityQuizBinding.inflate(layoutInflater)
    }

    private var timer: CountDownTimer? = null
    private lateinit var questionAdapter: QuestionsAdapter
    private lateinit var questionNumberAdapter: QuestionNumberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id") ?: ""
        initView()
        listner()
    }

    private fun initView() {
        observer()
        questionAdapter = QuestionsAdapter(::answerClicked)
        questionNumberAdapter = QuestionNumberAdapter(::questionNumberClicked)

        binding.rvQuestions.adapter = questionAdapter
        binding.rvQuestionNumber.adapter = questionNumberAdapter

        binding.rvQuestions.isEnabled = false
        LinearSnapHelper().attachToRecyclerView(binding.rvQuestions)
        binding.rvQuestions.addOnScrollListener(scrollListener)

        /*for (i in 0..29) {
            questionAdapter.add(Question())
            questionNumberAdapter.add(QuestionAnswered(false))
        }*/
        /*  startTimer()*/
        binding.btnPrevious.isEnabled = false
    }

    private fun listner() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnPrevious.setOnClickListener {
            if (currentPosition != 0) {
                questionNumberClicked(currentPosition - 1)
            }
        }
        binding.btnNext.setOnClickListener {
            ////currentPosition == 29 tha yha pe
            if (currentPosition == list.size) {
                val intent = ResultsActivity.getIntent(this@QuizActivity, true)
                startActivity(intent)
                finish()
            } else questionNumberClicked(currentPosition + 1)
        }
        binding.allNotiBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isOFFON = true
                questionAdapter.isSetChange(isOFFON)
            } else {
                isOFFON = false
                questionAdapter.isSetChange(isOFFON)
            }
        }
    }

    private fun startTest() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.startTestPractice(token, id, App.app.prefManager.loginData?._id ?: "")
    }

    private fun practiceDetais() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.practiceDetails(token, id)
    }

    private fun observer() {
        viewModel._startTestPracticeliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        startTestId = it.value.data?.existingMessage?._id ?: ""
                        practiceTestId = it.value.data?.existingMessage?.praticeTestId ?: ""
                        App.app.prefManager.practiceTestId =
                            it.value.data?.existingMessage?.praticeTestId ?: ""
                        practiceDetais()
                    } else {
                        Loaders.hide()
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
                        it.throwable?.getError(this)?.message?.toString(),
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
        viewModel._practiceDetailsliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        if (it.value.data?.result?.get(0)?.testQues?.size ?: 0 > 0) {
                            list.addAll(it.value.data?.result?.get(0)?.testQues!!)
                            questionAdapter.set(list)
                            list.forEach { questionNumberAdapter.add(QuestionAnswered(false)) }
                            startTimer()
                        } else {
                            shortToast("Question Data not Found")
                        }
                    } else {
                        Loaders.hide()
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
                        it.throwable?.getError(this)?.message?.toString(),
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
        viewModel._saveResponcePracticeliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    /* Loaders.show(this)*/
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                    } else {
                        /*  Loaders.hide()*/
                        CommonUtil.showSnackBar(
                            this,
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    /*Loaders.hide()*/
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    /*Loaders.hide()*/
                    CommonUtil.showSnackBar(
                        this,
                        it.throwable?.getError(this)?.message?.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    /*    Loaders.hide()*/
                    CommonUtil.showSnackBar(
                        this,
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    currentPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    binding.btnPrevious.isEnabled = currentPosition != 0
                    if (currentPosition == questionAdapter.itemCount - 1) {
                        binding.btnNext.text = "Submit"
                    } else binding.btnNext.text = "Next"
                }
            }
        }
    }

    private fun questionNumberClicked(position: Int) {
        Log.d("POSITION_CHECK", "questionNumberClicked: $position")
        currentPosition = position
        binding.btnPrevious.isEnabled = position != 0
        if (position == questionAdapter.itemCount) {
            binding.btnNext.text = "Submit"
            answerSubmittedPopup()
        } else binding.btnNext.text = "Next"
        binding.rvQuestions.smoothScrollToPosition(position)
    }

    private fun answerClicked(questionPosition: Int, optionPosition: Int) {
        currentPosition = questionPosition
        questionNumberAdapter.get(questionPosition).isAnswered = true
        questionNumberAdapter.notifyItemChanged(questionPosition)
    }

    inner class QuestionsAdapter(private val optionSelected: (Int, Int) -> Unit) :
        BaseAdapter<DetailPracticeTestRes.Data.Result.TestQue>() {

        var isOffff: Boolean = false
        fun isSetChange(isOFF: Boolean) {
            this.isOffff = isOFF
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return QuestionViewHolder(
                ItemPracticeQuestionQuizBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as QuestionViewHolder).apply {
                /* get(position).let {
                     adapter.set(it.options)
                     *//*binding.tvQuestion.text = it.question*//*
                    binding.tvQuestionNumber.text = "Question ${position + 1}"
                }*/
                get(position).let {
                    if (list[position] != null) {
                        if (isOffff) {
                            val htmlAsString =
                                get(absoluteAdapterPosition).questionInHin?.question ?: ""
                            val htmlAsSpanned = Html.fromHtml(htmlAsString)
                            binding.tvQuestion.text = htmlAsSpanned
                            //binding.tvQuestionStart.text = "${position + 1}"
                            binding.tvQuestionNumber.text = "Question ${position + 1}"
                            binding.tvOption1.text =
                                get(absoluteAdapterPosition).questionInHin?.option1
                                    ?: ""
                            binding.tvOption2.text =
                                get(absoluteAdapterPosition).questionInHin?.option2
                                    ?: ""
                            binding.tvOption3.text =
                                get(absoluteAdapterPosition).questionInHin?.option3
                                    ?: ""
                            binding.tvOption4.text =
                                get(absoluteAdapterPosition).questionInHin?.option4
                                    ?: ""
                        } else {
                            val htmlAsString =
                                get(absoluteAdapterPosition).questionInEng?.question ?: ""
                            val htmlAsSpanned = Html.fromHtml(htmlAsString)
                            binding.tvQuestion.text = htmlAsSpanned
                            //binding.tvQuestionStart.text = "${position + 1}"
                            binding.tvQuestionNumber.text = "Question ${position + 1}"
                            binding.tvOption1.text =
                                get(absoluteAdapterPosition).questionInEng?.option1
                                    ?: ""
                            binding.tvOption2.text =
                                get(absoluteAdapterPosition).questionInEng?.option2
                                    ?: ""
                            binding.tvOption3.text =
                                get(absoluteAdapterPosition).questionInEng?.option3
                                    ?: ""
                            binding.tvOption4.text =
                                get(absoluteAdapterPosition).questionInEng?.option4
                                    ?: ""
                        }
                    } else {
                        shortToast("Question Data not Found")
                    }
                }

            }
        }

        inner class QuestionViewHolder(val binding: ItemPracticeQuestionQuizBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val adapter: AnswerOptionAdapter = AnswerOptionAdapter {
                optionSelected.invoke(adapterPosition, it)
            }

            init {
                // setIsRecyclable(false)
                /*binding.linearLayout3.setOnClickListener {
                    binding.ra1.isChecked = true
                    binding.ra2.isChecked = false
                    binding.ra3.isChecked = false
                    binding.ra4.isChecked = false

                    optionSelected.invoke(absoluteAdapterPosition, 0)
                }
                binding.main2.setOnClickListener {
                    binding.ra1.isChecked = false
                    binding.ra2.isChecked = true
                    binding.ra3.isChecked = false
                    binding.ra4.isChecked = false
                    optionSelected.invoke(absoluteAdapterPosition, 1)
                }
                binding.main3.setOnClickListener {
                    binding.ra1.isChecked = false
                    binding.ra2.isChecked = false
                    binding.ra3.isChecked = true
                    binding.ra4.isChecked = false
                    optionSelected.invoke(absoluteAdapterPosition, 2)
                }
                binding.main4.setOnClickListener {
                    binding.ra1.isChecked = false
                    binding.ra2.isChecked = false
                    binding.ra3.isChecked = false
                    binding.ra4.isChecked = true
                    optionSelected.invoke(absoluteAdapterPosition, 3)
                }*/
                binding.ra1.setOnClickListener {
                    binding.ra2.isChecked = false
                    binding.ra3.isChecked = false
                    binding.ra4.isChecked = false
                    optionSelected.invoke(absoluteAdapterPosition, 0)
                    answered()
                }
                binding.ra2.setOnClickListener {
                    binding.ra1.isChecked = false
                    binding.ra3.isChecked = false
                    binding.ra4.isChecked = false
                    optionSelected.invoke(absoluteAdapterPosition, 1)
                    answered()
                }
                binding.ra3.setOnClickListener {
                    binding.ra1.isChecked = false
                    binding.ra2.isChecked = false
                    binding.ra4.isChecked = false
                    optionSelected.invoke(absoluteAdapterPosition, 2)
                    answered()
                }
                binding.ra4.setOnClickListener {
                    binding.ra1.isChecked = false
                    binding.ra2.isChecked = false
                    binding.ra3.isChecked = false
                    optionSelected.invoke(absoluteAdapterPosition, 3)
                    answered()
                }

                /* binding.rvOptions.adapter = adapter*/
            }

            fun answered() {
                println("=============Answer")
                val selectionAns =
                    if (binding.ra1.isChecked) "a" else if (binding.ra2.isChecked) "b" else if (binding.ra3.isChecked) "c" else "d"
                val isCorrect = when (list[absoluteAdapterPosition].correctOption) {
                    "option1" -> selectionAns == "a"
                    "option2" -> selectionAns == "b"
                    "option3" -> selectionAns == "c"
                    "option4" -> selectionAns == "d"
                    else -> false
                }

                if (isOffff) {
                    val selectedText = when (selectionAns) {
                        "a" -> list[absoluteAdapterPosition].questionInHin?.option1 ?: ""
                        "b" -> list[absoluteAdapterPosition].questionInHin?.option2 ?: ""
                        "c" -> list[absoluteAdapterPosition].questionInHin?.option3 ?: ""
                        "d" -> list[absoluteAdapterPosition].questionInHin?.option4 ?: ""
                        else -> ""
                    }
                    val params = HashMap<String, String>()
                    params.put("startTestId", startTestId)
                    params.put("praticeTestId", practiceTestId)
                    params.put("questionNo", (absoluteAdapterPosition + 1).toString())
                    params.put("ans", selectionAns)
                    params.put(
                        "question",
                        (list[absoluteAdapterPosition].questionInHin?.question).toString()
                    )
                    params.put("marks", (list[absoluteAdapterPosition].marks ?: 0).toString())
                    params.put("queAns", selectedText)
                    params.put("iscorrect", isCorrect.toString())
                    val token = App.app.prefManager.loginData?.jwtToken.toString()
                    viewModel.saveResponcePractice(token, params)

                } else {
                    val selectedText = when (selectionAns) {
                        "a" -> list[absoluteAdapterPosition].questionInEng?.option1 ?: ""
                        "b" -> list[absoluteAdapterPosition].questionInEng?.option2 ?: ""
                        "c" -> list[absoluteAdapterPosition].questionInEng?.option3 ?: ""
                        "d" -> list[absoluteAdapterPosition].questionInEng?.option4 ?: ""
                        else -> ""
                    }
                    val params = HashMap<String, String>()
                    params.put("startTestId", startTestId)
                    params.put("praticeTestId", practiceTestId)
                    params.put("questionNo", (absoluteAdapterPosition + 1).toString())
                    params.put("ans", selectionAns)
                    params.put(
                        "question",
                        (list[absoluteAdapterPosition].questionInHin?.question).toString()
                    )
                    params.put("marks", (list[absoluteAdapterPosition].marks ?: 0).toString())
                    params.put("queAns", selectedText)
                    params.put("iscorrect", isCorrect.toString())
                    val token = App.app.prefManager.loginData?.jwtToken.toString()
                    viewModel.saveResponcePractice(token, params)
                }

            }
        }

        inner class AnswerOptionAdapter(val optionClicked: (Int) -> Unit) :
            BaseAdapter<AnswerOption>() {

            private var selectedOption: Int = -1
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return AnswerOptionViewHolder(
                    ItemAnswerQuizBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            override fun onBindViewHolder(
                holder: RecyclerView.ViewHolder,
                @SuppressLint("RecyclerView") position: Int
            ) {
                (holder as AnswerOptionViewHolder).apply {
                    get(position).let {
                        binding.root.isSelected = it.isSelected
                        if (it.isSelected)
                            selectedOption = position
                    }
                }
            }

            inner class AnswerOptionViewHolder(val binding: ItemAnswerQuizBinding) :
                RecyclerView.ViewHolder(binding.root) {
                init {
                    //                    setIsRecyclable(false)
                    binding.root.setOnClickListener {
                        if (selectedOption == -1) {
                            get(adapterPosition).isSelected = true
                            notifyItemChanged(adapterPosition)
                        } else {
                            get(selectedOption).isSelected = false
                            get(adapterPosition).isSelected = true
                            notifyItemChanged(selectedOption)
                            notifyItemChanged(adapterPosition)
                        }
                        optionClicked.invoke(adapterPosition)
                    }
                }
            }

        }

    }

    class QuestionNumberAdapter(private val questionNumberClicked: (Int) -> Unit) :
        BaseAdapter<QuestionAnswered>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return QuestionNumberViewHolder(
                ItemQuestionNumberIndicatorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as QuestionNumberViewHolder).apply {
                binding.root.isSelected = get(position).isAnswered
                binding.tvNumber.text = "${position + 1}"
            }
        }

        inner class QuestionNumberViewHolder(val binding: ItemQuestionNumberIndicatorBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
//                setIsRecyclable(false)
                binding.root.setOnClickListener {
                    questionNumberClicked.invoke(adapterPosition)
                }
            }
        }
    }

    private fun startTimer() {
        //val time = (list.get(currentPosition).timeforContest.toString() + "000").toLong()
        var time1 = 0
        for (iyy in list) {
            time1 += (iyy.timeforContest?.toInt()!!)
        }
        println("===========$time1")
        val time = (time1.toString() + "000").toLong()
        timer?.cancel()
        timer = object : CountDownTimer(time, 1000L) {

            override fun onTick(timeLeft: Long) {
                val progress = ((timeLeft / time.toFloat()) * 100).roundToInt()
                Log.d("TIMER_QUIZ", "onTick: $progress")
                binding.progress.progress = progress
                binding.tvTimeRemaining.text = "${timeLeft / 1000}s"
            }

            override fun onFinish() {
                timer?.cancel()
                timer = null

                if (currentPosition != ((list.size).minus(1))) {
                    currentPosition++
                    binding.rvQuestions.scrollToPosition(currentPosition)
                    startTimer()
                } else {
                    answerSubmittedPopup()
                }
                /*if (currentPosition == (questionAdapter.itemCount - 1))
                    answerSubmittedPopup()
                else {
                    currentPosition++
                    binding.rvQuestions.scrollToPosition(currentPosition)
                    startTimer()
                    changeProgress()
                }*/
            }
        }
        timer?.start()
    }

    private fun reviewingYourResult() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_reviewing_your_result)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            /*startActivity(ResultsActivity.getIntent(this, false, QuizMoneyActivity.isWon))
            QuizMoneyActivity.isWon = !QuizMoneyActivity.isWon
            finish()*/
            val intent = ResultsActivity.getIntent(this@QuizActivity, true)
            startActivity(intent)
            finish()
        }, 2000)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun answerSubmittedPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_task_submitted)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            reviewingYourResult()
        }, 1500)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        startTest()
    }
}