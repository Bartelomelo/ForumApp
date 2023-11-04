package com.example.froumapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.ui.auth.AuthActivity
import com.example.froumapp.ui.forum.ForumActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)
        userPreferences.authToken.asLiveData().observe(this, Observer {
            val activity = if(it == null) AuthActivity::class.java else ForumActivity::class.java
            startActivity(Intent(this, activity))
            Toast.makeText(this, it ?: "Token is null.", Toast.LENGTH_SHORT).show()
        })

    }
}