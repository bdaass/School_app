package com.example.school_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import android.widget.EditText


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the Login button by its ID
        val myButton: Button = findViewById(R.id.loginButton)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        myButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            handleButtonClick(username, password)
        }


    }

    private fun handleButtonClick(username: String, password: String) {        val expectedUsername = "a"
        // Check if the entered username and password match the expected values
        if (username == "a" && password == "b") {
            // If the credentials match, switch to the admin layout
            setContentView(R.layout.admin_layout)
        } else {
            // Handle the case where the credentials do not match (optional)
            // For example, display an error message or perform other actions
        }
    }
}