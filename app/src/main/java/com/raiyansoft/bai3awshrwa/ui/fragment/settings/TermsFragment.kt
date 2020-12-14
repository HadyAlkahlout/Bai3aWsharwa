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
import com.raiyansoft.bai3awshrwa.databinding.FragmentTermsBinding
import com.raiyansoft.bai3awshrwa.ui.viewmodel.SettingsViewModel
import com.raiyansoft.bai3awshrwa.util.Commons

class TermsFragment : Fragment() {

    private lateinit var mBinding: FragmentTermsBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTermsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        mBinding.imgTermsBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        fillTerms()

    }

    private fun fillTerms() {
        loading.show()
        viewModel.dataTerms.observe(viewLifecycleOwner,
            {response ->
                if (response.status && response.code == 200){
                    if (response.data.conditions != ""){
                        mBinding.tvTerms.text = response.data.conditions
                        mBinding.tvTerms.visibility = View.VISIBLE
                        loading.dismiss()
                    }else{
                        mBinding.tvTerms.visibility = View.GONE
                    }
                }else{
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

}