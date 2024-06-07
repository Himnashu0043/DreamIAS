package com.example.dream_ias.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.dream_ias.R
import com.example.dream_ias.activity.dreamIas.DreamIasActivity
import com.example.dream_ias.activity.faq.FAQActivity
import com.example.dream_ias.activity.help.HelpSupportActivity
import com.example.dream_ias.activity.help.PrivacyPolicyActivity
import com.example.dream_ias.activity.kycDocument.VerifyAccountActivity
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.activity.notification.NotificationActivity
import com.example.dream_ias.activity.subscription.SubscriptionActivity
import com.example.dream_ias.activity.wallet.WalletActivity
import com.example.dream_ias.adapter.DrawerAdapter
import com.example.dream_ias.databinding.ActivityHomeBinding
import com.example.dream_ias.fragments.HomeFragment
import com.example.dream_ias.fragments.ProfileFragment
import com.example.dream_ias.fragments.myTests.MyTestsFragment
import com.example.dream_ias.fragments.practiceTests.PracticeTestFragment
import com.example.dream_ias.fragments.subScription.SubScriptionFragment
import com.example.dream_ias.model.DrawerModel
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * HomeActivity as the name depicts, Screen for Main Content of the application.
 *
 * This screen has following components,
 *
 * Drawer -> To Show Options on right and uses a recycler view
 *
 * Toolbar -> Made in RelativeLayout
 *
 * BottomNavigationView -> For Bottom Tabs
 *
 * ViewPager -> For respective screen
 *
 * @see closeDrawer
 * */
