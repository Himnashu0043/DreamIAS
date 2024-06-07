package com.example.dream_ias.fragments.practiceTests

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.R
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.activity.testInfo.PracticeInstructionActivity
import com.example.dream_ias.adapter.BaseAdapter
import com.example.dream_ias.apiResponse.practiceTest.OngoingPracticeTestRes
import com.example.dream_ias.databinding.FragmentOngoingTestBinding
import com.example.dream_ias.databinding.OngoingPracticeItemBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel

class OngoingPracticeFragment: BaseFragment<FragmentOngoingTestBinding>() {
    private var examId: String = ""
    private lateinit var dialog: Dialog
    private lateinit var adapter: OngoingAdapter
    private var list = ArrayList<OngoingPracticeTestRes.Data.Result>()
    private var sub: Boolean = false
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private val activity: HomeActivity by lazy {
        requireActivity() as HomeActivity
    }

    override fun getLayout(): FragmentOngoingTestBinding {
        return FragmentOngoingTestBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examId = arguments?.getString("ongoing_examId").toString()
        println("========examId$examId")
        observer()

    }

    private fun ongoingList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.practiceTestListList(token, examId, "ongoing")
    }

    private fun observer() {
        viewModel._practiceTestliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data?.result!!)
                        adapter = OngoingAdapter()
                        binding.rvList.adapter = adapter
                        adapter.set(list)
                        App.app.prefManager.isPracticeSubS = it.value.data?.subscribed == true
                        sub = it.value.data?.subscribed == true
                        if (list.isEmpty()) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvList.visibility = View.INVISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.GONE
                            binding.rvList.visibility = View.VISIBLE
                        }

                    } else {
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rvList.visibility = View.INVISIBLE
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
                    binding.rvList.visibility = View.INVISIBLE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        it.throwable?.getError(requireContext())?.message.toString(),
                        R.color.red
                    )
                }
                else -> {
                    Loaders.hide()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                }
            }
        }
    }

    inner class OngoingAdapter : BaseAdapter<OngoingPracticeTestRes.Data.Result>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return OngoingViewHolder(
                OngoingPracticeItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as OngoingViewHolder).apply {
                binding.tvTitle.text = get(absoluteAdapterPosition).testData?.testTitle ?: ""
                binding.tvBottomTitle.text =
                    get(absoluteAdapterPosition).testData?.testsubTitle ?: ""
                binding.tvtotalMarks.text =
                    "${get(absoluteAdapterPosition).testData?.totalMarks ?: 0} Marks"
                binding.tvBottomQuestion.text =
                    "${get(absoluteAdapterPosition).testData?.questions ?: ""} Question"
                binding.tvBottomTime.text =
                    get(absoluteAdapterPosition).testData?.duration ?: ""
                binding.tvBottomNegativeMark.text =
                    "${get(absoluteAdapterPosition).negativeMarking?.marking ?: 0} Marks "
                if (!sub) {
                    subscriptionPopup()
                    binding.flPremium.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.blur_drawable)
                    binding.tvPremium.visibility = View.VISIBLE
                } else if (!App.app.prefManager.isAllSubS) {
                    subscriptionPopup()
                    binding.flPremium.setBackgroundResource(0)
                    binding.tvPremium.visibility = View.INVISIBLE
                } else {
                    binding.flPremium.setBackgroundResource(0)
                    binding.tvPremium.visibility = View.INVISIBLE
                }
            }
        }

        inner class OngoingViewHolder(val binding: OngoingPracticeItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.tvPremium.setOnClickListener {
                    activity.binding.viewPagerHome.setCurrentItem(2, false)
                    activity.binding.bottomNavigation.selectedItemId = R.id.menuNewSub
                    activity.binding.fabSubscription.visibility = View.GONE
                }
                binding.btnContinue.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            PracticeInstructionActivity::class.java
                        ).putExtra("id", get(absoluteAdapterPosition)._id)
                    )
                }
            }
        }
    }

    private fun subscriptionPopup() {
        dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.buy_subscription_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val yes = dialog.findViewById<AppCompatButton>(R.id.btnContinue)
        yes.setOnClickListener {
            dialog.dismiss()
            activity.binding.viewPagerHome.setCurrentItem(2, false)
            activity.binding.bottomNavigation.selectedItemId = R.id.menuNewSub
            activity.binding.fabSubscription.visibility = View.GONE
        }
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onResume() {
        super.onResume()
        ongoingList()
    }

}