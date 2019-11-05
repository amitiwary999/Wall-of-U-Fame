package com.example.amit.uniconnexample.Fragment

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.R
import kotlinx.android.synthetic.main.activity_detail_frag.*

import java.util.ArrayList

/**
 * Created by amit on 18/2/17.
 */

class Mainfrag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_detail_frag, container, false)
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as NewTabActivity).setTitle("    Home        ")
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(activity as Context, childFragmentManager)
        adapter.addFrag(Detailfrag(), "My Campus")
        adapter.addFrag(Globalfrag(), "Global")
        adapter.addFrag(Trendfrag(), "Trending")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(var context: Context, manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }
}
