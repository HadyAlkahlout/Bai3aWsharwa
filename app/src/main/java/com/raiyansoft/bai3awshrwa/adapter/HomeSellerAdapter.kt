package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.home.User

class HomeSellerAdapter(var onClick: SellerClick) : RecyclerView.Adapter<HomeSellerAdapter.MyViewHolder>() {

    var data: ArrayList<User> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.broker_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide
            .with(context)
            .load(data[position].image)
            .centerCrop()
            .placeholder(R.drawable.seller_placeholder)
            .into(holder.image)
        holder.name.text = data[position].name
        holder.image.setOnClickListener {
            onClick.onClick(position, data[position].id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var image: ImageView = item.findViewById(R.id.imgBrokerImage)
        var name: TextView = item.findViewById(R.id.tvBrokerName)
    }

    interface SellerClick{
        fun onClick(position: Int, id: Int)
    }
}