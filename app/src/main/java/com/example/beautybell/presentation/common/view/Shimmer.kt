package com.example.beautybell.presentation.common.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.example.beautybell.R
import com.example.beautybell.presentation.common.extension.isVisible

class Shimmer : View {
    companion object {
        private const val ANIMATION_DURATION = 1500L
    }

    private var animator: ValueAnimator? = null

    private var gradientPaint: Paint? = null
    private var gradientColors: IntArray? = null
    private val centerColor = ContextCompat.getColor(context, R.color.white)
    private val beforeEdgeColor = ContextCompat.getColor(context, R.color.black_05)
    private val edgeColor = ContextCompat.getColor(context, R.color.black_10)

    private var contentViewBitmap: Bitmap? = null
    private var contentViewPaint: Paint? = null
    private var contentViewRes: Int = -1
    private var contentView: View? = null
    private var gradientCenterColorWidth: Float = 0.1f
    private var bgColor = ContextCompat.getColor(context, R.color.white)

    private val gradientColorDistribution: FloatArray
        get() {
            val colorDistribution = FloatArray(5)

            colorDistribution[0] = 0f
            colorDistribution[1] = 0.2f - gradientCenterColorWidth / 2f
            colorDistribution[2] = 0.4f + gradientCenterColorWidth / 2f
            colorDistribution[3] = 0.2f
            colorDistribution[4] = 1f

            return colorDistribution
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initContentViewPaint()
        initGradientPaint()
        initAnimation()

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.Shimmer, 0, 0)
        if (attrs != null) {
            contentViewRes = typedArray.getResourceId(R.styleable.Shimmer_layout, -1)
            bgColor = ContextCompat.getColor(
                context,
                if (typedArray.getInt(
                        R.styleable.Shimmer_backgroundColorShimmer,
                        0
                    ) == 0
                ) R.color.white else R.color.black
            )
            gradientCenterColorWidth =
                typedArray.getFloat(R.styleable.Shimmer_gradient_center_color_width, 0.1f)
            if (contentViewRes == -1) {
                throw IllegalAccessException("cannot inflate shimmer view without a layout attribute")
            }
        }
    }

    private fun initAnimation() {
        animator = ValueAnimator.ofFloat(-1f, 2f)
        animator!!.duration = ANIMATION_DURATION
        animator!!.interpolator = LinearInterpolator()
        animator!!.repeatCount = ValueAnimator.INFINITE
        animator!!.addUpdateListener {
            val f = it.animatedValue as Float
            updateGradient(width.toFloat(), f)
            invalidate()
        }
    }

    private fun initGradientPaint() {
        gradientPaint = Paint()
        gradientPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        gradientPaint!!.isAntiAlias = true
        gradientColors =
            intArrayOf(edgeColor, beforeEdgeColor, centerColor, beforeEdgeColor, edgeColor)
    }

    private fun initContentViewPaint() {
        contentViewPaint = Paint()
        contentViewPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (contentView == null) {
            contentView = initContentView(widthMeasureSpec, heightMeasureSpec)
        }

        val wms = MeasureSpec.makeMeasureSpec(contentView!!.width, MeasureSpec.EXACTLY)
        val hms = MeasureSpec.makeMeasureSpec(contentView!!.height, MeasureSpec.EXACTLY)
        super.onMeasure(wms, hms)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateGradient(w.toFloat(), -1f)
        if (h <= 0 || w <= 0) {
            contentViewBitmap = null
            animator!!.cancel()
        }
    }

    private fun updateGradient(w: Float, animatedValue: Float) {
        val left = w * animatedValue
        val linearGradient = gradientColors?.let {
            LinearGradient(
                left,
                0f,
                left + w,
                250f,
                it,
                gradientColorDistribution,
                Shader.TileMode.CLAMP
            )
        }
        gradientPaint!!.shader = linearGradient
    }

    private fun initContentView(widthMeasureSpec: Int, heightMeasureSpec: Int): View {
        // Inflate content view and call measure and layout manually to get dimensions
        val contentView =
            LayoutInflater.from(context).inflate(contentViewRes, parent as ViewGroup, false)
        contentView.measure(widthMeasureSpec, heightMeasureSpec)
        contentView.layout(0, 0, contentView.measuredWidth, contentView.measuredHeight)

        // Remove background, as shimmer show's up on the colored areas of the content view
        contentView.background = null

        // Draw the content view to a bitmap, we just need the alpha values of the bitmap here
        contentViewBitmap = try {
            Bitmap.createBitmap(
                contentView.measuredWidth,
                contentView.measuredHeight,
                Bitmap.Config.ALPHA_8
            )
        } catch (e: OutOfMemoryError) {
            Log.e(
                javaClass.simpleName,
                "Width = ${contentView.measuredWidth}, Height = ${contentView.measuredHeight}, Root = ${contentView.rootView}",
                e
            )
            System.gc()
            Bitmap.createBitmap(
                contentView.measuredWidth / 2,
                contentView.measuredHeight / 2,
                Bitmap.Config.ALPHA_8
            )
        }

        if (contentViewBitmap != null) {
            val canvas = Canvas(contentViewBitmap!!)
            contentView.draw(canvas)
        }
        return contentView
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (contentViewBitmap != null) {
            canvas.drawBitmap(contentViewBitmap!!, 0F, 0F, contentViewPaint)
            gradientPaint?.let {
                canvas.drawRect(
                    0F,
                    0F,
                    contentViewBitmap!!.width.toFloat(),
                    contentViewBitmap!!.height.toFloat(),
                    it
                )
            }
            canvas.drawColor(bgColor, PorterDuff.Mode.DST_ATOP)
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            VISIBLE -> startAnimationIfNotAlreadyRunning()
            INVISIBLE, GONE -> animator?.cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimationIfNotAlreadyRunning()
    }

    private fun startAnimationIfNotAlreadyRunning() {
        if (this.isVisible() && animator != null && !animator!!.isRunning) animator!!.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}