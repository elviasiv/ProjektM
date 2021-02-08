package com.elviva.projektm.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.elviva.projektm.activities.MainActivity
import com.elviva.projektm.activities.MyProfileActivity
import com.elviva.projektm.activities.SignInActivity
import com.elviva.projektm.activities.SignUpActivity
import com.elviva.projektm.models.User
import com.elviva.projektm.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userinfo: User){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .set(userinfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener {
                e ->
                Log.e(activity.javaClass.simpleName, "Error: " + e)
            }
    }

    fun loadUserData(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if(loggedInUser != null) {
                    when(activity){
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                        is MyProfileActivity -> {
                            activity.setUserDataUI(loggedInUser)
                        }
                    }

                } else {
                    Log.e(activity.javaClass.simpleName, "loggedInUser is null")
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is SignInActivity -> activity.hideProgressDialog()
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error: " + e)
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully")
                Toast.makeText(activity, "Profile data updated successfully!", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }
            .addOnFailureListener{
                e ->
                Log.e(activity.javaClass.simpleName, "Error while updating", e)
                Toast.makeText(activity, "Error while updating profile", Toast.LENGTH_SHORT).show()
            }
        activity.hideProgressDialog()

    }


    fun getCurrentUUID(): String{

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}
