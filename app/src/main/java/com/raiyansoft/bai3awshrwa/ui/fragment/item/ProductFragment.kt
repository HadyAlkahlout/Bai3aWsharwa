package com.raiyansoft.bai3awshrwa.ui.fragment.item

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.FeatureAdapter
import com.raiyansoft.bai3awshrwa.adapter.SliderAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentProductBinding
import com.raiyansoft.bai3awshrwa.model.general.Fav
import com.raiyansoft.bai3awshrwa.ui.fragment.main.AddFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.main.ProfileFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductFunctionsViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.UpgradeViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

class ProductFragment : Fragment() {

    private lateinit var mBinding: FragmentProductBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private val functionViewModel by lazy {
        ViewModelProvider(this)[ProductFunctionsViewModel::class.java]
    }

    private val specialViewModel by lazy {
        ViewModelProvider(this)[UpgradeViewModel::class.java]
    }

    private lateinit var special: String
    private var myId = 0

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var productId = 0
    private var userId = 0
    private var fav = false
    private var call = ""
    private var whats = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProductBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        special = Commons.getSharedPreferences(requireContext()).getString(Commons.PROMOTE, "")!!
        myId = Commons.getSharedPreferences(requireContext()).getInt(Commons.USER_ID, 0)

        productId = Commons.getSharedPreferences(requireContext()).getInt(Commons.PRODUCT_ID, 0)

        loading = Dialog(requireContext())
        loading.setContentView(R.layout.dialog_loading)
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

        fillData()

        mBinding.imgFav.setOnClickListener {
            favClick()
        }

        mBinding.clCall.setOnClickListener {
            call()
        }

        mBinding.clWhats.setOnClickListener {
            whats()
        }

        mBinding.imgProfile.setOnClickListener {
            openProfile()
        }

        mBinding.llSpecial.setOnClickListener {
            loading.show()
            val fav = Fav(productId)
            specialViewModel.property(fav)
            specialViewModel.dataProduct.observe(viewLifecycleOwner,
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

        mBinding.imgEditProduct.setOnClickListener {
            val fragment = AddFragment()
            val bundle = Bundle()
            bundle.putInt("id", productId)
            bundle.putInt("case", 1)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.interContainer, fragment).commit()
        }

    }

    private fun fillData() {
        loading.show()
        viewModel.product(productId)
        viewModel.dataProduct.observe(viewLifecycleOwner,
            { response ->
                if (response != null){
                    if (response.status && response.code == 200) {
                        if (response.data.user_id == myId){
                            mBinding.imgEditProduct.visibility = View.VISIBLE
                            if (response.data.special_status){
                                mBinding.llSpecial.visibility = View.GONE
                            }else{
                                if (special == "no"){
                                    mBinding.llSpecial.visibility = View.GONE
                                }else{
                                    mBinding.llSpecial.visibility = View.VISIBLE
                                }
                            }
                        }else{
                            mBinding.imgEditProduct.visibility = View.GONE
                        }
                        mBinding.tvTitle.text = response.data.title
                        if (response.data.fav) {
                            fav = true
                            mBinding.imgFav.setImageResource(R.drawable.ic_fav_fill)
                        } else {
                            fav = false
                            mBinding.imgFav.setImageResource(R.drawable.ic_fav)
                        }
                        if (response.data.images.isEmpty()) {
                            mBinding.imageSlider.visibility = View.GONE
                        } else {
                            mBinding.imageSlider.setSliderAdapter(
                                SliderAdapter(
                                    requireActivity(),
                                    response.data.images
                                )
                            )
                            mBinding.imageSlider.startAutoCycle()
                            mBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
                            mBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                        }
                        Glide
                            .with(requireActivity())
                            .load(response.data.user_image)
                            .centerCrop()
                            .placeholder(R.drawable.seller_placeholder)
                            .into(mBinding.imgProfile)
                        mBinding.tvName.text = response.data.user_name
                        userId = response.data.user_id
                        mBinding.tvPrice.text = response.data.price
                        if (response.data.filters.isEmpty()) {
                            mBinding.rcFeatures.visibility = View.GONE
                        } else {
                            val adapter = FeatureAdapter()
                            mBinding.rcFeatures.adapter = adapter
                            mBinding.rcFeatures.layoutManager = LinearLayoutManager(requireContext())
                            mBinding.rcFeatures.setHasFixedSize(true)
                            adapter.data.addAll(response.data.filters)
                        }
                        mBinding.tvNote.text = response.data.note
                        call = response.data.phone
                        whats = response.data.whats
                        loading.dismiss()
                    } else {
                        loading.dismiss()
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            }
        )
    }

    private fun favClick(){
        loading.show()
        val favs = Fav(productId)
        if (fav){
            functionViewModel.delete(favs)
        }else{
           functionViewModel.add(favs)
        }
        functionViewModel.dataFav.observe(viewLifecycleOwner,
            { response ->
                if (response != null){
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        if (fav) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.favDelete),
                                Toast.LENGTH_SHORT
                            ).show()
                            mBinding.imgFav.setImageResource(R.drawable.ic_fav)
                            fav = false
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.favAdd),
                                Toast.LENGTH_SHORT
                            ).show()
                            mBinding.imgFav.setImageResource(R.drawable.ic_fav_fill)
                            fav = true
                        }
                    } else {
                        loading.dismiss()
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            }
        )
    }

    private fun call() {
        val i = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${call}")
        }
        if (i.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(i)
        }
    }

    private fun whats() {
        val isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp")
        if (isWhatsappInstalled) {

            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators(whats) + "@s.whatsapp.net"
            )

            startActivity(sendIntent)
        } else {
            val uri = Uri.parse("market://details?id=com.whatsapp")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            Toast.makeText(
                activity!!, "WhatsApp not Installed",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(goToMarket)
        }

    }

    private fun whatsappInstalledOrNot(uri: String): Boolean {
        val pm = activity!!.packageManager
        var app_installed = false
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            app_installed = true
        } catch (e: PackageManager.NameNotFoundException) {
            app_installed = false
        }
        return app_installed
    }

    private fun openProfile() {
        val fragment = ProfileFragment()
        val bundle = Bundle()
        bundle.putInt("open", 1)
        bundle.putInt("sellerId", userId)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(
            R.id.interContainer,
            fragment
        ).commit()
    }

}