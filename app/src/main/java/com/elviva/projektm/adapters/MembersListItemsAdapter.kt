package com.elviva.projektm.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elviva.projektm.R
import com.elviva.projektm.models.User
import com.elviva.projektm.utils.Constants

open class MembersListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<User>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_member,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvMemberName: TextView = holder.itemView.findViewById<View>(R.id.tvMemberName) as TextView
        val tvMemberEmail: TextView = holder.itemView.findViewById<View>(R.id.tvMemberEmail) as TextView

        val ivMemberImage: ImageView = holder.itemView.findViewById<View>(R.id.ivMemberImage) as ImageView
        val ivSelectedMember: ImageView = holder.itemView.findViewById<View>(R.id.ivSelectedMember) as ImageView


        val model = list[position]
        if(holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(ivMemberImage)

            tvMemberName.text = model.name
            tvMemberEmail.text = model.email

            if (model.selected) {
                ivSelectedMember.visibility = View.VISIBLE
            } else {
                ivSelectedMember.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {

                    if (model.selected) {
                        onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                    } else {
                        onClickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }
        } else {
            Log.e("HOLDER", holder.toString())
            Toast.makeText(context, "not a view holder in MembersListItemsAdapter", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}