package com.example.dream_ias.activity.wallet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityTopUpBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopUpActivity : BaseActivity<ActivityTopUpBinding>() {
    private var from: String = ""
    private var wallet_amt: Int = 0
    private val viewModel: AuthViewModel by viewModels()

    override fun getLayout(): ActivityTopUpBinding {
        return ActivityTopUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        wallet_amt = intent.getIntExtra("wallet_amt", 0)
        if (from == "withDraw") {
            binding.toolbar.tvTittle.text = "Withdraw"
            binding.btnContinue.text = "Withdraw ₹ 0"
            binding.textView30.text = "Note : You can withdraw maximum Rs. 100 from you wallet"
            binding.textView29.text = "₹ $wallet_amt"
        } else {
            binding.toolbar.tvTittle.text = "Top Up"
            binding.btnContinue.text = "Add ₹ 0"
            binding.textView30.text = "Note : Add minimum Rs. 30 to the wallet"
            binding.textView29.text = "₹ $wallet_amt"
        }
        initView()
        listener()
    }

    private fun initView() {
        observer()
    }

    private fun addAmountAPI() {
        val amount = binding.editText.text.trim().toString()
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel._addAmount(token, amount.toInt())
        }
    }
    private fun withDrowAmountAPI() {
        val amount = binding.editText.text.trim().toString()
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.withDrowAmount(token, amount.toInt())
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (from == "withDraw") {
                    binding.btnContinue.text = "Withdraw ₹ ${s}"
                } else {
                    binding.btnContinue.text = "Add ₹ ${s}"
                }
            }
        })
        binding.constraintLayout8.setOnClickListener {
            binding.editText.setText("50")
            if (from == "withDraw") {
                binding.btnContinue.text = "Withdraw ₹ ${binding.editText.text}"
            } else {
                binding.btnContinue.text = "Add ₹ ${binding.editText.text}"
            }

        }
        binding.constraintLayout9.setOnClickListener {
            binding.editText.setText("100")
            if (from == "withDraw")
                binding.btnContinue.text = "Withdraw ₹ ${binding.editText.text}"
            else
                binding.btnContinue.text = "Add ₹ ${binding.editText.text}"
        }
        binding.constraintLayout10.setOnClickListener {
            binding.editText.setText("200")
            if (from == "withDraw") {
                CommonUtil.showSnackBar(this, "You can withdrawal maximum 100", R.color.red)
                binding.btnContinue.text = "Withdraw ₹ ${binding.editText.text}"
            } else {
                binding.btnContinue.text = "Add ₹ ${binding.editText.text}"
            }

        }

        binding.btnContinue.setOnClickListener {
            if (TextUtils.isEmpty(binding.editText.text?.trim().toString())) {
                CommonUtil.showSnackBar(this, "Please Enter Amount!!", R.color.red)
            } else {
                if (binding.editText.text.trim().toString()
                        .toInt() >= 30 && binding.editText.text.trim().isNotEmpty()
                ) {
                    if (from != "withDraw") {
                        addAmountAPI()
                    } else {
                        if (wallet_amt <= 0) {
                            CommonUtil.showSnackBar(
                                this,
                                "Not Sufficient balance in Wallet",
                                R.color.red
                            )
                        } else {
                            withDrowAmountAPI()
                        }

                    }
                } else {
                    enterMinAmount()
                }

            }

        }
    }

    private fun observer() {
        viewModel._addAmountLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        addPaymentPopup()
                    } else {
                        Loaders.hide()
                        CommonUtil.showSnackBar(
                            binding.root.context, it.value?.message, android.R.color.holo_red_light
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
                        binding.root.context,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    shortToast("Something Went Wrong")
                }
            }
        }
        viewModel._withDrowAmountLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        addPaymentPopup()
                    } else {
                        Loaders.hide()
                        CommonUtil.showSnackBar(
                            binding.root.context, it.value?.message, android.R.color.holo_red_light
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
                        binding.root.context,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    shortToast("Something Went Wrong")
                }
            }
        }
    }

    private fun enterMinAmount() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_enter_min_amount)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun addPaymentPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.money_add_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        val yes = dialog.findViewById<AppCompatButton>(R.id.btnContinue)
        val text = dialog.findViewById<TextView>(R.id.textView49)
        if (from != "withDraw") {
            text.text = "₹ ${binding.editText.text} added to your wallet successfully"
        } else {
            text.text = "₹ ${binding.editText.text} Amount withdrawn from wallet successfully"

        }

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finish()
        }, 2000)
        yes.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.show()
    }
}