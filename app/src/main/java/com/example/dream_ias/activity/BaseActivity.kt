package com.example.dream_ias.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.dream_ias.util.CommonUtil

abstract class BaseActivity<viewBinding:ViewBinding>:AppCompatActivity() {

    private var _binding:viewBinding? = null
    val binding get() = _binding!!
    abstract fun getLayout():viewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getLayout()
        setContentView(binding.root)
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
    }

}