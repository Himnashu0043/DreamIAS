package com.example.dream_ias.activity.quiz

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.activity.results.ResultsActivity
import com.example.dream_ias.activity.socket.SocketCallbacks
import com.example.dream_ias.activity.socket.SocketConnection
import com.example.dream_ias.activity.socket.SocketConstants.*
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.DetailsQuestionList
import com.example.dream_ias.apiResponse.StartResult
import com.example.dream_ias.databinding.ActivityQuizMoneyBinding
import com.example.dream_ias.databinding.ItemQuestionQuizBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject
import kotlin.math.roundToInt

class QuizMoneyActivity : BaseActivity<ActivityQuizMoneyBinding>(), SocketCallbacks {
    private var contestId: String = ""
    private var questionId: String = ""
    private var testId: String = ""
    private var userId: String = ""
    private var startTestId: String = ""
    private var mSocket: Socket? = null
    private val viewModel: AuthViewModel by viewModels()
    private var list = ArrayList<DetailsQuestionList.Data.Result.QuestionsData.TestQue>()
    var isRoomJoin = true
    var isOFFON = false
    private var dialog: Dialog? = null
    override fun getLayout(): ActivityQuizMoneyBinding {
        return ActivityQuizMoneyBinding.inflate(layoutInflater)
    }

    companion object {
        var isWon = true
    }

    private var currentPosition: Int = 0
    private var timer: CountDownTimer? = null
    private lateinit var questionAdapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // questionAdapter = QuestionsAdapter(::answerClicked)
        contestId = App.app.prefManager.contestId
        userId = App.app.prefManager.userId
        testId = App.app.prefManager.testId
        questionId = App.app.prefManager.questionId

