package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.product.Image
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(var context: Context, var data : List<Image>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_image, parent, false)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {

        Glide
            .with(context)
            .load(data[position].image)
            .centerCrop()
            .placeholder(R.drawable.product_placeholder)
            .into(viewHolder.image)
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return data.size
    }

    inner class SliderAdapterVH(itemView: View) :
        ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.imgSlider)
    }
}