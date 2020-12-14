package com.raiyansoft.bai3awshrwa.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.ui.fragment.auth.LoginFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.splash.SplashFragment
import com.raiyansoft.bai3awshrwa.util.Commons

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        setLang()
        if (intent != null){
            if (intent.getIntExtra("open", 0) == 0){
                supportFragmentManager.beginTransaction().replace(R.id.startContainer, SplashFragment()).commit()
            }else{
                supportFragmentManager.beginTransaction().replace(R.id.startContainer, LoginFragment()).commit()
            }
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.startContainer, SplashFragment()).commit()
        }

    }

    private fun setLang(){
        var lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}