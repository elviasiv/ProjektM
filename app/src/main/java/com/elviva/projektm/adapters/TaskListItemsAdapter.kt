package com.elviva.projektm.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.elviva.projektm.R
import com.elviva.projektm.activities.TaskListActivity
import com.elviva.projektm.models.Task
import org.w3c.dom.Text

open class TaskListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Task>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT //Wrapping content at 70% width
        )
        layoutParams.setMargins(
            (15.toDp().toPx()), 0, (40.toDp()).toPx(), 0
        )
        view.layoutParams = layoutParams

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvAddTaskList: TextView = holder.itemView.findViewById(R.id.tvAddTaskList) as TextView
        val tvAddCard: TextView = holder.itemView.findViewById(R.id.tvAddCard) as TextView
        val tvTaskListTitle: TextView =
            holder.itemView.findViewById(R.id.tvTaskListTitle) as TextView

        val llTaskItem: LinearLayout = holder.itemView.findViewById(R.id.llTaksItem) as LinearLayout
        val llTitleView: LinearLayout =
            holder.itemView.findViewById(R.id.llTitleView) as LinearLayout

        val cvAddTaskListName: CardView =
            holder.itemView.findViewById(R.id.cvAddTaskListName) as CardView
        val cvEditTaskListName: CardView =
            holder.itemView.findViewById(R.id.cvEditTaskListName) as CardView

        val ibCloseListName: ImageButton =
            holder.itemView.findViewById(R.id.ibCloseListName) as ImageButton
        val ibDoneListName: ImageButton =
            holder.itemView.findViewById(R.id.ibDoneListName) as ImageButton
        val ibEditListName: ImageButton =
            holder.itemView.findViewById(R.id.ibEditListName) as ImageButton
        val ibCloseEditableView: ImageButton =
            holder.itemView.findViewById(R.id.ibCloseEditableView) as ImageButton
        val ibDoneEditListName: ImageButton =
            holder.itemView.findViewById(R.id.ibDoneEditListName) as ImageButton
        val ibDeleteListName: ImageButton =
            holder.itemView.findViewById(R.id.ibDeleteListName) as ImageButton

        val etTaskListName: EditText = holder.itemView.findViewById(R.id.etTaskListName) as EditText
        val etEditTaskListName: EditText =
            holder.itemView.findViewById(R.id.etEditTaskListName) as EditText

        val model = list[position]
        if (holder is MyViewHolder) {
            if (position == list.size - 1) {
                tvAddTaskList.visibility = View.VISIBLE
                llTaskItem.visibility = View.GONE
            } else {
                tvAddTaskList.visibility = View.GONE
                llTaskItem.visibility = View.VISIBLE
            }

            tvTaskListTitle.text = model.title

            tvAddTaskList.setOnClickListener {
                tvAddTaskList.visibility = View.GONE
                cvAddTaskListName.visibility = View.VISIBLE
            }

            ibCloseListName.setOnClickListener {
                tvAddTaskList.visibility = View.VISIBLE
                cvAddTaskListName.visibility = View.GONE
            }

            ibDoneListName.setOnClickListener {
                val listName = etTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.createTaskList(listName)
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Cannot create empty lists. Please enter list name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            ibEditListName.setOnClickListener {
                etEditTaskListName.setText(model.title)
                llTitleView.visibility = View.GONE
                cvEditTaskListName.visibility = View.VISIBLE
            }

            ibCloseEditableView.setOnClickListener {
                llTitleView.visibility = View.VISIBLE
                cvEditTaskListName.visibility = View.GONE
            }

            ibDoneEditListName.setOnClickListener {
                val listName = etEditTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.updateTaskList(position, listName, model)
                    }
                } else {
                    Toast.makeText(context, "Please enter a list name", Toast.LENGTH_SHORT).show()
                }
            }

            ibDeleteListName.setOnClickListener {
                alertDialogForDeleteList(position, model.title)
            }
        }
    }

    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete $title?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()

            if (context is TaskListActivity) {
                context.deleteTaskList(position)
            }
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //Getting density pixels from pixels and converting to Int
    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

    //Getting pixels from density pixels and converting to Int
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}