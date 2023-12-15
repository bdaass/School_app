package com.example.school_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import android.content.Intent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import okhttp3.ResponseBody
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val URL = "http://192.168.1.98:3000"
    private lateinit var receivedMessageTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Find the Login button by its ID
        val mainloginButton: Button = findViewById(R.id.main_login_Button)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        mainloginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            handleloginButtonClick(username, password)
        }
    }
    private fun handleloginButtonClick(username: String, password: String) {
        val isAdmin = checkAdminCredentials(username, password)
        val isStudent = checkStudentCredentials(username, password)
        val isTeacher = checkTeacherCredentials(username, password)
        if (isAdmin) {
            switchToAdminLayout(username)
        } else if (isStudent) {
            switchToStudentLayout(username)
        } else if (isTeacher){
            switchToTeacherLayout(username)
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////  check username type
    private fun checkAdminCredentials(username: String, password: String): Boolean {
        return username.startsWith("a_") && password.isEmpty()
    }
    private fun checkStudentCredentials(username: String, password: String): Boolean {
        return username.startsWith("s_") && password.isEmpty()
    }
    private fun checkTeacherCredentials(username: String, password: String): Boolean {
        return username.startsWith("t_") && password.isEmpty()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////// switch to other layout
   ////////////////   If ADMIN
    private fun switchToAdminLayout(username: String) {
        setContentView(R.layout.admin_layout)

        // Find the Spinner by ID : to add new user
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
    private fun handleTypeSelection(position: Int) {

        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        // Reset visibility for all EditText fields
        setVisibilityForEditTextFields(View.VISIBLE)

        when (position) {
            0 -> {  // Student

            }
            1 -> {  // Teacher
                // Hide supervisor fields

            }
            2 -> {  // Supervisor
                // Hide section field
                editTextSection.visibility = View.GONE
            }
            3 -> {  // Director
                // Hide section and supervisor fields
                editTextSection.visibility = View.GONE
                editTextBranch.visibility = View.GONE
            }
            4 -> {  // Admin
                // Hide section and supervisor fields
                editTextSection.visibility = View.GONE
                editTextBirthday.visibility = View.GONE
            }
        }
    }
    private fun setVisibilityForEditTextFields(visibility: Int) {
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        editTextName.visibility = visibility
        editTextBirthday.visibility = visibility
        editTextBranch.visibility = visibility
        editTextSection.visibility = visibility
        editTextPassword.visibility = visibility
    }
    ////////////////   If Student
    private fun switchToStudentLayout(username: String) {
        setContentView(R.layout.student_layout)
        // fetch new info from server
        receivedMessageTextView = findViewById(R.id.student_howmework_msg)
        fetchMessagesFromServer()

        val logoutButton: Button = findViewById(R.id.student_logoutButton)
        // Logout button
        logoutButton.setOnClickListener {
            // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)

            // Clear the back stack, so pressing back won't return to the current activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Start the MainActivity
            startActivity(intent)
        }
    }
    private fun fetchMessagesFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        // Fetch messages
        val getMessagesCall = service.getMessages()
        getMessagesCall.enqueue(object : Callback<GetMessagesResponse> {
            override fun onResponse(call: Call<GetMessagesResponse>, response: Response<GetMessagesResponse>) {
                if (response.isSuccessful) {
                    val getMessagesResponse = response.body()
                    getMessagesResponse?.let {
                        val message = it.message
                        val messageText = message.content
                        showToast(messageText)
                        receivedMessageTextView.text = messageText
                    }
                } else {
                    showToast("Failed to fetch messages. Error code: ${response.code()}")
                }

                // Log the response body for debugging
                Log.d("Response", "Response Body: ${response.body()}")
            }

            override fun onFailure(call: Call<GetMessagesResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })

    }

    ////////////////   If Teacher
    private fun switchToTeacherLayout(username: String) {
        setContentView(R.layout.teacher_layout)

        val homework_msg: EditText = findViewById(R.id.teacher_homework_msg)
        val sendButton: Button = findViewById(R.id.teacher_send_btn)


        // Set OnClickListener for the Send button
        sendButton.setOnClickListener {
            val message = homework_msg.text.toString()
            if (message.isNotEmpty()) {
                sendHomeworkMessage(username, message)
            } else {
                showToast("Please enter a message")
            }
        }

        // Set OnClickListener for the Logout button
        val logoutButton: Button = findViewById(R.id.teacher_logoutButton)
        logoutButton.setOnClickListener {
            // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Clear the back stack, so pressing back won't return to the current activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Start the MainActivity
            startActivity(intent)
        }
    }
    private fun sendHomeworkMessage(username: String, message: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val message = Message(1, username, message, System.currentTimeMillis())
        // Send the message
        val sendMessageCall = service.sendMessage(message)
        sendMessageCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    showToast("Message sent successfully")
                } else {
                    showToast("Failed to send message")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

}
