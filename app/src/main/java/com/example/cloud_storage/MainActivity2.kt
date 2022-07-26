package com.example.cloud_storage

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
//import android.support.v7.widget.SearchView
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

 class MainActivity2 : AppCompatActivity() {


     lateinit var arraylist:ArrayList<videoDetails>
     lateinit var search: SearchView
     lateinit var adapter: videoAdapter
     lateinit var db: DatabaseReference
     lateinit var RecyclerView: RecyclerView
     lateinit var videolist: ArrayList<videoDetails>
     lateinit var tempvideolist: ArrayList<videoDetails>

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main2)

         RecyclerView = findViewById(R.id.recycler)
         RecyclerView.layoutManager = LinearLayoutManager(this)

         arraylist= arrayListOf<videoDetails>()
         videolist = arrayListOf<videoDetails>()
         tempvideolist = arrayListOf<videoDetails>()
         adapter = videoAdapter(videolist)



         search = findViewById(R.id.search_action)

         //search.clearFocus()
         search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
             override fun onQueryTextSubmit(query: String?): Boolean {

                 return false
             }

             override fun onQueryTextChange(newText: String?): Boolean {
                 filterlist(newText)
                 return true
             }
         })
         search.clearFocus()

         val add = findViewById<FloatingActionButton>(R.id.fab)
         add.setOnClickListener {
             val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)
         }
         db = FirebaseDatabase.getInstance().getReference("videos")
         db.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {

                 if (snapshot.exists()) {
                     for (videosnapshot in snapshot.children) {

                         val video = videosnapshot.getValue(videoDetails::class.java)
                         videolist.add(video!!)

                     }
                     tempvideolist.addAll(videolist)
                     arraylist.addAll(videolist)
                     adapter=videoAdapter(videolist)
                     RecyclerView.adapter = adapter


                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })

     }

     private fun filterlist(newText: String?) {

         tempvideolist.clear()
         videolist.forEach {

             if (newText != null) {
                 if(it.title?.toLowerCase(Locale.getDefault())?.contains(newText) == true)
                 {
                     tempvideolist.add(it)}
             }
         }
         if (newText != null) {
             if(newText.isNullOrBlank())

             {tempvideolist.clear()
                 tempvideolist.addAll(arraylist)

             }
         }


     adapter.updateitems(tempvideolist)
     }
 }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//
//       menuInflater.inflate(R.menu.menu_search,menu)
//         val item=menu?.findItem(R.id.search_action)
//         val searchview =item?.actionView as SearchView
//         searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//             override fun onQueryTextSubmit(query: String?): Boolean {
//              return true
//             }
//
//             override fun onQueryTextChange(newText: String?): Boolean {
//
//                 tempvideolist.clear()
//                 val searchtext = newText!!.toLowerCase(Locale.getDefault())
//                 if(searchtext.isNotEmpty())
//                 {
//                     videolist.forEach{
//                         if(it.title?.toLowerCase(Locale.getDefault())?.contains(searchtext) == true)
//                         {
//                             tempvideolist.add(it)
//                         }
//                     }
//                    RecyclerView.adapter=videoAdapter(tempvideolist)
//
//                 }
//                 else
//                 {
//                     tempvideolist.clear()
//                     tempvideolist.addAll(videolist)
//                     RecyclerView.adapter=videoAdapter(videolist)
//                 }
//                 return false
//
//             }
//         })
//        return true
//    }



