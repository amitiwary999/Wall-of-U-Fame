package com.example.amit.uniconnexample.MediaPicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.amit.uniconnexample.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerActivity : AppCompatActivity(),AnkoLogger, MediaSelected {
    var mediaPickerViewModel: MediaPickerViewModel ?= null
    var i = 0
    var first: Media?=null
    var second: Media?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_picker_activity)
        info { "media picker activity" }
        val fragment: Fragment = MediaPickerFolderFragment.newInstance(this)
        supportFragmentManager.beginTransaction()
                .replace(R.id.mediasend_fragment_container, fragment)
                .commit()

        mediaPickerViewModel = ViewModelProviders.of(this).get(MediaPickerViewModel::class.java)
    }

    override fun onMediaFolderSelected(mediaFolder: MediaFolder) {
        info { "folder clicked ${mediaFolder.bucketId}" }
        mediaPickerViewModel?.getBucketMedia(this, mediaFolder.bucketId)?.observe(this, Observer {
            info { "first in list ${it}" }
        })
    }

    override fun onMediaSelected(media: Media) {
        mediaPickerViewModel?.selectedMedia?.let {
            if(it.get(media.id) != null){
                mediaPickerViewModel?.removeSelectedMedia(media)
            }else{
               mediaPickerViewModel?.setSelectedMedia(media)
            }
        }
    }
}