package com.example.super_resolution
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.pytorch.Tensor
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color

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

    fun tensorToBitmap(outputTensor: Tensor): Bitmap {
        val width = outputTensor.shape()[3].toInt() // 图像宽度
        val height = outputTensor.shape()[2].toInt() // 图像高度
        val pixels = IntArray(width * height)

        // 获取张量数据
        val outputData = outputTensor.getDataAsFloatArray()

        // 调整像素值范围并转换为 Bitmap
        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val r = (outputData[index] * 255).toInt().coerceIn(0, 255)
                val g = (outputData[width * height + index] * 255).toInt().coerceIn(0, 255)
                val b = (outputData[2 * width * height + index] * 255).toInt().coerceIn(0, 255)
                pixels[index] = Color.rgb(r, g, b)
            }
        }

        // 创建 Bitmap
        return createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }
}
