package com.example.super_resolution
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.IOException
import java.io.InputStream

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
//                2 -> customImageView.setResult(bitmap)
                2 -> runPtModule(bitmap)
            }
        }
    }

    private fun runPtModule(img: Bitmap) {
        try {
            // load pytorch module
            val module = Module.load(assetFilePath(this, "1234.pt"))
            // set input tensor
            val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                img,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                TensorImageUtils.TORCHVISION_NORM_STD_RGB
            )
            // run the module
            val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()
            println(outputTensor)
            // get the output result
            val outputImage = customImageView.tensorToBitmap(outputTensor)
            customImageView.setResult(img)
        } catch (e: IOException) {
            Log.e("PytorchHelloWorld", "Error reading assets", e)
            finish()
        }
    }

    private fun assetFilePath(context: Context, assetName: String): String {
//        val file = File(context.filesDir, assetName)
//        if (file.exists() && file.length() > 0) {
//            println("path"+file.absolutePath)
//            return file.absolutePath
//        }
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
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        println("path"+file.absolutePath)
//        return file.absolutePath

        val file = File(context.filesDir, assetName)

        try {
            val inpStream: InputStream = context.assets.open(assetName)
            try {
                val outStream = FileOutputStream(file, false)
                val buffer = ByteArray(4 * 1024)
                var read: Int

                while (true) {
                    read = inpStream.read(buffer)
                    if (read == -1) {
                        break
                    }
                    outStream.write(buffer, 0, read)
                }
                outStream.flush()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
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
