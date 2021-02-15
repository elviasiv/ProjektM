package com.elviva.projektm.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elviva.projektm.R

open class LabelColorListAdapter (
    private val context: Context,
    private var list: ArrayList<String>,
    private val mSelectedColor: String
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.item_label_color,
            parent,
            false
        )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewMain : View = holder.itemView.findViewById(R.id.viewMain) as View
        val ivSelectedColor : ImageView = holder.itemView.findViewById(R.id.ivSelectedColor) as ImageView

        var item = list[position]
        if(holder is MyViewHolder){
            //Setting the color of the view based on the color of the individual item in the list
            viewMain.setBackgroundColor(Color.parseColor(item))

            if(item == mSelectedColor){
                ivSelectedColor.visibility = View.VISIBLE
            } else {
                ivSelectedColor.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onItemClickListener != null){
                    onItemClickListener!!.onClick(position, item)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener{
        fun onClick(position: Int, color: String)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}