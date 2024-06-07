package com.example.dream_ias.activity.testInfo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.apiResponse.TestContestListRes
import com.example.dream_ias.databinding.ActivityTestInfoBinding
import com.example.dream_ias.fragments.testinfo.LeaderboardTestFragment
import com.example.dream_ias.fragments.testinfo.WinningsTestFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.android.material.tabs.TabLayoutMediator

class TestInfoActivity : BaseActivity<ActivityTestInfoBinding>() {
    private var list: TestContestListRes.Data.Result? = null
    private var amount: Int = 0
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun getLayout(): ActivityTestInfoBinding {
        return ActivityTestInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = intent.getSerializableExtra("list") as TestContestListRes.Data.Result?
        println("================$list")
        /////fetch Data
        binding.tvTitle.text = list!!.testTitle ?: ""
        binding.tvTittle.text = list!!.testTitle ?: ""
        if (list!!.rangeAmount?.get(0)?.amount != null) {
            for (amt in list!!.rangeAmount!!) {
                amount += amt.amount ?: 0
            }
            binding.tvTotalAmount.text = "₹ $amount"
        } else {
            amount = list!!.dreamCandidateAmount ?: 0
            binding.tvTotalAmount.text = "₹ ${list!!.dreamCandidateAmount ?: 0}"
        }

        binding.tvSpotCount.text = "${list!!.spots?.totalNoOfSpots ?: 0} Sports"
        var totalSport = list!!.spots?.totalNoOfSpots ?: 0
        var fillSport = list!!.studentEntrySpotsNo ?: 0
        var leftSport = totalSport - fillSport
        binding.tvSpotLeft.text = "$leftSport Sports Left"
        binding.tvbottomPrice.text = "₹ ${list!!.dreamCandidateAmount ?: 0}"
        binding.tvbottomQuestion.text = "${list!!.questions ?: 0} Questions"
        binding.tvbottomtime.text = list!!.duration ?: ""
        binding.btnOffer.text =
            "Join ₹${(list!!.entryFee?.entryFee ?: 0) - (list!!.entryFee?.discount ?: 0)}"
        binding.indicator.progress = list!!.studentEntrySpotsNo ?: 0
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.viewPager.adapter = ViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Winnings"
                else -> "Leaderboard"
            }
        }.attach()


        val isJoined = intent.getStringExtra(BundleKey.TYPE.name) ?: ""
        println("=======$isJoined")
        setUpView(isJoined)
        binding.btnOffer.setOnClickListener {
            println("======amoo${App.app.prefManager.wallect_amt}")
            if (App.app.prefManager.wallect_amt < ((list!!.entryFee?.entryFee
                    ?: 0) - (list!!.entryFee?.discount ?: 0))
            ) {
                Toast.makeText(this, "Please Add Amount in Wallet", Toast.LENGTH_SHORT).show()
            } else {
                val token = App.app.prefManager.loginData?.jwtToken.toString()
                val params = HashMap<String, String>()
                params["contestId"] = list!!._id ?: ""
                params["examId"] = list!!.examId ?: ""
                params["questionId"] = list!!.quesPaperId ?: ""
                params["testId"] =
                    (list!!.testId ?: "").toString()
                params["amount"] =
                    if (list!!.entryFee?.entryFee == null) "0" else (((list!!.entryFee?.entryFee
                        ?: 0) - list!!.entryFee?.discount!!)).toString()


                viewModel._getContestEntry(token, params)
            }

            /* setUpView(BundleKey.JOINED.name)*/
        }
        observer()
    }
    /*private fun addPlan() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.addPlanHome(token, planId, name, examId, amount, duration, validity)
    }*/
    private fun observer() {
        viewModel._getContestEntryLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                       Toast.makeText(this,it.value.message,Toast.LENGTH_SHORT).show()
                       /* Handler(Looper.getMainLooper()).postDelayed({

                        }, 1000)*/
                        finish()

                    } else {
                        CommonUtil.showSnackBar(
                            this,
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        this,
                        it.throwable?.getError(this)?.message.toString(),
                        R.color.red
                    )
                }
                else -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        this,
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    private fun setUpView(joinStatus: String) {
        when (joinStatus) {
            BundleKey.CONTEST_JOINED.name -> {
                binding.clAlreadyJoined.visibility = View.VISIBLE
                binding.clJoin.visibility = View.GONE
                binding.tvPricePoolValue.text = "₹ $amount"
                binding.tvSpotsValue.text = "${list!!.spots?.totalNoOfSpots ?: 0}"
                binding.tvEntryValue.text =
                    "₹${(list!!.entryFee?.entryFee ?: 0) - (list!!.entryFee?.discount ?: 0)}"
            }

            BundleKey.JOINED.name -> {
                binding.clAlreadyJoined.visibility = View.GONE
                binding.btnOffer.isEnabled = false
                binding.btnOffer.text = "Joined"
                binding.btnOffer.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.textBlackSecondary
                    )
                )
                binding.clJoin.visibility = View.VISIBLE
            }

            BundleKey.NOT_JOINED.name -> {
                binding.clAlreadyJoined.visibility = View.GONE
                binding.clJoin.visibility = View.VISIBLE
            }
        }
    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> WinningsTestFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("list_winning", list)
                    }
                }
                else -> LeaderboardTestFragment()
            }
        }

    }


}