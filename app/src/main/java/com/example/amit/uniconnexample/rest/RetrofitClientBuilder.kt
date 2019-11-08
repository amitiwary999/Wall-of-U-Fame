package com.example.amit.uniconnexample.rest

import android.util.Log
import com.example.amit.uniconnexample.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Meera on 09,November,2019
 */
class RetrofitClientBuilder() {
    private var mRetrofit: Retrofit? = null
    private var mRetrofitBuilder: Retrofit.Builder? = null
    private var mNetworkRepository: NetworkRepository? = null

    //we need to check this so that we can authenticate user
    constructor(baseUrl: String):this() {
        if (mRetrofitBuilder == null) {
            createNetworkService()
        }
        init(baseUrl)
    }

    fun init(baseUrl: String) {
        mRetrofit = mRetrofitBuilder?.baseUrl(baseUrl)?.build()
        mNetworkRepository = createApi(NetworkRepository::class.java)
    }


    private fun <T> createApi(clazz: Class<T>): T {
        return mRetrofit!!.create(clazz)
    }

    fun createNetworkService() {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Content-Type", "application/json")
                    .build()
            chain.proceed(request)
        }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        val gson = GsonBuilder().setLenient().create()
        try {
            mRetrofitBuilder = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
        } catch (e: Exception) {
            Log.e("Retrofit Client ", "error in retrofit initialization", e)
        }

    }

    fun getmNetworkRepository(): NetworkRepository? {
        return mNetworkRepository
    }
}