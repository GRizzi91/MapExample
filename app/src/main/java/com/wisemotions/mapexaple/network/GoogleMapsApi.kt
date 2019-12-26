package com.wisemotions.mapexaple.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

@BaseUrl("https://maps.googleapis.com/maps/api/")
interface GoogleMapsApi {

    @GET("place/nearbysearch/json")
    fun findNearbyLocation(
        @Query("location") location: String,
        @Query("key") key: String,
        @Query("radius") radius: String,
        @Query("types") types:String
    ):Observable<GoogleMapsApiResponse>
}