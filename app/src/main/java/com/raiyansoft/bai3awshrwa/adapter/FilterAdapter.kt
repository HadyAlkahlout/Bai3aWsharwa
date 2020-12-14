package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.categories.Category

class FilterAdapter(var onClick: FilterClick) : RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

    var data: ArrayList<Category> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = data[position].title
        holder.card.setOnClickListener {
            for (i in 0 until data.size) {
                if (data[i].active == 1) {
                    data[i].active = 2
                }
            }
            data[position].active = 1
            notifyDataSetChanged()
            onClick.onFilterClick(data[position].id)
        }
        if (data[position].active == 1)
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
        else if (data[position].active == 2)
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.gray))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var card: CardView = item.findViewById(R.id.cvFilter)
        var name: TextView = item.findViewById(R.id.tvName)
    }

    interface FilterClick {
        fun onFilterClick(id: Int)
    }
}