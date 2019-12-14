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
import kotlinx.android.synthetic.main.media_picker_folder_fragment.*

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerFolderFragment : Fragment() {
    var mediaPickerViewModel: MediaPickerViewModel ?= null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPickerViewModel = ViewModelProviders.of(this, MediaPickerViewModel.Factory(activity!!.application, MediapickerRepository())).get(MediaPickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.media_picker_folder_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridLayoutManager = GridLayoutManager(context, 2)
        val mediaPickerFolderAdapter = MediaPickerFolderAdapter()
        mediapicker_folder_list.layoutManager = gridLayoutManager
        mediapicker_folder_list.adapter = mediaPickerFolderAdapter
        mediaPickerViewModel?.folders?.observe(this, Observer {
            mediaPickerFolderAdapter.setData(it)
        })
    }
}