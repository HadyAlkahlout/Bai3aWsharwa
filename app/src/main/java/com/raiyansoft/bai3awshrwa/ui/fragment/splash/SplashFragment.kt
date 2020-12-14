package com.raiyansoft.bai3awshrwa.ui.fragment.splash

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentSplashBinding
import com.raiyansoft.bai3awshrwa.ui.activity.InterActivity
import com.raiyansoft.bai3awshrwa.ui.fragment.auth.StartFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.SettingsViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlin.system.exitProcess

class SplashFragment : Fragment() {

    private lateinit var mBinding: FragmentSplashBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private lateinit var updateDialog: Dialog
    private lateinit var updateTitle: TextView
    private lateinit var updateDetails: TextView
    private lateinit var updateOk: Button

    private lateinit var editDialog: Dialog
    private lateinit var editTitle: TextView
    private lateinit var editDetails: TextView
    private lateinit var editOk: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSplashBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        mBinding.imgSplashLogo.startAnimation(anim)
        mBinding.pbWait.startAnimation(anim)

        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        updateDialog = Dialog(activity!!)
        updateDialog.setContentView(R.layout.custom_dialog)
        updateTitle = updateDialog.findViewById(R.id.tvDialogTitle)
        updateDetails = updateDialog.findViewById(R.id.tvDialogText)
        updateOk = updateDialog.findViewById(R.id.btnOk)
        updateDialog.setCancelable(false)
        updateOk.setOnClickListener {
            updateDialog.cancel()
            val appPackageName =
                activity!!.packageName // getPackageName() from Context or Activity object

            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
                activity!!.finish()
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
                activity!!.finish()
            }
        }

        editDialog = Dialog(activity!!)
        editDialog.setContentView(R.layout.custom_dialog)
        editTitle = editDialog.findViewById(R.id.tvDialogTitle)
        editDetails = editDialog.findViewById(R.id.tvDialogText)
        editOk = editDialog.findViewById(R.id.btnOk)
        editDialog.setCancelable(false)
        editOk.setOnClickListener {
            editDialog.cancel()
            exitProcess(0)
        }
        viewModel.dataSettings.observe(viewLifecycleOwner,
            { response ->
                mBinding.pbWait.visibility = View.VISIBLE
                if (response!!.status && response.code == 200) {
                    val pInfo: PackageInfo =
                        activity!!.packageManager.getPackageInfo(activity!!.packageName, 0)
                    val version = pInfo.versionCode
                    if ((response.data.force_update == "yes" || response.data.force_update == "android") && response.data.android_version == version.toString()) {
                        mBinding.pbWait.visibility = View.GONE
                        updateTitle.text = getString(R.string.attention)
                        updateDetails.text = getString(R.string.update)
                        updateDialog.show()
                    } else if (response.data.force_close == "yes" || response.data.force_close == "android") {
                        mBinding.pbWait.visibility = View.GONE
                        editTitle.text = getString(R.string.attention)
                        editDetails.text = getString(R.string.edit)
                        editDialog.show()
                    } else if (response.data.force_update == "no" && response.data.force_close == "no") {
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.PROMOTE, response.data.special).apply()
                        mBinding.pbWait.visibility = View.GONE
                        if (Commons.getSharedPreferences(requireContext())
                                .getString(Commons.USER_TOKEN, "") == ""
                        ) {
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.startContainer, StartFragment()).commit()
                        } else {
                            requireActivity().startActivity(
                                Intent(
                                    requireContext(),
                                    InterActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }
                    } else {
                        mBinding.pbWait.visibility = View.GONE
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                } else {
                    mBinding.pbWait.visibility = View.GONE
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )

    }

}