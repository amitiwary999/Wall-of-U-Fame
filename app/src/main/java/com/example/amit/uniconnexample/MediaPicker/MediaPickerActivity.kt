package com.example.amit.uniconnexample.MediaPicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.amit.uniconnexample.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerActivity : AppCompatActivity(),AnkoLogger, MediaFolderSelected {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_picker_activity)
        info { "media picker activity" }
        val fragment: Fragment = MediaPickerFolderFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.mediasend_fragment_container, fragment)
                .commit()
    }

    override fun onMediaFolderSelected(bucketId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}