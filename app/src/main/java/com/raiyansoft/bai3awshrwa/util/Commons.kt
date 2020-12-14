package com.raiyansoft.bai3awshrwa.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.*

object Commons {

    const val LANGUAGE = "lang"
    const val COUNTRY = "country"
    const val USER_TOKEN = "userToken"
    const val USER_ID = "userId"
    const val USER_IMAGE = "userImage"
    const val USER_NAME = "userName"
    const val USER_NOTE = "userNote"
    const val USER_MOBILE = "userMobile"
    const val DEVICE_TOKEN = "deviceToken"
    const val PROMOTE = "promote"
    const val SPECIAL = "special"
    const val OPEN = "open"
    const val CAT_ID = "catId"
    const val CAT_NAME = "catName"
    const val SUB_NO = "subNo"
    const val PRODUCT_ID = "productId"

    fun setLocale(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources
            .updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

    fun getSharedEditor(context: Context) : SharedPreferences.Editor = getSharedPreferences(context).edit()

}