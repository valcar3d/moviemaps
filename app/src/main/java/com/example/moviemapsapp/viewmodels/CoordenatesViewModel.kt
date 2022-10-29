package com.example.moviemapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviemapsapp.models.MapDataModel

class CoordinatesViewModel : ViewModel() {

    val mapData: MutableLiveData<MapDataModel> by lazy {
        MutableLiveData<MapDataModel>()
    }
}