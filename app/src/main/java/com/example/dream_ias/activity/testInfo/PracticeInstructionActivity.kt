package com.example.dream_ias.activity.testInfo

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.quiz.QuizActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.practiceTest.DetailPracticeTestRes
import com.example.dream_ias.databinding.ActivityPracticeIntructionBinding
import com.example.dream_ias.databinding.ItemInstructionsBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class PracticeInstructionActivity : BaseActivity<ActivityPracticeIntructionBinding>() {
    private lateinit var adapter: InstructionsAdapter
    private var id: String = ""
    private var list = ArrayList<DetailPracticeTestRes.Data.Result>()
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): ActivityPracticeIntructionBinding {
        return ActivityPracticeIntructionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id") ?: ""
        println("========Prac$id")
        initView()
        listener()

    }

    private fun initView() {
        observer()

    }

    private fun practiceDetais() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.practiceDetails(token, id)
    }

    private fun observer() {
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
                        list.addAll(it.value.data?.result!!)
                        binding.tvTittle.text = list[0].testData?.testTitle ?: ""
                        binding.tvTitle.text = list[0].testData?.testTitle ?: ""
                        binding.tvSubTitle.text =
                            "Play the game and get all general knowledge about ${list[0].testData?.testTitle ?: ""}"
                        binding.tvQuestion.text =
                            "${(list[0].testData?.questions ?: 0)} Question"
                        binding.tvMarks.text = "${(list[0].testData?.totalMarks ?: 0)} Marks"
                        binding.tvNegativeMarks.text =
                            "Negative ${(list[0].negativeMarking?.marking ?: 0)} Marks"
                        binding.tvTime.text = "${(list[0].testData?.duration ?: 0)}"
                        binding.tvPrize.text = "0 Prize"
                        adapter = InstructionsAdapter()
                        binding.rvInstuction.adapter = adapter
                        adapter.set(list)
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
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    /* "Something Went Wrong".show(binding, R.color.red)*/
//                    CommonUtil.showSnackBar(
//                        this,
//                        "Something Went Wrong",
//                        android.R.color.holo_red_light
//                    )
                }
            }
        }
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnContinue.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java).putExtra("id", id))
        }
    }

    class InstructionsAdapter : BaseAdapter<DetailPracticeTestRes.Data.Result>() {

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
                val htmlAsString =
                    get(absoluteAdapterPosition).testData?.instructions ?: ""
                val htmlAsSpanned = Html.fromHtml(htmlAsString)
                binding.tvInstruction.text =
                    htmlAsSpanned
            }
        }

    }

    override fun onResume() {
        super.onResume()
        practiceDetais()
    }
}