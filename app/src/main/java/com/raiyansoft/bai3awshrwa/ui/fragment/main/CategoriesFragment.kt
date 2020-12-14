package com.raiyansoft.bai3awshrwa.ui.fragment.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.CategoryAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentCategoriesBinding
import com.raiyansoft.bai3awshrwa.ui.fragment.group.CategoryBayFilterFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.settings.SearchFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.CategoryViewModel
import com.raiyansoft.bai3awshrwa.util.Commons

class CategoriesFragment : Fragment(), CategoryAdapter.CategoryClick {

    private lateinit var mBinding: FragmentCategoriesBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
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
        mBinding = FragmentCategoriesBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        mBinding.imgSearch.setOnClickListener {
            val fragment = SearchFragment()
            val bundle = Bundle()
            bundle.putInt("id", 0)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.categoryContainer, fragment).commit()
        }

        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)


        fillCategories()

    }

    private fun fillCategories() {
        loading.show()
        viewModel.dataCategory.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    if (response.data.isNotEmpty()){
                        val adapter = CategoryAdapter(this)
                        mBinding.rcCategories.adapter = adapter
                        mBinding.rcCategories.layoutManager = GridLayoutManager(requireContext(), 3)
                        mBinding.rcCategories.setHasFixedSize(true)
                        adapter.data.addAll(response.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcCategories.layoutAnimation =
                            AnimationUtils.loadLayoutAnimation(
                                requireContext(),
                                R.anim.recyclerview_layout_animation
                            )
                        mBinding.rcCategories.visibility = View.VISIBLE
                    }
                    loading.dismiss()
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    override fun onClick(id: Int, subNo: Int, catName: String) {
        Commons.getSharedEditor(requireContext()).putInt(Commons.CAT_ID, id).apply()
        Commons.getSharedEditor(requireContext()).putString(Commons.CAT_NAME, catName).apply()
        Commons.getSharedEditor(requireContext()).putInt(Commons.SUB_NO, subNo).apply()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.categoryContainer, CategoryBayFilterFragment()).commit()
    }

}