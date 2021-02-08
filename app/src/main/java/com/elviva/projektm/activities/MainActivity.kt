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
import com.bumptech.glide.Glide
import com.elviva.projektm.R
import com.elviva.projektm.databinding.ActivityMainBinding
import com.elviva.projektm.databinding.MainContentBinding
import com.elviva.projektm.databinding.NavHeaderMainBinding
import com.elviva.projektm.firebase.FirestoreClass
import com.elviva.projektm.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding : ActivityMainBinding

    companion object {
        const val MY_PROFILE_REQUEST_CODE : Int = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mainContent = binding.includeAppBar.includeMainContent


        setupActionBar()

        binding.navView.setNavigationItemSelectedListener(this) //We pass in "this" because we inherit from NavigationView

        FirestoreClass().loadUserData(this)
    }

    //Set the image and username text in the drawer
    fun updateNavigationUserDetails(user : User) {

        //Workaround binding. With binding cant access NavigationView header layout, so I access it manually
        //and you cannot use 'kotlin-android-extensions' with 'kotlin-parcelize'
        val nav : View = binding.navView.getHeaderView(0)
        val navTextView : TextView = nav.findViewById(R.id.tvUsername)
        val navImageView : ImageView = nav.findViewById(R.id.navUserImage)

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navImageView)

        navTextView.text = user.name
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
        } else {
            Log.i("Cancelled", "Cancelled on activity result in MainActivity")
        }
    }


}