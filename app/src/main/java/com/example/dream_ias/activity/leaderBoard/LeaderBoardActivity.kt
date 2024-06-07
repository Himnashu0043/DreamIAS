package com.example.dream_ias.activity.leaderBoard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.adapter.leaderBoard.LeaderBoardAdapter
import com.example.dream_ias.apiResponse.leaderBoard.ContestMainLeaderBoardRes
import com.example.dream_ias.databinding.ActivityLeaderBoardBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class LeaderBoardActivity : BaseActivity<ActivityLeaderBoardBinding>() {
    private var from: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<ContestMainLeaderBoardRes.Data>()
    override fun getLayout(): ActivityLeaderBoardBinding {
        return ActivityLeaderBoardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        println("======lead$from")
        initView()
        listener()

    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Leaderboard"
        observer()

    }

    private fun leaderBoard() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.contestLeaderBoard(token, App.app.prefManager.contestId)
    }

    private fun practiceleaderBoard() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.leaderBoardPractice(token, App.app.prefManager.practiceTestId)
    }

    private fun observer() {
        viewModel._contestLeaderBoardliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                    Loaders.show(this)
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data!!)
                        println("=======list${list.size}")
                        if (list.size == 4) {
                            binding.rcy.layoutManager = LinearLayoutManager(this)
                            val adptr = LeaderBoardAdapter(this, list)
                            binding.rcy.adapter = adptr
                            adptr?.notifyDataSetChanged()
                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            Glide.with(this).load(list[1].userData?.image ?: "")
                                .into(binding.ivSliver)
                            Glide.with(this).load(list[2].userData?.image ?: "")
                                .into(binding.ivthird)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView65.text =
                                "${list[1].userData?.firstName ?: ""} ${list[1].userData?.lastName ?: ""}"
                            binding.textView674.text =
                                "${list[2].userData?.firstName ?: ""} ${list[2].userData?.lastName ?: ""}"

                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"
                            binding.textView66.text = "${list[1].totalScore ?: 0} Score"
                            binding.textView6164.text = "${list[2].totalScore ?: 0} Score"

                            binding.ivSliver.visibility = View.VISIBLE
                            binding.textView65.visibility = View.VISIBLE
                            binding.textView66.visibility = View.VISIBLE

                            binding.ivthird.visibility = View.VISIBLE
                            binding.textView674.visibility = View.VISIBLE
                            binding.textView6164.visibility = View.VISIBLE

                        } else if (list.size == 1) {
                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"

                            binding.ivSliver.visibility = View.INVISIBLE
                            binding.textView65.visibility = View.INVISIBLE
                            binding.textView66.visibility = View.INVISIBLE

                            binding.ivthird.visibility = View.INVISIBLE
                            binding.textView674.visibility = View.INVISIBLE
                            binding.textView6164.visibility = View.INVISIBLE
                        } else if (list.size == 2) {
                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"

                            Glide.with(this).load(list[1].userData?.image ?: "")
                                .into(binding.ivSliver)
                            binding.textView65.text =
                                "${list[1].userData?.firstName ?: ""} ${list[1].userData?.lastName ?: ""}"
                            binding.textView66.text = "${list[1].totalScore ?: 1} Score"

                            binding.ivSliver.visibility = View.VISIBLE
                            binding.textView65.visibility = View.VISIBLE
                            binding.textView66.visibility = View.VISIBLE

                            binding.ivthird.visibility = View.INVISIBLE
                            binding.textView674.visibility = View.INVISIBLE
                            binding.textView6164.visibility = View.INVISIBLE

                        } else if (list.size == 3) {
                            binding.ivSliver.visibility = View.VISIBLE
                            binding.textView65.visibility = View.VISIBLE
                            binding.textView66.visibility = View.VISIBLE

                            binding.ivthird.visibility = View.VISIBLE
                            binding.textView674.visibility = View.VISIBLE
                            binding.textView6164.visibility = View.VISIBLE

                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            Glide.with(this).load(list[1].userData?.image ?: "")
                                .into(binding.ivSliver)
                            Glide.with(this).load(list[2].userData?.image ?: "")
                                .into(binding.ivthird)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView65.text =
                                "${list[1].userData?.firstName ?: ""} ${list[1].userData?.lastName ?: ""}"
                            binding.textView674.text =
                                "${list[2].userData?.firstName ?: ""} ${list[2].userData?.lastName ?: ""}"

                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"
                            binding.textView66.text = "${list[1].totalScore ?: 0} Score"
                            binding.textView6164.text = "${list[2].totalScore ?: 0} Score"
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
        viewModel._leaderBoardPracticeliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                    Loaders.show(this)
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data!!)
                        println("=======list${list.size}")
                        if (list.size == 4) {
                            binding.rcy.layoutManager = LinearLayoutManager(this)
                            val adptr = LeaderBoardAdapter(this, list)
                            binding.rcy.adapter = adptr
                            adptr?.notifyDataSetChanged()
                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            Glide.with(this).load(list[1].userData?.image ?: "")
                                .into(binding.ivSliver)
                            Glide.with(this).load(list[2].userData?.image ?: "")
                                .into(binding.ivthird)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView65.text =
                                "${list[1].userData?.firstName ?: ""} ${list[1].userData?.lastName ?: ""}"
                            binding.textView674.text =
                                "${list[2].userData?.firstName ?: ""} ${list[2].userData?.lastName ?: ""}"

                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"
                            binding.textView66.text = "${list[1].totalScore ?: 0} Score"
                            binding.textView6164.text = "${list[2].totalScore ?: 0} Score"

                            binding.ivSliver.visibility = View.VISIBLE
                            binding.textView65.visibility = View.VISIBLE
                            binding.textView66.visibility = View.VISIBLE

                            binding.ivthird.visibility = View.VISIBLE
                            binding.textView674.visibility = View.VISIBLE
                            binding.textView6164.visibility = View.VISIBLE

                        } else if (list.size == 1) {
                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"

                            binding.ivSliver.visibility = View.INVISIBLE
                            binding.textView65.visibility = View.INVISIBLE
                            binding.textView66.visibility = View.INVISIBLE

                            binding.ivthird.visibility = View.INVISIBLE
                            binding.textView674.visibility = View.INVISIBLE
                            binding.textView6164.visibility = View.INVISIBLE
                        } else if (list.size == 2) {
                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"

                            Glide.with(this).load(list[1].userData?.image ?: "")
                                .into(binding.ivSliver)
                            binding.textView65.text =
                                "${list[1].userData?.firstName ?: ""} ${list[1].userData?.lastName ?: ""}"
                            binding.textView66.text = "${list[1].totalScore ?: 1} Score"

                            binding.ivSliver.visibility = View.VISIBLE
                            binding.textView65.visibility = View.VISIBLE
                            binding.textView66.visibility = View.VISIBLE

                            binding.ivthird.visibility = View.INVISIBLE
                            binding.textView674.visibility = View.INVISIBLE
                            binding.textView6164.visibility = View.INVISIBLE

                        } else if (list.size == 3) {
                            binding.ivSliver.visibility = View.VISIBLE
                            binding.textView65.visibility = View.VISIBLE
                            binding.textView66.visibility = View.VISIBLE

                            binding.ivthird.visibility = View.VISIBLE
                            binding.textView674.visibility = View.VISIBLE
                            binding.textView6164.visibility = View.VISIBLE

                            Glide.with(this).load(list[0].userData?.image ?: "")
                                .into(binding.ivMain)
                            Glide.with(this).load(list[1].userData?.image ?: "")
                                .into(binding.ivSliver)
                            Glide.with(this).load(list[2].userData?.image ?: "")
                                .into(binding.ivthird)
                            binding.textView67.text =
                                "${list[0].userData?.firstName ?: ""} ${list[0].userData?.lastName ?: ""}"
                            binding.textView65.text =
                                "${list[1].userData?.firstName ?: ""} ${list[1].userData?.lastName ?: ""}"
                            binding.textView674.text =
                                "${list[2].userData?.firstName ?: ""} ${list[2].userData?.lastName ?: ""}"

                            binding.textView616.text = "${list[0].totalScore ?: 0} Score"
                            binding.textView66.text = "${list[1].totalScore ?: 0} Score"
                            binding.textView6164.text = "${list[2].totalScore ?: 0} Score"
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

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (from == "practice") {
            practiceleaderBoard()
        } else {
            leaderBoard()
        }

    }
}