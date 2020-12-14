package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.general.Fav
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpgradeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataAccount = MutableLiveData<General>()
    val dataProduct = MutableLiveData<General>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun upgradeAccount() {
        dataAccount.postValue(repository.upgradeAccount(country, lang, token).body())
    }

    private suspend fun upgradeProperty(fav: Fav) {
        dataProduct.postValue(repository.upgradeProperty(country, lang, token, fav).body())
    }

    fun account() {
        viewModelScope.launch {
            upgradeAccount()
        }
    }

    fun property(fav: Fav) {
        viewModelScope.launch {
            upgradeProperty(fav)
        }
    }

}