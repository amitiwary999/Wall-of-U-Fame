package com.example.amit.uniconnexample.Fragment.Home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amit.uniconnexample.R
import kotlinx.android.synthetic.main.activity_global_frag.*

/**
 * Created by Meera on 26,November,2019
 */
class HomeFragment : Fragment() {
    var homeAdapter: HomeAdapter ?= null
    var mContext: Context ?= null
    var homeFragmentViewModel: HomeFragmentViewModel ?= null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_global_frag, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext?.let {
            mblog_list.layoutManager = LinearLayoutManager(mContext)
            homeAdapter = HomeAdapter()
            homeFragmentViewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
            homeFragmentViewModel?.getListLivedata()?.observe(this, Observer {
                homeAdapter?.submitList(it)
            })
        }

    }
}