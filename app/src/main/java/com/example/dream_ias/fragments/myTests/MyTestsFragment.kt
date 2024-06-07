package com.example.dream_ias.fragments.myTests

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.apiResponse.ExamNameListRes
import com.example.dream_ias.databinding.FragmentMyTestsBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.fragments.SubHomeFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MyTestsFragment : BaseFragment<FragmentMyTestsBinding>() {
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<ExamNameListRes.Data.Result>()
    override fun getLayout(): FragmentMyTestsBinding {
        return FragmentMyTestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.isUserInputEnabled = false
        observer()


        //binding.viewPager.offscreenPageLimit = 1
        /*TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "IAS"
                1 -> "RAS"
                2 -> "UPPCS"
                3 -> "BPSC"
                4 -> "MPSC"
                else -> "UPSC"
            }
        }.attach()*/
    }
    private fun examCategoryList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._exam_CategoryList(token)
    }

    private fun observer() {
        viewModel._examcategoryList.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result!!)
                        binding.viewPager.adapter = ViewPagerAdapter()
                        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                            tab.text = list.get(position).examName
                        }.attach()

                    } else {
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }
    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            return SubTestsFragment().apply {
                arguments = Bundle().apply {
                    putString("test_exam_id", list.get(position)._id)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        examCategoryList()
    }
}