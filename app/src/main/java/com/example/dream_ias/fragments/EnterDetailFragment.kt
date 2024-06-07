package com.example.dream_ias.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.dream_ias.activity.RegistrationActivity
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.databinding.FragmentEnterYourDetailBinding
import com.example.dream_ias.util.App
import com.example.dream_ias.util.CommonUtil

class EnterDetailFragment : BaseRegistrationFragment<FragmentEnterYourDetailBinding>() {
    override fun onContinue() {
        val activity = requireActivity() as RegistrationActivity
       /* if (!CommonUtil.isValidEmail(binding.email.text.toString().trim())){
            CommonUtil.showSnackBar(binding.root.context,"Please Enter valid Email Address",android.R.color.holo_red_light)
        }else if (TextUtils.isEmpty(binding.firstName.text.toString().trim())){
            CommonUtil.showSnackBar(binding.root.context,"Please Enter First Name",android.R.color.holo_red_light)
        }else if (TextUtils.isEmpty(binding.lastName.text.toString().trim())){
            CommonUtil.showSnackBar(binding.root.context,"Please Enter Last Name",android.R.color.holo_red_light)
        }
        else{
            activity.params["email"] = binding.email.text.toString().trim()
            activity.params["firstName"] = binding.firstName.text.toString().trim()
            activity.params["lastName"] = binding.lastName.text.toString().trim()
            moveToNext()
        }*/

        if (TextUtils.isEmpty(binding.firstName.text.toString().trim())){
            CommonUtil.showSnackBar(binding.root.context,"Please Enter First Name",android.R.color.holo_red_light)
        }else if (TextUtils.isEmpty(binding.lastName.text.toString().trim())){
            CommonUtil.showSnackBar(binding.root.context,"Please Enter Last Name",android.R.color.holo_red_light)
        } else{
            activity.params["email"] = binding.email.text.toString().trim()
            activity.params["firstName"] = binding.firstName.text.toString().trim()
            activity.params["lastName"] = binding.lastName.text.toString().trim()
            activity.params["deviceToken"] = App.app.prefManager.deviceToken
            moveToNext()
        }

    }

    override fun getLayout(): FragmentEnterYourDetailBinding {
        return FragmentEnterYourDetailBinding.inflate(layoutInflater)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listener()
    }

    private fun listener() {
        binding.tvSkip.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

    private fun initView() {

    }
}