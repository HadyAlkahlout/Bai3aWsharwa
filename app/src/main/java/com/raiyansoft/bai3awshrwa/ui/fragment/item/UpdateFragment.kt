package com.raiyansoft.bai3awshrwa.ui.fragment.item

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.CursorLoader
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentUpdateBinding
import com.raiyansoft.bai3awshrwa.ui.activity.InterActivity
import com.raiyansoft.bai3awshrwa.ui.viewmodel.ProfileViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UpdateFragment : Fragment() {

    private lateinit var mBinding: FragmentUpdateBinding
    private val REQUEST_IMAGE_CODE = 1

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private var image = ""
    private var name = ""
    private var note = ""

    private var editImage: Uri? = null

    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private lateinit var partImage: MultipartBody.Part

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentUpdateBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        image = Commons.getSharedPreferences(requireContext())
            .getString(Commons.USER_IMAGE, "")!!
        name = Commons.getSharedPreferences(requireContext())
            .getString(Commons.USER_NAME, "")!!
        note = Commons.getSharedPreferences(requireContext())
            .getString(Commons.USER_NOTE, "")!!

        fillInfo()

        mBinding.imgEditBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        mBinding.imgEdit.setOnClickListener {
            permissionImage()
        }

        mBinding.btnEdit.setOnClickListener {
            mBinding.btnEdit.startAnimation {
                editProfile()
            }
        }
    }

    private fun fillInfo() {
        Glide
            .with(this)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.seller_placeholder)
            .into(mBinding.imgEditPhoto)
        mBinding.edEditName.setText(name)
        mBinding.edEditAbout.setText(note)
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

    private fun editProfile() {
        if (mBinding.edEditName.text.isEmpty() || mBinding.edEditAbout.text.isEmpty()) {
            title.text = getString(R.string.attention)
            details.text = getString(R.string.empty_fields)
            dialog.show()
            mBinding.btnEdit.revertAnimation {
                mBinding.btnEdit.text = getString(R.string.try_again)
            }
        } else {
            var sendImage = true
            if (editImage == null) {
                sendImage = false
            } else {
                val imageFile = File(getRealPathFromURI(editImage!!)!!)
                val reqBody = RequestBody.create("avatar".toMediaTypeOrNull(), imageFile)
                partImage = MultipartBody.Part.createFormData("avatar", imageFile.name, reqBody)
            }

            val map: MutableMap<String, RequestBody> = HashMap()
            map["name"] = toRequestBody(mBinding.edEditName.text.toString())
            map["note"] = toRequestBody(mBinding.edEditAbout.text.toString())

            if (sendImage) {
                viewModel.makeEdit(map, partImage)
            } else {
                viewModel.makeEdit(map)
            }
            viewModel.dataEditProfile.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        mBinding.btnEdit.revertAnimation()
                        Toast.makeText(requireContext(), getString(R.string.save_massege), Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    } else {
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                        mBinding.btnEdit.revertAnimation {
                            mBinding.btnEdit.text = getString(R.string.try_again)
                        }
                    }
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data!!.data != null) {
            editImage = data.data!!
            mBinding.imgEditPhoto.setImageURI(editImage)
        }
    }

}