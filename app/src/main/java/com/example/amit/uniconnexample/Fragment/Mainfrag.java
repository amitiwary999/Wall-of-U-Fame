package com.example.amit.uniconnexample.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amit.uniconnexample.Global;
import com.example.amit.uniconnexample.MainActivity;
import com.example.amit.uniconnexample.NewTabActivity;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.Tabs;
import com.example.amit.uniconnexample.Trending;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 18/2/17.
 */

public class Mainfrag extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;

    public Mainfrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_detail_frag,container,false);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NewTabActivity)getActivity()).setTitle("    Home        ");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(),getChildFragmentManager());
        adapter.addFrag(new Detailfrag(), "My Campus");
        adapter.addFrag(new Globalfrag(), "Global");
        adapter.addFrag(new Trendfrag(), "Trending");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        Context context;

        public ViewPagerAdapter(Context context,FragmentManager manager) {
            super(manager);
            this.context=context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
