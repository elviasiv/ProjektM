package com.elviva.projektm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.elviva.projektm.R
import com.elviva.projektm.databinding.ActivitySignInBinding
import com.elviva.projektm.firebase.FirestoreClass
import com.elviva.projektm.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        binding.btnSignInInForm.setOnClickListener {
            signInUser()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.tbSignIn)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.tbSignIn.setOnClickListener { onBackPressed() }
    }

    fun signInSuccess(user: User){
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun signInUser(){
        val email : String = binding.etEmail.text.toString().trim{ it <= ' '}
        val password : String = binding.etPassword.text.toString()

        showProgressDialog(resources.getString(R.string.please_wait))
        if(validateForm(email, password)) {
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        FirestoreClass().loadUserData(this)
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String) : Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackbar("Please enter an email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackbar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }
    }
}