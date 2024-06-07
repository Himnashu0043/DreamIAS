package com.example.dream_ias.fragments.practiceTests

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.R
import com.example.dream_ias.apiResponse.ExamNameListRes
import com.example.dream_ias.databinding.FragmentPracticeTestBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import com.google.android.material.tabs.TabLayoutMediator

class PracticeTestFragment : BaseFragment<FragmentPracticeTestBinding>() {
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<ExamNameListRes.Data.Result>()
    private var iddd: String? = ""
    override fun getLayout(): FragmentPracticeTestBinding {
        return FragmentPracticeTestBinding.inflate(layoutInflater)
    }

    private fun examCategoryList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._exam_CategoryList(token)
    }

    private fun myTestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._myTest(token, iddd ?: "")
    }

    private fun observer() {
        viewModel._examcategoryList.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is Resource.Loading -> {
                        Log.d("TAG", "observer: Loading")
                    }
                    is Resource.Success -> {
                        if (it.value?.status == Constants.SUCCESS) {
                            list.clear()
                            list.addAll(it.value.data?.result!!)
                            binding.viewPager.adapter = ViewPagerAdapter()
                            TabLayoutMediator(
                                binding.tabLayout,
                                binding.viewPager
                            ) { tab, position ->
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
                    is Resource.Error -> {
                        CommonUtil.showSnackBar(
                            requireContext(), it.throwable?.getError(requireContext())?.message.toString(),
                            R.color.red
                        )
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
        viewModel._myTestLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                   // Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                   // Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        App.app.prefManager.isPracticeSubS = it.value.data?.subscribed == true
                        println("======testPrr${it.value.data?.subscribed}")

                    } else {
                        Loaders.hide()
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
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
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.isUserInputEnabled = false
        observer()
        /*binding.viewPager.adapter = ViewPagerAdapter()*/
        /*binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 1*/

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

    inner class ViewPagerAdapter : FragmentStateAdapter(requireActivity()) {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            return SubPracticeTestFragment().apply {
                arguments = Bundle().apply {
                    iddd = list.get(position)._id ?: ""
                    putString("practice_examId", list.get(position)._id)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        examCategoryList()
        myTestList()

    }


}