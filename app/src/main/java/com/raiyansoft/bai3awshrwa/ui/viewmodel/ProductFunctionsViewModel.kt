package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.fav.Favorites
import com.raiyansoft.bai3awshrwa.model.general.Ad
import com.raiyansoft.bai3awshrwa.model.general.Fav
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFunctionsViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository = ApiRepository()
    val dataCreate = MutableLiveData<FullGeneral<Ad>>()
    val dataUpdate = MutableLiveData<General>()
    val dataDeleteImage = MutableLiveData<General>()
    val dataFav = MutableLiveData<General>()
    val dataGetFav = MutableLiveData<FullGeneral<Favorites>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun createProduct(params: Map<String, RequestBody>, images: List<MultipartBody.Part>) {
        dataCreate.postValue(repository.createProduct(country, lang, token, params, images).body())
    }

    fun create(params: Map<String, RequestBody>, images: List<MultipartBody.Part>) {
        viewModelScope.launch {
            createProduct(params, images)
        }
    }

    private suspend fun updateProduct(params: Map<String, RequestBody>, images: List<MultipartBody.Part>) {
        dataUpdate.postValue(repository.updateProduct(country, lang, token, params, images).body())
    }

    fun update(params: Map<String, RequestBody>, images: List<MultipartBody.Part>) {
        viewModelScope.launch {
            updateProduct(params, images)
        }
    }

    private suspend fun deleteImage(id: Int) {
        dataDeleteImage.postValue(repository.deleteImage(country, lang, token, id).body())
    }

    fun deleteIMG(id: Int) {
        viewModelScope.launch {
            deleteImage(id)
        }
    }

    private suspend fun addFav(fav: Fav) {
        dataFav.postValue(repository.addFav(country, lang, token, fav).body())
    }

    fun add(fav: Fav) {
        viewModelScope.launch {
            addFav(fav)
        }
    }

    private suspend fun deleteFav(fav: Fav) {
        dataFav.postValue(repository.deleteFav(country, lang, token, fav).body())
    }

    fun delete(fav: Fav) {
        viewModelScope.launch {
            deleteFav(fav)
        }
    }

    var page = 1
    private suspend fun getFav() {
        dataGetFav.postValue(repository.getFav(country, lang, token, page).body())
    }

    fun favorites() {
        if (dataGetFav.value != null){
            page++
        }
        viewModelScope.launch {
            getFav()
        }
    }

    init {
        favorites()
    }

}