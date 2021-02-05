package com.example.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Adapter(private val mContext: Context, private val mList: ArrayList<Items>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(mContext).inflate(R.layout.list, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = mList[position]
        val date = currentItem.date
        val cases = currentItem.cases
        holder.mTextViewDate.text = "Date: " + date.substring(0, date.length - 13)
        holder.mTextViewCases.text = "Cases: $cases"
        holder.mTextViewDate.setTextAppearance(myFont)
        holder.mTextViewCases.setTextAppearance(myFont)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextViewDate: TextView
        var mTextViewCases: TextView

        init {
            mTextViewDate = itemView.findViewById(R.id.text_view_date)
            mTextViewCases = itemView.findViewById(R.id.text_view_cases)
        }
    }
}