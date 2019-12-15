package com.example.amit.uniconnexample.MediaPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.R

/**
 * Created by Meera on 15,December,2019
 */
class MediaPickerFolderAdapter(var frameWidth : Int): RecyclerView.Adapter<MediaPickerFolderAdapter.ViewHolder>(){
    var mediaFolders: List<MediaFolder> = ArrayList()
    fun setData(mediaFolder: List<MediaFolder>){
        mediaFolders = mediaFolder
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_folder_picker_item, parent, false)
        return ViewHolder(view, frameWidth)
    }

    override fun getItemCount(): Int {
       return mediaFolders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mediaFolders.get(position))
    }

    class ViewHolder(var view: View, var frameWidth : Int) : RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.folder_image)
        val folderTitle: TextView = view.findViewById(R.id.folder_title)
        val folderCount: TextView = view.findViewById(R.id.folder_item_count)

        fun setData(mediaFolder: MediaFolder){
            Glide.with(view).setDefaultRequestOptions(RequestOptions().centerCrop().error(R.drawable.ic_folder)).load(mediaFolder.thumbnailUri).override(frameWidth).into(imageView)
            folderTitle.text = mediaFolder.title
            folderCount.text = mediaFolder.itemCount.toString()
        }
    }
}