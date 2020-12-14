package com.raiyansoft.bai3awshrwa.ui.fragment.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.QuestionsAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentQuestionsBinding
import com.raiyansoft.bai3awshrwa.ui.viewmodel.SettingsViewModel

class QuestionsFragment : Fragment() {

    private lateinit var mBinding: FragmentQuestionsBinding
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
        mBinding = FragmentQuestionsBinding.inflate(inflater, container, false).apply {
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

        mBinding.imgQuestionsBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        fillQuestions()
    }

    private fun fillQuestions() {
        loading.show()
        viewModel.dataQuestions.observe(viewLifecycleOwner,
            {response ->
                if (response.status && response.code == 200){
                    if (response.data.data.isNotEmpty()){
                        val adapter = QuestionsAdapter()
                        mBinding.rcQuestions.adapter = adapter
                        mBinding.rcQuestions.layoutManager = LinearLayoutManager(requireContext())
                        mBinding.rcQuestions.setHasFixedSize(true)
                        mBinding.rcQuestions.layoutAnimation =
                            AnimationUtils.loadLayoutAnimation(
                                requireContext(),
                                R.anim.recyclerview_layout_animation
                            )
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcQuestions.visibility = View.VISIBLE
                        loading.dismiss()
                    }else{
                        mBinding.rcQuestions.visibility = View.GONE
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