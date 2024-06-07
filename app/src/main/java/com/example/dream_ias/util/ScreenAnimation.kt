package com.example.dream_ias.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.children

class ScreenAnimation private constructor() {

    private val infoList = arrayListOf<AnimationInfo>()

    fun add(vararg info: AnimationInfo): ScreenAnimation {
        for (i in info) {
            infoList.add(i)
        }
        return this
    }

    fun add(view: View, type: AnimationType): ScreenAnimation {
        infoList.add(AnimationInfo(view, type))
        return this
    }

    fun start() {
        var position = 0
        for (animationInfo in infoList) {
            position++
//                val animator: android.animation.Animator
            when (animationInfo.animationType) {

                AnimationType.FROM_BOTTOM -> {
                    animationInfo.view.translationY =
                        animationInfo.view.resources.displayMetrics.heightPixels.toFloat()
                    val slideAnimator = ObjectAnimator.ofFloat(
                        animationInfo.view, "translationY", animationInfo.view.translationY, 0f
                    )
                    slideAnimator.interpolator = AccelerateInterpolator(1F)
                    slideAnimator.duration = 1000
                    slideAnimator.startDelay = (position * 50).toLong()
                    slideAnimator.start()
                }

                AnimationType.FROM_LEFT -> {
                    animationInfo.view.translationX =
                        -animationInfo.view.resources.displayMetrics.widthPixels.toFloat()
                    val slideAnimator = ObjectAnimator.ofFloat(
                        animationInfo.view, "translationX", animationInfo.view.translationX, 0f
                    )
                    slideAnimator.interpolator = AccelerateInterpolator(1F)
                    slideAnimator.duration = 1000
                    slideAnimator.startDelay = (position * 100).toLong()
                    slideAnimator.start()
                }

                AnimationType.FROM_RIGHT -> {
                    animationInfo.view.translationX =
                        animationInfo.view.resources.displayMetrics.widthPixels.toFloat()
                    val slideAnimator = ObjectAnimator.ofFloat(
                        animationInfo.view, "translationX", animationInfo.view.translationX, 0f
                    )
                    slideAnimator.interpolator = AccelerateInterpolator(1F)
                    slideAnimator.duration = 1000
                    slideAnimator.startDelay = (position * 100).toLong()
                    slideAnimator.start()
                }

                AnimationType.FROM_TOP -> {
                    animationInfo.view.translationY =
                        -animationInfo.view.resources.displayMetrics.heightPixels.toFloat()
                    val slideAnimator = ObjectAnimator.ofFloat(
                        animationInfo.view, "translationY", animationInfo.view.translationY, 0f
                    )
                    slideAnimator.interpolator = AccelerateInterpolator(1F)
                    slideAnimator.duration = 1000
                    slideAnimator.startDelay = (position * 100).toLong()
                    slideAnimator.start()
                }
            }

        }
    }


    companion object {
        fun get(): ScreenAnimation {
            return ScreenAnimation()
        }
    }


}

data class AnimationInfo(val view: View, val animationType: AnimationType)
enum class AnimationType {
    FROM_BOTTOM,
    FROM_TOP,
    FROM_LEFT,
    FROM_RIGHT
}