package com.example.dream_ias.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.R
import com.example.dream_ias.activity.wallet.WalletActivity
import com.example.dream_ias.databinding.ActivityContestsListBinding
import com.example.dream_ias.fragments.ContestsListFragment
import com.example.dream_ias.fragments.GuideFragment
import com.example.dream_ias.fragments.MyContestsFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

class ContestsListActivity : BaseActivity<ActivityContestsListBinding>() {
    private var contestid: String = ""
    private var time_value: String = ""
    private var examid: String = ""
    private var listSize: Int = 0
    private var timer: CountDownTimer? = null
    private var from: String = ""
    private val viewModel: AuthViewModel by viewModels()

    override fun getLayout(): ActivityContestsListBinding {
        return ActivityContestsListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        from = intent.getStringExtra("from") ?: ""
        println("=======$from")
        if (from == "my_test") {
            contestid = intent.getStringExtra("my_test_testId") ?: ""
            examid = intent.getStringExtra("my_test_exam_id") ?: ""
            time_value = intent.getStringExtra("time_value") ?: ""
            println("========$time_value")
            var timer = convertTimeStringToMillis(time_value) - getCurrentMillisSinceStartOfDay()
            startTimer(timer)

        } else {
            contestid = intent.getStringExtra("id") ?: ""
            examid = intent.getStringExtra("examId") ?: ""
            time_value = intent.getStringExtra("time_value") ?: ""
            println("========$time_value")
            var timer = convertTimeStringToMillis(time_value) - getCurrentMillisSinceStartOfDay()
            startTimer(timer)

        }

        // listSize = intent.getIntExtra("list_size", 0)

        println("========timer$timer")
        println("=========$contestid")
        println("=========listSize$listSize")

        observer()
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.llEndOptions.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    WalletActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
        /*(${if (!App.app.prefManager.myContesntSize.isNullOrEmpty()) App.app.prefManager.myContesntSize else 0})*/
        binding.viewPager.adapter = ViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Contests"
                1 -> "My Contests"
                else -> "Guide"
            }
        }.attach()

    }

    private fun getAmountAPI() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel._getAmount(token)
        }
    }

    private fun observer() {
        viewModel._getAmountLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        binding.tvWallet.text = "₹ ${it.value.data?.amount ?: 0}"
                        App.app.prefManager.wallect_amt = it.value.data?.amount ?: 0
                    } else {
                        Loaders.hide()
                        CommonUtil.showSnackBar(
                            binding.root.context,
                            it.value?.message,
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    Loaders.hide()
                    shortToast("Something Went Wrong")
                }
            }
        }
    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ContestsListFragment().apply {
                    arguments = Bundle().apply {
                        putString("id", contestid)
                    }
                }
                1 -> MyContestsFragment().apply {
                    arguments = Bundle().apply {
                        putString("examId", examid)
                        putString("contestId",contestid)
                    }
                }
                else -> GuideFragment().apply {
                    arguments = Bundle().apply {
                        putString("id", contestid)
                    }
                }
            }
        }
    }

    fun startTimer(time: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val hours = millisUntilFinished / 3600000
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                if (hours > 0) {
                    binding.tvTimeLeft.post {
                        binding.tvTimeLeft.text = binding.root.context.getString(
                            R.string.hour_min_sec,
                            f.format(hours),
                            f.format(min),
                            f.format(sec)
                        )
                    }
                } else {
                    binding.tvTimeLeft.post {
                        binding.tvTimeLeft.text = binding.root.context.getString(
                            R.string.min_sec,
                            f.format(min),
                            f.format(sec)
                        )
                    }
                }
            }

            override fun onFinish() {
                try {
                    binding.tvTimeLeft.post {
                        binding.tvTimeLeft.text = "00:00"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        getAmountAPI()
    }
}