package com.raiyansoft.bai3awshrwa.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.model.general.General
import com.raiyansoft.bai3awshrwa.model.questions.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionsAdapter : RecyclerView.Adapter<QuestionsAdapter.MyViewHolder>() {

    var data: ArrayList<Question> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.question.text = data[position].question
        holder.answer.text = data[position].answer
        holder.question.setOnClickListener {
            if (data[position].open == 0){
                data[position].open = 1
                holder.answer.visibility = View.VISIBLE
            }else{
                data[position].open = 0
                holder.answer.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var question: TextView = item.findViewById(R.id.tvQuestion)
        var answer: TextView = item.findViewById(R.id.tvAnswer)
    }
}