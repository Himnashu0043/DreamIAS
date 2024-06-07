package com.example.dream_ias.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.dream_ias.R
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.databinding.ActivityRegistrationBinding
import com.example.dream_ias.fragments.*
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {

    private var currentPosition = 0
    var countryCode = ""
    var isSkip: Boolean = false
    var params = HashMap<String, String>()
    private val viewModel: AuthViewModel by viewModels()
    private val TAG: String? = RegistrationActivity::class.java.name

    private val fragmentList = arrayListOf<BaseRegistrationFragment<*>>(
        PhoneNumberFragment(),
        VerifyFragment(),
        EnterDetailFragment(),
//        SelectGenderFragment(),
        UploadPhotoFragment(),
        CongratulationFragment()
    )

    override fun getLayout(): ActivityRegistrationBinding {
        return ActivityRegistrationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        supportFragmentManager.beginTransaction()
            .add(R.id.flRegistration, fragmentList[currentPosition]).commit()
        changeIndicator()
        binding.btnContinue.setOnClickListener {
            fragmentList[currentPosition].onContinue()
        }

        observer()

    }

    private fun observer() {
        viewModel.userSignUp.observe(this@RegistrationActivity){
            when(it){
                is Resource.Loading -> {
                    Log.d(TAG, "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        shortToast(it.value?.message.toString())
//                        App.app.prefManager.loginData = it.value?.data
                        App.app.prefManager.loginData = it.value.data
                        currentPosition++
                        fragmentList[currentPosition].replaceFragment()
                        changeIndicator()
//                        moveToNext()

                    } else if (it.value?.status == Constants.STATUS_FAILURE) {
                        shortToast(it.value.message ?: "")
                    } else if (it.value?.status == Constants.STATUS_409) {
                        shortToast(it.value.message ?: "")
                    } else if (it.value?.status == Constants.STATUS_500) {
                        shortToast(it.value.message ?: "")
                    }
                }
                is Resource.Failure ->{
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else ->{
                    shortToast("Something Went Wrong")
                }
            }
        }
    }

    private fun changeIndicator() {
        for (childIndex in 0 until binding.llIndicators.childCount)
            binding.llIndicators.getChildAt(childIndex).isSelected = childIndex <= currentPosition
    }

    fun moveToNext() {
        if (currentPosition == 4) {
            //startActivity(Intent(this, LoginActivity::class.java))
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }else if (currentPosition == 3) {
            if (!isSkip) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.signUp(params)
                }
            }else{
                currentPosition++
                fragmentList[currentPosition].replaceFragment()
                changeIndicator()
            }

        } else {
            currentPosition++
            fragmentList[currentPosition].replaceFragment()
            changeIndicator()
        }

    }

    private fun Fragment.replaceFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.flRegistration, this)
            .addToBackStack(this::class.java.simpleName).commit()
    }

    override fun onStart() {
        super.onStart()
        if (App.app.prefManager.isloggedIn){
            startActivity(Intent(this@RegistrationActivity,HomeActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        currentPosition--
        changeIndicator()
    }

}