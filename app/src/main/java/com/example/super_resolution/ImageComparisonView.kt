package com.example.super_resolution
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
class ImageComparisonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK // Line color
        strokeWidth = 5f // Line width
    }

    private var touchX: Float = -1f // Start outside the view
    private var initialImage: Bitmap? = null
    private var recoveredImage: Bitmap? = null

    init {
        // Enable drawing cache
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    fun setInitialImage(bitmap: Bitmap) {
        initialImage = bitmap
        invalidate() // Redraw the view
    }

    fun setRecoveredImage(bitmap: Bitmap) {
        recoveredImage = bitmap
        invalidate() // Redraw the view
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                touchX = event.x
                invalidate() // Redraw the view
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the recovered image
        recoveredImage?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }

        // Clip the canvas to show the initial image only in the clipped area
        if (touchX >= 0) {
            canvas.save()
            canvas.clipRect(0f, 0f, touchX, height.toFloat())
            initialImage?.let {
                canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
            }
            canvas.restore()

            // Draw the line
            canvas.drawLine(touchX, 0f, touchX, height.toFloat(), linePaint)
        }
    }
}
