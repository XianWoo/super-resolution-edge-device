package com.example.super_resolution
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil
import android.graphics.Bitmap
import android.util.Log
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.FileOutputStream
import android.content.Context
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var customImageView: CustomImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customImageView = findViewById(R.id.customImageView)

        findViewById<Button>(R.id.buttonImage1).setOnClickListener {
            pickImage(1)
        }

        findViewById<Button>(R.id.buttonImage2).setOnClickListener {
            pickImage(2)
        }

    }
//    fun assetFilePath(context: Context, assetName: String): String? {
//        val file = File(context.filesDir, assetName)
//        try {
//            context.assets.open(assetName).use { inputStream ->
//                FileOutputStream(file).use { outputStream ->
//                    val buffer = ByteArray(4 * 1024)
//                    var read: Int
//                    while (inputStream.read(buffer).also { read = it } != -1) {
//                        outputStream.write(buffer, 0, read)
//                    }
//                    outputStream.flush()
//                }
//                return file.absolutePath
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }

//    fun preprocessImage(imagePath: String): List<Mat> {
//        // Load the image using OpenCV
//        val image = Imgcodecs.imread(imagePath)
//        val slices = mutableListOf<Mat>()
//        val height = image.rows()
//        val width = image.cols()
//
//        val xSlices = ceil(width / 96.0).toInt()
//        val ySlices = ceil(height / 96.0).toInt()
//
//        for (i in 0 until ySlices) {
//            for (j in 0 until xSlices) {
//                val left = j * 96
//                val top = i * 96
//                val right = (j + 1) * 96
//                val bottom = (i + 1) * 96
//
//                val slice = Mat(image, org.opencv.core.Rect(left, top, right - left, bottom - top))
//                slices.add(slice)
//            }
//        }
//
//        return slices
//    }
    private fun pickImage(requestCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            // Set the image to the CustomImageView based on the request code to differentiate between the two images
            when (requestCode) {
                1 -> customImageView.setUploadImage(bitmap)
                2 -> customImageView.setResult(bitmap)
            }
        }
    }
}