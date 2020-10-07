package net.ljubvi.kursadarbslazy.Activities


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_detail.*

import net.ljubvi.kursadarbslazy.Activities.MainActivity.Companion.EXTRA_ID
import net.ljubvi.kursadarbslazy.Activities.MainActivity.Companion.EXTRA_NEW
import net.ljubvi.kursadarbslazy.App
import net.ljubvi.kursadarbslazy.DataClasses.ToDoItem
import net.ljubvi.kursadarbslazy.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {

    private val db get() = (application as App).db
    private lateinit var currentPhotoPath: String
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        private lateinit var photoUri: Uri
    }
    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && PackageManager.PERMISSION_GRANTED in grantResults) {
            takePicture()
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val color_array = mutableListOf<Int>(
            ContextCompat.getColor(this,R.color.green),
            ContextCompat.getColor(this,R.color.blue),
            ContextCompat.getColor(this,R.color.red),
            ContextCompat.getColor(this,R.color.yellow)
        )


        val id = intent.getLongExtra(EXTRA_ID, -1)


        if (id.toString()=="-1") { // new entry to be added
            val item = ToDoItem("", false)
            colorSample.setBackgroundColor(color_array[3])
            colorText.text = "3"
            detailsSave.setOnClickListener {
                if(detailsNameInput.text.toString()!="") {
                var hasimage = false
                val imageuri = imageText.text.toString()
                if (imageuri != "") { hasimage = true }

                item.uid = db.ToDoItemDao().insertAll(
                    ToDoItem(
                        name = detailsNameInput.text.toString(),

                        color = colorText.text.toString(),
                        done = detailsDoneInput.isChecked,
                        hasImage = hasimage,
                        imageUri = imageuri
                    )
                ).first()

                val intent = Intent().putExtra(EXTRA_ID, item.uid)
                    .putExtra(EXTRA_NEW, id.toLong())
                setResult(RESULT_OK, intent)
                finish()
            }else Toast.makeText(this,R.string.must_enter,Toast.LENGTH_SHORT).show()
            }
        } else {
            val item = db.ToDoItemDao().getItemById(id)
            detailsNameInput.setText(item.name)
            if (item.hasImage){
                imageText.text = item.imageUri
                imageView.setImageURI(item.imageUri.toUri())
            }
            colorText.setText(item.color.toString())

            if(item.color != "") colorSample.setBackgroundColor(color_array[item.color.toInt()])
            detailsDoneInput.isChecked = item.done

            detailsSave.setOnClickListener {
                if(detailsNameInput.text.toString()!="") {
                    var hasimage = false
                    val imageuri = imageText.text.toString()
                    if (imageuri != "") {
                        hasimage = true
                    }
                    db.ToDoItemDao().update(
                        item.copy(
                            name = detailsNameInput.text.toString(),
                            color = colorText.text.toString(),
                            done = detailsDoneInput.isChecked,
                            hasImage = hasimage,
                            imageUri = imageuri
                        )
                    )
                    val intent = Intent().putExtra(EXTRA_ID, item.uid)
                        .putExtra(EXTRA_NEW, 0)
                    setResult(RESULT_OK, intent)
                    finish()
                } else Toast.makeText(this,R.string.must_enter,Toast.LENGTH_SHORT).show()


            }


        }

        cameraButton.setOnClickListener{



            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,1)
            }





        }



                // if it looks stupid, but works, it is not stupid...
        greenButton.setOnClickListener {
            colorText.text = "0"
            colorSample.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
        }

        blueButton.setOnClickListener {
            colorText.text = "1"
            colorSample.setBackgroundColor(ContextCompat.getColor(this,R.color.blue))
        }

        redButton.setOnClickListener {
            colorText.text = "2"
            colorSample.setBackgroundColor(ContextCompat.getColor(this,R.color.red))
        }

        yellowButton.setOnClickListener {
            colorText.text = "3"
            colorSample.setBackgroundColor(ContextCompat.getColor(this,R.color.yellow))
        }
                // end of stupid part

    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)?.let {
            val photoFile = try {
                createImageFile()
            } catch (ex: IOException) {null}
            photoFile?.let {
                photoUri = FileProvider.getUriForFile(this,"net.ljubvi.lazy.kursadarbs.fileprovider",it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)}
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir   ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            imageView.setImageURI(photoUri)
            if (imageText.text.toString() != ""){
                val u:Uri = imageText.text.toString().toUri()
                val s:String? = u.lastPathSegment

                this.deleteFile(s)
            }
            imageText.text = photoUri.toString()


        }
    }



}


