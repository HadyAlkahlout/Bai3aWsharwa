package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.allSellers.AllSellers
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.seller.Seller
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataSeller = MutableLiveData<FullGeneral<Seller>>()
    val dataSpecialSeller = MutableLiveData<FullGeneral<AllSellers>>()
    val dataAllSeller = MutableLiveData<FullGeneral<AllSellers>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun getSeller(id: Int) {
        dataSeller.postValue(repository.getSeller(country, lang, token, id).body())
    }

    var sPage = 1
    private suspend fun getSpecialSeller() {
        dataSpecialSeller.postValue(repository.getSpecialSellers(country, lang, token, sPage).body())
    }

    var page = 1
    private suspend fun getAllSeller() {
        dataAllSeller.postValue(repository.getAllSellers(country, lang, token, page).body())
    }

    fun getSellerProfile(id: Int) {
        viewModelScope.launch {
            getSeller(id)
        }
    }

    fun getAllData() {
        if (dataAllSeller.value != null){
            page++
        }
        viewModelScope.launch {
            getAllSeller()
        }
    }

    fun getSpecialData() {
        if (dataSpecialSeller.value != null){
            sPage++
        }
        viewModelScope.launch {
            getSpecialSeller()
        }
    }

    init {
        getAllData()
        getSpecialData()
    }

}