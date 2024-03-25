package com.example.super_resolution
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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


//package com.example.super_resolution
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.Rect
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.super_resolution.R
//
//class MainActivity : AppCompatActivity() {
//    private val pickImageRequestCode = 1
//    private var imageUri: Uri? = null
//    private var secondImageUri: Uri? = null
//
//    private lateinit var imageComparisonView: ImageComparisonView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        imageComparisonView = findViewById(R.id.imageCompareView)
//
//        val uploadButton: Button = findViewById(R.id.uploadButton)
//        uploadButton.setOnClickListener { openGalleryForImage() }
//
//        val secondUploadButton: Button = findViewById(R.id.recoverButton)
//        secondUploadButton.setOnClickListener { openGalleryForSecondImage() }
//    }
//
//    private fun openGalleryForImage() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(intent, pickImageRequestCode)
//    }
//
//    private fun openGalleryForSecondImage() {
//        // Different request code for the second image
//        val pickSecondImageRequestCode = 2
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(intent, pickSecondImageRequestCode)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                pickImageRequestCode -> {
//                    imageUri = data?.data
//                    imageUri?.let { uri ->
//                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                        imageComparisonView.setInitialImage(bitmap)
//                    }
//                }
//                2 -> { // Request code for the second image
//                    secondImageUri = data?.data
//                    secondImageUri?.let { uri ->
//                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                        imageComparisonView.setRecoveredImage(bitmap)
//                    }
//                }
//            }
//        }
//    }
//
//}
//
