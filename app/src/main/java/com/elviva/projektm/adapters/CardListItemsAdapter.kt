package com.elviva.projektm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elviva.projektm.R
import com.elviva.projektm.models.Card


open class CardListItemsAdapter (
    private val context: Context,
    private var list: ArrayList<Card>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvCardName: TextView = holder.itemView.findViewById(R.id.tvCardName) as TextView

        val model = list[position]

        if(holder is MyViewHolder){
            tvCardName.text = model.name
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: View.OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, card: Card)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}