package com.example.sprintv2

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import com.example.sprintv2.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.sprintv2.FriendListActivity
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var signup: Button? = null
    private var alreadyUser: TextView?  = null
    private var isSigningUp = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val email = findViewById<EditText>(R.id.email)
        val signup = findViewById<Button>(R.id.signup)
        val alreadyUser = findViewById<TextView>(R.id.alreadyUser)

        //Checks if user is already signed in.
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this@MainActivity, FriendListActivity::class.java))
            finish()
        }
        signup.setOnClickListener(View.OnClickListener {
            if (isSigningUp) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    if (isSigningUp && username.getText().toString().isEmpty()) {
                        Toast.makeText(this@MainActivity, "Invalid input", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                }

                signUp()
            } else {
                login()
            }
        })
        alreadyUser.setOnClickListener(View.OnClickListener {
            if (isSigningUp) {
                isSigningUp = false
                signup.setText("Log in")
                username.setVisibility(View.GONE)
                alreadyUser.setText("Don't have an account? Click to Sign up.")
            } else {
                isSigningUp = true
                signup.setText("Sign up")
                username.setVisibility(View.VISIBLE)
                alreadyUser.setText("Already have an account? Click to Log in.")
            }
        })
    }

    fun signUp() {
        val password:String = findViewById<EditText>(R.id.password).text.toString()
        val email:String = findViewById<EditText>(R.id.email).text.toString()
        val username:String = findViewById<EditText>(R.id.username).text.toString()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().currentUser!!.uid).setValue(User(username, email))
                        Toast.makeText(this@MainActivity, "Successfully signed up.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, email, Toast.LENGTH_LONG).show()
                    }
                }
    }

    fun login() {
        val password:String = findViewById<EditText>(R.id.password).text.toString()
        val email:String = findViewById<EditText>(R.id.email).text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this@MainActivity, FriendListActivity::class.java))
                Toast.makeText(this@MainActivity, "Logged in successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Failed to sign in", Toast.LENGTH_LONG).show()
            }
        }
    }
}