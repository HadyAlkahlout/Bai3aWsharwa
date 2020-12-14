package com.raiyansoft.bai3awshrwa.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.model.notification.Notify
import com.raiyansoft.bai3awshrwa.repository.ApiRepository
import com.raiyansoft.bai3awshrwa.util.Commons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    var data: ArrayList<Notify> = ArrayList()
    private lateinit var context: Context
    private val repository = ApiRepository()
    private var country = 0
    private lateinit var lang: String
    private lateinit var token: String

    private lateinit var loading: Dialog
    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private lateinit var response: General

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)

        context = parent.context
        country = Commons.getSharedPreferences(context).getInt(Commons.COUNTRY, 0)
        lang = Commons.getSharedPreferences(context).getString(Commons.LANGUAGE, "ar")!!
        token = Commons.getSharedPreferences(context).getString(Commons.USER_TOKEN, "")!!

        loading = Dialog(context)
        loading.setContentView(R.layout.dialog_loading)
        loading.setCancelable(false)

        dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(false)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        ok.setOnClickListener {
            dialog.cancel()
        }

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.details.text = data[position].message
        holder.time.text = data[position].created_at
        if (data[position].read == 1) {
            holder.layout.setBackgroundResource(R.color.white)
        } else {
            holder.layout.setBackgroundResource(R.color.gray)
        }
        holder.layout.setOnClickListener {
            loading.show()
            CoroutineScope(IO).launch {
                response =
                    repository.readNotification(country, lang, token, data[position].id).body()!!
            }

            if (response.status && response.code == 200) {
                loading.dismiss()
                holder.layout.setBackgroundResource(R.color.white)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var title: TextView = item.findViewById(R.id.tvNotificationTitle)
        var details: TextView = item.findViewById(R.id.tvNotificationDetails)
        var time: TextView = item.findViewById(R.id.tvNotificationTime)
        var layout: ConstraintLayout = item.findViewById(R.id.clNotifyLayout)
    }
}