package com.example.longpressprogressivebutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.animation.doOnEnd

class ProgressiveButtonOval @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatButton(context, attrs) {


    init {
        background = ColorDrawable(Color.TRANSPARENT)
    }


    private var centerY = 0f
    lateinit var bounds: Rect
    private var x: Int = 0
    private var y: Int = 0
    private var progressPY1: Float = 0F
    private var quadProgressPY1: Float = 0F

    private var progressWidth: Float = 0F
    private val path: Path = Path()
    private val progressPath: Path = Path()

    // removed-listener true when animator listener got removed
    private var removedListener = true

    // max distance for progressbar to animate
    // its calculated in onSizedChanged()
    private var max = 0F

    private var length:Float = 0F

    // progressive position
    // invalidate each time position update from value animator
    private var stopX = 1F
        private set(value) {
            field = value
            paintOuterCircle.pathEffect = createPathEffect(length,value,0.0F)
            postInvalidate()
        }


    // interface declaration
    private var progressiveButton: ProgressiveButton? = null

    // value animator listener
    private val animatorListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val animateFloat: Float = animation?.animatedValue as Float
        stopX = animateFloat
        removedListener = false
    }

    // value animator to animate the whole progress path distance
    private val animator = ValueAnimator.ofFloat(1f, 0f).apply {
        duration = 1000 * ANIMATION_SPEED_FACTOR
        addUpdateListener(animatorListener)
        doOnEnd {
            if (max == stopX) {
                progressiveButton?.onProgressFinished()
                this.removeUpdateListener(animatorListener)
                removedListener = true
            }
        }
    }

    private var CIRCLECOLOR = BUTTON_COLOR
        private set(value) {
            field = value
            paintChange()
        }

    // circle paint object
    private val paintInnerCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(CIRCLECOLOR)
    }

    private fun paintChange() {
        paintInnerCircle.apply {
            color = Color.parseColor(CIRCLECOLOR)
        }
        invalidate()
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
        textSize = TEXT_SIZE

    }

    // touch event
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) return true;
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


        val diffH = height * 0.08F

        progressPY1 = height * 0.50F
        val progressPY2 = progressPY1 + diffH
        val bgPY = progressPY2 + diffH * 0.5F

        quadProgressPY1 = progressPY1 - width / 4
        val quadBgPY3 = bgPY - width / 4


        progressWidth = width.toFloat()
        val bgWidth = width.toFloat()
        val bgHeight = height.toFloat()

        centerY = (height.toFloat() / 2)

        bounds = Rect()



        textPaint.getTextBounds(text.toString(), 0, text.length, bounds)
        x = width / 2 - bounds.width() / 2
        y = height / 2 - bounds.height() / 2

        progressPath.apply {
            moveTo(0F, progressPY1)
            quadTo(
                progressWidth * 0.5F, quadProgressPY1 ,
                progressWidth , progressPY1
            )
        }
        val measure = PathMeasure(progressPath, false)
        length = measure.length
        paintOuterCircle.pathEffect = createPathEffect(length,1.0F,0.0F)

        // Bg
        path.apply {
            moveTo(0F, bgHeight);
            lineTo(0F, bgPY)
            quadTo(
                bgWidth * 0.5F, quadBgPY3,
                bgWidth, bgPY
            )
            lineTo(bgWidth, bgHeight)
            close()
        }
    }
    fun reset() {
        stopX = 1F
        if (removedListener) animator.addUpdateListener(animatorListener)
        isEnabled = true
    }
    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect? {
        return DashPathEffect(
            floatArrayOf(pathLength, pathLength),
            (phase * pathLength).coerceAtLeast(offset)
        )
    }


    override fun setEnabled(enabled: Boolean) {
        CIRCLECOLOR = if (enabled) {
            BUTTON_COLOR
        } else {
            BUTTON_COLOR_DISABLED
        }
        progressiveButton?.onEnableDisable(enabled)
        super.setEnabled(enabled)
    }

    // on draw
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawPath(path, paintInnerCircle)
            drawPath(
                progressPath,
                paintOuterCircle
            )

            drawText(
                text.toString(), x.toFloat(),
                centerY + TEXT_SIZE / 3, textPaint
            )
        }


    }

    // interface to get the call back on finish
    interface ProgressiveButton {
        fun onProgressFinished()
        fun onEnableDisable(isEnable: Boolean) {}
    }

    // method for reference the finished callback
    fun onEvent(progressiveButton: ProgressiveButton) {
        this.progressiveButton = progressiveButton
    }

    // static variables
    private companion object {
        const val ANIMATION_SPEED_FACTOR: Long = 1L
        const val PROGRESS_WIDTH = 30F
        const val PROGRESS_COLOR = "#2fa363"
        const val BUTTON_COLOR = "#FFC107"
        const val BUTTON_COLOR_DISABLED = "#7E7E7E"
        const val TEXT_SIZE = 60f;
    }

}
