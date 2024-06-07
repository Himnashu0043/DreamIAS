package com.example.dream_ias.activity.kycDocument

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dream_ias.R
import com.example.dream_ias.databinding.ConfirmAccountBottomsheetBinding
import com.example.dream_ias.databinding.FilterWalletBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmYourAccount() : BottomSheetDialogFragment() {
    private lateinit var binding: ConfirmAccountBottomsheetBinding

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    private val currentCode
        get() = binding.etOne.text.trim().toString() + binding.etTwo.text.trim()
            .toString() + binding.etThree.text.trim().toString() + binding.etFour.text.trim()
            .toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmAccountBottomsheetBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
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
    }

    private fun listener() {
        binding.btnContinue.setOnClickListener {
            dismiss()
        }
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

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}