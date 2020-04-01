package com.minutecodes.satelspeed.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PillView(context: Context, attr: AttributeSet) : View(context, attr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pill = Pill(paint)
    private val circularProgress = CircularProgress(paint)

    /**
     * Set the number of circles to light-up. Must not be less than 0 or greater than 4.
     */
    fun activate(progress: Int) {
        if (progress < 0 || progress > 4)
            throw RuntimeException("Progress must be from 0 to 4. Current progress = $progress")
    }

    override fun onDraw(canvas: Canvas) {
        drawPillView(canvas)
    }

    private fun drawPillView(canvas: Canvas) {
        pill.drawBody(canvas)
        circularProgress.drawCircularProgress(canvas)
    }

    private class Pill(private val paint: Paint) {
        private val radiusX = 300F
        private val radiusY = 300F
        private val bodyColor = Color.parseColor("#212121")
        private val body = RectF(600F, 200F, 540F, 400F)

        init {
            initPaintStyle()
        }

        fun drawBody(canvas: Canvas) {
            canvas.drawRoundRect(body, radiusX, radiusY, paint)
        }

        private fun initPaintStyle() = with(paint) {
            reset()
            color = bodyColor
            isAntiAlias = true
            style = Paint.Style.FILL
        }

    }

    private class CircularProgress(private val paint: Paint) {
        private val baseX = 570F
        private val baseY = 360F
        private val radius = 12F

        init {
            initPaintStyle()
        }

        fun drawCircularProgress(canvas: Canvas) {
            var yAxis = baseY

            for (i in 0 until 4) {
                canvas.drawCircle(baseX, yAxis, radius, paint)
                yAxis -= 40F
            }
        }

        private fun initPaintStyle() = with(paint) {
            reset()
            strokeWidth = 10F
            isAntiAlias = true
            color = Color.WHITE

            style = Paint.Style.FILL
            style = Paint.Style.FILL
        }

    }

}