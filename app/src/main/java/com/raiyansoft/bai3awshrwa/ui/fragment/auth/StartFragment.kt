package com.raiyansoft.bai3awshrwa.ui.fragment.auth

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentStartBinding
import com.raiyansoft.bai3awshrwa.util.Commons

class StartFragment : Fragment() {

    private lateinit var mBinding: FragmentStartBinding

    private var lang = ""
    private var country = 0

    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStartBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        mBinding.clArabic.setOnClickListener {
            mBinding.tvArabic.setBackgroundResource(R.drawable.profile_info_style)
            mBinding.tvEnglish.setBackgroundColor(resources.getColor(android.R.color.transparent))
            lang = "ar"
        }

        mBinding.clEnglish.setOnClickListener {
            mBinding.tvEnglish.setBackgroundResource(R.drawable.profile_info_style)
            mBinding.tvArabic.setBackgroundColor(resources.getColor(android.R.color.transparent))
            lang = "en"
        }

        mBinding.clEgypt.setOnClickListener {
            mBinding.clEgypt.setBackgroundResource(R.drawable.profile_info_style)
            mBinding.clKuwait.setBackgroundColor(resources.getColor(android.R.color.transparent))
            mBinding.clSaudia.setBackgroundColor(resources.getColor(android.R.color.transparent))
            country = 3
        }

        mBinding.clSaudia.setOnClickListener {
            mBinding.clSaudia.setBackgroundResource(R.drawable.profile_info_style)
            mBinding.clKuwait.setBackgroundColor(resources.getColor(android.R.color.transparent))
            mBinding.clEgypt.setBackgroundColor(resources.getColor(android.R.color.transparent))
            country = 2
        }

        mBinding.clKuwait.setOnClickListener {
            mBinding.clKuwait.setBackgroundResource(R.drawable.profile_info_style)
            mBinding.clSaudia.setBackgroundColor(resources.getColor(android.R.color.transparent))
            mBinding.clEgypt.setBackgroundColor(resources.getColor(android.R.color.transparent))
            country = 1
        }

        mBinding.btnEnter.setOnClickListener {
            if (lang == "" || country == 0) {
                title.text = getString(R.string.attention)
                details.text = getString(R.string.empty_fields)
                dialog.show()
            } else {
                Commons.getSharedEditor(requireContext()).putString(
                    Commons.LANGUAGE,
                    lang
                ).apply()
                Commons.getSharedEditor(requireContext()).putInt(
                    Commons.COUNTRY,
                    country
                ).apply()
                requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.startContainer, LoginFragment()).commit()
            }
        }
    }

}