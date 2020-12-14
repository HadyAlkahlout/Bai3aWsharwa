package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.model.login.Activation
import com.raiyansoft.bai3awshrwa.model.login.Login
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataLogin = MutableLiveData<FullGeneral<Login>>()
    val dataActivate = MutableLiveData<General>()
    val dataResend = MutableLiveData<General>()
    val dataLogout = MutableLiveData<General>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun register(params: Map<String, RequestBody>){
        dataLogin.postValue(repository.register(country, lang, params).body())
    }

    private suspend fun activate(activation: Activation){
        dataActivate.postValue(repository.activate(country, lang, token, activation).body())
    }

    private suspend fun resend(){
        dataResend.postValue(repository.resendActivation(country, lang, token).body())
    }

    private suspend fun logout(){
        dataLogout.postValue(repository.logout(country, lang, token).body())
    }

    fun makeAccount(params: Map<String, RequestBody>) {
        viewModelScope.launch {
            register(params)
        }
    }

    fun activateAccount(activation: Activation) {
        viewModelScope.launch {
            activate(activation)
        }
    }

    fun resendActivation() {
        viewModelScope.launch {
            resend()
        }
    }

    fun exitAccount() {
        viewModelScope.launch {
            logout()
        }
    }

}