        questionAdapter = QuestionsAdapter()
        binding.rvQuestions.adapter = questionAdapter
        binding.rvQuestions.isEnabled = false
        LinearSnapHelper().attachToRecyclerView(binding.rvQuestions)
        binding.rvQuestions.layoutManager = object : LinearLayoutManager(this, HORIZONTAL, false) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }


        binding.ivBack.setOnClickListener {
            finish()
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
        back()
    }

    private fun detailsQuestionAPI() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._detailsQuestion(token, contestId)
    }

    private fun observer() {
        viewModel._detailsQuestionLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        if (it.value.data?.result?.get(0)?.questionsData != null){
                            list.addAll(it.value.data?.result?.get(0)?.questionsData?.testQues!!)
                            questionAdapter.set(list)
//                            binding.tvProgressEnd.text =
//                                list.size.toString()
                            binding.tvtotalQuestion.text = list.size.toString()
                            // binding.progressIndicatorQuestion.max = list.size
                            startTimer()
                            //changeProgress()
                            App.app.prefManager.pdfSolution =
                                it.value.data?.result?.get(0)?.questionsData?.solution?.pdf ?: ""
                            App.app.prefManager.linkVideoSolution =
                                it.value.data?.result?.get(0)?.questionsData?.solution?.link ?: ""
                        }else{
                            shortToast("Question Data not Found")
                        }


                    } else {
                        CommonUtil.showSnackBar(
                            this,
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error ->{
                    CommonUtil.showSnackBar(
                        this,
                        it.throwable?.getError(this)?.message.toString(),
                        R.color.red
                    )
                }
                else -> {
                    CommonUtil.showSnackBar(
                        this,
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
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
            startActivity(ResultsActivity.getIntent(this, false, isWon))
            isWon = !isWon
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

    private fun answerClicked(questionPosition: Int, optionPosition: Int) {
        if (questionPosition == (questionAdapter.itemCount - 1)) answerSubmittedPopup()
    }

    private fun startTimer() {
        val time = (list.get(currentPosition).timeforContest.toString()+ "000").toLong()
        timer?.cancel()
        timer = object : CountDownTimer(time, 1000L) {
            override fun onTick(timeLeft: Long) {
                try {
                    val progress = ((timeLeft / time.toFloat()) * 100).roundToInt()
                    Log.d("TIMER_QUIZ", "onTick: $progress")
                    binding.progress.progress = progress
                    // binding.tvTimeRemaining.text = "${timeLeft / 1000}s"
                    binding.tvTitle.text = "${timeLeft / 1000}s"
                    binding.tvStartQuestion.text = "${((currentPosition + 1))} /"
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onFinish() {
                timer?.cancel()
                timer = null
                try {
                    //answerSubmittedPopup()
                    if (currentPosition == ((list.size).minus(1))) answerSubmittedPopup()
                    else {
                        currentPosition++
                        binding.rvQuestions.scrollToPosition(currentPosition)
                        // binding.tvProgressStart.text = ((currentPosition + 1).toString())
                        binding.tvStartQuestion.text = "${((currentPosition + 1))} /"
                        startTimer()
                        //changeProgress()
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        timer?.start()
    }


//    private fun changeProgress() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.progressIndicatorQuestion.setProgress(currentPosition + 1, true)
//        } else {
//            binding.progressIndicatorQuestion.progress = currentPosition + 1
//        }
//    }

    inner class QuestionsAdapter() :
        BaseAdapter<DetailsQuestionList.Data.Result.QuestionsData.TestQue>() {
        var isOffff: Boolean = false
        fun isSetChange(isOFF: Boolean) {
            this.isOffff = isOFF
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return QuestionViewHolder(
                ItemQuestionQuizBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as QuestionViewHolder).apply {
                get(position).let {
                    // adapter.set(it.questionsData?.testQues!!)
                    Log.d("PositionCheckingNow", "onBindViewHolder: ${it.selectedPosition}")
                    if (list[position] != null) {
                        when (get(position).selectedPosition) {
                            1 -> {
                                binding.linearLayout3.performClick()
                            }
                            2 -> {
                                binding.main2.performClick()
                            }
                            3 -> {
                                binding.main3.performClick()
                            }
                            4 -> {
                                binding.main4.performClick()
                            }

                        }

                        if (isOffff) {
                            val htmlAsString =
                                get(absoluteAdapterPosition).questionInHin?.question ?: ""
                            val htmlAsSpanned = Html.fromHtml(htmlAsString)
                            binding.tvQuestion.text = htmlAsSpanned
                            binding.tvQuestionStart.text = "${position + 1}"
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
                            binding.tvQuestionStart.text = "${position + 1}"
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

        inner class QuestionViewHolder(val binding: ItemQuestionQuizBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                setIsRecyclable(false)
                binding.linearLayout3.setOnClickListener {
                    /*binding.ra2.isEnabled = false
                    binding.ra3.isEnabled = false
                    binding.ra4.isEnabled = false*/
                    get(absoluteAdapterPosition).selectedPosition = 1
                    binding.linearLayout3.isSelected = true
                    binding.main2.isEnabled = false
                    binding.main3.isEnabled = false
                    binding.main4.isEnabled = false
                    answered()
                }
                binding.main2.setOnClickListener {
                    /*binding.ra1.isEnabled = false
                    binding.ra3.isEnabled = false
                    binding.ra4.isEnabled = false*/
                    get(absoluteAdapterPosition).selectedPosition = 2
                    binding.main2.isSelected = true
                    binding.linearLayout3.isEnabled = false
                    binding.main3.isEnabled = false
                    binding.main4.isEnabled = false
                    answered()
                }
                binding.main3.setOnClickListener {
                    /*binding.ra2.isEnabled = false
                    binding.ra1.isEnabled = false
                    binding.ra4.isEnabled = false*/
                    get(absoluteAdapterPosition).selectedPosition = 3
                    binding.main3.isSelected = true
                    binding.linearLayout3.isEnabled = false
                    binding.main2.isEnabled = false
                    binding.main4.isEnabled = false
                    answered()
                }
                binding.main4.setOnClickListener {
                    /*binding.ra2.isEnabled = false
                    binding.ra3.isEnabled = false
                    binding.ra1.isEnabled = false*/
                    get(absoluteAdapterPosition).selectedPosition = 4
                    binding.main4.isSelected = true
                    binding.linearLayout3.isEnabled = false
                    binding.main2.isEnabled = false
                    binding.main3.isEnabled = false
                    answered()
                }

            }

            fun answered() {
                if (isOffff) {
                    val selectionAns =
                        if (binding.linearLayout3.isSelected) "a" else if (binding.main2.isSelected) "b" else if (binding.main3.isSelected) "c" else "d"
                    val isCorrect = when (list[absoluteAdapterPosition].correctOption) {
                        "option1" -> selectionAns == "a"
                        "option2" -> selectionAns == "b"
                        "option3" -> selectionAns == "c"
                        "option4" -> selectionAns == "d"
                        else -> false
                    }
                    val selectedText = when (selectionAns) {
                        "a" -> list[absoluteAdapterPosition].questionInHin?.option1 ?: ""
                        "b" -> list[absoluteAdapterPosition].questionInHin?.option2 ?: ""
                        "c" -> list[absoluteAdapterPosition].questionInHin?.option3 ?: ""
                        "d" -> list[absoluteAdapterPosition].questionInHin?.option4 ?: ""
                        else -> ""
                    }
                    val hashMap = JSONObject()
                    hashMap.put("userId", userId)
                    hashMap.put("startTestId", startTestId)
                    hashMap.put("questionNo", absoluteAdapterPosition + 1)
                    hashMap.put("ans", selectionAns)
                    hashMap.put("question", list[absoluteAdapterPosition].questionInHin?.question)
                    hashMap.put("marks", list[absoluteAdapterPosition].marks ?: 0)
                    hashMap.put("queAns", selectedText)
                    hashMap.put("iscorrect", isCorrect)
                    Log.d("CheckingRoomIds", "AnswerEventHindi: ${hashMap}`")
                    mSocket?.emit(EVENT_MESSAGE, hashMap)
                } else {
                    val selectionAns =
                        if (binding.linearLayout3.isSelected) "a" else if (binding.main2.isSelected) "b" else if (binding.main3.isSelected) "c" else "d"
                    val isCorrect = when (list[absoluteAdapterPosition].correctOption) {
                        "option1" -> selectionAns == "a"
                        "option2" -> selectionAns == "b"
                        "option3" -> selectionAns == "c"
                        "option4" -> selectionAns == "d"
                        else -> false
                    }
                    val selectedText = when (selectionAns) {
                        "a" -> list[absoluteAdapterPosition].questionInEng?.option1 ?: ""
                        "b" -> list[absoluteAdapterPosition].questionInEng?.option2 ?: ""
                        "c" -> list[absoluteAdapterPosition].questionInEng?.option3 ?: ""
                        "d" -> list[absoluteAdapterPosition].questionInEng?.option4 ?: ""
                        else -> ""
                    }
                    val hashMap = JSONObject()
                    hashMap.put("userId", userId)
                    hashMap.put("startTestId", startTestId)
                    hashMap.put("questionNo", absoluteAdapterPosition + 1)
                    hashMap.put("ans", selectionAns)
                    hashMap.put("question", list[absoluteAdapterPosition].questionInEng?.question)
                    hashMap.put("marks", list[absoluteAdapterPosition].marks ?: 0)
                    hashMap.put("queAns", selectedText)
                    hashMap.put("iscorrect", isCorrect)
                    Log.d("CheckingRoomIds", "AnswerEventEnglish: ${hashMap}`")
                    mSocket?.emit(EVENT_MESSAGE, hashMap)
                }

            }

            /*val adapter: AnswerOptionAdapter = AnswerOptionAdapter {
                optionSelected.invoke(adapterPosition, it)
            }
            init {

                //  binding.rvOptions.adapter = adapter
            }*/
        }

        /*class AnswerOptionAdapter(val optionClicked: (Int) -> Unit) :
            BaseAdapter<DetailsQuestionList.Data.Result.QuestionsData.TestQue>() {

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
                    binding.tvOption1.text = get(position).questionInEng?.option1?:""
                    binding.tvOption2.text = get(position).questionInEng?.option2?:""
                    binding.tvOption3.text = get(position).questionInEng?.option3?:""
                    binding.tvOption4.text = get(position).questionInEng?.option4?:""
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

        }*/

    }

    override fun onConnect(vararg args: Any?) {
        Log.d("TAG", "onConnect:  .......")
    }

    override fun onDisconnect(vararg args: Any?) {
        Log.d("TAG", "onDisconnect: ..........")
    }

    override fun onConnectError(vararg args: Any?) {
        Log.e("TAG", "onConnectError:" + args[0].toString())
    }

    override fun onRoomJoin(vararg args: Any?) {
        Log.d("TAG", "onRoomJoin: .............. ${args[0].toString()}")
        val `object` = JSONObject()
        `object`.put(ROOM_ID, contestId)
        `object`.put(CONTEST_ID, contestId)
        `object`.put(USER_ID, userId)
        `object`.put(QUESTION_ID, questionId)
        `object`.put(TEST_ID, testId)
        Log.d("CheckingRoomIds", "RessultsSocket: $`object`")
        mSocket?.emit(START_CONTEST_RESULTS, `object`)
    }

    override fun onMessage(vararg args: Any?) {
        Log.e("TAG", "onMessage: ........." + args[0].toString())
    }

    override fun onRoomLeave(vararg args: Any?) {
        Log.d("TAG", "onRoomLeave: ........." + args[0].toString())
    }


    override fun onStartResults(vararg args: Any?) {
        Log.d("SOCKET_QUESTIONS", "onStartResults: ${args[0].toString()}")
        val json = args[0].toString()
        if (json.isNotEmpty()) {
            val resultData = Gson().fromJson<StartResult>(json, StartResult::class.java)
            startTestId = resultData.data?._id ?: ""


        }
    }

    private fun connectSocket() {
        try {
            mSocket = SocketConnection.connectSocket(this)
            if (mSocket?.connected() != true) {
                mSocket?.connect()
                val `object` = JSONObject()
                `object`.put(ROOM_ID, contestId)
                `object`.put(CONTEST_ID, contestId)
                `object`.put(USER_ID, userId)
                `object`.put(QUESTION_ID, questionId)
                `object`.put(TEST_ID, testId)
//                `object`.put(EVENT_MESSAGE, "question")
                Log.d("CheckingRoomIds", "connectSocket: $`object`")
                mSocket?.emit(EVENT_ROOM_JOIN, `object`)
                detailsQuestionAPI()
                observer()
            }
            /*if (isRoomJoin) {
                val `object` = JSONObject()
                `object`.put(SENDER_ID, mSenderId)
                `object`.put(ROOM_ID, mRoomId)
                Log.d("CheckingRoomIds", "connectSocket: $`object`")
                mSocket?.emit(EVENT_ROOM_JOIN, `object`)
            }*/


        } catch (e: Exception) {
            Log.e("TAG", "connectSocket: ${e.printStackTrace()}")
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        connectSocket()

    }


    /* override fun onDestroy() {
         super.onDestroy()
         timer?.cancel()
         timer = null
     }

     private fun startTimerQuizStart(timeValue: Long) {
         timer?.cancel()
         timer = object : CountDownTimer(timeValue, 1000L) {
             override fun onTick(timeLeft: Long) {
                 try {
                     App.app.prefManager.contesntTime = timeLeft.toString()
                     val progress = ((timeLeft / timeValue.toFloat()) * 100).roundToInt()
                     Log.d("TIMER_QUIZ", "onTick: ${timeLeft / 1000}")
                     *//* binding.progress.progress = progress
                     binding.tvTimeRemaining.text = "${timeLeft / 1000}s"*//*
                    binding.tvPLeaseWait.visibility = View.VISIBLE
                    binding.mainLay.visibility = View.GONE
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onFinish() {
                timer?.cancel()
                timer = null
                Log.d("TIMER_QUIZ", "fiished: ")
                try {
                    //answerSubmittedPopup()
                    println("+++++++++++++${App.app.prefManager.contesntTime}")
                    if (App.app.prefManager.contesntTime < getCurrentMillisSinceStartOfDay().toString()) {
                        connectSocket()
                        binding.tvPLeaseWait.visibility = View.GONE
                        binding.mainLay.visibility = View.VISIBLE
                    } else {
                        binding.tvPLeaseWait.visibility = View.VISIBLE
                        binding.mainLay.visibility = View.GONE
                    }


                    *//*if (currentPosition == ((list.size).minus(1))) answerSubmittedPopup()
                    else {
                        currentPosition++
                        binding.rvQuestions.scrollToPosition(currentPosition)
                        binding.tvProgressStart.text = ((currentPosition + 1).toString())
                        startTimer()
                        changeProgress()
                    }*//*
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        timer?.start()
    }*/
    private fun back() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                quizCancelPopup()
            }
        })
    }

    private fun quizCancelPopup() {
        if (dialog == null) {
            dialog = Dialog(this)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.quiz_cancel_popup)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.show()
            var yes = dialog?.findViewById<TextView>(R.id.tvYes)
            var no = dialog?.findViewById<TextView>(R.id.tvNo)
            yes?.setOnClickListener {
                dialog?.dismiss()
                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }
            no?.setOnClickListener {
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

    override fun onStop() {
        super.onStop()
        SocketConnection.disconnectSocket()
    }
}