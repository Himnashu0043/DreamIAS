package com.example.dream_ias.fragments.subScription

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.adapter.subScription.ActiveSubScriptionAdapter
import com.example.dream_ias.apiResponse.plan.SideSubscriptionRes
import com.example.dream_ias.databinding.FragmentActiveBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class ActiveFragment : BaseFragment<FragmentActiveBinding>() {
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<SideSubscriptionRes.Data>()
    override fun getLayout(): FragmentActiveBinding {
        return FragmentActiveBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        observer()

    }

    private fun subscriptionSideApi() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.subscriptionSide(token, false)
    }

    private fun observer() {
        viewModel._subscriptionSideliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data!!)
                        binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                        val adptr = ActiveSubScriptionAdapter(requireContext(), "",list)
                        binding.rcy.adapter = adptr
                        adptr?.notifyDataSetChanged()
                        if (list.isEmpty()) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.GONE
                        } else {
                            binding.lottieEmpty.visibility = View.GONE
                            binding.rcy.visibility = View.VISIBLE
                        }

                    } else {
                        Loaders.hide()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rcy.visibility = View.GONE
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
                    binding.rcy.visibility = View.GONE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        requireContext(),
                        it.throwable?.getError(requireContext())?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rcy.visibility = View.GONE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
    }

    private fun listener() {

    }

    override fun onResume() {
        super.onResume()
        subscriptionSideApi()
    }

}