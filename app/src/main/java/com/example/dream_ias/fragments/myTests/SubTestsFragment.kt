package com.example.dream_ias.fragments.myTests

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dream_ias.databinding.FragmentSubTestsBinding
import com.example.dream_ias.fragments.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class SubTestsFragment : BaseFragment<FragmentSubTestsBinding>() {
private var examid: String = ""
    override fun getLayout(): FragmentSubTestsBinding {
        return FragmentSubTestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter()
        examid = arguments?.getString("test_exam_id").toString()
        println("=======Test$id")
//        binding.viewPager.isUserInputEnabled = false
//        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ongoing"
                1 -> "Upcoming"
                else -> "Completed"
            }
        }.attach()
    }

    /**
     * Created only for sub fragments
     * To change viewPager Position
     * */
    fun moveToPosition(position: Int) {
        binding.viewPager.currentItem = position
    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OngoingTestFragment().apply {
                    arguments = Bundle().apply {
                        putString("test_ongoing_exam_id", examid)
                    }
                }
                1 -> UpcomingTestsFragment().apply {
                    arguments = Bundle().apply {
                        putString("test_upcoming_exam_id", examid)
                    }
                }
                else -> CompletedTestFragment().apply {
                    arguments = Bundle().apply {
                        putString("test_completed_exam_id", examid)
                    }
                }
            }
        }

    }

}