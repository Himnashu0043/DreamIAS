package com.example.dream_ias.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable

class CustomDrawable(context: Context, private val cutAngleDegrees: Float) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    init {
        paint.color = Color.BLUE
    }

    override fun draw(canvas: Canvas) {
        // Draw the rectangle
        canvas.drawRect(bounds, paint)

        // Clip the canvas based on the specified angle
        canvas.save()
        canvas.clipPath(createCutPath(), Region.Op.DIFFERENCE)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.restore()
    }

    private fun createCutPath(): Path {
        val rect = RectF(bounds)
        path.reset()

        // Calculate the coordinates of the cut based on the angle
        val cutX = rect.width() * Math.cos(Math.toRadians(cutAngleDegrees.toDouble())).toFloat()
        val cutY = rect.height() * Math.sin(Math.toRadians(cutAngleDegrees.toDouble())).toFloat()

        // Move to the starting point
        path.moveTo(0f, 0f)

        // Draw a line to the cut point
        path.lineTo(cutX, cutY)

        // Draw a line to the opposite corner
        path.lineTo(rect.width(), rect.height())

        // Draw a line back to the starting point
        path.close()

        return path
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        // Assuming the drawable is fully opaque, you might need to modify this based on your use case
        return PixelFormat.OPAQUE
    }
}
