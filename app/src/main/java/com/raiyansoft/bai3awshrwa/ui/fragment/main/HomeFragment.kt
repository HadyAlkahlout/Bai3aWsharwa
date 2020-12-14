package com.raiyansoft.bai3awshrwa.ui.fragment.main

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.HomeProductAdapter
import com.raiyansoft.bai3awshrwa.adapter.HomeSellerAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentHomeBinding
import com.raiyansoft.bai3awshrwa.model.general.Ad
import com.raiyansoft.bai3awshrwa.model.home.Banner
import com.raiyansoft.bai3awshrwa.model.home.User
import com.raiyansoft.bai3awshrwa.ui.fragment.group.AllProductFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.group.AllSellersFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.item.ProductFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.HomeViewModel
import com.raiyansoft.bai3awshrwa.util.Commons

class HomeFragment : Fragment(), HomeSellerAdapter.SellerClick, HomeProductAdapter.ProductClick {

    private lateinit var mBinding: FragmentHomeBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var upBannerURL = ""
    private var middleBannerURL = ""
    private var downBannerURL = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
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

        fillHomeData()

        mBinding.imgUpBanner.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(upBannerURL))
            requireActivity().startActivity(browserIntent)
        }

        mBinding.imgMiddleBanner.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(middleBannerURL))
            requireActivity().startActivity(browserIntent)
        }

        mBinding.imgDownBanner.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(downBannerURL))
            requireActivity().startActivity(browserIntent)
        }

        mBinding.tvAllSpecialSellers.setOnClickListener {
            Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
            val fragment = AllSellersFragment()
            val bundle = Bundle()
            bundle.putInt("open", 1)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, fragment).commit()
        }

        mBinding.tvAllSpecialProducts.setOnClickListener {
            Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
            val fragment = AllProductFragment()
            val bundle = Bundle()
            bundle.putInt("open", 1)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, fragment).commit()
        }

        mBinding.tvAllNewBrokers.setOnClickListener {
            Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
            val fragment = AllSellersFragment()
            val bundle = Bundle()
            bundle.putInt("open", 0)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, fragment).commit()
        }

        mBinding.tvAllNewProducts.setOnClickListener {
            val fragment = AllProductFragment()
            val bundle = Bundle()
            bundle.putInt("open", 0)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, fragment).commit()
        }

    }

    private fun fillHomeData() {
        loading.show()
        viewModel.dataHome.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    fillUpContent(
                        response.data.up_banner,
                        response.data.special_users
                    )
                    fillMiddleContent(
                        response.data.middle_banner,
                        response.data.special_ads
                    )
                    fillDownContent(
                        response.data.down_banner,
                        response.data.new_users,
                        response.data.new_ads
                    )
                    loading.dismiss()
                } else {
                    Log.e("hdhd", "fillHomeData: ${response.code}" )
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillUpContent(banner: List<Banner>, users: List<User>) {
        if (banner.isNotEmpty()) {
            Glide
                .with(requireActivity())
                .load(banner[0].image)
                .centerCrop()
                .placeholder(R.drawable.product_placeholder)
                .into(mBinding.imgUpBanner)
            upBannerURL = banner[0].url
            mBinding.imgUpBanner.visibility = View.VISIBLE
        } else {
            mBinding.imgUpBanner.visibility = View.GONE
        }
        if (users.isNotEmpty()) {
            val adapter = HomeSellerAdapter(this)
            mBinding.rcSpecialSellers.adapter = adapter
            mBinding.rcSpecialSellers.setHasFixedSize(true)
            mBinding.rcSpecialSellers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter.data.addAll(users)
            adapter.notifyDataSetChanged()
            mBinding.textView8.visibility = View.VISIBLE
            mBinding.tvAllSpecialSellers.visibility = View.VISIBLE
            mBinding.rcSpecialSellers.visibility = View.VISIBLE
        } else {
            mBinding.textView8.visibility = View.GONE
            mBinding.tvAllSpecialSellers.visibility = View.GONE
            mBinding.rcSpecialSellers.visibility = View.GONE
        }
    }


    private fun fillMiddleContent(banner: List<Banner>, products: List<Ad>) {
        if (banner.isNotEmpty()) {
            Glide
                .with(requireActivity())
                .load(banner[0].image)
                .centerCrop()
                .placeholder(R.drawable.product_placeholder)
                .into(mBinding.imgMiddleBanner)
            middleBannerURL = banner[0].url
            mBinding.imgMiddleBanner.visibility = View.VISIBLE
        } else {
            mBinding.imgMiddleBanner.visibility = View.GONE
        }
        if (products.isNotEmpty()) {
            val adapter = HomeProductAdapter(this)
            mBinding.rcSpecialProducts.adapter = adapter
            mBinding.rcSpecialProducts.setHasFixedSize(true)
            mBinding.rcSpecialProducts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter.data.addAll(products)
            adapter.notifyDataSetChanged()
            mBinding.textView10.visibility = View.VISIBLE
            mBinding.tvAllSpecialProducts.visibility = View.VISIBLE
            mBinding.rcSpecialProducts.visibility = View.VISIBLE
        } else {
            mBinding.textView10.visibility = View.GONE
            mBinding.tvAllSpecialProducts.visibility = View.GONE
            mBinding.rcSpecialProducts.visibility = View.GONE
        }
    }


    private fun fillDownContent(banner: List<Banner>, users: List<User>, products: List<Ad>) {
        if (banner.isNotEmpty()) {
            Glide
                .with(requireActivity())
                .load(banner[0].image)
                .centerCrop()
                .placeholder(R.drawable.product_placeholder)
                .into(mBinding.imgDownBanner)
            downBannerURL = banner[0].url

            mBinding.imgDownBanner.visibility = View.VISIBLE
        } else {
            mBinding.imgDownBanner.visibility = View.GONE
        }
        if (users.isNotEmpty()) {
            val adapter = HomeSellerAdapter(this)
            mBinding.rcNewBrokers.adapter = adapter
            mBinding.rcNewBrokers.setHasFixedSize(true)
            mBinding.rcNewBrokers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter.data.addAll(users)
            adapter.notifyDataSetChanged()
            mBinding.textView11.visibility = View.VISIBLE
            mBinding.tvAllNewBrokers.visibility = View.VISIBLE
            mBinding.rcNewBrokers.visibility = View.VISIBLE
        } else {
            mBinding.textView11.visibility = View.GONE
            mBinding.tvAllNewBrokers.visibility = View.GONE
            mBinding.rcNewBrokers.visibility = View.GONE
        }
        if (products.isNotEmpty()) {
            val adapters = HomeProductAdapter(this)
            mBinding.rcNewProducts.adapter = adapters
            mBinding.rcNewProducts.setHasFixedSize(true)
            mBinding.rcNewProducts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapters.data.addAll(products)
            adapters.notifyDataSetChanged()
            mBinding.textView12.visibility = View.VISIBLE
            mBinding.tvAllNewProducts.visibility = View.VISIBLE
            mBinding.rcNewProducts.visibility = View.VISIBLE
        } else {
            mBinding.textView12.visibility = View.GONE
            mBinding.tvAllNewProducts.visibility = View.GONE
            mBinding.rcNewProducts.visibility = View.GONE
        }
    }

    override fun onClick(position: Int, id: Int) {
        Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
        val fragment = ProfileFragment()
        val bundle = Bundle()
        bundle.putInt("open", 1)
        bundle.putInt("sellerId", id)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, fragment).commit()
    }

    override fun onProductClick(position: Int, id: Int) {
        Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
        Commons.getSharedEditor(requireContext()).putInt(Commons.PRODUCT_ID, id).apply()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, ProductFragment()).commit()
    }
}
