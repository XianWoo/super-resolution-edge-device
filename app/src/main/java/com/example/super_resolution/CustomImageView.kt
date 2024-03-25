package com.example.super_resolution
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomImageView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var resImage: Bitmap? = null
    private var originalImage: Bitmap? = null
    private var dividerX: Float = 0f


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val win_width = width.toFloat()
        val win_height = height.toFloat()

        dividerX = if (dividerX == 0f) win_width / 2 else dividerX

        // 让resImage 覆盖在originalImage上


        resImage?.let {
            val srcRect = Rect(0, 0, dividerX.toInt(), win_height.toInt())
            val dstRect = Rect(0, 0, dividerX.toInt(), win_height.toInt())
            canvas.drawBitmap(it, srcRect, dstRect, null)
        }

        originalImage?.let {
            val srcRect = Rect(dividerX.toInt(), 0, win_width.toInt(), win_height.toInt())
            val dstRect = Rect(dividerX.toInt(), 0, win_width.toInt(), win_height.toInt())
            canvas.drawBitmap(it, srcRect, dstRect, null)
        }

        // Draw divider line
        val paint = Paint()
        paint.color = Color.BLACK
        canvas.drawLine(dividerX, 0f, dividerX, win_height, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                dividerX = event.x
                invalidate()
            }
        }

        return true
    }

    fun setResult(bitmap: Bitmap) {
        resImage = bitmap
        invalidate()
    }

    fun setUploadImage(bitmap: Bitmap) {
        originalImage = bitmap
        invalidate()
    }

    fun getResImage(): Bitmap? {
        // get the result image from the model
        // waiting for shanzhi to implement
        return resImage
    }
}
