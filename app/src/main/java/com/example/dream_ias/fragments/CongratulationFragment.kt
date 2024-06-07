package com.example.dream_ias.fragments

import com.example.dream_ias.databinding.FragmentCongratulationBinding

class CongratulationFragment : BaseRegistrationFragment<FragmentCongratulationBinding>() {
    override fun onContinue() {
        moveToNext()
    }

    override fun getLayout(): FragmentCongratulationBinding {
        return FragmentCongratulationBinding.inflate(layoutInflater)
    }
}