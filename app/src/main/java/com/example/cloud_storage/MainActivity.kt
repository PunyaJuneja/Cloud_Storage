package com.example.cloud_storage

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    val VIDEO:Int=1
    private var uri:Uri?=null
    private var title:String=""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Hold on")
        progressDialog.setMessage("Uploading.....")

        val choose = findViewById<Button>(R.id.choose)
        choose.setOnClickListener(View.OnClickListener {

                view: View? -> val intent= Intent()
            intent.setType("video/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Select Video"),VIDEO)

        }


        )
        val upload = findViewById<Button>(R.id.upload)

        upload.setOnClickListener {

            val text = findViewById<EditText>(R.id.title)
            title = text.text.toString().trim()
            if(TextUtils.isEmpty(title))
            {
                Toast.makeText(this,"Title is required",Toast.LENGTH_SHORT).show()
            }
            else if(uri==null)
            {
                Toast.makeText(this,"Select a video first",Toast.LENGTH_SHORT).show()
            }
            else{
                uploadvideo()

            }
        }



        }


    private fun uploadvideo() {
        progressDialog.show()
        val timestamp=""+System.currentTimeMillis()
        val fileandpath = "Videos/video_$timestamp"
        val storagereference=FirebaseStorage.getInstance().getReference(fileandpath)

        storagereference.putFile(uri!!).addOnSuccessListener{
            val uritask =it.storage.downloadUrl

            while(!uritask.isSuccessful);

                val downloaduri=uritask.result
                if(uritask.isSuccessful)
                {
                    val map = HashMap<String,Any>()
                    map["id"]="$title"
                    map["title"]="$title"
                    map["time"]="$timestamp"
                    map["videoUri"]="$downloaduri"

                    val db=FirebaseDatabase.getInstance().getReference("videos")
                    db.child(timestamp).setValue(map).addOnSuccessListener {
                        taskSnapshot->progressDialog.dismiss()
                        Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MainActivity2::class.java)
                        startActivity(intent)

                    }.addOnFailureListener{
                        e->progressDialog.dismiss()
                        Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
                    }



                }

        }.addOnFailureListener{
                e->progressDialog.dismiss()
            Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode== RESULT_OK)
        {
            if(requestCode==VIDEO) {
                uri = data!!.data
                val mediacontroller = MediaController(this)
                val video= findViewById<VideoView>(R.id.video)
                mediacontroller.setAnchorView(video)

                video.setMediaController(mediacontroller)
                video.setVideoURI(uri)
               // video.requestFocus()
                //video.setOnPreparedListener{
                 //   video.
                //}
            }}

        super.onActivityResult(requestCode, resultCode, data)
    }


}