package com.example.android.finalloadapp

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes


class ButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Start of Declaring Some Variables
    private var downloadBackgroundColor = 0
    private var loadingBackgroundColor = 0
    private var textColor = 0
    private  var downloadText = ""
    private var loadingText = ""
    // Current State of Drawing
    private var buttonText = ""
    private var currentBackgroundColor = 0

    // Getting the Elements Drawn
    private var rectDrawn : Any? = null
    private var textDrawn : Any? = null

    // For Animation
    private val animator = downloadingRectAnimation()
    // End Of Declaring Some Variables


    // Start of Paint Objects
    // Rectangle Drawing
    private val paintRect = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // Text Drawing
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 65.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    // End Of Paint Objects


    // Make init block to get all the attrs and set them
    init {
        // Makes the View clickable
        isClickable = true

        // For attributes
        context.withStyledAttributes(attrs, R.styleable.ButtonView) {
            downloadBackgroundColor = getColor(R.styleable.ButtonView_downloadBackgroundColor, 0)
            loadingBackgroundColor = getColor(R.styleable.ButtonView_loadingBackgroundColor, 0)
            downloadText = getString(R.styleable.ButtonView_downloadText)!!
            loadingText = getString(R.styleable.ButtonView_loadingText)!!
            textColor = getColor(R.styleable.ButtonView_textColor, 0)
        }
        // Get Current State
        currentBackgroundColor = downloadBackgroundColor
        buttonText = downloadText

    }

    // overriding onDraw() method
    override fun onDraw(canvas: Canvas) {
        invalidate()
        // Get Available width and height
        val widthButton = measuredWidth.toFloat()
        val heightButton = measuredHeight.toFloat()

        // Draw Rectangle
        paintRect.color = currentBackgroundColor
        rectDrawn = canvas.drawRect(0f, 0f, widthButton, heightButton, paintRect)

        // Draw The Text
        paintText.color = textColor
        textDrawn = canvas.drawText(buttonText, widthButton/2, heightButton/1.7f, paintText)

    }

    // Start Of States of Downloading
    fun setSteadyState(){
        // Canceling Animation
        animator.cancel()
        // Change the colors
        currentBackgroundColor = downloadBackgroundColor
        buttonText = downloadText

        // Invalidate the view
        invalidate()
        requestLayout()
    }

    fun setDownloadingState(){
        // Starting Animation
        animator.start()
        // Changing the Colors
        currentBackgroundColor = loadingBackgroundColor
        buttonText = loadingText

        // Invalidate the View
        invalidate()
        requestLayout()
    }
    // End Of States of Downloading

    // Start of Animation Methods
    private fun downloadingRectAnimation() : Animator {
        val translateX = PropertyValuesHolder.ofFloat(TRANSLATION_X, 20f)
        val translate_X = PropertyValuesHolder.ofFloat(TRANSLATION_X, -20f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(this, translateX, translate_X)

        // For Repeat
        animator.duration = 500
        animator.repeatCount = 300
        animator.repeatMode = ValueAnimator.REVERSE

        return animator

    }
    // End Of Animation Methods

}


// This class will be make for circle

class CircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Variables
    private var circleBackgroundColor = 0
    private var animator = animateCircle()
    private var radiusOfCircle = 0f

    // Paint
    // Circle Drawing
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // Init
    // For attributes
    init {
        // Getting the CircleBackgroundColor
        context.withStyledAttributes(attrs, R.styleable.CircleView) {
            circleBackgroundColor = getColor(R.styleable.CircleView_circleBackgroundColor, 0)

        }
    }

    // Overriding OnDraw
    // overriding onDraw() method
    override fun onDraw(canvas: Canvas) {
        invalidate()
        // Get Available width and height
        val widthCircleView = measuredWidth.toFloat()
        val heightCircleView = measuredHeight.toFloat()

        // Draw Circle
        paintCircle.color = circleBackgroundColor
        canvas.drawCircle(widthCircleView/2f, heightCircleView/2f, radiusOfCircle, paintCircle)

    }

    fun setCircleSteadyState(){
        animator.cancel()
        radiusOfCircle = 0f

        invalidate()
        requestLayout()
    }
    fun setCircleDownloadingState(){
        animator.start()
        radiusOfCircle = 35f

        invalidate()
        requestLayout()

    }

    fun animateCircle() : Animator{
        val translateX = PropertyValuesHolder.ofFloat(TRANSLATION_X, 20f)
        val translate_X = PropertyValuesHolder.ofFloat(TRANSLATION_X, -20f)
        val fade = PropertyValuesHolder.ofFloat(ALPHA, 0f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(this, translateX, translate_X, fade)

        animator.duration = 500
        animator.repeatCount = 300
        animator.repeatMode = ObjectAnimator.REVERSE

        return animator

    }


}