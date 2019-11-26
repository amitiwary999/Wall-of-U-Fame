package com.example.amit.uniconnexample.Fragment.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.amit.uniconnexample.rest.model.PostModel

/**
 * Created by Meera on 26,November,2019
 */
class HomeFragmentViewModel : ViewModel() {
    private lateinit var homeBoardListLiveData: LiveData<PagedList<PostModel>>

    fun getListLivedata(): LiveData<PagedList<PostModel>> {
        val homeBoardListDataSourceFactory = HomeDataSourceFactory()
        val pagedConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20).build()
        homeBoardListLiveData = LivePagedListBuilder(homeBoardListDataSourceFactory, pagedConfig).build()

        return homeBoardListLiveData
    }
}