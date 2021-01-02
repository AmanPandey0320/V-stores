package com.dark_phoenix09.app2pcon2k20.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dark_phoenix09.app2pcon2k20.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this, dashboard::class.java))
            finish()
        },4000)
    }



}