package com.example.dream_ias.activity.dreamIas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.subscription.SubscriptionActivity
import com.example.dream_ias.activity.wallet.WalletActivity
import com.example.dream_ias.adapter.ProMemberAdapter
import com.example.dream_ias.apiResponse.plan.HomePlanRes
import com.example.dream_ias.databinding.ActivityDreamIasBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DreamIasActivity : BaseActivity<ActivityDreamIasBinding>()/*, PaymentResultListener*/ {
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var list = ArrayList<HomePlanRes.Data>()
    private var list1 = ArrayList<HomePlanRes.Data.PlanFeature>()
    private var planId: String = ""
    private var name: String = ""
    private var amount: Int = 0
    private var duration: String = ""
    private var validity: Int = 0
    private var wallet_amt: Int = 0
    override fun getLayout(): ActivityDreamIasBinding {
        return ActivityDreamIasBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()

    }

    private fun initView() {
        observer()
        /*binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adptr = DreamIASAdapter(this)
        binding.recyclerView.adapter = adptr
        adptr!!.notifyDataSetChanged()*/

    }

    private fun planList() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.planAllAccessHomeList(token, "all")
    }

    private fun getAmountAPI() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel._getAmount(token)
        }
    }



    private fun addPlan() {
        val token = App.app.prefManager.loginData?.jwtToken.toString()
        viewModel.addPlanHome(token, planId, name, "all", amount, duration, validity)
    }

    private fun observer() {
        viewModel._planAllAccessListliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        list.clear()
                        list.addAll(it.value.data!!)

                        if (list.isEmpty()) {
                            binding.constraintLayout16.visibility = View.GONE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.constraintLayout16.visibility = View.VISIBLE
                            binding.lottieEmpty.visibility = View.GONE
                            list1.clear()
                            list1.addAll(it.value.data?.get(0)?.planFeatures!!)
                            binding.recyclerView.layoutManager = LinearLayoutManager(this)
                            val adptr = ProMemberAdapter(this, list1)
                            binding.recyclerView.adapter = adptr
                            adptr?.notifyDataSetChanged()
                            planId = it.value.data?.get(0)?._id ?: ""
                            name = it.value.data?.get(0)?.configurePrice?.get(0)?._id ?: ""
                            validity = it.value.data?.get(0)?.configurePrice?.get(0)?.validity ?: 0
                            duration = it.value.data?.get(0)?.configurePrice?.get(0)?.duration ?: ""
                            binding.textView2091.text =
                                it.value.data?.get(0)?.configurePrice?.get(0)?.duration ?: ""
                            binding.tvBtmBasic.text =
                                "₹ ${it.value.data?.get(0)?.configurePrice?.get(0)?.price ?: 0}"
                            //planId = it.value.data?.get(0)?.configurePrice?.get(0)?._id ?: ""
                            amount = it.value.data?.get(0)?.configurePrice?.get(0)?.price ?: 0
                            getAmountAPI()
                        }
                    } else {
                        Loaders.hide()
                        binding.constraintLayout16.visibility = View.GONE
                        binding.lottieEmpty.visibility = View.VISIBLE
                        CommonUtil.showSnackBar(
                            this,
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Error ->{
                    CommonUtil.showSnackBar(
                        this,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
                    )
                }
                is Resource.Failure -> {
                    Loaders.hide()
                    binding.constraintLayout16.visibility = View.GONE
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    Loaders.hide()
                    binding.constraintLayout16.visibility = View.GONE
                    binding.lottieEmpty.visibility = View.VISIBLE
                    CommonUtil.showSnackBar(
                        this,
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
            }
        }
        viewModel._addPlanliveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
                    Log.d("TAG", "observer: Loading")
                }
                is Resource.Success -> {
                    Loaders.hide()
                    if (it.value?.status == Constants.SUCCESS) {
                        CommonUtil.showSnackBar(
                            this,
                            it.value.message.toString(),
                            android.R.color.holo_green_dark
                        )
                        startActivity(Intent(this, SubscriptionActivity::class.java))
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
                is Resource.Error ->{
                    CommonUtil.showSnackBar(
                        this,
                        it.throwable?.getError(this)?.message.toString(),
                        android.R.color.holo_red_light
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
        viewModel._getAmountLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Loaders.show(this)
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
    private fun listener() {
        binding.imageView7.setOnClickListener {
            finish()
        }
        binding.constraintLayout6.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    WalletActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
        /* binding.checkBox.setOnClickListener {
             binding.checkBox.isChecked = true
             binding.checkBox1.isChecked = false
             binding.checkBox11.isChecked = false
             binding.btmBasic.background =
                 ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
             binding.btmfree.background =
                 ContextCompat.getDrawable(this, R.drawable.black_stroke_3_bg)
             binding.btmPro.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)

         }
         binding.checkBox1.setOnClickListener {
             binding.checkBox.isChecked = false
             binding.checkBox1.isChecked = true
             binding.checkBox11.isChecked = false
             binding.btmBasic.background =
                 ContextCompat.getDrawable(this, R.drawable.light_green_stroke_bg)
             binding.btmfree.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
             binding.btmPro.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)

         }
         binding.checkBox11.setOnClickListener {
             binding.checkBox.isChecked = false
             binding.checkBox1.isChecked = false
             binding.checkBox11.isChecked = true
             binding.btmBasic.background =
                 ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
             binding.btmfree.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
             binding.btmPro.background =
                 ContextCompat.getDrawable(this, R.drawable.light_yellow_stroke_bg)
         }*/
//        binding.btnContinue.setOnClickListener {
//            // paymentPopup()
//        }
        binding.radioButton.setOnClickListener {
            if (wallet_amt < amount) {
                Toast.makeText(this, "Not Sufficient balance in Wallet", Toast.LENGTH_SHORT).show()
            } else {
                amount = amount
            }
            println("======click$amount")

        }
        binding.btnContinue.setOnClickListener {
           /* if (planId.isNullOrEmpty()) {
                Toast.makeText(this, "PlanId not Found", Toast.LENGTH_SHORT).show()
            } else {
                val token = App.app.prefManager.loginData?.jwtToken.toString()
                viewModel.addPlanHome(token, planId, name, "all", amount, duration, validity)
            }*/
            if (planId.isNullOrEmpty()) {
                Toast.makeText(this, "PlanId not Found", Toast.LENGTH_SHORT).show()
            } else if (wallet_amt < amount) {
                Toast.makeText(this, "Please add Amount in Wallet", Toast.LENGTH_SHORT).show()
            } else if (!binding.radioButton.isChecked) {
                Toast.makeText(this, "Please Select Wallet", Toast.LENGTH_SHORT).show()
            } else {
                addPlan()
                // initiateRazorpayPayment()
            }
        }
    }

    /*  private fun initiateRazorpayPayment() {
          val checkout = Checkout()
          checkout.setKeyID("rzp_test_HAjmA7HGJpglFK")


          try {
              val options = JSONObject()
              options.put("name", "Dream IAS")
              options.put("description", "Payment for service")
              options.put("currency", "INR")
              options.put("amount", amount)

              val paymentObject = JSONObject()
              paymentObject.put("order_id", "YOUR_ORDER_ID")
              options.put("order_id", paymentObject)

              checkout.open(this, options)
          } catch (e: Exception) {
              e.printStackTrace()
          }

      }*/

//    override fun onPaymentSuccess(p0: String?) {
//        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onPaymentError(p0: Int, p1: String?) {
//        Log.e("PaymentError", "Payment error: $p1")
//    }


    /* private fun paymentPopup() {
         val dialog = this.let { Dialog(this) }
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog.setContentView(R.layout.payment_succesfull_popup)
         dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         dialog.setCanceledOnTouchOutside(false)
         dialog.show()
         val yes = dialog.findViewById<AppCompatButton>(R.id.btnContinue)

         yes.setOnClickListener {
             dialog.dismiss()
             startActivity(
                 Intent(
                     this,
                     HomeActivity::class.java
                 ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
             )
             finishAffinity()
         }

         val window = dialog.window
         if (window != null) {
             window.setLayout(
                 WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
             )
         }

     }*/
    override fun onResume() {
        super.onResume()
        planList()
    }
}