package com.example.dream_ias.activity.solution

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.databinding.ActivitySolutionforContestBinding
import com.example.dream_ias.util.App
import com.example.dream_ias.util.CommonUtil

class SolutionForContestActivity : BaseActivity<ActivitySolutionforContestBinding>() {
    private var pdfSolution: String = ""
    private var linkSolution: String = ""
    private var from: String = ""
    override fun getLayout(): ActivitySolutionforContestBinding {
        return ActivitySolutionforContestBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        println("=====from$from")
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Solution for Contest"
        if (from == "contest") {
            pdfSolution = App.app.prefManager.pdfSolution
            linkSolution = App.app.prefManager.linkVideoSolution
            println("=========pdfSolution${pdfSolution}")
            println("=========linkSolution${linkSolution}")
        } else if (from == "my_test_main_completed") {
            pdfSolution = App.app.prefManager.pdfSolution
            linkSolution = App.app.prefManager.linkVideoSolution
            println("=========my_test_main_completed${pdfSolution}")
            println("=========my_test_main_completed${linkSolution}")
        } else if (from == "my_practice_test_completed") {
            pdfSolution = App.app.prefManager.pdfSolution
            linkSolution = App.app.prefManager.linkVideoSolution
            println("=========my_practice_test_completed${pdfSolution}")
            println("=========my_practice_test_completed${linkSolution}")
        } else if (from == "practice") {
            pdfSolution = App.app.prefManager.pdfSolution
            linkSolution = App.app.prefManager.linkVideoSolution
            println("=========practice${pdfSolution}")
            println("=========practice${linkSolution}")
        }
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.imageView32.setOnClickListener {
            if (linkSolution.isNullOrEmpty()) {
                Toast.makeText(this, "No link Found", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkSolution))
                startActivity(intent)
            }

            //linkSolution = ""
        }
        binding.btnContinue.setOnClickListener {
            if (pdfSolution.isNullOrEmpty()) {
                Toast.makeText(this, "No Pdf Found", Toast.LENGTH_SHORT).show()
            } else {
                CommonUtil.startDownload(pdfSolution, this, "Solution PDF", "Solution PDF")
                Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
            }

            //pdfSolution = ""
        }
    }
}