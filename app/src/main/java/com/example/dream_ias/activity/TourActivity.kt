package com.example.dream_ias.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.dream_ias.MainActivity
import com.example.dream_ias.R
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.adapter.TourAdapter
import com.example.dream_ias.databinding.ActivityTourBinding
import com.example.dream_ias.model.TourModel
import com.example.dream_ias.util.App
import com.example.dream_ias.util.CommonUtil

class TourActivity : BaseActivity<ActivityTourBinding>(), ViewPager.OnPageChangeListener {
    private val walkthroughmodel: ArrayList<TourModel> = ArrayList()
    override fun getLayout(): ActivityTourBinding {
        return ActivityTourBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()

    }

    private fun initView() {
        walkthroughmodel.add(TourModel(R.drawable.tour_1))
        walkthroughmodel.add(TourModel(R.drawable.tour_2))
        walkthroughmodel.add(TourModel(R.drawable.tour_3))
        binding.commonBtn.tv.text = "Continue"
        binding.viewPager.adapter = TourAdapter(this, walkthroughmodel)
    }

    private fun listener() {
        binding.commonBtn.tv.setOnClickListener {
            if (binding.viewPager.currentItem != 2) {
                binding.viewPager.currentItem = binding.viewPager.currentItem + 1
            } else {
                App.app.prefManager.isTutorial = true
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            }

        }
        binding.tvSkip.setOnClickListener {
            App.app.prefManager.isTutorial = true
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
        binding.viewPager.addOnPageChangeListener(this)
        changeIndicators(0)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    private fun changeIndicators(position: Int) {
        for (childIndex in 0 until binding.tabLayout.childCount)
            binding.tabLayout.getChildAt(childIndex).isSelected = childIndex <= position
    }

    override fun onPageSelected(position: Int) {
        changeIndicators(position)
        binding.tvSkip.visibility = if (position == 2) View.GONE else View.VISIBLE
        when (position) {
            0 -> {
                binding.textView5.text = "Play to gain your knowledge"
            }
            1 -> {
                binding.textView5.text = "Play with many quiz themes"
            }
            else -> {
                binding.textView5.text = "Play with your friends"
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (App.app.prefManager.isTutorial == true && App.app.prefManager.isloggedIn == true){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}