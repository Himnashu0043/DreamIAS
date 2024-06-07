package com.example.dream_ias.activity.faq

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.adapter.FAQAdapter
import com.example.dream_ias.apiResponse.FAQListRes
import com.example.dream_ias.databinding.ActivityFaqactivityBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class FAQActivity : BaseActivity<ActivityFaqactivityBinding>() {
    private val viewModel: AuthViewModel by viewModels()
    private var list = ArrayList<FAQListRes.Data>()
    override fun getLayout(): ActivityFaqactivityBinding {
        return ActivityFaqactivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        binding.toolbar.tvTittle.text = "FAQs"


    }

    private fun faqList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._faq(token)
    }

    private fun observer() {
        viewModel._faqListLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data!!)
                        binding.rcy.layoutManager = LinearLayoutManager(this)
                        val adpt = FAQAdapter(this, list)
                        binding.rcy.adapter = adpt
                        adpt?.notifyDataSetChanged()
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

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        faqList()
        observer()
    }
}