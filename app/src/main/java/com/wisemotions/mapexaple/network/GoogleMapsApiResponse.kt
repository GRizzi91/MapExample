package com.wisemotions.mapexaple.network

import com.google.gson.annotations.SerializedName

class GoogleMapsApiResponse {

    @SerializedName("status") var status:String = ""
    @SerializedName("next_page_token") var nextPageToken:String = ""
    @SerializedName("results") var geometry:Array<Results> = arrayOf()

    class Results {
        @SerializedName("scope") var scope:String = ""
        @SerializedName("geometry") var geometry:Geometry = Geometry()

        override fun toString(): String {
            return "Results(scope='$scope', geometry=$geometry)"
        }
    }

    class Geometry {
        @SerializedName("location") var location:Location = Location()

        override fun toString(): String {
            return "Geometry(location=$location)"
        }
    }

    class Location {
        @SerializedName("lat") var latitude:Double = 0.toDouble()
        @SerializedName("lng") var longitude:Double = 0.toDouble()

        override fun toString(): String {
            return "Location(latitude=$latitude, longitude=$longitude)"
        }
    }

    override fun toString(): String {
        return "GoogleMapsApiResponse(status='$status', nextPageToken='$nextPageToken', geometry=${geometry.contentToString()})"
    }
}