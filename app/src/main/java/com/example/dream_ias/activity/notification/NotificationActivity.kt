package com.example.dream_ias.activity.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivityNotificationBinding
import com.example.dream_ias.util.CommonUtil

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override fun getLayout(): ActivityNotificationBinding {
        return ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Notifications Settings"
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}