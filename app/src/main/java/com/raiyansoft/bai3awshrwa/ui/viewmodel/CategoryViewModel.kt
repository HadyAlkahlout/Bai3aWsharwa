package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.categories.Category
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataCategory = MutableLiveData<FullGeneral<List<Category>>>()
    val dataSubcategory = MutableLiveData<FullGeneral<List<Category>>>()
    val dataSubcategory2 = MutableLiveData<FullGeneral<List<Category>>>()
    val dataSubcategory3 = MutableLiveData<FullGeneral<List<Category>>>()
    val dataSubcategory4 = MutableLiveData<FullGeneral<List<Category>>>()
    val dataSubcategory5 = MutableLiveData<FullGeneral<List<Category>>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun getCategories() {
        dataCategory.postValue(repository.getCategories(country, lang, token).body())
    }

    private suspend fun getCategories(parent_id: Int) {
        dataSubcategory.postValue(repository.getCategories(country, lang, token, parent_id).body())
    }

    private suspend fun getCategories2(parent_id: Int) {
        dataSubcategory2.postValue(repository.getCategories(country, lang, token, parent_id).body())
    }

    private suspend fun getCategories3(parent_id: Int) {
        dataSubcategory3.postValue(repository.getCategories(country, lang, token, parent_id).body())
    }

    private suspend fun getCategories4(parent_id: Int) {
        dataSubcategory4.postValue(repository.getCategories(country, lang, token, parent_id).body())
    }

    private suspend fun getCategories5(parent_id: Int) {
        dataSubcategory5.postValue(repository.getCategories(country, lang, token, parent_id).body())
    }

    private fun getData() {
        viewModelScope.launch {
            getCategories()
        }
    }

    fun subCategory(parent_id: Int) {
        viewModelScope.launch {
            getCategories(parent_id)
        }
    }

    fun subCategory2(parent_id: Int) {
        viewModelScope.launch {
            getCategories2(parent_id)
        }
    }

    fun subCategory3(parent_id: Int) {
        viewModelScope.launch {
            getCategories3(parent_id)
        }
    }

    fun subCategory4(parent_id: Int) {
        viewModelScope.launch {
            getCategories4(parent_id)
        }
    }

    fun subCategory5(parent_id: Int) {
        viewModelScope.launch {
            getCategories5(parent_id)
        }
    }

    init {
        getData()
    }

}