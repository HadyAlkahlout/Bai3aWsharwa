package com.raiyansoft.bai3awshrwa.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.bai3awshrwa.model.allProduct.AllProducts
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.product.Product
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    val dataProduct = MutableLiveData<FullGeneral<Product>>()
    val dataSpecialProducts = MutableLiveData<FullGeneral<AllProducts>>()
    val dataAllProducts = MutableLiveData<FullGeneral<AllProducts>>()
    val dataCategoryProducts = MutableLiveData<FullGeneral<AllProducts>>()
    val dataSearchProducts = MutableLiveData<FullGeneral<AllProducts>>()

    private val country = Commons.getSharedPreferences(application.applicationContext).getInt(Commons.COUNTRY, 0)
    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.USER_TOKEN, "")!!

    private suspend fun getProduct(id: Int) {
        dataProduct.postValue(repository.getProduct(country, lang, token, id).body())
    }

    fun product(id: Int) {
        viewModelScope.launch {
            getProduct(id)
        }
    }

    var page = 1
    private suspend fun getSpecialProduct() {
        dataSpecialProducts.postValue(repository.getSpecialProduct(country, lang, token, page).body())
    }

    fun special() {
        if (dataSpecialProducts.value != null){
            page++
        }
        viewModelScope.launch {
            getSpecialProduct()
        }
    }

    var allPage = 1
    private suspend fun getAllProduct() {
        dataAllProducts.postValue(repository.getAllProduct(country, lang, token, allPage, 0, 0, 0).body())
    }

    fun all() {
        if (dataAllProducts.value != null){
            allPage++
        }
        viewModelScope.launch {
            getAllProduct()
        }
    }

    var cityPage = 1
    var cityPages = 0
    private suspend fun getAllProduct(cat_id: Int, city_id: Int, region_id: Int, type: Int) {
        if (type == 0){
            cityPage = 1
            var response = repository.getAllProduct(country, lang, token, cityPage, cat_id, city_id, region_id).body()
            cityPages = response!!.data.pages
            dataCategoryProducts.value = response
        }else {
            cityPage++
            if (cityPage <= cityPages){
                dataCategoryProducts.postValue(repository.getAllProduct(country, lang, token, cityPage, cat_id, city_id, region_id).body())
            }
        }
    }

    fun all(cat_id: Int, city_id: Int, region_id: Int, type: Int) {
        viewModelScope.launch {
            getAllProduct(cat_id, city_id, region_id, type)
        }
    }

    var searchPage = 1
    var searchPages = 0
    private suspend fun getAllProduct(keyword: String, order: Int, cat_id: Int, type: Int ){
        if (type == 0){
            searchPage = 1
            var response = repository.getAllProduct(country, lang, token, searchPage, keyword, order, cat_id).body()
            searchPages = response!!.data.pages
            dataSearchProducts.value = response

        }else{
            searchPage++
            if (searchPage <= searchPages){
                dataSearchProducts.postValue(repository.getAllProduct(country, lang, token, searchPage, keyword, order, cat_id).body())
            }
        }
    }

    fun all(keyword: String, order: Int, cat_id: Int, type: Int) {
        viewModelScope.launch {
            getAllProduct(keyword, order, cat_id, type)
        }
    }

    init {
        special()
        all()
    }

}