package com.example.longpressprogressivebutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.animation.doOnEnd

class ProgressiveButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {


    // removedlistener true when animator listener got removed
    private var removedListener = true

    // max distance for progressbar to animate
    // its calculated in onSizedChanged()
    private var max = 0F

    // progressive position
    // invalidate each time position update from value animator
    private var stopX = 0F
        private set(value) {
            field = value
            postInvalidate()
        }

    // interface declaration
    private lateinit var progressiveButton: ProgressiveButton

    // value animator listener
    private val animatorListener =
        ValueAnimator.AnimatorUpdateListener { animation ->
            val animateFloat: Float = animation?.animatedValue as Float
            stopX = (animateFloat * max)
        }

    // value animator to animate the whole progress path distance
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 500 * ANIMATION_SPEED_FACTOR
        addUpdateListener(animatorListener)
        doOnEnd {
            if (max == stopX) {
                progressiveButton.onProgressFinished()
                this.removeUpdateListener(animatorListener)
            }
        }
    }

    // progress paint object
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(PROGRESS_COLOR)
    }

    // touch event
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (max != stopX) animator.start()
                performClick()
            }
            MotionEvent.ACTION_UP -> {
                if (max != stopX) animator.reverse()
                performClick()
            }
        }
        return true
    }


    // dependency function by onTouchEvent for
    // accessibility improvement
    override fun performClick(): Boolean {
        return super.performClick()
    }

    // adding listener if removed on attach window
    override fun onAttachedToWindow() {
        if (removedListener) {
            animator.addUpdateListener(animatorListener)
            removedListener = true
        }
        super.onAttachedToWindow()
    }

    // removing listener when detach window
    override fun onDetachedFromWindow() {
        animator.removeUpdateListener(animatorListener)
        removedListener = true
        super.onDetachedFromWindow()
    }

    // calculate max path distance for progress
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        max = width.toFloat()
    }

    // on draaw
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0F, 0F, stopX, PROGRESS_WIDTH, paintProgress)
    }

    // interface to get the call back on finish
    interface ProgressiveButton {
        fun onProgressFinished()
    }

    // method for reference the finished callback
    fun onEvent(progressiveButton: ProgressiveButton) {
        this.progressiveButton = progressiveButton
    }

    // static variables
    private companion object {
        const val ANIMATION_SPEED_FACTOR: Long = 2L
        const val PROGRESS_WIDTH = 35F
        const val PROGRESS_COLOR = "#2fa363"
    }

}
