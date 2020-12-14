package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.about.About
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.model.settings.CallInfo
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.questions.Ques
import com.raiyansoft.bai3awshrwa.model.settings.Setting
import com.raiyansoft.bai3awshrwa.model.terms.Terms
import com.raiyansoft.bai3awshrwa.model.usagePolicy.Policy
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataSettings = MutableLiveData<FullGeneral<Setting>>()
    val dataQuestions = MutableLiveData<FullGeneral<Ques>>()
    val dataPolicy = MutableLiveData<FullGeneral<Policy>>()
    val dataTerms = MutableLiveData<FullGeneral<Terms>>()
    val dataAbout = MutableLiveData<FullGeneral<About>>()
    val dataCall = MutableLiveData<General>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun getSettingsData() {
        dataSettings.postValue(repository.getSettings(country).body())
    }

    private suspend fun editFaq() {
        dataQuestions.postValue(repository.getFaq(country, lang).body())
    }

    private suspend fun editPolicy() {
        dataPolicy.postValue(repository.getPolicy(country, lang).body())
    }

    private suspend fun editTerms() {
        dataTerms.postValue(repository.getTerms(country, lang).body())
    }

    private suspend fun aboutUs() {
        dataAbout.postValue(repository.aboutUs(country, lang).body())
    }

    private suspend fun callUs(call: CallInfo) {
        dataCall.postValue(repository.callUs(country, lang, token, call).body())
    }

    private fun getData(){
        viewModelScope.launch {
            getSettingsData()
            editFaq()
            editPolicy()
            editTerms()
            aboutUs()
        }
    }

    fun sendCall(call: CallInfo){
        viewModelScope.launch {
            callUs(call)
        }
    }

    init {
        getData()
    }

}