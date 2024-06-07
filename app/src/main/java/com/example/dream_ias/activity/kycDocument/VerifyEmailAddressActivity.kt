package com.example.dream_ias.activity.kycDocument

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.wallet.FilterWalletBottomSheet
import com.example.dream_ias.databinding.ActivityVerifyEmailAddressBinding
import com.example.dream_ias.databinding.ConfirmAccountBottomsheetBinding
import com.example.dream_ias.util.CommonUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

class VerifyEmailAddressActivity : BaseActivity<ActivityVerifyEmailAddressBinding>() {
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var confrim_bottom_Sheet: ConfirmAccountBottomsheetBinding
    override fun getLayout(): ActivityVerifyEmailAddressBinding {
        return ActivityVerifyEmailAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Email Address"
    }

    private fun listener() {
        binding.btnContinue.setOnClickListener {
            finish()
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.textView38.setOnClickListener {
            ConfirmYourAccount().apply {
                show(
                    this@VerifyEmailAddressActivity.supportFragmentManager,
                    FilterWalletBottomSheet.TAG
                )
            }
        }

    }

}