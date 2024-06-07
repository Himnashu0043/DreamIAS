package com.example.dream_ias.activity.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.edit
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.activity.RegistrationActivity
import com.example.dream_ias.activity.help.PrivacyPolicyActivity
import com.example.dream_ias.databinding.ActivityLoginBinding
import com.example.dream_ias.util.App
import com.example.dream_ias.util.CommonUtil
import com.example.dream_ias.util.Constants
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    lateinit var mAuth: FirebaseAuth
    private val TAG: String? = LoginActivity::class.java.name
    private var deviceToken: String? = ""
    override fun getLayout(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            deviceToken = task.result
            Log.d(ContentValues.TAG, "DeviceToken==>>: ${deviceToken} ")
            getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).edit {
                App.app.prefManager.deviceToken = deviceToken!!
                apply()
            }
            App.app.prefManager.deviceToken = deviceToken!!
        })
        initView()
        listener()
//        observer()

    }

    private fun observer() {
//        viewModel.userLogin.observe(this@LoginActivity){
//            when(it){
//                is Resource.Loading -> {
//                    Log.d(TAG, "observer: Loading")
//                }
//                is Resource.Success -> {
//                    if (it.value?.status == Constants.SUCCESS){
//                        shortToast(it.value?.message.toString())
//                        App.app.prefManager.loginData = it.value?.data
//                        App.app.prefManager.isloggedIn = true
//                        App.app.prefManager.isTutorial = true
//                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
//                        finish()
//                    } else {
//                        shortToast(it.value?.message.toString())
//                    }
//                }
//                is Resource.Failure ->{
//                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
//                }
//                else ->{
//                    shortToast("Something Went Wrong")
//                }
//            }
//        }
    }

    private fun initView() {

        mAuth = FirebaseAuth.getInstance()

    }

    private fun listener() {
        binding.textView11.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
//            finish()
        }
        binding.btnContinue.setOnClickListener {
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val countryCode = binding.countryCodePicker.selectedCountryCode.toString()
            if (!CommonUtil.isValidMobile1(phoneNumber)) {
                CommonUtil.showSnackBar(
                    binding.root.context,
                    "Please Enter Valid Phone Number",
                    android.R.color.holo_red_light
                )
            } else if (!binding.checkBox.isChecked) {
                CommonUtil.showSnackBar(
                    binding.root.context,
                    "Please Select Privacy Policy and Terms and Conditions!!",
                    android.R.color.holo_red_light
                )
            } else {
                startActivity(
                    Intent(this, OtpVerificationActivity::class.java)
                        .putExtra("phoneNumber", phoneNumber)
                        .putExtra("countryCode", countryCode)
                )
            }

//            val params = HashMap<String, String>()
//            params["phoneNumber"] = binding.phoneNumber.text.toString().trim()
//            CoroutineScope(Dispatchers.Main).launch {
//                viewModel.login(params)
//            }
        }
        binding.textView9.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java).putExtra("from", "terms"))
        }
        binding.textView10.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))

        }

    }

    override fun onStart() {
        super.onStart()
        if (App.app.prefManager.isloggedIn == true){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

}