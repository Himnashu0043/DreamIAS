package com.example.dream_ias.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.dream_ias.activity.RegistrationActivity
import com.example.dream_ias.databinding.FragmentConfirmAccountBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class VerifyFragment:BaseRegistrationFragment<FragmentConfirmAccountBinding>() {

    private var phoneVerified: Boolean = false
    private lateinit var code1: String
    private lateinit var verificationId: String
    lateinit var mAuth: FirebaseAuth

    private val TAG = "VerifyFragment"
    override fun onContinue() {
//        if (phoneVerified){
            moveToNext()
//        }
    }

    private val currentCode
        get() = binding.etOne.text.trim().toString() + binding.etTwo.text.trim()
            .toString() + binding.etThree.text.trim().toString() + binding.etFour.text.trim()
            .toString()

    override fun getLayout(): FragmentConfirmAccountBinding {
        return FragmentConfirmAccountBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etOne.addTextChangedListener(textChangeListener)
        binding.etTwo.addTextChangedListener(textChangeListener)
        binding.etThree.addTextChangedListener(textChangeListener)
        binding.etFour.addTextChangedListener(textChangeListener)
        binding.etOne.setOnKeyListener(backKeyListener)
        binding.etTwo.setOnKeyListener(backKeyListener)
        binding.etThree.setOnKeyListener(backKeyListener)
        binding.etFour.setOnKeyListener(backKeyListener)

        initView()
    }

    private fun initView() {
        mAuth = FirebaseAuth.getInstance()

        val activity = requireActivity() as RegistrationActivity

        binding.phonenumber.text = "+"+activity.countryCode+activity.params.get("phoneNumber").toString()

//        sendVerificationCode(activity.params.get("phoneNumber").toString())
    }

    private fun sendVerificationCode(number: String) {
        // Send OTP to the user's phone number
        val options = mAuth?.let {
            PhoneAuthOptions.newBuilder(it)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
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
            Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyCode(code: String) {
//         Verify the code using PhoneAuthCredential
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    // If the code is correct and the task is successful,
                    // send the user to the new activity.
                    phoneVerified = true

                } else {
                    phoneVerified = false
                    // If the code is not correct, display an error message.
                    Toast.makeText(requireActivity(), task.exception?.message, Toast.LENGTH_LONG).show()
                }
            })
    }


    private var backClicked = false
    private val backKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            Log.d(TAG, "back clicked")
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
                Log.d(TAG, "afterTextChanged: ${currentCode.length}")
                when (currentCode.length) {
                    0 -> binding.etOne.requestFocus()
                    1 -> binding.etOne.requestFocus()
                    2 -> binding.etTwo.requestFocus()
                    3 -> binding.etThree.requestFocus()
                }
            }
        }
    }
}

