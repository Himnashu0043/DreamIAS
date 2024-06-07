package com.example.dream_ias.activity.help

import android.os.Bundle
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityHelpSupportBinding
import com.example.dream_ias.util.CommonUtil

class HelpSupportActivity : BaseActivity<ActivityHelpSupportBinding>() {
    override fun getLayout(): ActivityHelpSupportBinding {
        return ActivityHelpSupportBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        binding.toolbar.tvTittle.text = "Help & Support"

    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}