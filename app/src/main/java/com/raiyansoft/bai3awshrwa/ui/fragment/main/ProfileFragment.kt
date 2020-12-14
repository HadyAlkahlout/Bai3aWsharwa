package com.raiyansoft.bai3awshrwa.ui.fragment.main

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
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.ProfileProductAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentProfileBinding
import com.raiyansoft.bai3awshrwa.ui.fragment.item.FavFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.item.ProductFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.item.UpdateFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.settings.SettingsFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProfileViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.SellerViewModel
import com.raiyansoft.bai3awshrwa.util.Commons

class ProfileFragment : Fragment(), ProfileProductAdapter.ProductClick {

    private lateinit var mBinding: FragmentProfileBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private val sellerViewModel by lazy {
        ViewModelProvider(this)[SellerViewModel::class.java]
    }

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var sellerId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val open = arguments!!.getInt("open")
        sellerId = arguments!!.getInt("sellerId")

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


        if (open == 1) {
            mBinding.imgEdit.visibility = View.GONE
            mBinding.imgSettings.visibility = View.GONE
            mBinding.llMyFavourite.visibility = View.GONE
            getSeller()
        } else {
            mBinding.llMyFavourite.visibility = View.VISIBLE
            getProfile()
        }

        mBinding.imgEdit.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, UpdateFragment()).commit()
        }

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation)
        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)
        mBinding.clInfo.startAnimation(anim)

        mBinding.imgSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, SettingsFragment()).commit()
        }

        mBinding.llMyFavourite.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.interContainer, FavFragment()).commit()
        }

    }

    private fun getProfile() {
        loading.show()
        viewModel.dataProfile.observe(viewLifecycleOwner,
            { response ->
                if (response != null){
                    if (response.status && response.code == 200) {
                        Glide
                            .with(requireActivity())
                            .load(response.data.info.image)
                            .centerCrop()
                            .placeholder(R.drawable.seller_placeholder)
                            .into(mBinding.imgProfile)
                        mBinding.textView6.text = response.data.info.name
                        mBinding.tvAdsNo.text = response.data.info.ads.toString()
                        mBinding.tvViewsNo.text = response.data.info.views.toString()
                        mBinding.tvFavouriteNo.text = response.data.info.favorites.toString()
                        if (response.data.info.note != null && response.data.info.note != "") {
                            mBinding.tvAbout.text = response.data.info.note
                            mBinding.tvAboutTitle.visibility = View.VISIBLE
                            mBinding.tvAbout.visibility = View.VISIBLE
                        } else {
                            mBinding.tvAboutTitle.visibility = View.GONE
                            mBinding.tvAbout.visibility = View.GONE
                        }
                        if (response.data.ads.isNotEmpty()) {
                            val adapter = ProfileProductAdapter(this)
                            mBinding.rcMyAds.adapter = adapter
                            mBinding.rcMyAds.layoutManager = GridLayoutManager(requireContext(), 2)
                            mBinding.rcMyAds.setHasFixedSize(true)
                            adapter.data.addAll(response.data.ads)
                            adapter.notifyDataSetChanged()
                            mBinding.rcMyAds.layoutAnimation =
                                AnimationUtils.loadLayoutAnimation(
                                    requireContext(),
                                    R.anim.recyclerview_layout_animation
                                )
                            mBinding.tvAdsTitle.visibility = View.VISIBLE
                            mBinding.rcMyAds.visibility = View.VISIBLE
                        } else {
                            mBinding.tvAdsTitle.visibility = View.GONE
                            mBinding.rcMyAds.visibility = View.GONE
                        }
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_IMAGE, response.data.info.image).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_NAME, response.data.info.name).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_NOTE, response.data.info.note).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_MOBILE, response.data.info.mobile).apply()
                        Commons.getSharedEditor(requireContext())
                            .putBoolean(Commons.SPECIAL, response.data.info.special_status).apply()
                        loading.dismiss()
                    } else {
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            }
        )
    }

    private fun getSeller() {
        loading.show()
        sellerViewModel.getSellerProfile(sellerId)
        sellerViewModel.dataSeller.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    Glide
                        .with(requireActivity())
                        .load(response.data.info.image)
                        .centerCrop()
                        .placeholder(R.drawable.seller_placeholder)
                        .into(mBinding.imgProfile)
                    mBinding.textView6.text = response.data.info.name
                    mBinding.tvAdsNo.text = response.data.ads.size.toString()
                    mBinding.tvViewsNo.text = response.data.info.views.toString()
                    if (response.data.info.note != null && response.data.info.note != "") {
                        mBinding.tvAbout.text = response.data.info.note
                        mBinding.tvAboutTitle.visibility = View.VISIBLE
                        mBinding.tvAbout.visibility = View.VISIBLE
                    } else {
                        mBinding.tvAboutTitle.visibility = View.GONE
                        mBinding.tvAbout.visibility = View.GONE
                    }
                    if (response.data.ads.isNotEmpty()) {
                        val adapter = ProfileProductAdapter(this)
                        mBinding.rcMyAds.adapter = adapter
                        mBinding.rcMyAds.layoutManager = GridLayoutManager(requireContext(), 3)
                        mBinding.rcMyAds.setHasFixedSize(true)
                        adapter.data.addAll(response.data.ads)
                        adapter.notifyDataSetChanged()
                        mBinding.rcMyAds.layoutAnimation =
                            AnimationUtils.loadLayoutAnimation(
                                requireContext(),
                                R.anim.recyclerview_layout_animation
                            )
                        mBinding.tvAdsTitle.visibility = View.VISIBLE
                        mBinding.rcMyAds.visibility = View.VISIBLE
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

    override fun onClick(position: Int, id: Int) {
        Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
        Commons.getSharedEditor(requireContext()).putInt(Commons.PRODUCT_ID, id).apply()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.interContainer, ProductFragment()).commit()
    }

}