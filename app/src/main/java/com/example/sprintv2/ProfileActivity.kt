package com.example.sprintv2

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.example.sprintv2.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.sprintv2.MainActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
       val logout = findViewById<Button>(R.id.logout)
       val username = findViewById<TextView>(R.id.username)
       val email = findViewById<TextView>(R.id.email)
        val user = FirebaseAuth.getInstance().currentUser
        username.setText(user!!.displayName)
        username.setText(user.displayName.toString())
        email.setText(user.email)
        logout.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        })
    }
}