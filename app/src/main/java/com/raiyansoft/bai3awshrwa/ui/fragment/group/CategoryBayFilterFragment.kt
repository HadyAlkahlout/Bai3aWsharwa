package com.raiyansoft.bai3awshrwa.ui.fragment.group

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.FilterAdapter
import com.raiyansoft.bai3awshrwa.adapter.ProfileProductAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentCategoryBayFilterBinding
import com.raiyansoft.bai3awshrwa.model.cities.City
import com.raiyansoft.bai3awshrwa.ui.fragment.item.ProductFragment
import com.raiyansoft.bai3awshrwa.ui.fragment.settings.SearchFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.CategoryViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.CitiesViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.raiyansoft.bai3awshrwa.util.OnScrollListener

class CategoryBayFilterFragment : Fragment(), ProfileProductAdapter.ProductClick,
    FilterAdapter.FilterClick {

    private lateinit var mBinding: FragmentCategoryBayFilterBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private val catViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private val cityViewModel by lazy {
        ViewModelProvider(this)[CitiesViewModel::class.java]
    }

    private lateinit var loadiing: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private lateinit var cities: ArrayList<City>
    private var cityId = 0
    private lateinit var regions: ArrayList<City>
    private var regionId = 0
    private var catName = ""
    private var catId = -1
    private var subNo = 0

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    val adapter = ProfileProductAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCategoryBayFilterBinding.inflate(inflater, container, false).apply {
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
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        cities = ArrayList()
        regions = ArrayList()
        catId = Commons.getSharedPreferences(requireContext()).getInt(Commons.CAT_ID, 0)
        catName = Commons.getSharedPreferences(requireContext()).getString(Commons.CAT_NAME, "")!!
        subNo = Commons.getSharedPreferences(requireContext()).getInt(Commons.SUB_NO, 0)

        mBinding.tvTitle.text = catName

        mBinding.imgSearch.setOnClickListener {
            val fragment = SearchFragment()
            val bundle = Bundle()
            bundle.putInt("id", catId)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.categoryContainer, fragment).commit()
        }

        mBinding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.clTool.startAnimation(down)

        fillCity()
        if (subNo == 0) {
            mBinding.rcFilters.visibility = View.GONE
        } else {
            fillFilters()
        }

        mBinding.spCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                for (i in cities) {
                    if (mBinding.spCities.selectedItem.toString() == i.title) {
                        Log.e("hdhd", "onItemSelected: cat: $catId, city: ${i.id}, region: $regionId")
                        viewModel.all(catId, i.id, regionId, 0)
                        fillProducts()
                        cityId = i.id
                        break
                    }
                }
                if (cityId == 0) {
                    regionId = 0
                    mBinding.spRegion.visibility = View.GONE
                } else {
                    fillRegion(cityId)
                    mBinding.spRegion.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        mBinding.spRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                for (i in regions) {
                    if (mBinding.spRegion.selectedItem.toString() == i.title) {
                        viewModel.all(catId, cityId, i.id, 0)
                        fillProducts()
                        regionId = i.id
                        break
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.all(catId, cityId, regionId, 1)
        isScrolling = false
    }

    private fun fillProducts() {
        showProgressBar()
        viewModel.dataCategoryProducts.observe(
            viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status && response.code == 200) {
                        Log.e("hdhd", "fillProducts: $response")
                        onScrollListener.totalCount = response.data.pages
                        adapter.data.clear()
                        if (response.data.data.isNotEmpty()) {
                            adapter.data.addAll(response.data.data)
                            adapter.notifyDataSetChanged()
                            mBinding.rcProducts.visibility = View.VISIBLE
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
        mBinding.rcProducts.adapter = adapter
        mBinding.rcProducts.layoutManager =
            GridLayoutManager(requireContext(), 3)
        mBinding.rcProducts.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )
        mBinding.rcProducts.addOnScrollListener(onScrollListener)
    }

    private fun fillCity() {
        val names = ArrayList<String>()
        names.add(getString(R.string.all))
        cities.add(City(0, getString(R.string.all)))
        cityViewModel.dataCities.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    for (i in response.data) {
                        cities.add(i)
                        names.add(i.title)
                    }
                    val adapter = ArrayAdapter(
                        requireContext(), R.layout.spinner_category_item, names
                    )
                    mBinding.spCities.adapter = adapter
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillRegion(cityId: Int) {
        val names = ArrayList<String>()
        cityViewModel.region(cityId)
        cityViewModel.dataRegions.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    names.clear()
                    regions.clear()
                    names.add(getString(R.string.all))
                    regions.add(City(0, getString(R.string.all)))
                    for (i in response.data) {
                        regions.add(i)
                        names.add(i.title)
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
        val adapter = ArrayAdapter(
            requireContext(), R.layout.spinner_category_item, names
        )
        mBinding.spRegion.adapter = adapter
    }

    private fun fillFilters() {
        catViewModel.subCategory(catId)
        catViewModel.dataSubcategory.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status && response.code == 200) {
                        val adapter = FilterAdapter(this)
                        mBinding.rcFilters.adapter = adapter
                        mBinding.rcFilters.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        val all = com.raiyansoft.bai3awshrwa.model.categories.Category(
                            1,
                            20,
                            ArrayList(),
                            0,
                            "",
                            0,
                            getString(R.string.all)
                        )
                        adapter.data.add(all)
                        adapter.data.addAll(response.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcFilters.visibility = View.VISIBLE
                        catViewModel.dataSubcategory.value = null
                    } else {
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            }
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
        Commons.getSharedEditor(requireContext()).putInt(Commons.PRODUCT_ID, id).apply()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.categoryContainer, ProductFragment()).commit()
    }

    override fun onFilterClick(id: Int) {
        Log.e("hdhd", "onFilterClick: $id", )
        if (id == 0) {
            catId = Commons.getSharedPreferences(requireContext()).getInt(Commons.CAT_ID, 0)
            viewModel.all(catId, cityId, regionId, 0)
        } else {
            catId = id
            viewModel.all(id, cityId, regionId, 0)
        }
        fillProducts()
    }

}