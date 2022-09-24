package com.example.imageexplorer4

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.ActionMode
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
     lateinit var filepath : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)
        // Camera
        button.isEnabled = false

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),111)
        }
        else
            button.isEnabled=true

        button.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
        }

        //Gallery
        button2.setOnClickListener {
        startFileChooser()
    }
        button3.setOnClickListener {
            uploadFile()
        }
    }

    private fun uploadFile() {
         if(filepath!=null){
             var pd = ProgressDialog(this)
             pd.setTitle("Uploading")
             pd.show()

             var imageRef = FirebaseStorage.getInstance().reference.child("images/fruit.jpg")
             imageRef.putFile(filepath)
                 .addOnSuccessListener { p0 ->
                     pd.dismiss()
                     Toast.makeText(applicationContext,"File Uploaded", Toast.LENGTH_LONG).show()
                 }
                 .addOnFailureListener { p0 ->
                     pd.dismiss()
                     Toast.makeText(applicationContext,p0.message, Toast.LENGTH_LONG).show()
                 }

                 .addOnProgressListener {p0 ->

                     var progress = (100.0 * p0.bytesTransferred)/ p0.totalByteCount
                     pd.setMessage("Uploaded ${progress.toInt()}%")
                 }


         }

        if(filepath!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child("images/fruit.jpg")
            imageRef.putFile(filepath)
                .addOnSuccessListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"File Uploaded", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext,p0.message, Toast.LENGTH_LONG).show()
                }

                .addOnProgressListener {p0 ->

                    var progress = (100.0 * p0.bytesTransferred)/ p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }


        }
    }


    private fun startFileChooser() {
        var i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture" ),111)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == RESULT_OK && data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            imageView.setImageBitmap(bitmap)
        }

        //Camera
        if (requestCode==101){
            var pic = data?.getParcelableExtra<Bitmap>("data")
            imageView.setImageBitmap(pic)

            //binding.firebaseImage.setImageURI(ImageUri)

        }
    }
    // Camera
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==101 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            button.isEnabled = true
        }

        else{
            Toast.makeText(this,"Please accept the permission!", Toast.LENGTH_LONG).show()
        }
    }


}