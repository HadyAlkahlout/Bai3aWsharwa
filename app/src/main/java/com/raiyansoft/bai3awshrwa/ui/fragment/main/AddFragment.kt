package com.raiyansoft.bai3awshrwa.ui.fragment.main

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.CursorLoader
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.ProductImageAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentAddBinding
import com.raiyansoft.bai3awshrwa.model.categories.Category
import com.raiyansoft.bai3awshrwa.model.cities.City
import com.raiyansoft.bai3awshrwa.model.product.Image
import com.raiyansoft.bai3awshrwa.ui.activity.InterActivity
import com.raiyansoft.bai3awshrwa.ui.fragment.item.ProductFragment
import com.raiyansoft.bai3awshrwa.ui.viewmodel.CategoryViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.CitiesViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductFunctionsViewModel
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProductViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddFragment : Fragment(), ProductImageAdapter.CancelClick  {

    private lateinit var mBinding: FragmentAddBinding
    private val REQUEST_IMAGE_CODE = 1

    private val viewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private val productViewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private val sendViewModel by lazy {
        ViewModelProvider(this)[ProductFunctionsViewModel::class.java]
    }

    private val cityViewModel by lazy {
        ViewModelProvider(this)[CitiesViewModel::class.java]
    }

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private val categories = ArrayList<Category>()
    private val subcategories = ArrayList<Category>()
    private val subcategories2 = ArrayList<Category>()
    private val subcategories3 = ArrayList<Category>()
    private val subcategories4 = ArrayList<Category>()
    private val subcategories5 = ArrayList<Category>()
    private lateinit var cities: ArrayList<City>
    private lateinit var regions: ArrayList<City>
    private var images = ArrayList<Uri>()

    private var catId = 0
    private var subcatId = 0
    private var subcatId2 = 0
    private var subcatId3 = 0
    private var subcatId4 = 0
    private var subcatId5 = 0
    private var cityId = 0
    private var regionId = 0
    private var case = 0
    private var productId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cities = ArrayList()
        regions = ArrayList()

        case = arguments!!.getInt("case", 0)


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

        fillCategories()
        mBinding.llSubcategory.visibility = View.GONE
        mBinding.llSubcategory2.visibility = View.GONE
        mBinding.llSubcategory3.visibility = View.GONE
        mBinding.llSubcategory4.visibility = View.GONE
        mBinding.llSubcategory5.visibility = View.GONE

        mBinding.spCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in categories) {
                        if (mBinding.spCategory.selectedItem.toString() == i.title) {
                            catId = i.id
                            if (i.subCategory > 0) {
                                fillSubcategories(i.id)
                            }else{
                                mBinding.llSubcategory.visibility = View.GONE
                                mBinding.llSubcategory2.visibility = View.GONE
                                mBinding.llSubcategory3.visibility = View.GONE
                                mBinding.llSubcategory4.visibility = View.GONE
                                mBinding.llSubcategory5.visibility = View.GONE
                            }
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        mBinding.spSubcategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in subcategories) {
                        if (mBinding.spSubcategory.selectedItem.toString() == i.title) {
                            subcatId = i.id
                            if (i.subCategory > 0) {
                                fillSubcategories2(i.id)
                            }else{
                                mBinding.llSubcategory2.visibility = View.GONE
                                mBinding.llSubcategory3.visibility = View.GONE
                                mBinding.llSubcategory4.visibility = View.GONE
                                mBinding.llSubcategory5.visibility = View.GONE
                            }
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        mBinding.spSubcategory2.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in subcategories2) {
                        if (mBinding.spSubcategory2.selectedItem.toString() == i.title) {
                            subcatId2 = i.id
                            if (i.subCategory > 0) {
                                fillSubcategories3(i.id)
                            }else{
                                mBinding.llSubcategory3.visibility = View.GONE
                                mBinding.llSubcategory4.visibility = View.GONE
                                mBinding.llSubcategory5.visibility = View.GONE
                            }
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        mBinding.spSubcategory3.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in subcategories3) {
                        if (mBinding.spSubcategory3.selectedItem.toString() == i.title) {
                            subcatId3 = i.id
                            if (i.subCategory > 0) {
                                fillSubcategories4(i.id)
                            }else{
                                mBinding.llSubcategory4.visibility = View.GONE
                                mBinding.llSubcategory5.visibility = View.GONE
                            }
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        mBinding.spSubcategory4.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in subcategories4) {
                        if (mBinding.spSubcategory4.selectedItem.toString() == i.title) {
                            subcatId4 = i.id
                            if (i.subCategory > 0) {
                                fillSubcategories5(i.id)
                            }else{
                                mBinding.llSubcategory5.visibility = View.GONE
                            }
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        mBinding.spSubcategory5.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in subcategories5) {
                        if (mBinding.spSubcategory5.selectedItem.toString() == i.title) {
                            subcatId5 = i.id
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        mBinding.spCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                for (i in cities) {
                    if (mBinding.spCity.selectedItem.toString() == i.title) {
                        cityId = i.id
                        break
                    }
                }
                if (cityId == -1){
                    mBinding.llRegion.visibility = View.GONE
                }else{
                    fillRegions(cityId)
                    mBinding.llRegion.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        mBinding.spRegion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in regions) {
                        if (mBinding.spRegion.selectedItem.toString() == i.title) {
                            regionId = i.id
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        fillCity()

        mBinding.imgAdd.setOnClickListener {
            permissionImage()
        }

        mBinding.btnSend.setOnClickListener {
            mBinding.btnSend.startAnimation {
                send()
            }
        }

        if (case == 1){
            productId = arguments!!.getInt("id", 0)
            fillData()
        }
    }

    private fun fillCategories() {
        viewModel.dataCategory.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    for (i in response.data) {
                        categories.add(i)
                        names.add(i.title)
                    }
                    val adapter = ArrayAdapter(
                        requireContext(), R.layout.spinner_item, names
                    )
                    mBinding.spCategory.adapter = adapter
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillSubcategories(parent_id: Int) {
        viewModel.subCategory(parent_id)
        viewModel.dataSubcategory.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    if (response.data != null){
                        for (i in response.data) {
                            subcategories.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), R.layout.spinner_item, names
                        )
                        mBinding.spSubcategory.adapter = adapter
                        mBinding.llSubcategory.visibility = View.VISIBLE
                    }else{
                        subcatId = 0
                        mBinding.llSubcategory.visibility = View.GONE
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillSubcategories2(parent_id: Int) {
        viewModel.subCategory2(parent_id)
        viewModel.dataSubcategory2.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    if (response.data != null){
                        for (i in response.data) {
                            subcategories2.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), R.layout.spinner_item, names
                        )
                        mBinding.spSubcategory2.adapter = adapter
                        mBinding.llSubcategory2.visibility = View.VISIBLE
                    }else{
                        subcatId2 = 0
                        mBinding.llSubcategory2.visibility = View.GONE
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillSubcategories3(parent_id: Int) {
        viewModel.subCategory3(parent_id)
        viewModel.dataSubcategory3.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    if (response.data != null){
                        for (i in response.data) {
                            subcategories3.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), R.layout.spinner_item, names
                        )
                        mBinding.spSubcategory3.adapter = adapter
                        mBinding.llSubcategory3.visibility = View.VISIBLE
                    }else{
                        subcatId3 = 0
                        mBinding.llSubcategory3.visibility = View.GONE
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillSubcategories4(parent_id: Int) {
        viewModel.subCategory4(parent_id)
        viewModel.dataSubcategory4.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    if (response.data != null){
                        for (i in response.data) {
                            subcategories4.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), R.layout.spinner_item, names
                        )
                        mBinding.spSubcategory4.adapter = adapter
                        mBinding.llSubcategory4.visibility = View.VISIBLE
                    }else{
                        subcatId4 = 0
                        mBinding.llSubcategory4.visibility = View.GONE
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillSubcategories5(parent_id: Int) {
        viewModel.subCategory5(parent_id)
        viewModel.dataSubcategory5.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    if (response.data != null){
                        for (i in response.data) {
                            subcategories5.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), R.layout.spinner_item, names
                        )
                        mBinding.spSubcategory5.adapter = adapter
                        mBinding.llSubcategory5.visibility = View.VISIBLE
                    }else{
                        subcatId4 = 0
                        mBinding.llSubcategory5.visibility = View.GONE
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillCity() {
        val names = ArrayList<String>()
        names.add(getString(R.string.city))
        cities.add(City(-1, getString(R.string.city)))
        cityViewModel.dataCities.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    for (i in response.data) {
                        cities.add(i)
                        names.add(i.title)
                    }
                    val adapter = ArrayAdapter(
                        requireContext(), R.layout.spinner_item, names
                    )
                    mBinding.spCity.adapter = adapter
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillRegions(cityId: Int) {
        val names = ArrayList<String>()
        cityViewModel.region(cityId)
        cityViewModel.dataRegions.observe(viewLifecycleOwner,
            { response ->
                names.clear()
                if (response.status && response.code == 200) {
                    if (response.data != null) {
                        for (i in response.data) {
                            regions.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), R.layout.spinner_item, names
                        )
                        mBinding.spRegion.adapter = adapter
                    }
                } else {
                    title.text = getString(R.string.attention)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                }
            }
        )
    }

    private fun fillData() {
        loading.show()
        productViewModel.product(productId)
        productViewModel.dataProduct.observe(viewLifecycleOwner,
            { response ->
                if (response != null){
                    if (response.status && response.code == 200) {
                        mBinding.edAddress.setText(response.data.title)
                        if (response.data.images.isNotEmpty()) {
                            adapter.data.addAll(response.data.images)
                            setImageRecycler()
                            adapter.notifyDataSetChanged()
                        }
                        mBinding.edPrice.setText(response.data.price)
                        mBinding.edDetails.setText(response.data.note)
                        var x = 0
                        for (i in cities){
                            if (i.id == response.data.city_id){
                                mBinding.spCity.setSelection(x)
                            }
                            x++
                        }
                        var y = 0
                        for (i in regions){
                            if (i.id == response.data.region_id){
                                mBinding.spRegion.setSelection(y)
                            }
                            y++
                        }
                        var z = 0
                        for (i in categories){
                            if (i.id == response.data.category_id.toInt()){
                                mBinding.spCategory.setSelection(z)
                            }
                            z++
                        }
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

    private fun permissionImage() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            getImage()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {

            }
            .check()
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/png"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

    val imagesArray = ArrayList<Image>()
    val adapter by lazy {
        ProductImageAdapter(requireActivity(), imagesArray, this)
    }
    private fun setImageRecycler() {
        mBinding.rcImages.adapter = adapter
        mBinding.rcImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.rcImages.setHasFixedSize(true)
        mBinding.rcImages.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data!!.data != null) {
            images.add(data.data!!)
            var image = Image(imagesArray.size-1, data.data.toString())
            adapter.data.add(image)
            setImageRecycler()
            adapter.notifyDataSetChanged()
        }
    }

    override fun cancelClick(position: Int, id: Int) {
        loading.show()
        if (case == 0) {
            imagesArray.removeAt(position)
            loading.dismiss()
        }else{
            sendViewModel.deleteIMG(id)
            sendViewModel.dataDeleteImage.observe(this,
                { response ->
                    if (response != null){
                        if (response.status && response.code == 200) {
                            adapter.data.removeAt(position)
                            adapter.notifyDataSetChanged()
                            loading.dismiss()
                        } else {
                            Log.e("hdhd", "send: ${response.code}")
                            loading.dismiss()
                            title.text = getString(R.string.attention)
                            details.text = getString(R.string.somthing_wrong)
                            dialog.show()
                        }
                    }
                }
            )
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    private fun send(){
        if (mBinding.edAddress.text.isEmpty() || mBinding.edDetails.text.isEmpty() || mBinding.edPrice.text.isEmpty()){
            mBinding.btnSend.revertAnimation {
                mBinding.btnSend.text = getString(R.string.try_again)
            }
            title.text = getString(R.string.attention)
            details.text = getString(R.string.empty_fields)
            dialog.show()
        }else{
            val map: MutableMap<String, RequestBody> = HashMap()
            val mapimage = ArrayList<MultipartBody.Part>()
            val mobile = Commons.getSharedPreferences(requireContext()).getString(Commons.USER_MOBILE, "")
            if (images.isNotEmpty()) {
                for ((i, image) in images.withIndex()) {
                    val imageFile = File(getRealPathFromURI(image))
                    val reqBody = RequestBody.create("images[$i]".toMediaTypeOrNull(), imageFile)
                    val partImage: MultipartBody.Part =
                        MultipartBody.Part.createFormData("images[$i]", imageFile.name, reqBody)
                    mapimage.add(partImage)
                }
            }
            map["title"] = toRequestBody(mBinding.edAddress.text.toString())
            map["note"] = toRequestBody(mBinding.edDetails.text.toString())
            map["price"] = toRequestBody(mBinding.edPrice.text.toString())
            map["phone"] = toRequestBody(mobile!!)
            map["whats"] = toRequestBody(mobile)
            if (cityId != -1){
                map["city_id"] = toRequestBody(cityId.toString())
                map["region_id"] = toRequestBody(regionId.toString())
            }
            if (subcatId == 0){
                map["cat_id"] = toRequestBody(catId.toString())
            }else{
                if (subcatId2 == 0){
                    map["cat_id"] = toRequestBody(subcatId.toString())
                }else {
                    if (subcatId3 == 0){
                        map["cat_id"] = toRequestBody(subcatId2.toString())
                    }else{
                        if (subcatId4 == 0){
                            map["cat_id"] = toRequestBody(subcatId3.toString())
                        }else{
                            if (subcatId5 == 0){
                                map["cat_id"] = toRequestBody(subcatId4.toString())
                            }else{
                                map["cat_id"] = toRequestBody(subcatId5.toString())
                            }
                        }
                    }
                }
            }

            if (case == 0) {
                sendViewModel.create(map, mapimage)
                sendViewModel.dataCreate.observe(viewLifecycleOwner,
                    { response ->
                        if (response != null){
                            if (response.status && response.code == 200) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.send_result2),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                mBinding.btnSend.revertAnimation()
                                Commons.getSharedEditor(requireContext()).putInt(Commons.OPEN, 10).apply()
                                Commons.getSharedEditor(requireContext()).putInt(Commons.PRODUCT_ID, response.data.id).apply()
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.interContainer, ProductFragment()).commit()
                            } else {
                                mBinding.btnSend.revertAnimation {
                                    mBinding.btnSend.text = getString(R.string.try_again)
                                }
                                Log.e("hdhd", "send: ${response.code}")
                                title.text = getString(R.string.attention)
                                details.text = getString(R.string.somthing_wrong)
                                dialog.show()
                            }
                        }
                    }
                )
            } else {
                map["id"] = toRequestBody(productId.toString())
                sendViewModel.update(map, mapimage)
                sendViewModel.dataUpdate.observe(viewLifecycleOwner,
                    { response ->
                        if (response != null) {
                            if (response.status && response.code == 200) {
                                mBinding.btnSend.revertAnimation()
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.send_result3),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                startActivity(Intent(requireContext(), InterActivity::class.java))
                                requireActivity().finish()
                            } else {
                                mBinding.btnSend.revertAnimation {
                                    mBinding.btnSend.text = getString(R.string.try_again)
                                }
                                Log.e("hdhd", "send: ${response.code}")
                                title.text = getString(R.string.attention)
                                details.text = getString(R.string.somthing_wrong)
                                dialog.show()
                            }
                        }
                    }
                )
            }
        }
    }

}