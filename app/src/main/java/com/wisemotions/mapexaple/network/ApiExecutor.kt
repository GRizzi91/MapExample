package com.wisemotions.mapexaple.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiExecutor {

    private var gson: Gson
    private var okHttpClient: OkHttpClient
    private var retrofit: Retrofit

    val BASE_URL:String = "https://maps.googleapis.com/maps/api/place/nearbysearch/"

    ///// Timeout const
    val TIMEOUT_CONNECT_MILLISEC: Long = 90000L
    val TIMEOUT_READ_MILLISEC: Long = 90000L
    val TIMEOUT_WRITE_MILLISEC: Long = 90000L
    ////

    init {
        gson = createGson()
        okHttpClient = createOkHttpClient()
        retrofit = createRetrofit(okHttpClient, gson)
    }

    fun <T> createApiClient(service:Class<T>):T{
        return retrofit.create(service)
    }

    fun createRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return with(Retrofit.Builder()){
            baseUrl(BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(okHttpClient)
            build()
        }
    }

    fun <T> createMyApiClient(service:Class<T>):T{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build(service)
    }

    fun createGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    fun createOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        with(OkHttpClient.Builder()){
            addInterceptor(logging)
            connectTimeout(TIMEOUT_CONNECT_MILLISEC, TimeUnit.MILLISECONDS)
            readTimeout(TIMEOUT_READ_MILLISEC, TimeUnit.MILLISECONDS)
            writeTimeout(TIMEOUT_WRITE_MILLISEC, TimeUnit.MILLISECONDS)
        }
        return client.build()
    }


}