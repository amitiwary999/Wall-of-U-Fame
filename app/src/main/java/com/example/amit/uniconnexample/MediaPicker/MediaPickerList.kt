package com.example.amit.uniconnexample.MediaPicker

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.utils.UtilDpToPixel
import kotlinx.android.synthetic.main.media_picker_list_fragment.*
import org.jetbrains.anko.AnkoLogger
import kotlin.math.roundToInt

/**
 * Created by Meera on 16,December,2019
 */
class MediaPickerList(val mediaSelected: MediaSelected) : Fragment(), AnkoLogger, MediaPickerListAdapter.MediaSelected {
    var mediaPickerViewModel: MediaPickerViewModel ?= null
    var mediaPickerListAdapter: MediaPickerListAdapter ?= null
    val spanCount = 3

    companion object {
        fun newInstance(mediaSelected: MediaSelected): MediaPickerList{
            return MediaPickerList(mediaSelected)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPickerViewModel = ViewModelProviders.of(this).get(MediaPickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.media_picker_list_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val gridLayoutManager = GridLayoutManager(context, spanCount)

            val dm = resources.displayMetrics
            val paddingFrame = UtilDpToPixel.convertDpToPixel(1f, it).roundToInt()
            val frameWidth = (dm.widthPixels - paddingFrame)/spanCount

            mediaPickerListAdapter = MediaPickerListAdapter(frameWidth, this)
            media_picker_list.layoutManager = gridLayoutManager
            media_picker_list.adapter = mediaPickerListAdapter

            mediaPickerViewModel?.selectedMedia?.let {
                mediaPickerListAdapter?.setSelectedMedia(it)
            }

            mediaPickerViewModel?.bucketMedia?.observe(this, Observer {
                mediaPickerListAdapter?.setData(it)
            })
        }
    }

    override fun onMediaSelected(media: Media) {
        mediaSelected.onMediaSelected(media)
    }
}