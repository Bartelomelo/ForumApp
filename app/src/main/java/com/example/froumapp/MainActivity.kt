package com.example.froumapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.ui.auth.AuthActivity
import com.example.froumapp.ui.forum.ForumActivity

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