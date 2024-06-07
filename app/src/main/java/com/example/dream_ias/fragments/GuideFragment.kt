package com.example.dream_ias.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.adapter.guide.GuideAdapter
import com.example.dream_ias.apiResponse.TestContestListRes
import com.example.dream_ias.databinding.FragmentGuideBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class GuideFragment : BaseFragment<FragmentGuideBinding>() {
    private var id: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<TestContestListRes.Data.Result.ConfigureGuide>()

    override fun getLayout(): FragmentGuideBinding {
        return FragmentGuideBinding.inflate(layoutInflater)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString("id").toString()
        println("=======conet$id")
        observer()
    }

    private fun myTestContestList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel._myTestContest(token, id)
    }

    private fun observer() {
        viewModel._myTestContestLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result?.first()?.configureGuide!!)
                        binding.rcyGuid.layoutManager = LinearLayoutManager(requireContext())
                        val adptr = GuideAdapter(requireContext(), list)
                        binding.rcyGuid.adapter = adptr
                        adptr!!.notifyDataSetChanged()
                        if (list.isEmpty()) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.mainLay.visibility = View.GONE
                        } else {
                            binding.lottieEmpty.visibility = View.GONE
                            binding.mainLay.visibility = View.VISIBLE
                        }

                    } else {
                        Loaders.hide()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.mainLay.visibility = View.GONE
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.mainLay.visibility = View.GONE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.mainLay.visibility = View.GONE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        it.throwable?.getError(requireContext())?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.mainLay.visibility = View.GONE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        myTestContestList()
    }


}