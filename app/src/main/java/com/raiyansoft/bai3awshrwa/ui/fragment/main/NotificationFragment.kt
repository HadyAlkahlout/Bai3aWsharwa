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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.adapter.NotificationAdapter
import com.raiyansoft.bai3awshrwa.databinding.FragmentNotificationBinding
import com.raiyansoft.bai3awshrwa.model.notification.Notify
import com.raiyansoft.bai3awshrwa.ui.viewmodel.NotificationViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import com.raiyansoft.bai3awshrwa.util.OnScrollListener

class NotificationFragment : Fragment() {

    private lateinit var mBinding: FragmentNotificationBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    val adapter = NotificationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNotificationBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = Dialog(requireContext())
        loading.setContentView(R.layout.dialog_loading)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(false)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        ok.setOnClickListener {
            dialog.cancel()
        }

        getNotification()

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation)
        val down = AnimationUtils.loadAnimation(requireContext(), R.anim.down_move_anim)
        mBinding.textView7.startAnimation(down)
        mBinding.rcNotification.startAnimation(anim)
        mBinding.rcNotification.adapter = adapter
        mBinding.rcNotification.layoutManager =
            LinearLayoutManager(requireContext())
        mBinding.rcNotification.addOnScrollListener(onScrollListener)
        mBinding.rcNotification.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.recyclerview_layout_animation
            )

    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getData()
        isScrolling = false
    }

    private fun getNotification() {
        loading.show()
        viewModel.dataNotification.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    onScrollListener.totalCount = response.data.pages
                    if (response.data.data.isNotEmpty()) {
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        mBinding.rcNotification.visibility = View.VISIBLE
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

}