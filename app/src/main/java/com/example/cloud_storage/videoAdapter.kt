package com.example.cloud_storage

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController

import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener

class videoAdapter( private var videolist:ArrayList<videoDetails>) :RecyclerView.Adapter<videoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_item,parent,false)
        return VideoViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        val currentItem=videolist[position]
        holder.titleR.text = "Video Title-"+currentItem.title
        holder.timeR.text="Video Time-"+currentItem.time
        val mediaController = MediaController(holder.timeR.context)
        mediaController.setAnchorView(holder.videoR)
        holder.videoR.setMediaController(mediaController)
        holder.videoR.setVideoURI(Uri.parse(currentItem.videoUri))
        holder.videoR.requestFocus()
       // holder.videoR.pause()

    }
    fun updateitems( updateditems :ArrayList<videoDetails>)
    {
        videolist.clear()
        videolist.addAll(updateditems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return videolist.size
    }
    class VideoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val timeR:TextView=itemView.findViewById<TextView>(R.id.timeR)
        val titleR:TextView=itemView.findViewById<TextView>(R.id.titleR)
        val videoR:VideoView=itemView.findViewById<VideoView>(R.id.videoR)
    }
}