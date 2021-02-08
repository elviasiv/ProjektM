package com.elviva.projektm.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.MimeTypeFilter
import com.bumptech.glide.Glide
import com.elviva.projektm.R
import com.elviva.projektm.databinding.ActivityMyProfileBinding
import com.elviva.projektm.firebase.FirestoreClass
import com.elviva.projektm.models.User
import com.elviva.projektm.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class MyProfileActivity : BaseActivity() {

    lateinit var binding : ActivityMyProfileBinding

    private var mSelectedImageFileUri : Uri? = null
    private lateinit var mUserDetails : User
    private var mProfileImageURL : String = ""

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        FirestoreClass().loadUserData(this)

        binding.ivMyProfile.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.btnMyProfileUpdate.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            } else{
                updateUserProfileData()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }
        } else {
            Toast.makeText(this, "In order to change the picture we need permissions to storage. You can enable them in the settings.", Toast.LENGTH_LONG).show()
        }
    }

    private fun showImageChooser(){
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            mSelectedImageFileUri = data.data // data.data is the URI of the image that we pick

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding.ivMyProfile)
            } catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

    private fun uploadUserImage(){
        if(mSelectedImageFileUri != null){
            val storageRef : StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "USER_IMAGE" +
                            System.currentTimeMillis()
                            + "." + getFileExtension(mSelectedImageFileUri))


            storageRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener {
                taskSnapshot ->

                Log.i("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.i("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()                                    //Storing the URI we get on a constant which we will upload to database (storage to database)
                    updateUserProfileData()
                }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
        hideProgressDialog()
    }

    //Finding out the extension of URI
    private fun getFileExtension(uri : Uri?) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    // Making changes in database
    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()

        var anyChangesMade = false

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMade = true
        }

        if(binding.etProfileName.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = binding.etProfileName.text.toString()
            anyChangesMade = true
        }

        if(binding.etProfileMobile.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = binding.etProfileMobile.text.toString().toLong()
            anyChangesMade = true
        }
        if(anyChangesMade)
            FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    //Filling the fields in My Profile form
    fun setUserDataUI(user : User){

        mUserDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding.ivMyProfile)

        binding.etProfileName.setText(user.name)
        binding.etProfileEmail.setText(user.email)
        if(user.mobile != 0L){
        binding.etProfileMobile.setText(user.mobile.toString())
        }
    }

    private fun setupActionBar() {
        val toolbar = binding.tbMyProfile
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}