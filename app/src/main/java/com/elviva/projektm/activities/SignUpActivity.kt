package com.elviva.projektm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.elviva.projektm.R
import com.elviva.projektm.databinding.ActivitySignUpBinding
import com.elviva.projektm.firebase.FirestoreClass
import com.elviva.projektm.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        binding.btnSignUpInForm.setOnClickListener {
            registerUser()
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this, "You have successfully registered", Toast.LENGTH_SHORT).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.tbSignUp)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.tbSignUp.setOnClickListener { onBackPressed() }
    }

    private fun registerUser() {
        val name : String = binding.etName.text.toString().trim{ it <= ' '}
        val email : String = binding.etEmail.text.toString().trim{ it <= ' '}
        val password : String = binding.etPassword.text.toString()

        if(validateForm(name, email, password)){
           showProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun validateForm(name: String, email: String, password: String) : Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackbar("Please enter a name")
                false
            }
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