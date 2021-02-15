package com.elviva.projektm.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elviva.projektm.R
import com.elviva.projektm.models.Card


open class CardListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

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
        val vLabelColor: View = holder.itemView.findViewById(R.id.vLabelColor) as View

        val model = list[position]

        if (holder is MyViewHolder) {
            tvCardName.text = model.name

            if (model.labelColor.isNotEmpty()) {
                vLabelColor.visibility = View.VISIBLE
                vLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            } else {
                vLabelColor.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}