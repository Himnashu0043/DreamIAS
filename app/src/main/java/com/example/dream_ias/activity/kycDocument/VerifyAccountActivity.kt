package com.example.dream_ias.activity.kycDocument

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityVerifyAccountBinding
import com.example.dream_ias.util.CommonUtil

class VerifyAccountActivity : BaseActivity<ActivityVerifyAccountBinding>() {
    override fun getLayout(): ActivityVerifyAccountBinding {
        return ActivityVerifyAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Verify Account"

    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.constraintLayout12.setOnClickListener {
            startActivity(Intent(this, VerifyEmailAddressActivity::class.java))
        }
        binding.constraintLayout13.setOnClickListener {
            startActivity(Intent(this, VerifyPanCardActivity::class.java))

        }
        binding.constraintLayout14.setOnClickListener {
            startActivity(Intent(this, VerifyBankAccountActivity::class.java))

        }
    }
}