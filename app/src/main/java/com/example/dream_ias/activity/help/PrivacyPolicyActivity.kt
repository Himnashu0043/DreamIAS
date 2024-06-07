package com.example.dream_ias.activity.help

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.activity.viewModels
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityPrivacyPolicyBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {
    private var from: String = ""
    private val viewModel: AuthViewModel by viewModels()
    override fun getLayout(): ActivityPrivacyPolicyBinding {
        return ActivityPrivacyPolicyBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        if (from == "terms") {
            binding.toolbar.tvTittle.text = "Terms & Conditions"
        } else if (from == "about") {
            binding.toolbar.tvTittle.text = "About Us"
        } else {
            binding.toolbar.tvTittle.text = "Privacy Policy"
        }
        observer()
    }

    /*  private fun privacyPolicy() {
          val token = App.app.prefManager.loginData?.jwtToken.toString()
          viewModel._privacyPolicyList(token, "privacy_policy")
      }*/
    private fun observer() {
        viewModel._privacyPolicyList.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        binding.textView793.text = it.value?.data?.get(0)?.title?:""
                        val htmlAsString =it.value?.data?.get(0)?.description?:""
                        val htmlAsSpanned = Html.fromHtml(htmlAsString)
                        binding.textView7193.text = htmlAsSpanned

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
                    CommonUtil.showSnackBar(
                        this,
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (from == "terms") {
            val token = App.app.prefManager.loginData?.jwtToken.toString()
            viewModel._privacyPolicyList(token, "terms_conditions")
        } else if (from == "about") {
            val token = App.app.prefManager.loginData?.jwtToken.toString()
            viewModel._privacyPolicyList(token, "about_us")
        } else {
            val token = App.app.prefManager.loginData?.jwtToken.toString()
            viewModel._privacyPolicyList(token, "privacy_policy")
        }
    }

}