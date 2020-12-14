package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.cities.City
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitiesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataCities = MutableLiveData<FullGeneral<List<City>>>()
    val dataRegions = MutableLiveData<FullGeneral<List<City>>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!

    private suspend fun getCities(){
        dataCities.postValue(repository.getCities(country, lang).body())
    }

    private suspend fun getRegions(cityId: Int){
        dataRegions.postValue(repository.getRegions(country, lang, cityId).body())
    }

    private fun getData(){
        viewModelScope.launch {
            getCities()
        }
    }

    fun region(id: Int){
        viewModelScope.launch {
            getRegions(id)
        }
    }

    init {
        getData()
    }
}