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
import com.raiyansoft.bai3awshrwa.model.general.Ad

class HomeProductAdapter(var onClick: ProductClick) : RecyclerView.Adapter<HomeProductAdapter.MyViewHolder>() {

    var data: ArrayList<Ad> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide
            .with(context)
            .load(data[position].image)
            .centerCrop()
            .placeholder(R.drawable.product_placeholder)
            .into(holder.image)
        holder.name.text = data[position].title
        holder.image.setOnClickListener {
            onClick.onProductClick(position, data[position].id)
        }
        holder.price.text = "${data[position].price}  ${context.getString(R.string.dollar)}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var image: ImageView = item.findViewById(R.id.imgProductImage)
        var name: TextView = item.findViewById(R.id.tvProductName)
        var price: TextView = item.findViewById(R.id.tvPrice)
    }

    interface ProductClick{
        fun onProductClick(position: Int, id: Int)
    }
}