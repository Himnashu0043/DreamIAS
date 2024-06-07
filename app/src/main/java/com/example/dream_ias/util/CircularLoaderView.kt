package com.example.dream_ias.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

class CircularLoaderView : View {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rotationAngle = 0f
    private val animation: Animation

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        circlePaint.color = Color.parseColor("#4CAF50")
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 10f

        // Animation setup
        animation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.interpolator = LinearInterpolator()
        animation.duration = 1000
        animation.repeatCount = Animation.INFINITE
        startAnimation(animation)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2
        val radius = (Math.min(width, height) - circlePaint.strokeWidth) / 2

        // Draw the circular loader
        canvas.drawCircle(centerX, centerY, radius, circlePaint)
    }
}
