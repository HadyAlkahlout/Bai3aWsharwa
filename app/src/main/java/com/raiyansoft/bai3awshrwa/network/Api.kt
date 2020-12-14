package com.raiyansoft.bai3awshrwa.network

import com.raiyansoft.bai3awshrwa.model.about.About
import com.raiyansoft.bai3awshrwa.model.allProduct.AllProducts
import com.raiyansoft.bai3awshrwa.model.allSellers.AllSellers
import com.raiyansoft.bai3awshrwa.model.categories.Category
import com.raiyansoft.bai3awshrwa.model.cities.City
import com.raiyansoft.bai3awshrwa.model.editProfile.EditProfile
import com.raiyansoft.bai3awshrwa.model.fav.Favorites
import com.raiyansoft.bai3awshrwa.model.general.Ad
import com.raiyansoft.bai3awshrwa.model.general.Fav
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.model.login.Activation
import com.raiyansoft.bai3awshrwa.model.settings.CallInfo
import com.raiyansoft.bai3awshrwa.model.general.FullGeneral
import com.raiyansoft.bai3awshrwa.model.home.Home
import com.raiyansoft.bai3awshrwa.model.login.Login
import com.raiyansoft.bai3awshrwa.model.notification.Notification
import com.raiyansoft.bai3awshrwa.model.product.Product
import com.raiyansoft.bai3awshrwa.model.profile.Profile
import com.raiyansoft.bai3awshrwa.model.questions.Ques
import com.raiyansoft.bai3awshrwa.model.seller.Seller
import com.raiyansoft.bai3awshrwa.model.settings.Setting
import com.raiyansoft.bai3awshrwa.model.terms.Terms
import com.raiyansoft.bai3awshrwa.model.usagePolicy.Policy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("setting")
    suspend fun getSettings(
        @Header("country") country: Int
    ) : Response<FullGeneral<Setting>>

    @GET("cities")
    suspend fun getCities(
        @Header("country") country: Int,
        @Header("lang") lang: String
    ) : Response<FullGeneral<List<City>>>

    @GET("regions/{id}")
    suspend fun getRegions(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Path("id") cityId: Int
    ) : Response<FullGeneral<List<City>>>

    @Multipart
    @POST("user/register")
    suspend fun register(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ) : Response<FullGeneral<Login>>

    @GET("user/logout")
    suspend fun logout(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<General>

    @POST("user/activateAccount")
    suspend fun activateAccount(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body activation: Activation
    ) : Response<General>

    @POST("user/resendActivation")
    suspend fun resendActivation(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<General>

    @GET("home")
    suspend fun getHome(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<FullGeneral<Home>>

    @GET("user/notification")
    suspend fun getNotification(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int
    ) : Response<FullGeneral<Notification>>

    @GET("user/read/{id}")
    suspend fun readNotification(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: Int
    ) : Response<General>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<FullGeneral<Profile>>

    @GET("advertisements/broker/{id}")
    suspend fun getSeller(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") sellerId: Int
    ) : Response<FullGeneral<Seller>>

    @Multipart
    @POST("user/update")
    suspend fun editProfile(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part avatar: MultipartBody.Part
    ): Response<FullGeneral<EditProfile>>

    @Multipart
    @POST("user/update")
    suspend fun editProfile(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<FullGeneral<EditProfile>>

    @GET("faq")
    suspend fun getFaq(
        @Header("country") country: Int,
        @Header("lang") lang: String,
    ): Response<FullGeneral<Ques>>

    @GET("privacy")
    suspend fun getPolicy(
        @Header("country") country: Int,
        @Header("lang") lang: String,
    ): Response<FullGeneral<Policy>>

    @GET("about")
    suspend fun aboutUs(
        @Header("country") country: Int,
        @Header("lang") lang: String,
    ): Response<FullGeneral<About>>

    @GET("conditions")
    suspend fun getTerms(
        @Header("country") country: Int,
        @Header("lang") lang: String,
    ): Response<FullGeneral<Terms>>

    @POST("contactUs")
    suspend fun callUs(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body call: CallInfo
    ): Response<General>

    @GET("advertisements/users/special")
    suspend fun getSpecialSeller(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int
    ): Response<FullGeneral<AllSellers>>

    @GET("advertisements/users/all")
    suspend fun getAllSeller(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int
    ): Response<FullGeneral<AllSellers>>

    @GET("advertisements/index/special")
    suspend fun getSpecialProduct(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int
    ): Response<FullGeneral<AllProducts>>

    @GET("advertisements/index/all")
    suspend fun getAllProduct(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("cat_id") cat_id: Int,
        @Query("city_id") city_id: Int,
        @Query("region_id") region_id: Int
    ): Response<FullGeneral<AllProducts>>

    @GET("advertisements/index/all")
    suspend fun getAllProduct(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
        @Query("order") order: Int,
        @Query("cat_id") cat_id: Int
    ): Response<FullGeneral<AllProducts>>

    @GET("advertisements/details/{id}")
    suspend fun getProduct(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") productId: Int
    ) : Response<FullGeneral<Product>>

    @GET("advertisements/categories")
    suspend fun getCategories(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<FullGeneral<List<Category>>>

    @GET("advertisements/categories")
    suspend fun getCategories(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("parent_id") parent_id: Int
    ) : Response<FullGeneral<List<Category>>>

    @Multipart
    @POST("advertisements/createProperty")
    suspend fun createProduct(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<FullGeneral<Ad>>

    @Multipart
    @POST("advertisements/updateProperty")
    suspend fun updateProduct(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<General>

    @GET("advertisements/deleteImage/{id}")
    suspend fun deleteImage(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: Int
    ) : Response<General>

    @POST("advertisements/addFav")
    suspend fun addFav(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body fav: Fav
    ) : Response<General>

    @POST("advertisements/deleteFav")
    suspend fun deleteFav(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body fav: Fav
    ) : Response<General>

    @GET("advertisements/getFav")
    suspend fun getFav(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int
    ) : Response<FullGeneral<Favorites>>

    @GET("user/specialUpgrade")
    suspend fun upgradeAccount(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<General>

    @POST("user/specialProperty")
    suspend fun upgradeProperty(
        @Header("country") country: Int,
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body fav: Fav
    ) : Response<General>

}