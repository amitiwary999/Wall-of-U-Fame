package com.example.amit.uniconnexample.MediaPicker

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import kotlinx.android.synthetic.main.media_picker_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.ArrayList

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerActivity : AppCompatActivity(),AnkoLogger, MediaSelected {
    var mediaPickerViewModel: MediaPickerViewModel ?= null
    var singleMediaAllowed: Int = 0
    companion object{
        const val SINGLE_MEDIA_ALLOWED = "single_media_allowed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_picker_activity)
        info { "media picker activity" }
        singleMediaAllowed = intent.extras?.getInt(SINGLE_MEDIA_ALLOWED, 0)?:0
        mediaPickerViewModel = ViewModelProviders.of(this).get(MediaPickerViewModel::class.java)
        val fragment: Fragment = MediaPickerFolderFragment.newInstance(this)
        supportFragmentManager.beginTransaction()
                .replace(R.id.mediasend_fragment_container, fragment)
                .addToBackStack(MediaPickerFolderFragment.TAG)
                .commit()

        send_image.setOnClickListener {
            mediaPickerViewModel?.selectedMedia?.let {
                info { "medias send ${it.values}" }
                val resultIntent = Intent()
                resultIntent.putParcelableArrayListExtra(CommonString.MEDIA, ArrayList(it.values) as ArrayList<out Parcelable>)
                setResult(CommonString.MEDIA_PICKER_ACTIVITY, resultIntent)
                finish()
            }
        }
    }

    override fun onMediaFolderSelected(mediaFolder: MediaFolder) {
        info { "folder clicked ${mediaFolder.bucketId}" }
        val b = Bundle()
        b.putString(MediaPickerList.BUCKET_ID, mediaFolder.bucketId)
        val fragment: Fragment = MediaPickerList.newInstance(this, b)
        supportFragmentManager.beginTransaction()
                .replace(R.id.mediasend_fragment_container, fragment)
                .addToBackStack(MediaPickerList.TAG)
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
            if(singleMediaAllowed == 1){
                val resultIntent = Intent()
                resultIntent.putParcelableArrayListExtra(CommonString.MEDIA, ArrayList(it.values) as ArrayList<out Parcelable>)
                setResult(CommonString.MEDIA_PICKER_ACTIVITY, resultIntent)
                finish()
            }else{
                if(it.size > 0){
                    selected_items.visibility = View.VISIBLE
                    count_item_selected.text = it.size.toString()
                }else{
                    selected_items.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if(backStackEntryCount == 1){
            finish()
            super.onBackPressed()
        }else{
            supportFragmentManager.popBackStackImmediate()
        }
        info { "back stack entry count ${backStackEntryCount}" }
    }
}