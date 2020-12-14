package com.raiyansoft.bai3awshrwa.ui.fragment.group

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.HomeFullSellerAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentAllSellersBinding
import com.raiyansoft.bai3awshrwa.model.home.User
import com.raiyansoft.bai3awshrwa.ui.fragment.main.ProfileFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.SellerViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.raiyansoft.bai3awshrwa.util.OnScrollListener

class AllSellersFragment : Fragment(), HomeFullSellerAdapter.SellerClick {

    private lateinit var mBinding: FragmentAllSellersBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[SellerViewModel::class.java]
    }

    private lateinit var loadiing: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var isLoading = false
    private var isLastPage = false

    private var open = 0

    val adapter = HomeFullSellerAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAllSellersBinding.inflate(inflater, container, false).apply {
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

        mBinding.rcSellers.visibility = View.VISIBLE

        if (open == 0) {
            mBinding.tvTitle.text = getString(R.string.new_brokers)
            getAllSellers()
        } else {
            mBinding.tvTitle.text = getString(R.string.special_brokers)
            getSpecialSellers()
        }

        mBinding.imgAllSellerBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation)
        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)
        mBinding.rcSellers.startAnimation(anim)


        mBinding.rcSellers.adapter = adapter
        mBinding.rcSellers.layoutManager =
            GridLayoutManager(requireContext(), 3)
        mBinding.rcSellers.addOnScrollListener(onScrollListener)
        mBinding.rcSellers.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )

    }

    private fun getSpecialSellers() {
        showProgressBar()
        viewModel.dataSpecialSeller.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    if (response.data.data.isNotEmpty()) {
                        if (viewModel.sPage == 1){
                            adapter.data.clear()
                        }
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcSellers.visibility = View.VISIBLE
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

    private fun getAllSellers() {
        showProgressBar()
        viewModel.dataAllSeller.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    onScrollListener.totalCount = response.data.count_total
                    if (response.data.data.isNotEmpty()) {
                        if (viewModel.page == 1){
                            adapter.data.clear()
                        }
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        hideProgressBar()
                        mBinding.rcSellers.visibility = View.VISIBLE
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
        mBinding.rcSellers.visibility = View.VISIBLE
        if (open == 0) {
            viewModel.getAllData()
        } else {
            viewModel.getSpecialData()
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
        val fragment = ProfileFragment()
        val bundle = Bundle()
        bundle.putInt("open", 1)
        bundle.putInt("sellerId", id)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.interContainer, fragment).commit()
    }

}