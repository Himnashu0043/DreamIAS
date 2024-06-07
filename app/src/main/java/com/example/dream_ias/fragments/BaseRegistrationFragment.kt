package com.example.dream_ias.fragments

import androidx.viewbinding.ViewBinding
import com.example.dream_ias.activity.RegistrationActivity

abstract class BaseRegistrationFragment<viewBinding : ViewBinding> : BaseFragment<viewBinding>() {

    protected val activity get() = requireActivity() as RegistrationActivity

    protected fun moveToNext() {
        activity.moveToNext()
    }

    abstract fun onContinue()
}