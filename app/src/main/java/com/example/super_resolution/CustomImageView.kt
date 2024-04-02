package com.example.super_resolution
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.pytorch.Tensor

class CustomImageView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var resImage: Bitmap? = null
    private var originalImage: Bitmap? = null
    private var dividerY: Float = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val win_width = width
        val win_height = height
        dividerY = if (dividerY == 0f) height / 2f else dividerY

        resImage?.let {
            val rotatedBitmap = rotateBitmap(it, 90f)
            val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, win_width, win_height, true)

            val srcRect = Rect(0, 0, width, dividerY.toInt())
            val dstRect = Rect(0, 0, width, dividerY.toInt())

            canvas.drawBitmap(scaledBitmap, srcRect, dstRect, null)
        }

        originalImage?.let {
            val rotatedBitmap = rotateBitmap(it, 90f)
            val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, win_width, win_height, true)

            val srcRect = Rect(0, dividerY.toInt(), width, height)
            val dstRect = Rect(0, dividerY.toInt(), width, height)

            canvas.drawBitmap(scaledBitmap, srcRect, dstRect, null)
        }

        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 5f
        }
        canvas.drawLine(0f, dividerY, width.toFloat(), dividerY, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                dividerY = event.y
                invalidate()
            }
        }

        return true
    }

    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
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

    fun tensorToBitmap(tensor: Tensor): Bitmap {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Placeholder return value
    }
}
