package com.example.amit.uniconnexample.MediaPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 16,December,2019
 */
class MediaPickerListAdapter(var frameWidth: Int, var mediaSelected: MediaSelected) : RecyclerView.Adapter<MediaPickerListAdapter.MediaListViewHolder>(),AnkoLogger {
    val mediaList: ArrayList<ChosenMediaFile> = ArrayList()
    val selectedMediaFile: HashMap<String, ChosenMediaFile> = HashMap()

    fun setData(medias: List<ChosenMediaFile>){
        mediaList.addAll(medias)
        notifyDataSetChanged()
    }

    fun setSelectedMedias(medias: HashMap<String, ChosenMediaFile>){
        info { "set selected medias ${medias.size}" }
        selectedMediaFile.putAll(medias)
    }

    fun setSelectedMedia(media: ChosenMediaFile){
        if(selectedMediaFile.get(media.id) != null){
           selectedMediaFile.remove(media.id)
        }else{
            media.id?.let {
                selectedMediaFile.put(it, media)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_picker_list_item, parent, false)
        return MediaListViewHolder(view, frameWidth)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int) {
        holder.parent.setOnClickListener {
            val media = mediaList[position]
            setSelectedMedia(media)
            mediaSelected.onMediaSelected(media)
            if(selectedMediaFile[media.id] != null){
                notifyItemChanged(position, CommonString.MEDIA_UNSELECTED)
            }else{
                notifyItemChanged(position, CommonString.MEDIA_SELECTED)
            }
        }

        holder.setData(mediaList[position], selectedMediaFile)
    }

    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.setSelect((payloads[0] as String))
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class MediaListViewHolder(var view: View, var frameWidth: Int) : RecyclerView.ViewHolder(view){
        val parent: LinearLayout = view.findViewById(R.id.cardView)
        val itemImage: ImageView = view.findViewById(R.id.imageView)
        val pickedImageFrame: FrameLayout = view.findViewById(R.id.picked_image_frame)
        val pickedImageIcon: ImageView = view.findViewById(R.id.picked_media_sign)

        fun setData(media: ChosenMediaFile, selectedMedia: HashMap<String, ChosenMediaFile>){
            if(selectedMedia.get(media.id) != null){
                //show selected
                pickedImageFrame.visibility = View.VISIBLE
                pickedImageIcon.visibility = View.VISIBLE
            }else{
                //show not selected
                pickedImageFrame.visibility = View.GONE
                pickedImageIcon.visibility = View.GONE
            }

            Glide.with(view).setDefaultRequestOptions(RequestOptions().centerCrop().error(R.drawable.ic_folder)).load(media.uri).override(frameWidth).into(itemImage)
        }

        fun setSelect(payload: String){
            if(payload == CommonString.MEDIA_UNSELECTED){
                pickedImageFrame.visibility = View.GONE
            }else if(payload == CommonString.MEDIA_SELECTED){
                pickedImageFrame.visibility = View.VISIBLE
            }
        }
    }

    interface MediaSelected{
        fun onMediaSelected(media: ChosenMediaFile)
    }
}