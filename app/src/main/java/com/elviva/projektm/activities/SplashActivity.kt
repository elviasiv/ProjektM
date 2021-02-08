package com.elviva.projektm.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.elviva.projektm.databinding.ActivitySplashBinding
import com.elviva.projektm.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface =  Typeface.createFromAsset(assets, "Roboto-Black.ttf")
        binding.tvSplashScreenTitle.typeface = typeFace

        Handler().postDelayed({
            //Auto login
            var currentUserID = FirestoreClass().getCurrentUUID()

            if(currentUserID != ""){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
        }, 2500)
    }

}