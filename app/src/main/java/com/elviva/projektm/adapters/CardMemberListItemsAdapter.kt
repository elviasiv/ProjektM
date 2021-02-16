package com.elviva.projektm.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elviva.projektm.R
import com.elviva.projektm.models.SelectedMembers

open class CardMemberListItemsAdapter(
    private val context: Context,
    private val list: ArrayList<SelectedMembers>,
    private val assignMembers: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card_selected_member,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ivAddMember: ImageView = holder.itemView.findViewById(R.id.ivAddMember) as ImageView
        val ivSelectedMemberImage: ImageView = holder.itemView.findViewById(R.id.ivSelectedMemberImage) as ImageView

        val model = list[position]

        if (holder is MyViewHolder) {
            if (position == list.size - 1 && assignMembers) {
                ivAddMember.visibility = View.VISIBLE
                ivSelectedMemberImage.visibility = View.GONE
            } else {
                ivAddMember.visibility = View.GONE
                ivSelectedMemberImage.visibility = View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(ivSelectedMemberImage)
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick()
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
        fun onClick()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}