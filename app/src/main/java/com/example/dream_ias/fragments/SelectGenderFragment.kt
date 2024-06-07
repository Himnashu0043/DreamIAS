package com.example.dream_ias.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.databinding.FragmentSelectGenderBinding

class SelectGenderFragment:BaseRegistrationFragment<FragmentSelectGenderBinding>() {
    override fun onContinue() {
        moveToNext()
    }

    override fun getLayout(): FragmentSelectGenderBinding {
        return FragmentSelectGenderBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivMale.isSelected = true
        binding.ivMale.setOnClickListener {
            binding.ivMale.isSelected = true
            binding.ivFemale.isSelected = false
        }
        binding.ivFemale.setOnClickListener {
            binding.ivMale.isSelected = false
            binding.ivFemale.isSelected = true
        }
        binding.tvSkip.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

    }
}