class HomeActivity : BaseActivity<ActivityHomeBinding>(), DrawerAdapter.Click {
    private lateinit var dialog: Dialog
    private var drwlist = ArrayList<DrawerModel>()
    private val viewModel: AuthViewModel by viewModels()
    private val TAG: String? = HomeActivity::class.java.name
    private var isSubscribed: Boolean = false
    var doubleBackToExitPressedOnce = false
    override fun getLayout(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        back()
        drwlist.add(DrawerModel(R.drawable.mywallet_icon, "My Wallet"))
        drwlist.add(DrawerModel(R.drawable.mysub_icon, "My Subscription"))
        drwlist.add(DrawerModel(R.drawable.kyc_icon, "KYC Document"))
        drwlist.add(DrawerModel(R.drawable.noti_setting_icon, "Notification Settings"))
        drwlist.add(DrawerModel(R.drawable.help_icon, "Help & Support"))
        drwlist.add(DrawerModel(R.drawable.faq_icon, "FAQs"))
        drwlist.add(DrawerModel(R.drawable.privacy_icon, "Privacy Policy"))
        drwlist.add(DrawerModel(R.drawable.term_icon, "Terms & Conditions"))
        drwlist.add(DrawerModel(R.drawable.about_icon, "About Us"))
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        val adptr = DrawerAdapter(this, drwlist, this)
        binding.nav.rcyProfile.adapter = adptr
        binding.ivProfile.setOnClickListener {
            binding.mainDrw.openDrawer(GravityCompat.START)
        }
        binding.nav.cardView19.setOnClickListener {
            startActivity(Intent(this, DreamIasActivity::class.java))
        }

        binding.ivNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        binding.ivWallet.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        binding.viewPagerHome.adapter = ViewPagerAdapter()/*
        binding.viewPagerHome.offscreenPageLimit = 1*/
        binding.viewPagerHome.isUserInputEnabled = false
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> binding.viewPagerHome.setCurrentItem(0,false)
                R.id.menuMyContests -> binding.viewPagerHome.setCurrentItem(1,false)
                R.id.menuNewSub -> {
                    binding.viewPagerHome.setCurrentItem(2, false)
                }
                R.id.menuMyTests -> {
                    if (!App.app.prefManager.isPracticeSubS){
                        if (!isSubscribed){
                            subscriptionPopup()
                        }

                    }

                    binding.viewPagerHome.setCurrentItem(3, false)
                }
                else -> binding.viewPagerHome.setCurrentItem(4, false)
            }
            binding.fabSubscription.isSelected = it.itemId == R.id.menuNewSub
            if (it.itemId == R.id.menuNewSub) {
                binding.fabSubscription.visibility = View.GONE
            } else {
                binding.fabSubscription.visibility = View.VISIBLE
            }
            return@setOnItemSelectedListener true
        }

        binding.fabSubscription.setOnClickListener {
            binding.bottomNavigation.selectedItemId = R.id.menuNewSub
        }
        binding.ivWallet.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }
        binding.ivNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        binding.viewPagerHome.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 4) {
                    binding.ivWallet.visibility = View.INVISIBLE
                    binding.ivNotification.visibility = View.INVISIBLE
                } else {
                    binding.ivWallet.visibility = View.VISIBLE
                    binding.ivNotification.visibility = View.VISIBLE
                }
            }
        })

        binding.nav.tvLogout.setOnClickListener {
            val token = App.app.prefManager.loginData?.jwtToken.toString()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel._getLogOut(token)
            }

        }
        observer()


    }

    private fun observer() {
        viewModel.userDetail.observe(this@HomeActivity){
            when(it){
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d(TAG, "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        App.app.prefManager.userDetail = it.value?.data
                        isSubscribed = it.value.data.Subscription
                        App.app.prefManager.isAllSubS = it.value.data.Subscription
                        println("=======isSubscribed$isSubscribed")
                        binding.nav.name.text =
                            it.value?.data?.firstName + " " + it.value?.data?.lastName
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
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        binding.root.context,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    shortToast("Something Went Wrong")
                }
            }
        }
        viewModel._getLogOutLiveData.observe(this@HomeActivity) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d(TAG, "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        CommonUtil.showSnackBar(
                            this,
                            it.value.message.toString(),
                            android.R.color.holo_green_dark
                        )
                        App.app.prefManager.clearPreferences()
                        App.app.prefManager.isTutorial = true
                        App.app.prefManager.isloggedIn = false
                        startActivity(
                            Intent(this@HomeActivity, LoginActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                            )
                        )
                        finishAffinity()

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
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        binding.root.context,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    shortToast("Something Went Wrong")
                }
            }
        }
    }

    private fun subscriptionPopup() {
        dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.buy_subscription_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val yes = dialog.findViewById<AppCompatButton>(R.id.btnContinue)
        yes.setOnClickListener {
            dialog.dismiss()
            binding.viewPagerHome.setCurrentItem(2, false)
            binding.bottomNavigation.selectedItemId = R.id.menuNewSub
            binding.fabSubscription.visibility = View.GONE
        }
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 5
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> MyTestsFragment()
                2 -> SubScriptionFragment()
                3 -> PracticeTestFragment()
                4 -> ProfileFragment()
                else -> Fragment()
            }
        }
    }

    override fun onClickSideItem(position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, WalletActivity::class.java))

            }

            1 -> {
                startActivity(Intent(this, SubscriptionActivity::class.java))

            }

            2 -> {
                startActivity(Intent(this, VerifyAccountActivity::class.java))

            }

            3 -> {
                startActivity(Intent(this, NotificationActivity::class.java))

            }

            4 -> {
                startActivity(Intent(this, HelpSupportActivity::class.java))
            }

            5 -> {
                startActivity(Intent(this, FAQActivity::class.java))
            }

            6 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }

            7 -> {
                startActivity(
                    Intent(this, PrivacyPolicyActivity::class.java).putExtra(
                        "from", "terms"
                    )
                )
            }

            8 -> {
                startActivity(
                    Intent(this, PrivacyPolicyActivity::class.java).putExtra(
                        "from", "about"
                    )
                )
               // startActivity(Intent(this, AboutUsActivity::class.java))
            }
        }
    }

    private fun closeDrawer() {
        if (binding.mainDrw.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.userDetail(token)
        }
    }

    private fun back() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (binding.bottomNavigation.selectedItemId) {
                    R.id.menuHome -> finishAffinity()
                    else -> {
                        /*if (::dialog.isInitialized &&  dialog.isShowing) dialog.dismiss()*/
                        binding.bottomNavigation.selectedItemId = R.id.menuHome
                    }

                }
            }
        })
    }
}