package com.raiyansoft.bai3awshrwa.ui.fragment.auth

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentLoginBinding
import com.raiyansoft.bai3awshrwa.ui.viewmodel.LoginViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }
    private var coutry = 0

    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var deviceToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        mBinding.clLayout.startAnimation(anim)

        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        getFCMToken(requireContext())

        coutry = Commons.getSharedPreferences(requireContext()).getInt(Commons.COUNTRY, 0)

        mBinding.btnLogin.setOnClickListener {
            mBinding.btnLogin.startAnimation {
                loginClick()
            }
        }
    }

    private fun loginClick() {
        if (mBinding.edName.text!!.isEmpty() || mBinding.edPhone.text!!.isEmpty()) {
            title.text = getString(R.string.login_faild)
            details.text = getString(R.string.empty_fields)
            dialog.show()
            mBinding.btnLogin.revertAnimation {
                mBinding.btnLogin.text = getString(R.string.try_again)
            }
        } else {
            val name = mBinding.edName.text.toString()
            val email = mBinding.edEmail.text.toString()
            var phone = ""
            when (coutry) {
                1 -> {
                    phone = "965"
                }
                2 -> {
                    phone = "966"
                }
                3 -> {
                    phone = "20"
                }
            }
            phone += mBinding.edPhone.text.toString()
            val map: MutableMap<String, RequestBody> = HashMap()
            map["full_name"] = toRequestBody(mBinding.edName.text.toString())
            map["mobile_number"] = toRequestBody(phone)
            map["device_type"] = toRequestBody("android")
            map["device_token"] = toRequestBody(deviceToken)
            if (mBinding.edEmail.text!!.isNotEmpty()){
                map["email"] = toRequestBody(mBinding.edEmail.text.toString())
            }
            viewModel.makeAccount(map)
            viewModel.dataLogin.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_TOKEN, "Bearer ${response.data.token}")
                            .apply()
                        Commons.getSharedEditor(requireContext())
                            .putInt(Commons.USER_ID, response.data.user_id).apply()
                        mBinding.btnLogin.revertAnimation()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.startContainer, ActivationFragment()).commit()
                    } else {
                        Log.e("hdhd", "loginClick: ${response.code}")
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                        mBinding.btnLogin.revertAnimation {
                            mBinding.btnLogin.text = getString(R.string.try_again)
                        }
                    }
                }
            )
        }
    }

    private fun getFCMToken(context: Context) {
        FirebaseMessaging.getInstance().subscribeToTopic("allUsers").addOnSuccessListener {
            Log.e("hdhd", "getFCMToken: subscribing the topic done successfully")


            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    deviceToken = task.result?.token!!
                    Commons.getSharedEditor(requireContext())
                        .putString(Commons.DEVICE_TOKEN, task.result?.token!!).apply()
                })
        }

    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }
}