package com.elviva.projektm.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elviva.projektm.R
import com.elviva.projektm.activities.BaseActivity
import com.elviva.projektm.models.Board

open class BoardItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Board>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater
                .from(context)
                .inflate(
                    R.layout.item_board,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.itemView.findViewById(R.id.ivItemBoardImage))

            val tvName : TextView = holder.itemView.findViewById<View>(R.id.tvItemBoardName) as TextView
            val tvCreator : TextView = holder.itemView.findViewById<View>(R.id.tvItemBoardCreator) as TextView

            tvName.text = model.name
            tvCreator.text = model.creator


            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Board)
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

}