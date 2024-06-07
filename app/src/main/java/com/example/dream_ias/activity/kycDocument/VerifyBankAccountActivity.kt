package com.example.dream_ias.activity.kycDocument

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.subscription.SubscriptionActivity
import com.example.dream_ias.adapter.ProMemberAdapter
import com.example.dream_ias.apiResponse.razorpay.BankAccount
import com.example.dream_ias.apiResponse.razorpay.Contact
import com.example.dream_ias.apiResponse.razorpay.FundAccount
import com.example.dream_ias.apiResponse.razorpay.RazorPayPostModel
import com.example.dream_ias.apiResponse.razorpay.RazorPayValidationRes
import com.example.dream_ias.databinding.ActivityVerifyBankAccountBinding
import com.example.dream_ias.network.RazorApi
import com.example.dream_ias.network.RazorPayRestClient
import com.example.dream_ias.util.App
import com.example.dream_ias.util.CommonUtil
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.util.Constants
import com.example.dream_ias.util.ErrorUtil
import com.example.dream_ias.util.ErrorUtil.getError
import com.example.dream_ias.util.Loaders
import com.example.dream_ias.util.Resource
import com.example.dream_ias.viewModel.AuthViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyBankAccountActivity : BaseActivity<ActivityVerifyBankAccountBinding>() {
    var authHeader: String = ""
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    override fun getLayout(): ActivityVerifyBankAccountBinding {
        return ActivityVerifyBankAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Verify Bank Account"
        val apiKey = "rzp_test_HAjmA7HGJpglFK"
        // val apiSecret = "YOUR_API_SECRET"
        val credentials = "$apiKey"
        authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)


    }

    private fun listener() {
       /* binding.btnContinue.setOnClickListener {
            val payoutRequest = RazorPayValidationRes(
                account_number = binding.editText.text.trim().toString(),
                fund_account = FundAccount(
                    account_type = "bank_account",
                    bank_account = BankAccount(
                        name = "Test",
                        ifsc = binding.editDate.text.trim().toString(),
                        account_number = binding.editText.text.trim().toString()
                    ),
                    contact = Contact(
                        name = "Account Holder Name",
                        email = "email@example.com",
                        contact = "phone_number"
                    )
                ),
                amount = 0,  // Amount in paise
                currency = "INR",
                mode = "IMPS",
                purpose = "payout"
            )
            val call = RazorPayRestClient.instance.razorPayValidation(authHeader, payoutRequest)
            call.enqu(object : Callback<RazorPayPostModel> {
                override fun onResponse(call: Call<RazorPayPostModel>, response: Response<RazorPayPostModel>) {
                    if (response.isSuccessful) {
                        val payoutResponse = response.body()
                        Log.d("MainActivity", "Payout successful: $payoutResponse")
                    } else {
                        Log.e("MainActivity", "Payout failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<RazorPayPostModel>, t: Throwable) {
                    Log.e("MainActivity", "Error: ${t.message}")
                }
            })
        }*/
        binding.btnContinue.setOnClickListener {
            finish()
        }

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}