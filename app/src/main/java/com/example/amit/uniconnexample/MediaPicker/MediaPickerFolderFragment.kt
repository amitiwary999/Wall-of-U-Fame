package com.example.amit.uniconnexample.MediaPicker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.utils.UtilDpToPixel
import kotlinx.android.synthetic.main.media_picker_folder_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.math.roundToInt

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerFolderFragment(var mediaFolderSelected: MediaSelected) : Fragment(), AnkoLogger, MediaPickerFolderAdapter.MediaFolderClicked {
    var mediaPickerViewModel: MediaPickerViewModel ?= null
    val spanCount = 2
    companion object {
        fun newInstance(mediaFolderSelected: MediaSelected): MediaPickerFolderFragment {
            return MediaPickerFolderFragment(mediaFolderSelected)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "view create" }
        mediaPickerViewModel = ViewModelProviders.of(this).get(MediaPickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.media_picker_folder_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val gridLayoutManager = GridLayoutManager(context, spanCount)

            val dm = resources.displayMetrics
            val paddingFrame = UtilDpToPixel.convertDpToPixel(1f, it).roundToInt()
            val frameWidth = (dm.widthPixels - paddingFrame)/spanCount

            val mediaPickerFolderAdapter = MediaPickerFolderAdapter(frameWidth, this)
            mediapicker_folder_list.layoutManager = gridLayoutManager
            mediapicker_folder_list.adapter = mediaPickerFolderAdapter
            mediaPickerViewModel?.getFolders(view.context)?.observe(this, Observer {
                mediaPickerFolderAdapter.setData(it)
            })
        }
    }

    override fun onFolderClicked(mediaFolder: MediaFolder) {
        mediaFolderSelected.onMediaFolderSelected(mediaFolder)
    }
}