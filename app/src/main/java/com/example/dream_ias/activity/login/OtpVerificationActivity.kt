package com.example.dream_ias.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.databinding.ActivityOtpVerificationBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : BaseActivity<ActivityOtpVerificationBinding>() {

    private lateinit var code1: String
    private lateinit var verificationId: String
    private var phoneNumber: String? = ""
    private var countryCode: String? = ""
    private val TAG: String? = OtpVerificationActivity::class.java.name
    private val viewModel: AuthViewModel by viewModels()
    lateinit var mAuth: FirebaseAuth

    override fun getLayout(): ActivityOtpVerificationBinding {
        return ActivityOtpVerificationBinding.inflate(layoutInflater)
    }

    private val currentCode
        get() = binding.etOne.text.trim().toString() + binding.etTwo.text.trim()
            .toString() + binding.etThree.text.trim().toString() + binding.etFour.text.trim()
            .toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
        observer()

    }

    private fun observer() {
        viewModel.userLogin.observe(this@OtpVerificationActivity){
            when(it){
                is Resource.Loading -> {
                    Log.d(TAG, "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
//                        shortToast(it.value.message)
//                        shortToast(it.value.message)
                        App.app.prefManager.loginData = it.value.data
                        App.app.prefManager.isloggedIn = true
                        startActivity(
                            Intent(
                                this@OtpVerificationActivity,
                                HomeActivity::class.java
                            )
                        )
                        finish()
                    } else if (it.value?.status == Constants.STATUS_404) {
                        shortToast(it.value.message)
                    } else if (it.value?.status == Constants.STATUS_401) {
                        shortToast(it.value?.message.toString())
                    } else if (it.value?.status == Constants.STATUS_FAILURE) {
                        shortToast(it.value?.message.toString())
                    }
                }
                is Resource.Failure -> {
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    it.throwable?.getError(this)?.message.toString().show(
                        binding,
                        com.example.dream_ias.R.color.red
                    )
                }
                else -> {
                    shortToast("Something Went Wrong")
                }
            }
        }
    }

    private fun initView() {
        binding.etOne.addTextChangedListener(textChangeListener)
        binding.etTwo.addTextChangedListener(textChangeListener)
        binding.etThree.addTextChangedListener(textChangeListener)
        binding.etFour.addTextChangedListener(textChangeListener)
        binding.etOne.setOnKeyListener(backKeyListener)
        binding.etTwo.setOnKeyListener(backKeyListener)
        binding.etThree.setOnKeyListener(backKeyListener)
        binding.etFour.setOnKeyListener(backKeyListener)

        phoneNumber = intent.getStringExtra("phoneNumber")?.trim()
        countryCode = intent.getStringExtra("countryCode")?.trim()

        binding.textView12.text = "+"+countryCode +""+phoneNumber

        mAuth = FirebaseAuth.getInstance()
//        sendVerificationCode("+"+countryCode + phoneNumber)

    }

    private fun sendVerificationCode(number: String) {
        // Send OTP to the user's phone number
        val options = mAuth?.let {
            PhoneAuthOptions.newBuilder(it)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(
            s: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(s, forceResendingToken)
            // When we receive the OTP, store it in the verificationId variable
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            code1 = phoneAuthCredential.smsCode.toString()
            Log.d("countrycodemintu", code1.toString())
            if (code1 != null) {
//                binding.firstPinView.setText(code1)
                verifyCode(code1)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Display an error message with Firebase exception
            Toast.makeText(this@OtpVerificationActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyCode(code: String) {
//         Verify the code using PhoneAuthCredential
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this@OtpVerificationActivity, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    // If the code is correct and the task is successful,
                    // send the user to the new activity.

                } else {
                    // If the code is not correct, display an error message.
                    Toast.makeText(this@OtpVerificationActivity, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private var backClicked = false
    private val backKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            Log.d("TAG", "back clicked")
            backClicked = true
            true
        } else backClicked = false
        false
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (!backClicked) {
                when (currentCode.length) {
                    0 -> binding.etOne.requestFocus()
                    1 -> binding.etTwo.requestFocus()
                    2 -> binding.etThree.requestFocus()
                    3 -> binding.etFour.requestFocus()
                }
            } else {
                Log.d("TAG", "afterTextChanged: ${currentCode.length}")
                when (currentCode.length) {
                    0 -> binding.etOne.requestFocus()
                    1 -> binding.etOne.requestFocus()
                    2 -> binding.etTwo.requestFocus()
                    3 -> binding.etThree.requestFocus()
                }
            }
        }
    }
    private fun listener() {
        binding.btnContinue.setOnClickListener {
            val params = HashMap<String, String>()
            params["phoneNumber"] = phoneNumber.toString()
            params["deviceToken"] = App.app.prefManager.deviceToken
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.login(params)
            }
        }
        binding.imageView4.setOnClickListener {
            finish()
        }
    }
}