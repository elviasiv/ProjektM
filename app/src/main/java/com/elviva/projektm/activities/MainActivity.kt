package com.elviva.projektm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
    lateinit var navBinding : NavHeaderMainBinding
    lateinit var mainBinding : MainContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navBinding = NavHeaderMainBinding.inflate(layoutInflater)


        binding.includeAppBar.includeMainContent.tvHelloWorld.text = "kendrick lmao"


        setupActionBar()

        binding.navView.setNavigationItemSelectedListener(this) //We pass in "this" because we inherit from NavigationView

        FirestoreClass().signInUser(this)
    }

    //Set the image and username text in the drawer
    fun updateNavigationUserDetails(user : User) {
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navBinding.navUserImage)

        navBinding.tvUsername.text = user.name
       // mainBinding.tvHelloWorld.text = user.name
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
            R.id.nav_my_profile -> Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
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

}