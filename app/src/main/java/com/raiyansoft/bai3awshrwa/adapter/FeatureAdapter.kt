package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.product.Filter
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class FeatureAdapter : RecyclerView.Adapter<FeatureAdapter.MyViewHolder>() {

    var data: ArrayList<Filter> = ArrayList()
    private lateinit var context: Context

    var x = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View? = null
        when (x % 2) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feature_1,
                    parent,
                    false
                )
                x++
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feature_2,
                    parent,
                    false
                )
                x++
            }
        }
        context = parent.context
        return MyViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.value.text = data[position].value_text
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var title: TextView = item.findViewById(R.id.tvFeatureName)
        var value: TextView = item.findViewById(R.id.tvFeatureValue)
    }
}