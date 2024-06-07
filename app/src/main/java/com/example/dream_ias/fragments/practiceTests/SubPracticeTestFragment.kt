package com.example.dream_ias.fragments.practiceTests

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.databinding.FragmentSubPracticeTestBinding
import com.example.dream_ias.fragments.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class SubPracticeTestFragment : BaseFragment<FragmentSubPracticeTestBinding>() {
    private var practice_examId: String = ""
    override fun getLayout(): FragmentSubPracticeTestBinding {
        return FragmentSubPracticeTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        practice_examId = arguments?.getString("practice_examId").toString()
        println("========$id")
        binding.viewPager.adapter = ViewPagerAdapter()
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ongoing"
                1 -> "Upcoming"
                else -> "Completed"
            }
        }.attach()
    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OngoingPracticeFragment().apply {
                    arguments = Bundle().apply {
                        putString("ongoing_examId", practice_examId)
                    }
                }
                1 -> UpcommingPracticeFragment().apply {
                    arguments = Bundle().apply {
                        putString("upcoming_examId", practice_examId)
                    }
                }
                else -> CompletedPracticeFragment().apply {
                    arguments = Bundle().apply {
                        putString("completed_examId", practice_examId)
                    }
                }
            }
        }

    }

}