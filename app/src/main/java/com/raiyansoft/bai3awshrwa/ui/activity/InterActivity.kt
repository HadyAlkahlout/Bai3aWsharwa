package com.raiyansoft.bai3awshrwa.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceOnClickListener
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.ActivityInterBinding
import com.raiyansoft.bai3awshrwa.ui.fragment.main.*
import com.raiyansoft.bai3awshrwa.util.Commons


class InterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityInterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityInterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setLang()

        mBinding.bottomNav.addSpaceItem(
            SpaceItem(
                getString(R.string.notification),
                R.drawable.ic_bill
            )
        )
        mBinding.bottomNav.addSpaceItem(
            SpaceItem(
                getString(R.string.profile),
                R.drawable.ic_person
            )
        )
        mBinding.bottomNav.addSpaceItem(SpaceItem(getString(R.string.home), R.drawable.ic_home))
        mBinding.bottomNav.addSpaceItem(
            SpaceItem(
                getString(R.string.addProduct),
                R.drawable.ic_add
            )
        )
        mBinding.bottomNav.showIconOnly()

        mBinding.bottomNav.changeCurrentItem(2)

        Commons.getSharedEditor(this).putInt(Commons.OPEN, 0).apply()
        supportFragmentManager.beginTransaction()
            .replace(R.id.interContainer, HomeFragment()).commit()

        val top = AnimationUtils.loadAnimation(this, R.anim.top_move_anim)
        val down = AnimationUtils.loadAnimation(this, R.anim.down_move_anim)
        mBinding.bottomNav.startAnimation(top)
        mBinding.interContainer.startAnimation(down)

        clickListener()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mBinding.bottomNav.onSaveInstanceState(outState)
    }

    private fun clickListener() {
        mBinding.bottomNav.setSpaceOnClickListener(object : SpaceOnClickListener {
            override fun onCentreButtonClick() {
                startActivity(Intent(this@InterActivity, CategoriesActivity::class.java))
            }

            override fun onItemClick(itemIndex: Int, itemName: String) {
                navigate(itemIndex)
            }

            override fun onItemReselected(itemIndex: Int, itemName: String) {
            }
        })
    }

    private fun navigate(id: Int) {
        when (id) {
            2 -> {
                //Home
                if (Commons.getSharedPreferences(this).getInt(Commons.OPEN, 0) != 0) {
                    Commons.getSharedEditor(this).putInt(Commons.OPEN, 0).apply()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.interContainer, HomeFragment()).commit()
                }
            }
            3 -> {
                //Add
                if (Commons.getSharedPreferences(this).getInt(Commons.OPEN, 0) != 1) {
                    Commons.getSharedEditor(this).putInt(Commons.OPEN, 1).apply()
                    val fragment = AddFragment()
                    val bundle = Bundle()
                    bundle.putInt("case", 0)
                    fragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.interContainer, fragment).commit()
                }
            }
            0 -> {
                //Notification
                if (Commons.getSharedPreferences(this).getInt(Commons.OPEN, 0) != 2) {
                    Commons.getSharedEditor(this).putInt(Commons.OPEN, 2).apply()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.interContainer, NotificationFragment()).commit()
                }
            }
            1 -> {
                //Profile
                if (Commons.getSharedPreferences(this).getInt(Commons.OPEN, 0) != 3) {
                    Commons.getSharedEditor(this).putInt(Commons.OPEN, 3).apply()
                    val fragment = ProfileFragment()
                    val bundle = Bundle()
                    bundle.putInt("open", 0)
                    fragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.interContainer, fragment).commit()
                }
            }
        }
    }

    private fun setLang() {
        var lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}