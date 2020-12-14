package com.raiyansoft.bai3awshrwa.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.ActivityCategoriesBinding
import com.raiyansoft.bai3awshrwa.ui.fragment.main.CategoriesFragment
import com.raiyansoft.bai3awshrwa.util.Commons

class CategoriesActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setLang()

        supportFragmentManager.beginTransaction()
            .replace(R.id.categoryContainer, CategoriesFragment()).commit()

    }

    private fun setLang() {
        var lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}