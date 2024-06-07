package com.example.dream_ias.activity.viewAnalysis

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.adapter.viewAnalysis.ViewAnalysisAdapter
import com.example.dream_ias.apiResponse.practiceTest.ResultsPracticeRes
import com.example.dream_ias.apiResponse.results.ResultsListRes
import com.example.dream_ias.databinding.ActivityViewAnalysisBinding
import com.example.dream_ias.util.CommonUtil

class ViewAnalysisActivity : BaseActivity<ActivityViewAnalysisBinding>() {
    private var list = ArrayList<ResultsListRes.Data.Result.Answer>()
    private var list1 = ArrayList<ResultsPracticeRes.Data.Result.Answer>()
    private var from: String = ""
    override fun getLayout(): ActivityViewAnalysisBinding {
        return ActivityViewAnalysisBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        println("===========Viieee$from")
        if (from == "contest") {
            list =
                intent.getSerializableExtra("ans_list") as ArrayList<ResultsListRes.Data.Result.Answer>
            println("==========ansList$list")
        } else {
            list1 =
                intent.getSerializableExtra("ans_list") as ArrayList<ResultsPracticeRes.Data.Result.Answer>
            println("==========ansList$list1")
        }
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "ViewAnalysis"
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        binding.rcy.layoutManager = LinearLayoutManager(this)
        val adptr = ViewAnalysisAdapter(this,list,from,list1)
        binding.rcy.adapter = adptr
        adptr?.notifyDataSetChanged()

    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}