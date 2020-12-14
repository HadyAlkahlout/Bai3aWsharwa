package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.categories.Category

class CategoryAdapter(var onClick: CategoryClick) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    var data: ArrayList<Category> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
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
        holder.card.setOnClickListener {
            onClick.onClick(data[position].id, data[position].subCategory, data[position].title)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var card: CardView = item.findViewById(R.id.cardCategory)
        var image: ImageView = item.findViewById(R.id.imgCategory)
        var name: TextView = item.findViewById(R.id.tvCategoryName)
    }

    interface CategoryClick {
        fun onClick (id: Int, subNo: Int, catName: String)
    }
}