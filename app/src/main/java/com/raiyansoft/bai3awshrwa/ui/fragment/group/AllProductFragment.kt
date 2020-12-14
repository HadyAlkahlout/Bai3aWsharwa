package com.raiyansoft.bai3awshrwa.ui.fragment.group

import android.app.Dialog
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
import androidx.recyclerview.widget.GridLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.ProfileProductAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentAllProductBinding
import com.raiyansoft.bai3awshrwa.ui.fragment.item.ProductFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.raiyansoft.bai3awshrwa.util.OnScrollListener

class AllProductFragment : Fragment(), ProfileProductAdapter.ProductClick {

    private lateinit var mBinding: FragmentAllProductBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private lateinit var loadiing: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var isLoading = false
    private var isLastPage = false

    private var open = 0

    val adapter = ProfileProductAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAllProductBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        open = arguments!!.getInt("open")

        loadiing = Dialog(requireContext())
        loadiing.setContentView(R.layout.dialog_loading)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(false)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        ok.setOnClickListener {
            dialog.cancel()
        }

        if (open == 0) {
            mBinding.tvTitle.text = getString(R.string.new_products)
            getAllProduct()
        } else {
            mBinding.tvTitle.text = getString(R.string.special_products)
            getSpecialProduct()
        }

        mBinding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation)
        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)
        mBinding.rcProducts.startAnimation(anim)


        mBinding.rcProducts.adapter = adapter
        mBinding.rcProducts.layoutManager =
            GridLayoutManager(requireContext(), 3)
        mBinding.rcProducts.addOnScrollListener(onScrollListener)
        mBinding.rcProducts.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )

    }

    private fun getSpecialProduct() {
        showProgressBar()
        viewModel.dataSpecialProducts.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    onScrollListener.totalCount = response.data.count_total
                    if (response.data.data.isNotEmpty()) {
                        Log.e("hdhd", "getSpecialProduct: ${response.data.pages}")
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcProducts.visibility = View.VISIBLE
                        hideProgressBar()
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                    hideProgressBar()
                }
            }
        )
    }

    private fun getAllProduct() {
        showProgressBar()
        viewModel.dataAllProducts.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    onScrollListener.totalCount = response.data.count_total
                    if (response.data.data.isNotEmpty()) {
                        Log.e("hdhd", "getSpecialProduct: ${response.data.pages}")
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcProducts.visibility = View.VISIBLE
                        hideProgressBar()
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                    hideProgressBar()
                }
            }
        )
    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        mBinding.rcProducts.visibility = View.VISIBLE
        if (open == 0) {
            viewModel.all()
        } else {
            viewModel.special()
        }
    }

    private fun hideProgressBar() {
        loadiing.dismiss()
    }

    private fun showProgressBar() {
        loadiing.show()
    }

    override fun onClick(position: Int, id: Int) {
        Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
        Commons.getSharedEditor(requireContext()).putInt(Commons.PRODUCT_ID, id).apply()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.interContainer, ProductFragment()).commit()
    }

}