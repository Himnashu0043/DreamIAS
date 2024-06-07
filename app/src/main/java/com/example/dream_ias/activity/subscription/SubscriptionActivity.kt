package com.example.dream_ias.activity.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivitySubscriptionBinding
import com.example.dream_ias.fragments.subScription.ActiveFragment
import com.example.dream_ias.fragments.subScription.ExpiredFragment
import com.example.dream_ias.util.CommonUtil
import com.google.android.material.tabs.TabLayoutMediator

class SubscriptionActivity : BaseActivity<ActivitySubscriptionBinding>() {
    override fun getLayout(): ActivitySubscriptionBinding {
        return ActivitySubscriptionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        binding.toolbar.tvTittle.text = "Subscription"
        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLay, binding.viewpagr) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Active"
                }
                1 -> {
                    tab.text = "Expired"

                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {

            when (position) {
                0 -> {
                    return ActiveFragment()
                }
                1 -> {
                    return ExpiredFragment()
                }

            }


            return ActiveFragment()
        }
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}