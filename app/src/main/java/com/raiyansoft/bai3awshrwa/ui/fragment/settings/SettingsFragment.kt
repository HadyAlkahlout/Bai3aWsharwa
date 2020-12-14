package com.raiyansoft.bai3awshrwa.ui.fragment.settings

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentSettingsBinding
import com.raiyansoft.bai3awshrwa.ui.activity.InterActivity
import com.raiyansoft.bai3awshrwa.ui.activity.StartActivity
import com.raiyansoft.bai3awshrwa.ui.viewmodel.LoginViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.UpgradeViewModel
import com.raiyansoft.bai3awshrwa.util.Commons

class SettingsFragment : Fragment() {

    private lateinit var mBinding: FragmentSettingsBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private val specialViewModel by lazy {
        ViewModelProvider(this)[UpgradeViewModel::class.java]
    }

    private lateinit var lang: String
    private var country = 0
    private lateinit var special: String
    private var areSpecial: Boolean = false

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSettingsBinding.inflate(inflater,container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lang = Commons.getSharedPreferences(requireContext()).getString(Commons.LANGUAGE, "ar")!!
        country = Commons.getSharedPreferences(requireContext()).getInt(Commons.COUNTRY, 0)
        special = Commons.getSharedPreferences(requireContext()).getString(Commons.PROMOTE, "")!!
        areSpecial = Commons.getSharedPreferences(requireContext()).getBoolean(Commons.SPECIAL, false)

        if (areSpecial){
            mBinding.tvDisc.visibility = View.GONE
            mBinding.tvDiscView.visibility = View.GONE
        }else{
            if (special == "no"){
                mBinding.tvDisc.visibility = View.GONE
                mBinding.tvDiscView.visibility = View.GONE
            }
        }

        loading = Dialog(activity!!)
        loading.setContentView(R.layout.dialog_loading)
        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        mBinding.imgSettingsBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        mBinding.tvLogout.setOnClickListener {
            viewModel.exitAccount()
            viewModel.dataLogout.observe(viewLifecycleOwner,
                {response ->
                    if (response.status && response.code == 200) {
                        Commons.getSharedEditor(requireContext()).clear().apply()
                        val intent = Intent(requireContext(), StartActivity::class.java)
                        intent.putExtra("open", 1)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    } else {
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            )
        }

        mBinding.tvLanguage.setOnClickListener {
            val langDialog = Dialog(requireContext())
            langDialog.setContentView(R.layout.language_dialoge)
            langDialog.setCancelable(true)
            val pick = langDialog.findViewById<Button>(R.id.btnPickLang)
            val english = langDialog.findViewById<RadioButton>(R.id.rbEnglish)
            val arabic = langDialog.findViewById<RadioButton>(R.id.rbArabic)
            when (lang) {
                "en" -> {
                    english.isChecked = true
                    arabic.isChecked = false
                }
                "ar" -> {
                    english.isChecked = false
                    arabic.isChecked = true
                }
            }
            pick.setOnClickListener {
                if (english.isChecked) {
                    Commons.getSharedEditor(requireContext()).putString(Commons.LANGUAGE, "en").apply()
                } else if (arabic.isChecked) {
                    Commons.getSharedEditor(requireContext()).putString(Commons.LANGUAGE, "ar").apply()
                } else {
                    langDialog.dismiss()
                }
                val intent = Intent(activity!!, InterActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }
            langDialog.show()
        }

        mBinding.tvCountry.setOnClickListener {
            val countryDialog = Dialog(requireContext())
            countryDialog.setContentView(R.layout.dialog_country)
            countryDialog.setCancelable(true)
            val set = countryDialog.findViewById<Button>(R.id.btnPickCountry)
            val kuwait = countryDialog.findViewById<RadioButton>(R.id.rbKuwait)
            val saudi = countryDialog.findViewById<RadioButton>(R.id.rbSaudi)
            val egypt = countryDialog.findViewById<RadioButton>(R.id.rbEgypt)
            when (country) {
                1 -> {
                    kuwait.isChecked = true
                    saudi.isChecked = false
                    egypt.isChecked = false
                }
                2 -> {
                    kuwait.isChecked = false
                    saudi.isChecked = true
                    egypt.isChecked = false
                }
                3 -> {
                    kuwait.isChecked = false
                    saudi.isChecked = false
                    egypt.isChecked = true
                }
            }
            set.setOnClickListener {
                when {
                    kuwait.isChecked -> {
                        Commons.getSharedEditor(requireContext()).putInt(Commons.COUNTRY, 1).apply()
                    }
                    saudi.isChecked -> {
                        Commons.getSharedEditor(requireContext()).putInt(Commons.COUNTRY, 2).apply()
                    }
                    egypt.isChecked -> {
                        Commons.getSharedEditor(requireContext()).putInt(Commons.COUNTRY, 3).apply()
                    }
                }
                countryDialog.dismiss()
            }
            countryDialog.show()
        }

        mBinding.tvQuestion.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, QuestionsFragment()).commit()
        }

        mBinding.tvUsePolicy.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, PolicyFragment()).commit()
        }

        mBinding.tvUseTerms.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, TermsFragment()).commit()
        }

        mBinding.tvAboutUs.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, AboutUsFragment()).commit()
        }

        mBinding.tvCallUs.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, CallFragment()).commit()
        }

        mBinding.tvDisc.setOnClickListener {
            loading.show()
            specialViewModel.account()
            specialViewModel.dataAccount.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        Toast.makeText(requireContext(), getString(R.string.send_result4), Toast.LENGTH_SHORT).show()
                    } else {
                        loading.dismiss()
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            )
        }

    }

}