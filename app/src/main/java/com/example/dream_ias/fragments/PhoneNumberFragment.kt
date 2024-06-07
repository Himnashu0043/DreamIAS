package com.example.dream_ias.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.dream_ias.activity.RegistrationActivity
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.databinding.FragmentPhoneBinding
import com.example.dream_ias.util.CommonUtil

class PhoneNumberFragment : BaseRegistrationFragment<FragmentPhoneBinding>() {
    override fun onContinue() {
        activity.countryCode = binding.countryCodePicker.selectedCountryCode
        val phoneNumber = binding.etPhone.text.toString().trim()
        if (!CommonUtil.isValidMobile1(phoneNumber)){
            CommonUtil.showSnackBar(binding.root.context,"Please Enter Valid Phone Number",android.R.color.holo_red_light)
        }else{
            activity.params["phoneNumber"] = binding.etPhone.text.toString().trim()
            moveToNext()
        }

    }

    override fun getLayout(): FragmentPhoneBinding {
        return FragmentPhoneBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAlreadyHaveAnAccount.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    }
}