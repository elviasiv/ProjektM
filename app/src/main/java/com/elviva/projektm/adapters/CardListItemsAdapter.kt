package com.elviva.projektm.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elviva.projektm.R
import com.elviva.projektm.activities.TaskListActivity
import com.elviva.projektm.models.Card
import com.elviva.projektm.models.SelectedMembers


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

        val rvCardSelectedMembersList: RecyclerView = holder.itemView.findViewById(R.id.rvCardSelectedMembersList) as RecyclerView

        val model = list[position]

        if (holder is MyViewHolder) {
            tvCardName.text = model.name

            if (model.labelColor.isNotEmpty()) {
                vLabelColor.visibility = View.VISIBLE
                vLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            } else {
                vLabelColor.visibility = View.GONE
            }

            //Basically showing all assigned members to the list in the board view
            if((context as TaskListActivity)
                    .mAssignedMemberDetailList.size > 0){
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for(i in context.mAssignedMemberDetailList.indices){
                    for (j in model.assignedTo){
                        if(context.mAssignedMemberDetailList[i].id == j){
                            val selectedMember = SelectedMembers(
                                context.mAssignedMemberDetailList[i].id,
                                context.mAssignedMemberDetailList[i].image
                            )

                            selectedMembersList.add(selectedMember)
                        }
                    }
                }

                //If only the creator is assigned to the list then don't show the recycler view
                if (selectedMembersList.size > 0){
                    if(selectedMembersList.size == 1 && selectedMembersList[0].id == model.creator){
                        rvCardSelectedMembersList.visibility = View.GONE
                    } else {
                        rvCardSelectedMembersList.visibility = View.VISIBLE

                        rvCardSelectedMembersList.layoutManager = GridLayoutManager(
                            context,
                            4
                        )
                        val adapter = CardMemberListItemsAdapter(context, selectedMembersList, false)
                        rvCardSelectedMembersList.adapter = adapter

                        adapter.setOnClickListener(object: CardMemberListItemsAdapter.OnClickListener{
                            override fun onClick() {
                                if(onClickListener != null){
                                    onClickListener!!.onClick(position)
                                }
                            }

                        })
                    }
                } else {
                    rvCardSelectedMembersList.visibility = View.GONE
                }
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