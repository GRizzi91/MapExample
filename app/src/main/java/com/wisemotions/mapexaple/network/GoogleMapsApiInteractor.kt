package com.wisemotions.mapexaple.network

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GoogleMapsApiInteractor {

    private val client:GoogleMapsApi = ApiExecutor().createMyApiClient(GoogleMapsApi::class.java)

    fun getNearbyATM(): Observable<String> {
        return client.findNearbyLocation("37.5754167,-121.9318217","AIzaSyDKovl3qKEUE1NPLm-wk9z6YkoYzFAYnxw","5000","atm")
            .subscribeOn(Schedulers.io())
            .doOnNext {
                val s = it.toString()
                System.out.println(s)
            }
            .map { it.status }
    }
}