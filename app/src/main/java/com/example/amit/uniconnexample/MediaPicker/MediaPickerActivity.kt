package com.example.amit.uniconnexample.MediaPicker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.amit.uniconnexample.R
import kotlinx.android.synthetic.main.media_picker_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerActivity : AppCompatActivity(),AnkoLogger, MediaSelected {
    var mediaPickerViewModel: MediaPickerViewModel ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_picker_activity)
        info { "media picker activity" }
        mediaPickerViewModel = ViewModelProviders.of(this).get(MediaPickerViewModel::class.java)
        val fragment: Fragment = MediaPickerFolderFragment.newInstance(this)
        supportFragmentManager.beginTransaction()
                .replace(R.id.mediasend_fragment_container, fragment)
                .commit()
    }

    override fun onMediaFolderSelected(mediaFolder: MediaFolder) {
        info { "folder clicked ${mediaFolder.bucketId}" }
        val b = Bundle()
        b.putString(MediaPickerList.BUCKET_ID, mediaFolder.bucketId)
        val fragment: Fragment = MediaPickerList.newInstance(this, b)
        supportFragmentManager.beginTransaction()
                .replace(R.id.mediasend_fragment_container, fragment)
                .commit()
    }

    override fun onMediaSelected(media: Media) {
        mediaPickerViewModel?.selectedMedia?.let {
            if(it.get(media.id) != null){
                mediaPickerViewModel?.removeSelectedMedia(media)
            }else{
               mediaPickerViewModel?.setSelectedMedia(media)
            }
            showSelectedCount()
        }
    }

    fun showSelectedCount(){
        mediaPickerViewModel?.selectedMedia?.let {
            if(it.size > 0){
                selected_items.visibility = View.VISIBLE
                count_item_selected.text = it.size.toString()
            }else{
                selected_items.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}