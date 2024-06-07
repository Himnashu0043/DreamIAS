package com.example.dream_ias.activity.help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityAboutUsBinding

class AboutUsActivity : BaseActivity<ActivityAboutUsBinding>() {
    override fun getLayout(): ActivityAboutUsBinding {
        return ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.tvTittle.text = "About Us"
    }
}