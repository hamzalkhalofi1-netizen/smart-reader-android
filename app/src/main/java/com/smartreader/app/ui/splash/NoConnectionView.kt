package com.smartreader.app.ui.splash

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Self-contained custom View that draws three animated signal bars.
 * No Lottie or external library needed — pure Canvas + ValueAnimator.
 */
class NoConnectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val activePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF7C3AED.toInt()
        style = Paint.Style.FILL
    }
    private val inactivePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF2D3748.toInt()
        style = Paint.Style.FILL
    }

    private val barCount   = 4
    private val barFills   = FloatArray(barCount) { 0.15f + it * 0.2f }
    private val animators  = mutableListOf<ValueAnimator>()

    override fun onDraw(canvas: Canvas) {
        if (width == 0 || height == 0) return
        val totalGaps = barCount + 1
        val barW      = width  / (barCount + totalGaps * 0.4f)
        val gap       = (width - barW * barCount) / (barCount + 1)

        for (i in 0 until barCount) {
            val left  = gap  + i * (barW + gap)
            val right = left + barW
            val barH  = height * barFills[i]
            val top   = height - barH
            val r     = barW / 3f

            // Grey full-height track
            canvas.drawRoundRect(RectF(left, 0f, right, height.toFloat()), r, r, inactivePaint)
            // Coloured active fill
            canvas.drawRoundRect(RectF(left, top, right, height.toFloat()), r, r, activePaint)
        }
    }

    fun startAnimation() {
        stopAnimation()
        for (i in 0 until barCount) {
            val minH = 0.15f + i * 0.18f
            val maxH = minH  + 0.45f
            val anim = ValueAnimator.ofFloat(minH, maxH.coerceAtMost(1f)).apply {
                duration    = 500L + i * 80L
                startDelay  = i * 120L
                repeatCount = ValueAnimator.INFINITE
                repeatMode  = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    barFills[i] = it.animatedValue as Float
                    invalidate()
                }
            }
            animators += anim
            anim.start()
        }
    }

    fun stopAnimation() {
        animators.forEach { it.cancel() }
        animators.clear()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }
}
