package com.example.dream_ias.activity.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.activity.leaderBoard.LeaderBoardActivity
import com.example.dream_ias.activity.solution.SolutionForContestActivity
import com.example.dream_ias.activity.viewAnalysis.ViewAnalysisActivity
import com.example.dream_ias.adapter.results.ResultsAdapter
import com.example.dream_ias.apiResponse.practiceTest.ResultsPracticeRes
import com.example.dream_ias.apiResponse.results.ResultsListRes
import com.example.dream_ias.databinding.ActivityResultsBinding
import com.example.dream_ias.model.ResultsModel
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class ResultsActivity : BaseActivity<ActivityResultsBinding>() {
    private var list = ArrayList<ResultsModel>()
    private var listanswer = ArrayList<ResultsListRes.Data.Result.Answer>()
    private var listPracticeAnswer = ArrayList<ResultsPracticeRes.Data.Result.Answer>()
    private var from: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }


    override fun getLayout(): ActivityResultsBinding {
        return ActivityResultsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startViewGroupAnimation()
        initView()
        listener()
        observer()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Result"
        from = intent.getStringExtra(FROM) ?: ""
        println("=======reeee$from")
        when (from) {
            PRACTICE_TEST -> {
                if (intent.getBooleanExtra(IS_WON, false))
                    binding.layWhooo.visibility = View.VISIBLE
                else binding.layAhh.visibility = View.GONE
                binding.btnViewLeaderBoard.visibility = View.VISIBLE
                practiceResults()
            }

            else -> {
                binding.btnViewLeaderBoard.visibility = View.VISIBLE
                contestResults()

            }
        }

    }

    private fun contestResults() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._contestResultsList(token, App.app.prefManager.contestId)
    }

    private fun practiceResults() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.resultsPractice(token, App.app.prefManager.practiceTestId)
    }

    private fun observer() {
        viewModel._contestResultsliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                    Loaders.show(this)
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        listanswer.clear()
                        listanswer.addAll(it.value.data?.result?.get(0)?.answers!!)
                        /* list.clear()
                         list.addAll(it.value.data?.result!!)*/
                        binding.rcy.layoutManager = GridLayoutManager(this, 2)
                        binding.rcy.adapter = ResultsAdapter(
                            this,
                            prepareList(
                                it.value.data?.result?.get(0)?.totalcorrectAnswers ?: 0,
                                it.value.data?.result?.get(0)?.totalIncorrect ?: 0,
                                ((it.value.data?.result?.get(0)?.answers?.size
                                    ?: 0) - (it.value.data?.result?.get(
                                    0
                                )?.noOfAttempts?:0)),
                                it.value.data?.result?.get(0)?.answers?.size ?: 0,
                                it.value.data?.totalScore ?: 0.0,
                                "%.2f".format(it.value.data?.negativeMarking ?: 0.0).toDouble()
                            )
                        )
                        binding.textView601.text = "You won! : ₹ ${it.value.data?.prize ?: 0}"
                        binding.textView60.text =
                            "${it.value.data?.ranking ?: 0} out of ${it.value.data?.totalParticipants ?: 0} participants"
                        /* adapter = LeaderboardTestFragment.LeaderBoardAdapter()
                         binding.rcy.adapter = adapter
                         adapter.set(list)
                         binding.tvContestantCount.text = "All Contestants (${list.size})"*/

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
                is Resource.Error ->{
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
        viewModel._resultsPracticeliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                    Loaders.show(this)
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        listPracticeAnswer.clear()
                        listPracticeAnswer.addAll(it.value.data?.result?.get(0)?.answers!!)
                        println("=======${it.value.data?.result!!}")
                        App.app.prefManager.pdfSolution =
                            it.value.data?.result?.get(0)?.solutionData?.solution?.pdf ?: ""
                        App.app.prefManager.linkVideoSolution =
                            it.value.data?.result?.get(0)?.solutionData?.solution?.link ?: ""
                        binding.rcy.layoutManager = GridLayoutManager(this, 2)
                        binding.rcy.adapter = ResultsAdapter(
                            this,
                            prepareList(
                                it.value.data?.result?.get(0)?.totalcorrectAnswers ?: 0,
                                it.value.data?.result?.get(0)?.totalIncorrect ?: 0,
                                ((it.value.data?.result?.get(0)?.answers?.size
                                    ?: 0) - (it.value.data?.result?.get(
                                    0
                                )?.noOfAttempts ?: 0)),
                                it.value.data?.result?.get(0)?.answers?.size ?: 0,
                                it.value.data?.totalScore ?: 0.0,
                                "%.2f".format(it.value.data?.negativeMarking ?: 0.0).toDouble()
                            )
                        )
                       // binding.textView601.text = "You won! : ₹ ${it.value.data?.prize ?: 0}"
                        binding.textView60.text =
                            "${it.value.data?.ranking ?: 0} out of ${it.value.data?.totalParticipants ?: 0} participants"

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
                is Resource.Error ->{
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

    /**
     * Preparing List for recycler question according to the quiz performance
     * replace with actual data later on
     * */
    private fun prepareList(
        rightAnswers: Int,
        wrongAnswers: Int,
        unattemptedQuestions: Int,
        totalQuestions: Int,
        score: Double,
        negativeMarking: Double
    ): ArrayList<ResultsModel> {
        return arrayListOf<ResultsModel>().apply {
            add(
                ResultsModel(
                    R.drawable.right_light_yellow_img, "$rightAnswers questions", "Correct answer"
                )
            )

            add(
                ResultsModel(
                    R.drawable.wrong_light_yellow_img, "$wrongAnswers questions", "Wrong answer"
                )
            )
            add(
                ResultsModel(
                    R.drawable.question_light_yellow_img,
                    "$unattemptedQuestions questions",
                    "unattempt\nquestions"
                )
            )
            add(
                ResultsModel(
                    R.drawable.right_light_yellow_img,
                    "$totalQuestions questions",
                    "Completion\nquestions"
                )
            )
            add(
                ResultsModel(R.drawable.question_light_yellow_img, "$score", "Score")
            )
            add(
                ResultsModel(
                    R.drawable.right_light_yellow_img, "$negativeMarking Marks", "Negative\nMarking"
                )
            )

        }
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            if (from == "tests") {
                finish()
            } else {
                startActivity(
                    Intent(
                        this, HomeActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }
        }
        binding.tvViewAnalysis.setOnClickListener {
            if (from == "practice") {
                startActivity(
                    Intent(this, ViewAnalysisActivity::class.java).putExtra("from", from)
                        .putExtra("ans_list", listPracticeAnswer)
                )
            } else {
                startActivity(
                    Intent(this, ViewAnalysisActivity::class.java).putExtra("from", from)
                        .putExtra("ans_list", listanswer)
                )
            }

        }
        binding.btnViewLeaderBoard.setOnClickListener {
            startActivity(Intent(this, LeaderBoardActivity::class.java).putExtra("from", from))
        }
        binding.btnContinue.setOnClickListener {
            startActivity(Intent(this, SolutionForContestActivity::class.java).putExtra("from",from))
        }
    }

    private fun startViewGroupAnimation() {
        val builder = ScreenAnimation.get()
        when (intent.getStringExtra(FROM)) {
            PRACTICE_TEST -> {
                binding.btnViewLeaderBoard.visibility = View.GONE
            }
            else -> {
                if (intent.getBooleanExtra(IS_WON, false)) {
                    builder.add(AnimationInfo(binding.layWhooo, AnimationType.FROM_BOTTOM))
                    binding.layWhooo.visibility = View.VISIBLE
                } else {
                    builder.add(AnimationInfo(binding.layAhh, AnimationType.FROM_BOTTOM))
                    binding.layAhh.visibility = View.VISIBLE
                }
                binding.btnViewLeaderBoard.visibility = View.VISIBLE

            }
        }

        builder.add(AnimationInfo(binding.clYourRank, AnimationType.FROM_BOTTOM))
        builder.add(AnimationInfo(binding.rcy, AnimationType.FROM_BOTTOM))
        if (binding.btnViewLeaderBoard.isVisible) builder.add(
            AnimationInfo(
                binding.btnViewLeaderBoard, AnimationType.FROM_BOTTOM
            )
        )
        builder.add(AnimationInfo(binding.btnContinue, AnimationType.FROM_BOTTOM))
        ScreenAnimation.get().apply {
            add(AnimationInfo(binding.tvResult, AnimationType.FROM_LEFT))
            add(AnimationInfo(binding.tvViewAnalysis, AnimationType.FROM_RIGHT))
            start()
        }
        builder.start()
    }


    companion object {
        private const val FROM = "from"
        private const val PRACTICE_TEST = "practice"
        private const val CONTEST_TEST = "contest"
        private const val IS_WON = "isWon"
        fun getIntent(context: Context, fromPractice: Boolean, isWon: Boolean = false): Intent {
            val intent = Intent(context, ResultsActivity::class.java)
            if (fromPractice) {
                intent.putExtra(FROM, PRACTICE_TEST)
            } else {
                intent.putExtra(FROM, CONTEST_TEST)
                intent.putExtra(IS_WON, isWon)
            }
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }
    }
}
