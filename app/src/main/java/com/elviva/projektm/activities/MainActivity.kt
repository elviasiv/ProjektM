package com.elviva.projektm.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elviva.projektm.R
import com.elviva.projektm.adapters.BoardItemsAdapter
import com.elviva.projektm.databinding.ActivityMainBinding
import com.elviva.projektm.databinding.MainContentBinding
import com.elviva.projektm.databinding.NavHeaderMainBinding
import com.elviva.projektm.firebase.FirestoreClass
import com.elviva.projektm.models.Board
import com.elviva.projektm.models.User
import com.elviva.projektm.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding : ActivityMainBinding
    private lateinit var mUsername : String
    private lateinit var mainContent : MainContentBinding

    companion object {
        const val MY_PROFILE_REQUEST_CODE : Int = 11
        const val CREATE_BOARD_REQUEST_CODE : Int = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainContent = binding.includeAppBar.includeMainContent
        val fabButton = binding.includeAppBar.fabAppBar


        setupActionBar()

        binding.navView.setNavigationItemSelectedListener(this) //We pass in "this" because we inherit from NavigationView

        FirestoreClass().loadUserData(this, true)

        fabButton.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUsername)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>){
        hideProgressDialog()

        if(boardsList.size > 0){
            mainContent.rvBoardsList.visibility = View.VISIBLE
            mainContent.tvNoBoardsAvailable.visibility = View.GONE

            mainContent.rvBoardsList.layoutManager = LinearLayoutManager(this)
            mainContent.rvBoardsList.setHasFixedSize(true)

            val adapter = BoardItemsAdapter(this, boardsList)

            mainContent.rvBoardsList.adapter = adapter

            //Adding on click listener on all boards
            adapter.setOnClickListener(object: BoardItemsAdapter.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })

        } else {
            mainContent.rvBoardsList.visibility = View.GONE
            mainContent.tvNoBoardsAvailable.visibility = View.VISIBLE
        }
    }

    //Set the image and username text in the drawer
    fun updateNavigationUserDetails(user : User, readBoardsList: Boolean) {

        //Workaround binding. With binding cant access NavigationView header layout, so I access it manually
        //and you cannot use 'kotlin-android-extensions' with 'kotlin-parcelize'
        val nav : View = binding.navView.getHeaderView(0)
        val navTextView : TextView = nav.findViewById(R.id.tvUsername)
        val navImageView : ImageView = nav.findViewById(R.id.navUserImage)

        mUsername = user.name


        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navImageView)

        navTextView.text = user.name

        if(readBoardsList){
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().getBoardsList(this)
        }
    }

    private fun setupActionBar(){
        val toolbar = binding.includeAppBar.toolbarMainActivity

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this)
        } else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FirestoreClass().getBoardsList(this)
        }
        else {
            Log.i("Cancelled", "Cancelled on activity result in MainActivity")
        }
    }


}