package com.example.dream_ias.fragments.subScription

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.R
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.activity.proMember.ProMemberActivity
import com.example.dream_ias.activity.wallet.WalletActivity
import com.example.dream_ias.adapter.ChildSubscriptionAdapter
import com.example.dream_ias.apiResponse.plan.HomePlanRes
import com.example.dream_ias.databinding.FragmentChildSubScriptionBinding
import com.example.dream_ias.fragments.BaseFragment
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ChildSubScriptionFragment : BaseFragment<FragmentChildSubScriptionBinding>()
    /*PaymentResultListener*/ {
    private var examId: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<HomePlanRes.Data>()
    private var list1 = ArrayList<HomePlanRes.Data.PlanFeature>()

    /*private var clickList = ArrayList<HomePlanRes.Data.ConfigurePrice>()*/
    private var planId: String = ""
    private var name: String = ""
    private var amount: Int = 0
    private var duration: String = ""
    private var validity: Int = 0
    private var wallet_amt: Int = 0
    private var adptr: ChildSubscriptionAdapter? = null
    private val activity: HomeActivity by lazy {
        requireActivity() as HomeActivity
    }

    override fun getLayout(): FragmentChildSubScriptionBinding {
        return FragmentChildSubScriptionBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examId = arguments?.getString("plan_id") ?: ""
        println("============planId$examId")

        initView()
        listener()

    }

    private fun initView() {
        //Checkout.preload(requireContext())
        observer()
    }

    private fun planList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.planHomeList(token, examId)
    }

    private fun getAmountAPI() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel._getAmount(token)
        }
    }

    private fun addPlan() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.addPlanHome(token, planId, name, examId, amount, duration, validity)
    }

    private fun observer() {
        viewModel._planListliveData.observe(requireActivity()) {
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
                        /* list1.clear()
                         if (it.value.data?.get(0)?.planFeatures?.size ?: 0 > 0) {
                             list1.addAll(it.value.data?.get(0)?.planFeatures!!)
                         } else {
                             Toast.makeText(
                                 requireContext(),
                                 "Feature Plan not Found",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }*/

                        binding.recyclerView.layoutManager =
                            LinearLayoutManager(requireContext())
                        adptr = ChildSubscriptionAdapter(requireContext(), list1)
                        binding.recyclerView.adapter = adptr
                        adptr?.notifyDataSetChanged()

                        if (list.isEmpty()) {
                            binding.nestedScrollView.visibility = View.GONE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.nestedScrollView.visibility = View.VISIBLE
                            binding.lottieEmpty.visibility = View.GONE
                            list1.clear()
                            list1.addAll(it.value.data?.get(0)?.planFeatures!!)
                            if (!it.value.data.isNullOrEmpty()) {
                                planId = it.value.data?.get(0)?._id ?: ""
                                if (!it.value.data?.get(0)?.configurePrice.isNullOrEmpty()) {
                                    if (it.value.data?.get(0)?.configurePrice?.size == 3) {
                                        binding.textView209.text =
                                            it.value.data?.get(0)?.configurePrice?.get(0)?.duration
                                                ?: ""
                                        binding.tvFreeBtm.text =
                                            "₹ ${it.value.data?.get(0)?.configurePrice?.get(0)?.price ?: 0}"

                                        binding.textView2091.text =
                                            it.value.data?.get(0)?.configurePrice?.get(1)?.duration
                                                ?: ""
                                        binding.tvBtmBasic.text =
                                            "₹ ${it.value.data?.get(0)?.configurePrice?.get(1)?.price ?: 0}"

                                        binding.textView20911.text =
                                            it.value.data?.get(0)?.configurePrice?.get(2)?.duration
                                                ?: ""
                                        binding.tvbtmPro.text =
                                            "₹ ${it.value.data?.get(0)?.configurePrice?.get(2)?.price ?: 0}"
                                    } else if (it.value.data?.get(0)?.configurePrice?.size == 2) {
                                        binding.textView209.text =
                                            it.value.data?.get(0)?.configurePrice?.get(0)?.duration
                                                ?: ""
                                        binding.tvFreeBtm.text =
                                            "₹ ${it.value.data?.get(0)?.configurePrice?.get(0)?.price ?: 0}"

                                        binding.textView2091.text =
                                            it.value.data?.get(0)?.configurePrice?.get(1)?.duration
                                                ?: ""
                                        binding.tvBtmBasic.text =
                                            "₹ ${it.value.data?.get(0)?.configurePrice?.get(1)?.price ?: 0}"
                                    } else if (it.value.data?.get(0)?.configurePrice?.size == 1) {
                                        binding.textView209.text =
                                            it.value.data?.get(0)?.configurePrice?.get(0)?.duration
                                                ?: ""
                                        binding.tvFreeBtm.text =
                                            "₹ ${it.value.data?.get(0)?.configurePrice?.get(0)?.price ?: 0}"
                                    }


                                }
                            }
                            getAmountAPI()


                        }
                    } else {
                        Loaders.hide()
                        binding.nestedScrollView.visibility = View.GONE
                        binding.lottieEmpty.visibility = View.VISIBLE
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    binding.nestedScrollView.visibility = View.GONE
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        binding.root.context,
                        it.throwable?.getError(requireContext())?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    binding.nestedScrollView.visibility = View.GONE
                    binding.lottieEmpty.visibility = View.VISIBLE
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
        viewModel._addPlanliveData.observe(requireActivity()) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        activity.binding.viewPagerHome.setCurrentItem(3,false)
                        activity.binding.bottomNavigation.selectedItemId = R.id.menuMyTests
                       /* planList()
                        binding.checkBox.isChecked = false
                        binding.checkBox1.isChecked = false
                        binding.checkBox11.isChecked = false
                        binding.btmBasic.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
                        binding.btmfree.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
                        binding.btmPro.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)*/
                        CommonUtil.showSnackBar(
                            requireContext(),
                            it.value.message.toString(),
                            android.R.color.holo_green_dark
                        )
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
                is Resource.Error -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        binding.root.context,
                        it.throwable?.getError(requireContext())?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    CommonUtil.showSnackBar(
                        requireContext(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
        viewModel._getAmountLiveData.observe(requireActivity()) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(requireContext())
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        binding.textView25.text = "₹ ${it.value.data?.amount ?: 0}"
                        wallet_amt = it.value.data?.amount ?: 0
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
                        it.throwable?.getError(requireContext())?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                else -> {
                    Loaders.hide()
                    requireActivity().shortToast("Something Went Wrong")
                }
            }
        }
    }

    private fun listener() {
        binding.textView231.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ProMemberActivity::class.java
                ).putExtra("exam_id", examId)
            )
        }
        binding.constraintLayout6.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    WalletActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
        binding.checkBox.setOnClickListener {
            name = list[0].configurePrice?.get(0)?._id ?: ""
            amount = list[0].configurePrice?.get(0)?.price ?: 0
            validity = list[0].configurePrice?.get(0)?.validity ?: 0
            duration = list[0].configurePrice?.get(0)?.duration ?: ""
            binding.checkBox.isChecked = true
            binding.checkBox1.isChecked = false
            binding.checkBox11.isChecked = false
            binding.btmBasic.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
            binding.btmfree.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_3_bg)
            binding.btmPro.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)

        }
        binding.checkBox1.setOnClickListener {
            name = list[0].configurePrice?.get(1)?._id ?: ""
            amount = list[0].configurePrice?.get(1)?.price ?: 0
            validity = list[0].configurePrice?.get(1)?.validity ?: 0
            duration = list[0].configurePrice?.get(1)?.duration ?: ""
            binding.checkBox.isChecked = false
            binding.checkBox1.isChecked = true
            binding.checkBox11.isChecked = false
            binding.btmBasic.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.light_green_stroke_bg)
            binding.btmfree.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
            binding.btmPro.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)

        }
        binding.checkBox11.setOnClickListener {
            name = list[0].configurePrice?.get(2)?._id ?: ""
            amount = list[0].configurePrice?.get(2)?.price ?: 0
            validity = list[0].configurePrice?.get(2)?.validity ?: 0
            duration = list[0].configurePrice?.get(2)?.duration ?: ""
            binding.checkBox.isChecked = false
            binding.checkBox1.isChecked = false
            binding.checkBox11.isChecked = true
            binding.btmBasic.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
            binding.btmfree.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
            binding.btmPro.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.light_yellow_stroke_bg)
        }
        binding.radioButton.setOnClickListener {
            if (wallet_amt < amount) {
                Toast.makeText(
                    requireContext(),
                    "Not Sufficient Balance in Wallet",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                amount = amount
            }
            println("======click$amount")

        }
        binding.btnContinue.setOnClickListener {
            if (planId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "PlanId not Found", Toast.LENGTH_SHORT).show()
            } else if (wallet_amt < amount) {
                Toast.makeText(requireContext(), "Please add Amount in Wallet", Toast.LENGTH_SHORT)
                    .show()
            } else if (!binding.checkBox.isChecked && !binding.checkBox1.isChecked && !binding.checkBox11.isChecked) {
                Toast.makeText(requireContext(), "Please Select Subscription Plan", Toast.LENGTH_SHORT).show()
            } else if (!binding.radioButton.isChecked) {
                Toast.makeText(requireContext(), "Please Select Wallet", Toast.LENGTH_SHORT).show()
            } else {
                addPlan()
                // initiateRazorpayPayment()
            }

        }
    }

    private fun paymentPopup() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.payment_succesfull_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val yes = dialog.findViewById<AppCompatButton>(R.id.btnContinue)

        yes.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireContext(), ProMemberActivity::class.java))
        }

        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        planList()
        binding.checkBox.isChecked = false
        binding.checkBox1.isChecked = false
        binding.checkBox11.isChecked = false
        binding.radioButton.isChecked = false
        binding.btmBasic.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
        binding.btmfree.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
        binding.btmPro.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_bg)
    }
  /*  private fun initiateRazorpayPayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_HAjmA7HGJpglFK")
        try {
            val options = JSONObject()
            options.put("name", "Dream IAS")
            options.put("description", "Payment for service")
            options.put("currency", "INR")
            options.put("amount", "${amount * 100}")

            *//*  val paymentObject = JSONObject()
              paymentObject.put("order_id", "YOUR_ORDER_ID")
              options.put("order_id", paymentObject)*//*

            checkout.open(requireActivity(), options)
        } catch (e: Exception) {
            e.printStackTrace()
            println("===============${e.printStackTrace()}")
        }

    }*/

 /*   override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.e("PaymentError", "Payment error: $p1")
        println("=======================$p1")
    }*/

    /*  override fun onClick(
          msg: ArrayList<HomePlanRes.Data.ConfigurePrice>,
          nameTitle: String,
          position: Int
      ) {
          binding.textView209.text = ""
          binding.tvFreeBtm.text =
              ""
          binding.textView2091.text = ""
          binding.tvBtmBasic.text =
              ""

          binding.textView20911.text = ""
          binding.tvbtmPro.text =
              ""
          clickList = msg
          name = nameTitle
          if (!msg.isNullOrEmpty()) {
              if (msg.size == 3) {
                  binding.textView209.text =
                      msg[0].duration
                          ?: ""
                  binding.tvFreeBtm.text =
                      "₹ ${msg[0].price ?: 0}"

                  binding.textView2091.text =
                      msg[1].duration
                          ?: ""
                  binding.tvBtmBasic.text =
                      "₹ ${msg[1].price ?: 0}"

                  binding.textView20911.text =
                      msg[2].duration
                          ?: ""
                  binding.tvbtmPro.text =
                      "₹ ${msg[2].price ?: 0}"
              } else if (msg.size == 2) {
                  binding.textView209.text =
                      msg[0].duration
                          ?: ""
                  binding.tvFreeBtm.text =
                      "₹ ${msg[0].price ?: 0}"

                  binding.textView2091.text =
                      msg[1].duration
                          ?: ""
                  binding.tvBtmBasic.text =
                      "₹ ${msg[1].price ?: 0}"
              } else if (msg.size == 1) {
                  binding.textView209.text =
                      msg[0].duration
                          ?: ""
                  binding.tvFreeBtm.text =
                      "₹ ${msg[0].price ?: 0}"
              }
          }
          println("========${clickList}...................${nameTitle}")
      }*/
}