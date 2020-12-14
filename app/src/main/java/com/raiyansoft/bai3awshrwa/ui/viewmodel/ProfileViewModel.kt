package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.editProfile.EditProfile
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.profile.Profile
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataProfile = MutableLiveData<FullGeneral<Profile>>()
    val dataEditProfile = MutableLiveData<FullGeneral<EditProfile>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun getProfile() {
        dataProfile.value = null
        dataProfile.postValue(repository.getProfile(country, lang, token).body())
    }

    private suspend fun editProfile(params: Map<String, RequestBody>, avatar: MultipartBody.Part) {
        dataEditProfile.postValue(repository.editProfile(country, lang, token, params, avatar).body())
    }

    private suspend fun editProfile(params: Map<String, RequestBody>) {
        dataEditProfile.postValue(repository.editProfile(country, lang, token, params).body())
    }

    fun getData() {
        viewModelScope.launch {
            getProfile()
        }
    }

    fun makeEdit(params: Map<String, RequestBody>, avatar: MultipartBody.Part){
        viewModelScope.launch {
            editProfile(params, avatar)
        }
    }

    fun makeEdit(params: Map<String, RequestBody>){
        viewModelScope.launch {
            editProfile(params)
        }
    }

    init {
        getData()
    }

}