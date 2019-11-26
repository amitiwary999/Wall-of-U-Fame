package com.example.amit.uniconnexample.Fragment.Home

import androidx.paging.DataSource
import com.example.amit.uniconnexample.rest.model.PostModel

/**
 * Created by Meera on 26,November,2019
 */
class HomeDataSourceFactory : DataSource.Factory<String, PostModel>() {
    lateinit var homeDataSource: HomeDataSource
    override fun create(): DataSource<String, PostModel> {
        homeDataSource = HomeDataSource()
        return homeDataSource
    }
}