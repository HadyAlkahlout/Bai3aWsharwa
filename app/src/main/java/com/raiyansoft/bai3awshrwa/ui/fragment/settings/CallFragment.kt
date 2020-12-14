package com.raiyansoft.bai3awshrwa.ui.fragment.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentCallBinding
import com.raiyansoft.bai3awshrwa.model.settings.CallInfo
import com.raiyansoft.bai3awshrwa.ui.viewmodel.SettingsViewModel
import com.raiyansoft.bai3awshrwa.util.Commons

class CallFragment : Fragment() {

    private lateinit var mBinding: FragmentCallBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    private lateinit var name: String
    private lateinit var mobile: String

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCallBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = Commons.getSharedPreferences(requireContext()).getString(Commons.USER_NAME, "ar")!!
        mobile = Commons.getSharedPreferences(requireContext()).getString(Commons.USER_MOBILE, "")!!

        loading = Dialog(requireContext())
        loading.setContentView(R.layout.dialog_loading)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(false)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        ok.setOnClickListener {
            dialog.cancel()
        }

        mBinding.imgCallBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        mBinding.btnSend.setOnClickListener {
            mBinding.btnSend.startAnimation {
                call()
            }
        }
    }

    private fun call() {
        if(mBinding.edEmail.text!!.isEmpty() || mBinding.edMessage.text!!.isEmpty()){
            mBinding.btnSend.revertAnimation()
            title.text = getString(R.string.attention)
            details.text = getString(R.string.empty_fields)
            dialog.show()
        }else{
            loading.show()
            val call = CallInfo(name, mobile, mBinding.edEmail.text.toString(), mBinding.edMessage.text.toString())
            viewModel.sendCall(call)
            viewModel.dataCall.observe(viewLifecycleOwner,
                {response ->
                    if (response.status && response.code == 200){
                        mBinding.edEmail.setText("")
                        mBinding.edMessage.setText("")
                        loading.dismiss()
                        mBinding.btnSend.revertAnimation{
                            mBinding.btnSend.text = getString(R.string.send_result)
                        }
                    }else{
                        mBinding.btnSend.revertAnimation()
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            )
        }
    }

}