package com.example.amit.uniconnexample.MediaPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.amit.uniconnexample.R

/**
 * Created by Meera on 16,December,2019
 */
class MediaPickerListAdapter(var frameWidth: Int, var mediaSelected: MediaSelected) : RecyclerView.Adapter<MediaPickerListAdapter.MediaListViewHolder>() {
    val mediaList: ArrayList<Media> = ArrayList()
    val selectedMediaFile: HashMap<String, Media> = HashMap()

    fun setData(medias: List<Media>){
        mediaList.addAll(medias)
        notifyDataSetChanged()
    }

    fun setSelectedMedia(medias: HashMap<String, Media>){
        selectedMediaFile.putAll(medias)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_picker_list_item, parent, false)
        return MediaListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int) {
        holder.parent.setOnClickListener {
            mediaSelected.onMediaSelected(mediaList[position])
        }

        holder.setData(mediaList[position], selectedMediaFile)
    }

    class MediaListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val parent: LinearLayout = view.findViewById(R.id.cardView)
        val itemImage: ImageView = view.findViewById(R.id.imageView)
        val pickedImageFrame: FrameLayout = view.findViewById(R.id.picked_image_frame)

        fun setData(media: Media, selectedMedia: HashMap<String, Media>){
            if(selectedMedia.get(media.id) != null){
                //show selected
            }else{
                //show not selected
            }
        }
    }

    interface MediaSelected{
        fun onMediaSelected(media: Media)
    }
}