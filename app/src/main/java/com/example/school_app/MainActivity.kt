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
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import okhttp3.ResponseBody
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {
        data class User(
        val newname: String,
        val newbirthday: String,
        val newbranch: String,
        val newsection: String,
        val newsexe: String,
        val newtype: String
    )

    private lateinit var receivedMessageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ////////////////////////////////////////////////////////////////////////////////////////////// Find the Login button by its ID
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
    //////////////////////////////////////////////////////////////////////////////////////////////// switch to user layout
   ////////////////   If ADMIN
    private fun switchToAdminLayout(username: String) {
        setContentView(R.layout.admin_layout)

        // Find the user type Spinner by ID : to add new user
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
        var newtype: String = ""
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Handle the selection and show/hide relevant EditText fields
                newtype = handleTypeSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing here
            }
        }
        ///// add new user
        ///////////////////////////////////////////////////////////////////////////////////// FIREBASE TEST

        val addnewuserBtn: Button = findViewById(R.id.addnewuserBtn)
        setVisibilityForEditTextFields(View.VISIBLE)
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val editTextSexe: EditText = findViewById(R.id.editTextSexe)
        // Reset visibility for all EditText fields

        when (newtype) {
            "Student" -> {  // Student

            }
            "Teacher" -> {  // Teacher
                // Hide supervisor fields

            }
            "Supervisor" -> {  // Supervisor
                // Hide section field
                editTextSection.visibility = View.GONE
            }
            "Director"-> {  // Director
                // Hide section and supervisor fields
                editTextSection.visibility = View.GONE
                editTextBranch.visibility = View.GONE
            }
            "Admin" -> {  // Admin
                // Hide section and supervisor fields
                editTextSection.visibility = View.GONE
                editTextBirthday.visibility = View.GONE
            }
        }

        addnewuserBtn.setOnClickListener {

            // Get values from EditText fields
            val newname = editTextName.text.toString()
            val newbirthday = editTextBirthday.text.toString()
            val newbranch = editTextBranch.text.toString()
            val newsection = editTextSection.text.toString()
            val newsexe = editTextSexe.text.toString()

            //
            // Validate that the fields are not empty (you can add more validation as needed)
            //if (id.isNotEmpty() && password.isNotEmpty() && status.isNotEmpty() && username.isNotEmpty()) {
                // Create a User object with the retrieved data
             val user = User(newname, newbirthday, newbranch, newsection, newsexe, newtype)
            val customUserKey = "your_custom_key_2"
            // Reference to the Firebase database
            val database = FirebaseDatabase.getInstance("https://bdaass0-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")
                // Set the user data under the generated key
                database.child(customUserKey).setValue(user)
                .addOnSuccessListener {
                   showToast("User added successfully")
                  }
               .addOnFailureListener { e ->
                  showToast("Failed to add user: ${e.message}")
                }
            }
        }


    private fun handleTypeSelection(position: Int): String {
        return when (position) {
            0 -> "student"
            1 -> "teacher"
            2 -> "supervisor"
            3 -> "director"
            4 -> "admin"
            else -> "unknown" // Handle other cases if needed
        }
    }

    private fun setVisibilityForEditTextFields(visibility: Int) {
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        editTextName.visibility = visibility
        editTextBirthday.visibility = visibility
        editTextBranch.visibility = visibility
        editTextSection.visibility = visibility
    }
    ////////////////   If Student
    private fun switchToStudentLayout(username: String) {
        setContentView(R.layout.student_layout)
        // fetch new info from server
        receivedMessageTextView = findViewById(R.id.student_howmework_msg)
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


    ////////////////   If Teacher
    private fun switchToTeacherLayout(username: String) {
        setContentView(R.layout.teacher_layout)

        val homework_msg: EditText = findViewById(R.id.teacher_homework_msg)
        val sendButton: Button = findViewById(R.id.teacher_send_btn)

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

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

}
