package com.example.shoppinglist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.model.GeoCodingResponse
import com.example.shoppinglist.model.GeoCodingResult
import com.example.shoppinglist.model.LocationData
import com.example.shoppinglist.model.RetrofitClient
import com.example.shoppinglist.network.GeoCodingApiService
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel(){

    private val TAG = "LocationViewModel"

    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeoCodingResult>())
    val address: State<List<GeoCodingResult>> = _address

    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latlng: String){
        try {
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(latlng, "AIzaSyDBPqWQYQ1xkwsMQilK8yyYRHHGdKGpOW4")

                _address.value = result.results
            }
        } catch (e:Exception){
            Log.d(TAG, e.toString())
        }
    }
}