package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.product.Image

class ProductImageAdapter(var context: Context, var data : ArrayList<Image>, var onClick: CancelClick) :
    RecyclerView.Adapter<ProductImageAdapter.PropertyAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo_add, parent, false)
        return PropertyAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: PropertyAdapterVH, position: Int) {

        Glide
            .with(context)
            .load(data[position].image)
            .centerCrop()
            .placeholder(R.drawable.product_placeholder)
            .into(viewHolder.image)

        viewHolder.cancel.setOnClickListener {
            onClick.cancelClick(position, data[position].id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PropertyAdapterVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.imgProduct)
        var cancel : ImageView = itemView.findViewById(R.id.imgCancel)
    }

    interface CancelClick{
        fun cancelClick(position: Int, id: Int)
    }
}