package com.elviva.projektm.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elviva.projektm.R
import com.elviva.projektm.adapters.LabelColorListAdapter
import com.elviva.projektm.adapters.MembersListItemsAdapter
import com.elviva.projektm.models.User

abstract class LabelMembersListDialog(
    context: Context,
    private var list: ArrayList<User>,
    private var title: String = ""
) : Dialog(context) {

    private var adapter: MembersListItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_list,
            null
        )

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle) as TextView
        val rvList: RecyclerView = view.findViewById(R.id.rvList) as RecyclerView

        tvTitle.text = title
        if (list.size > 0) {

            rvList.layoutManager = LinearLayoutManager(context)
            adapter = MembersListItemsAdapter(context, list)
            rvList.adapter = adapter

            adapter!!.setOnClickListener(object :
                MembersListItemsAdapter.OnClickListener {
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }

    }

    protected abstract fun onItemSelected(user: User, action: String)
}