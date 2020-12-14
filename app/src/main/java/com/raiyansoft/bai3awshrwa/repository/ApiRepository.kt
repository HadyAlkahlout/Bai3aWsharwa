package com.raiyansoft.bai3awshrwa.repository

import com.raiyansoft.bai3awshrwa.model.general.Fav
import com.raiyansoft.bai3awshrwa.model.login.Activation
import com.raiyansoft.bai3awshrwa.model.settings.CallInfo
import com.raiyansoft.bai3awshrwa.network.ServiceBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiRepository {

    suspend fun getSettings(country: Int) = ServiceBuilder.apis!!.getSettings(country)

    suspend fun getCities(country: Int, lang: String) = ServiceBuilder.apis!!.getCities(country, lang)

    suspend fun getRegions(country: Int, lang: String, cityId: Int) =
        ServiceBuilder.apis!!.getRegions(country, lang, cityId)

    suspend fun register(country: Int, lang: String, params: Map<String, RequestBody>) =
        ServiceBuilder.apis!!.register(country, lang, params)

    suspend fun logout(country: Int, lang: String, Authorization: String) =
        ServiceBuilder.apis!!.logout(country, lang, Authorization)

    suspend fun activate(country: Int, lang: String, Authorization: String, activation: Activation) =
        ServiceBuilder.apis!!.activateAccount(country, lang, Authorization, activation)

    suspend fun resendActivation(country: Int, lang: String, Authorization: String) =
        ServiceBuilder.apis!!.resendActivation(country, lang, Authorization)

    suspend fun getHome(country: Int, lang: String, Authorization: String) =
        ServiceBuilder.apis!!.getHome(country, lang, Authorization)

    suspend fun getNotification(country: Int, lang: String, Authorization: String, page: Int) =
        ServiceBuilder.apis!!.getNotification(country, lang, Authorization, page)

    suspend fun readNotification(country: Int, lang: String, Authorization: String, id: Int) =
        ServiceBuilder.apis!!.readNotification(country, lang, Authorization, id)

    suspend fun getProfile(country: Int, lang: String, Authorization: String) =
        ServiceBuilder.apis!!.getProfile(country, lang, Authorization)

    suspend fun getSeller(country: Int, lang: String, Authorization: String, id: Int) =
        ServiceBuilder.apis!!.getSeller(country, lang, Authorization, id)

    suspend fun editProfile(
        country: Int,
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>,
        avatar: MultipartBody.Part
    ) =
        ServiceBuilder.apis!!.editProfile(country, lang, Authorization, params, avatar)

    suspend fun editProfile(
        country: Int,
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.editProfile(country, lang, Authorization, params)

    suspend fun getFaq(country: Int, lang: String) = ServiceBuilder.apis!!.getFaq(country, lang)

    suspend fun getPolicy(country: Int, lang: String) = ServiceBuilder.apis!!.getPolicy(country, lang)

    suspend fun getTerms(country: Int, lang: String) = ServiceBuilder.apis!!.getTerms(country, lang)

    suspend fun aboutUs(country: Int, lang: String) = ServiceBuilder.apis!!.aboutUs(country, lang)

    suspend fun callUs(country: Int, lang: String, Authorization: String, call: CallInfo) =
        ServiceBuilder.apis!!.callUs(country, lang, Authorization, call)

    suspend fun getSpecialSellers(country: Int, lang: String, Authorization: String, page: Int) =
        ServiceBuilder.apis!!.getSpecialSeller(country, lang, Authorization, page)

    suspend fun getAllSellers(country: Int, lang: String, Authorization: String, page: Int) =
        ServiceBuilder.apis!!.getAllSeller(country, lang, Authorization, page)

    suspend fun getSpecialProduct(country: Int, lang: String, Authorization: String, page: Int) =
        ServiceBuilder.apis!!.getSpecialProduct(country, lang, Authorization, page)

    suspend fun getAllProduct(
        country: Int,
        lang: String,
        Authorization: String,
        page: Int,
        cat_id: Int,
        city_id: Int,
        region_id: Int
    ) =
        ServiceBuilder.apis!!.getAllProduct(country, lang, Authorization, page, cat_id, city_id, region_id)

    suspend fun getAllProduct(
        country: Int,
        lang: String,
        Authorization: String,
        page: Int,
        keyword: String,
        order: Int,
        cat_id: Int
    ) =
        ServiceBuilder.apis!!.getAllProduct(country, lang, Authorization, page, keyword, order, cat_id)

    suspend fun getProduct(country: Int, lang: String, Authorization: String, id: Int) =
        ServiceBuilder.apis!!.getProduct(country, lang, Authorization, id)

    suspend fun getCategories(country: Int, lang: String, Authorization: String) =
        ServiceBuilder.apis!!.getCategories(country, lang, Authorization)

    suspend fun getCategories(country: Int, lang: String, Authorization: String, parent_id: Int) =
        ServiceBuilder.apis!!.getCategories(country, lang, Authorization, parent_id)

    suspend fun createProduct(
        country: Int,
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) =
        ServiceBuilder.apis!!.createProduct(country, lang, Authorization, params, images)

    suspend fun updateProduct(
        country: Int,
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) =
        ServiceBuilder.apis!!.updateProduct(country, lang, Authorization, params, images)

    suspend fun deleteImage(country: Int, lang: String, Authorization: String, id: Int) =
        ServiceBuilder.apis!!.deleteImage(country, lang, Authorization, id)

    suspend fun addFav(country: Int, lang: String, Authorization: String, fav: Fav) =
        ServiceBuilder.apis!!.addFav(country, lang, Authorization, fav)

    suspend fun deleteFav(country: Int, lang: String, Authorization: String, fav: Fav) =
        ServiceBuilder.apis!!.deleteFav(country, lang, Authorization, fav)

    suspend fun getFav(country: Int, lang: String, Authorization: String, page: Int) =
        ServiceBuilder.apis!!.getFav(country, lang, Authorization, page)

    suspend fun upgradeAccount(country: Int, lang: String, Authorization: String) =
        ServiceBuilder.apis!!.upgradeAccount(country, lang, Authorization)

    suspend fun upgradeProperty(country: Int, lang: String, Authorization: String, fav: Fav) =
        ServiceBuilder.apis!!.upgradeProperty(country, lang, Authorization, fav)

}