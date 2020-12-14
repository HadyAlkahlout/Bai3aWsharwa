package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.notification.Notification
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataNotification = MutableLiveData<FullGeneral<Notification>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    var page = 1
    private suspend fun getNotification() {
        dataNotification.postValue(repository.getNotification(country, lang, token, page).body())
    }

    fun getData(){
        if (dataNotification.value != null){
            page++
        }
        viewModelScope.launch {
            getNotification()
        }
    }

    init {
        getData()
    }

}