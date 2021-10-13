package com.example.rssfeedpractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.simpletxt.view.*

class MyAdapter(val al:MutableList<Qu>):RecyclerView.Adapter<MyAdapter.VH>(){
    class VH(Items:View):RecyclerView.ViewHolder(Items) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
       return VH(LayoutInflater.from(parent.context).inflate(R.layout.simpletxt,parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var txt = al[position]
        holder.itemView.apply {
            textView.text = "${txt.title}\n${txt.rank}\n\n"
        }
    }

    override fun getItemCount(): Int = al.size
}