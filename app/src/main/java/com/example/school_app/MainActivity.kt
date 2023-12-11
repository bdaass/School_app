package com.example.school_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import android.view.Gravity
import android.util.Log

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
            handleloginButtonClick(username, password)
        }


    }

    private fun handleloginButtonClick(username: String, password: String) {
        // Check if the entered username and password match the expected values
        if (username == "" && password == "") {
            // If the credentials match, switch to the admin layout
            setContentView(R.layout.admin_layout)

            // Find the Spinner by ID
            val typeSpinner: Spinner = findViewById(R.id.typeSpinner)

            // Initialize the spinner with user types
            ArrayAdapter.createFromResource(
                this,
                R.array.user_types,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeSpinner.adapter = adapter
            }

            // Set up listener for spinner item selection
            typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // Handle the selection and show/hide relevant EditText fields
                    handleTypeSelection(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing here
                }
            }
        }
    }

    private fun handleTypeSelection(position: Int) {

        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val editTextSupervisor: EditText = findViewById(R.id.editTextSupervisor)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        // Reset visibility for all EditText fields
        setVisibilityForEditTextFields(View.VISIBLE)

        // Hide specific fields based on the selected type
        showToast(position.toString())

        when (position) {
            0 -> {  // Student

            }
            1 -> {  // Teacher
                // Hide section and supervisor fields
                editTextSection.visibility = View.GONE
                editTextSupervisor.visibility = View.GONE
            }
            2 -> {  // Supervisor
                // Hide section field
                editTextSection.visibility = View.GONE
            }
            3 -> {  // Director
                // Hide section and supervisor fields
                editTextSection.visibility = View.GONE
                editTextSupervisor.visibility = View.GONE
            }
        }
    }

    private fun setVisibilityForEditTextFields(visibility: Int) {
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val editTextSupervisor: EditText = findViewById(R.id.editTextSupervisor)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        editTextName.visibility = visibility
        editTextBirthday.visibility = visibility
        editTextBranch.visibility = visibility
        editTextSection.visibility = visibility
        editTextSupervisor.visibility = visibility
        editTextPassword.visibility = visibility
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}