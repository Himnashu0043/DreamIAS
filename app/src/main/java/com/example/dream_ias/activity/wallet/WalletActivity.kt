package com.example.dream_ias.activity.wallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.adapter.WalletAdapter
import com.example.dream_ias.databinding.ActivityWalletBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletActivity : BaseActivity<ActivityWalletBinding>() {
    private val viewModel: AuthViewModel by viewModels()
    private var wallet_amt: Int = 0
    companion object {
        private var haveData = true
    }

    override fun getLayout(): ActivityWalletBinding {
        return ActivityWalletBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        haveData = !haveData
        initView()
        listener()
    }

    private fun getAmountAPI() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel._getAmount(token)
        }
    }

    private fun initView() {
        binding.rcy.layoutManager = LinearLayoutManager(this)
        val adptr = WalletAdapter(this)
        binding.rcy.adapter = adptr
    }

    private fun observer() {
        viewModel._getAmountLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        binding.textView16.text = "₹ ${it.value.data?.amount ?: 0}"
                        wallet_amt = it.value.data?.amount ?: 0
                    } else {
                        Loaders.hide()
                        CommonUtil.showSnackBar(
                            binding.root.context,
                            it.value?.message,
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
                    shortToast("Something Went Wrong")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (haveData) {
            binding.rcy.visibility = View.VISIBLE
            binding.llNoTransactions.visibility = View.GONE
        } else {
            binding.rcy.visibility = View.GONE
            binding.llNoTransactions.visibility = View.VISIBLE
        }
        getAmountAPI()
        observer()
    }

    private fun listener() {
        binding.constraintLayout3.setOnClickListener {
            startActivity(Intent(this, TopUpActivity::class.java).putExtra("wallet_amt",wallet_amt))
        }
        binding.constraintLayout4.setOnClickListener {
            startActivity(Intent(this, TopUpActivity::class.java).putExtra("from", "withDraw").putExtra("wallet_amt",wallet_amt))

        }

        binding.imageView5.setOnClickListener {
            finish()
        }
        binding.tvFilter.setOnClickListener {
            FilterWalletBottomSheet().apply {
                show(this@WalletActivity.supportFragmentManager, FilterWalletBottomSheet.TAG)
            }
        }
    }
}