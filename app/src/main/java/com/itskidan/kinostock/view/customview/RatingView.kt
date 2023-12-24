package com.itskidan.kinostock.view.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.itskidan.kinostock.R
import kotlin.math.min

class RatingView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {
    // Oval for drawing segments of the progress bar
    private val oval = RectF()

    // View CENTER coordinates as well as RADIUS
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    // WIDTH of the progress line
    private var stroke = 10f

    // Progress VALUE from 0 - 100
    private var progress = 50

    // TEXT SIZE values inside the ring
    private var scaleSize = 60f

    // TEXT TYPEFACE values
    //val robotoMediumTypeface = ResourcesCompat.getFont(context, R.font.roboto_medium)

    // PAINTs for our figures
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint

    init {
        // Get attributes and set them in the appropriate fields
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingView, 0, 0)
        try {
            stroke = attributes.getFloat(R.styleable.RatingView_stroke, stroke)
            progress = attributes.getInt(R.styleable.RatingView_progress, progress)
            scaleSize = attributes.getFloat(R.styleable.RatingView_scale_size, scaleSize)
        } finally {
            attributes.recycle()
        }
        // Initialize the initial colors
        initPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw the ring and background
        drawRating(canvas)
        // Draw numbers
        drawText(canvas)
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }

    private fun drawRating(canvas: Canvas) {
        // Here we can adjust the size of our ring
        val scale = radius * 0.8f
        // Save CANVAS
        canvas.save()
        // We move the zero coordinates of the canvas to the center
        canvas.translate(centerX, centerY)
        // We set the dimensions to our oval
        oval.set(0f - scale, 0f - scale, scale, scale)
        // Draw the background (It is advisable to draw it once in a bitmap, since it is static)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        // We draw “arches”, our ring will consist of them + we have a special method here
        canvas.drawArc(oval, -90f, convertProgressToDegrees(progress), false, strokePaint)
        // Restoring canvas
        canvas.restore()
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * 3.6f

    private fun drawText(canvas: Canvas) {
        // We format the text so that we display a fractional number with one digit after the period
        val message = String.format("%.1f", progress / 10f)
        // Get the width and height of the text to compensate when rendering so the text is
        // exactly in the center
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        // Draw our text
        canvas.drawText(message, centerX - advance / 2, centerY + advance / 4, digitPaint)
    }

    private fun initPaint() {
        // Paint for rings
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            // We put the value from the class field here, because our colors will change
            strokeWidth = stroke
            // We will also get the color in a special method, because depending on the rating
            // we will change the color of our ring
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        // Paint for numbers
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        // Background paint
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }

    private fun getPaintColor(progress: Int): Int = when (progress) {
        in 0..25 -> Color.parseColor("#e84258")
        in 26..50 -> Color.parseColor("#fd8060")
        in 51..75 -> Color.parseColor("#fee191")
        else -> Color.parseColor("#b0d8a4")
    }

    fun setRatingAnimated(targetRating: Int) {
        val animator = ValueAnimator.ofInt(0, targetRating)
        // Animation duration in milliseconds
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            // Update the rating value and redraw the View
            setProgress(animatedValue)
        }
        // let's start our animation
        animator.start()
    }
    fun setProgress(pr: Int) {
        // We put a new value in our class field
        progress = pr
        // Create paints with new colors
        initPaint()
        // cause the View to be redrawn
        invalidate()
    }
}