package com.example.longpressprogressivebutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.animation.doOnEnd

class ProgressiveButtonCircular @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {


    init {
        background = ColorDrawable(Color.TRANSPARENT)
    }

    lateinit var outerCircle: RectF
    private var innerCircle = 0f
    private var centerX = 0f
    private var centerY = 0f
    lateinit var bounds: Rect
        private var x: Int = 0
        private var y: Int = 0

    // removedlistener true when animator listener got removed
    private var removedListener = true

    // max distance for progressbar to animate
    // its calculated in onSizedChanged()
    private var max = 360F

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
    private val animatorListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val animateFloat: Float = animation?.animatedValue as Float
        stopX = (animateFloat * max)
    }

    // value animator to animate the whole progress path distance
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000 * ANIMATION_SPEED_FACTOR
        addUpdateListener(animatorListener)
        doOnEnd {
            if (max == stopX) {
                progressiveButton.onProgressFinished()
                this.removeUpdateListener(animatorListener)
            }
        }
    }

    // circle paint object
    private val paintInnerCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(CIRCLE_COLOR)
    }

    // progress paint object
    private val paintOuterCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(PROGRESS_COLOR)
        strokeCap = Paint.Cap.ROUND
        strokeWidth = PROGRESS_WIDTH
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
    }

    //text paint
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(PROGRESS_COLOR)
        textSize = 40F

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
        centerX = (width.toFloat() / 2)
        centerY = (height.toFloat() / 2)
        innerCircle = centerY - PROGRESS_WIDTH
        outerCircle = RectF(
            PROGRESS_WIDTH / 2,
            PROGRESS_WIDTH / 2,
            width.toFloat() - PROGRESS_WIDTH / 2,
            height.toFloat() - PROGRESS_WIDTH / 2
        )
        bounds = Rect()
        textPaint.getTextBounds(text.toString(), 0, text.length, bounds)
        x = width / 2 - bounds.width() / 2
        y = height / 2 - bounds.height() / 2
    }


    // on draw
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        canvas?.drawCircle(centerX, centerY, innerCircle, paintInnerCircle)
        canvas?.save()
        canvas?.drawArc(outerCircle, -90f, stopX, false, paintOuterCircle)
        canvas?.drawText(text.toString(), x.toFloat(),centerY, textPaint)

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
        const val PROGRESS_WIDTH = 30F
        const val PROGRESS_COLOR = "#2fa363"
        const val CIRCLE_COLOR = "#FFC107"

        const val MIN_SIZE = 40f;
    }

}
