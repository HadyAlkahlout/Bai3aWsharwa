package com.raiyansoft.bai3awshrwa.ui.fragment.item

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
import androidx.recyclerview.widget.GridLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.ProfileProductAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentFavBinding
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductFunctionsViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.raiyansoft.bai3awshrwa.util.OnScrollListener

class FavFragment : Fragment(), ProfileProductAdapter.ProductClick {

    private lateinit var mBinding: FragmentFavBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductFunctionsViewModel::class.java]
    }

    private lateinit var loadiing: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    val adapter = ProfileProductAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        getFav()

        mBinding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation)
        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)
        mBinding.rcFav.startAnimation(anim)
        mBinding.rcFav.addOnScrollListener(onScrollListener)

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.favorites()
        isScrolling = false
    }

    private fun getFav() {
        showProgressBar()
        viewModel.dataGetFav.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    onScrollListener.totalCount = response.data.pages
                    if (response.data.data.isNotEmpty()) {
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcFav.visibility = View.VISIBLE
                    }
                    hideProgressBar()
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                    hideProgressBar()
                }
            }
        )
        mBinding.rcFav.adapter = adapter
        mBinding.rcFav.layoutManager =
            GridLayoutManager(requireContext(), 2)
        mBinding.rcFav.addOnScrollListener(onScrollListener)
        mBinding.rcFav.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
    }

    private fun hideProgressBar() {
        loadiing.dismiss()
        isLoading = false
    }

    private fun showProgressBar() {
        loadiing.show()
        isLoading = true
    }

    override fun onClick(position: Int, id: Int) {
        Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
        Commons.getSharedEditor(requireContext()).putInt(Commons.PRODUCT_ID, id).apply()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.interContainer, ProductFragment()).commit()
    }

}