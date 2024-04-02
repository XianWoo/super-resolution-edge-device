package com.example.super_resolution
import android.graphics.Bitmap
import android.graphics.BitmapFactory

// ImageProcessingUtils.kt
object ImageProcessingUtils {
    fun preprocessImageKt(imagePath: String): List<Bitmap> {
        val image = BitmapFactory.decodeFile(imagePath)
        val width = image.width
        val height = image.height
        val slices = mutableListOf<Bitmap>()

        val sliceWidth = 96
        val sliceHeigh
        t = 96
        val xSlices = kotlin.math.ceil(width / sliceWidth.toDouble()).toInt()
        val ySlices = kotlin.math.ceil(height / sliceHeight.toDouble()).toInt()

        for (i in 0 until ySlices) {
            for (j in 0 until xSlices) {
                val left = j * sliceWidth
                val top = i * sliceHeight
                val right = kotlin.math.min((j + 1) * sliceWidth, width)
                val bottom = kotlin.math.min((i + 1) * sliceHeight, height)
                val slice = Bitmap.createBitmap(image, left, top, right - left, bottom - top)
                slices.add(slice)
            }
        }
        return slices
    }
    }

