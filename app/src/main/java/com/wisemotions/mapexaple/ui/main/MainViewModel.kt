package com.wisemotions.mapexaple.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng


class MainViewModel : ViewModel() {

    val markPositions =  MutableLiveData<List<LatLng>>()

    init {

    }


}
