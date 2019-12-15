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
class MediaPickerFolderFragment : Fragment(), AnkoLogger {
    var mediaPickerViewModel: MediaPickerViewModel ?= null
    val spanCount = 2
    companion object {
        fun newInstance(): MediaPickerFolderFragment {
            return MediaPickerFolderFragment()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "view create" }
        mediaPickerViewModel = ViewModelProviders.of(this, MediaPickerViewModel.Factory(activity!!.application, MediapickerRepository())).get(MediaPickerViewModel::class.java)
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

            val mediaPickerFolderAdapter = MediaPickerFolderAdapter(frameWidth)
            mediapicker_folder_list.layoutManager = gridLayoutManager
            mediapicker_folder_list.adapter = mediaPickerFolderAdapter
            mediaPickerViewModel?.getFolders(view.context)?.observe(this, Observer {
                mediaPickerFolderAdapter.setData(it)
            })
        }
    }
}