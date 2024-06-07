package com.example.dream_ias.activity.kycDocument

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityVerifyPanCardBinding
import com.example.dream_ias.util.CommonUtil
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class VerifyPanCardActivity : BaseActivity<ActivityVerifyPanCardBinding>() {
    override fun getLayout(): ActivityVerifyPanCardBinding {
        return ActivityVerifyPanCardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Verify Pan Card"
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnContinue.setOnClickListener {
            finish()
        }
        binding.editDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@VerifyPanCardActivity.toString())
                addOnPositiveButtonClickListener {
                    binding.editDate.setText(
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
            }
        }

    }
}