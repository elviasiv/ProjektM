package com.elviva.projektm.activities

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.elviva.projektm.R
import com.elviva.projektm.databinding.ActivityCardDetailsBinding
import com.elviva.projektm.firebase.FirestoreClass
import com.elviva.projektm.models.Board
import com.elviva.projektm.models.Card
import com.elviva.projektm.models.Task
import com.elviva.projektm.utils.Constants

class CardDetailsActivity : BaseActivity() {

    lateinit var binding: ActivityCardDetailsBinding
    private lateinit var mBoardDetails: Board
    private var mTaskListItemPosition = -1
    private var mCardPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getIntentData()
        setupActionBar()

        binding.etCardNameDetails.setText(mBoardDetails.taskList[mTaskListItemPosition].cards[mCardPosition].name)
        binding.etCardNameDetails.setSelection(binding.etCardNameDetails.text.toString().length) //Sets focus at the end of the line

        binding.btnUpdateCardDetails.setOnClickListener {
            if(binding.etCardNameDetails.text.toString().isNotEmpty()){
                updateCardDetails()
            } else {
                Toast.makeText(this, "Please enter a card name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addUpdateTaskListSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCardDetails(){
        val card = Card(
            binding.etCardNameDetails.text.toString(),
            mBoardDetails.taskList[mTaskListItemPosition].cards[mCardPosition].creator,
            mBoardDetails.taskList[mTaskListItemPosition].cards[mCardPosition].assignedTo
        )

        mBoardDetails.taskList[mTaskListItemPosition].cards[mCardPosition] = card

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    private fun deleteCard(){
        val cardsList: ArrayList<Card> = mBoardDetails.taskList[mTaskListItemPosition].cards //getting all current cards for specific card list

        cardsList.removeAt(mCardPosition)

        val taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)

        taskList[mTaskListItemPosition].cards = cardsList

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    private fun getIntentData(){
        if (intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if(intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListItemPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if(intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete_card -> {
                alertDialogForDeleteCard(mBoardDetails.taskList[mTaskListItemPosition].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        val toolbar = binding.tbCardDetailsActivity
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.taskList[mTaskListItemPosition].cards[mCardPosition].name
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun alertDialogForDeleteCard(title: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Alert")
        builder.setMessage(resources.getString(R.string.confirmation_message_to_delete_card, title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, which ->
            dialogInterface.dismiss()
            deleteCard()
        }

        builder.setNegativeButton(R.string.no) { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}