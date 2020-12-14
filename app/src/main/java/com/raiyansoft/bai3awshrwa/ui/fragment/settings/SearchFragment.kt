package com.raiyansoft.bai3awshrwa.ui.fragment.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.ProfileProductAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentSearchBinding
import com.raiyansoft.bai3awshrwa.ui.fragment.item.ProductFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.raiyansoft.bai3awshrwa.util.OnScrollListener

class SearchFragment : Fragment(), ProfileProductAdapter.ProductClick,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var mBinding: FragmentSearchBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private lateinit var loadiing: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var catId = 0

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    val adapter = ProfileProductAdapter(this)
    private var filterType = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catId = arguments!!.getInt("id")

        loadiing = Dialog(requireContext())
        loadiing.setContentView(R.layout.dialog_loading)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        mBinding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        mBinding.imgSearchFilter.setOnClickListener {
            viewModel.dataSearchProducts.value = null
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.inflate(R.menu.search_filter)
            popupMenu.show()
        }

        mBinding.imgSearchIcon.setOnClickListener {
            search(0)
        }

        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)

        mBinding.rvSearchProperty.adapter = adapter
        mBinding.rvSearchProperty.layoutManager =
            GridLayoutManager(requireContext(), 3)
        mBinding.rvSearchProperty.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
        mBinding.rvSearchProperty.addOnScrollListener(onScrollListener)

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.itemAll -> {
                filterType = 0
            }
            R.id.itemOld -> {
                filterType = 1
            }
            R.id.itemNew -> {
                filterType = 2
            }
            R.id.itemExpensive -> {
                filterType = 3
            }
            R.id.itemCheaper -> {
                filterType = 4
            }
        }
        search(0)
        return true
    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.all(
            mBinding.edSearch.text.toString(),
            filterType,
            catId,
            1
        )
        isScrolling = false
    }

    private fun search(type: Int) {
        if (mBinding.edSearch.text.isEmpty()) {
            title.text = getString(R.string.attention)
            details.text = getString(R.string.empty_fields)
            dialog.show()
        } else {
            showProgressBar()
            viewModel.all(
                mBinding.edSearch.text.toString(),
                filterType,
                catId,
                type
            )
            viewModel.dataSearchProducts.observe(viewLifecycleOwner,
                { response ->
                    if (response != null) {
                        if (response.status && response.code == 200) {
                            if (response.data.count_total > 0) {
                                onScrollListener.totalCount = response.data.pages
                                if (type == 0) {
                                    adapter.data.clear()
                                }
                                adapter.data.addAll(response.data.data)
                                adapter.notifyDataSetChanged()
                                mBinding.rvSearchProperty.visibility = View.VISIBLE
                                mBinding.tvEmpty.visibility = View.GONE
                            }
                            hideProgressBar()
                        } else {
                            title.text = getString(R.string.attention)
                            details.text = getString(R.string.somthing_wrong)
                            dialog.show()
                        }
                    }
                }
            )
        }
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
            .replace(R.id.categoryContainer, ProductFragment()).commit()
    }

}