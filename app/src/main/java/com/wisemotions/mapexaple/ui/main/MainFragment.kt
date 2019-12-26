package com.wisemotions.mapexaple.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wisemotions.mapexaple.R
import com.wisemotions.mapexaple.network.GoogleMapsApiInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var map:MapView
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback:LocationCallback? = null
    private var googleMap:GoogleMap? = null
    private var locationRequest:LocationRequest? = null

    private val googleMapsApiInteractor = GoogleMapsApiInteractor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater.inflate(R.layout.main_fragment, container, false)

        map = view.mapView
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            //fastestInterval = 1000 ??
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    val position = LatLng(it.lastLocation.latitude, it.lastLocation.longitude)
                    googleMap?.addMarker(MarkerOptions().position(position).title("My position"))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLng(position))
                }
            }
        }

        googleMapsApiInteractor.getNearbyATM().observeOn(AndroidSchedulers.mainThread()).doOnNext {
            System.out.println(it)
        }.doOnError {
            it.printStackTrace()
        }.subscribe()

        context?.let { context ->
            if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),10)
            }else{
                initializeLocationService()
            }
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
        googleMap = mMap
    }

    override fun  onResume() {
        map.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 10){
            if (permissions.isNotEmpty() && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocationService()
            }
        }
    }

    private fun initializeLocationService(){
        context?.let { context ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient?.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
        }
    }
}
