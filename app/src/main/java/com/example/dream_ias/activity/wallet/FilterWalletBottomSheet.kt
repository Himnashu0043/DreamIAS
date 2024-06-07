package com.example.dream_ias.activity.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dream_ias.R
import com.example.dream_ias.databinding.FilterWalletBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterWalletBottomSheet() : BottomSheetDialogFragment() {
    private lateinit var binding: FilterWalletBottomsheetBinding
    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FilterWalletBottomsheetBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {

    }

    private fun listener() {

    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}