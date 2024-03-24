package com.example.super_resolution
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.super_resolution.R
//
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}
class MainActivity : AppCompatActivity() {
    private val pickImage = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uploadButton: Button = findViewById(R.id.uploadButton)
        uploadButton.setOnClickListener { openGallery() }

        val recoverButton: Button = findViewById(R.id.recoverButton)
        recoverButton.setOnClickListener { recoverImage() }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val selectedImageView: ImageView = findViewById(R.id.selectedImageView)
            selectedImageView.setImageURI(imageUri)
        }
    }

    private fun recoverImage() {
        // Here you would have your logic to send the image to a server
        // for processing and then receive and display the enhanced image.
        // This example does not implement the actual recovery process.
    }
}

