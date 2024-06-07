package com.example.dream_ias.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dream_ias.R
import com.example.dream_ias.databinding.BottomSheetDeadlinePassedBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeadlinePassedBottomSheet(private val onDismiss: () -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDeadlinePassedBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDeadlinePassedBinding.inflate(layoutInflater)
        listener()
        return binding.root
    }

    private fun listener() {
        binding.btnContinue.setOnClickListener {
            dismiss()
            onDismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}