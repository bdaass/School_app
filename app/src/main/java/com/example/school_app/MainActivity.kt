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
import android.content.Intent
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.util.Log



class MainActivity : ComponentActivity() {
    data class student_Users(
        val name: String,
        val birthday: String,
        val gender: String,
        val branch: String,
        val section: String,
        val password: String,
        val phone: Int,
    )
    data class teacher_Users(
        val name: String,
        val gender: String,
        val branch: String,
        val section: String,
        val password: String,
        val phone: Int,
    )
    data class supervisor_Users(
        val name: String,
        val branch: String,
        val password: String,
        val phone: Int,
    )
    data class director_Users(
        val name: String,
        val password: String,
        val phone: Int,
    )
    private var newStatus: String = ""
    private var selectedGender: String = ""
    private val database = FirebaseDatabase.getInstance("https://bdaass0-default-rtdb.europe-west1.firebasedatabase.app/")

    private lateinit var receivedMessageTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) { ////////////////////////////////////////////  This is the first thing we do : main layout
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ////////////////////////////////////////////////////////////////////////////////////////////// Find the Login button by its ID
        val mainloginButton: Button = findViewById(R.id.main_login_Button)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        // Find the user type Spinner by ID : to add new user
        val typeSpinner: Spinner = findViewById(R.id.mainstatuscheck)
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
                newStatus = handleTypeSelection(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        mainloginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            handleloginButtonClick(username, password, newStatus)
        }
    }
    private fun handleloginButtonClick(username: String, password: String, status: String) {
        val table = database.getReference(status)
        if (status == "admin") {
            checkifAdmin(table, username, password) { isAdmin ->
                if (isAdmin) {
                    switchToAdminLayout(username)
                }
            }
        }
        else if (status == "student") {
            checkifStudent(table, username, password) { isStudent ->
                if (isStudent) {
                    switchToStudentLayout(username)
                }
            }
        }
        else if  (status == "teacher") {
            checkifTeacher(table, username, password) { isTeacher ->
                if (isTeacher) {
                    switchToTeacherLayout(username)
                }
            }
        }
        else if  (status == "supervisor") {
            checkifSupervisor(table, username, password) { isSupervisor ->
                if (isSupervisor) {
                    switchToSupervisorLayout(username)
                }
            }
        }
        else if  (status == "director") {
            checkifDirector(table, username, password) { isDirector ->
                if (isDirector) {
                    switchToDirectorLayout(username)
                }
            }
        }
    }
    //////////////////////  check username types
    private fun checkifAdmin(table : DatabaseReference, username: String, password: String, callback: (Boolean) -> Unit) {
        checkIfNameExiststoLOGIN(table, username, password) { isAdmin ->
            callback(isAdmin)
        }
    }
    private fun checkifStudent(table : DatabaseReference, username: String, password: String, callback: (Boolean) -> Unit) {
        checkIfNameExiststoLOGIN(table, username, password) { isStudent ->
            callback(isStudent)
        }
    }
    private fun checkifTeacher(table : DatabaseReference, username: String, password: String, callback: (Boolean) -> Unit) {
        checkIfNameExiststoLOGIN(table, username, password) { isTeacher ->
            callback(isTeacher)
        }
    }
    private fun checkifSupervisor(table : DatabaseReference, username: String, password: String, callback: (Boolean) -> Unit) {
        checkIfNameExiststoLOGIN(table, username, password) { isSupervisor ->
            callback(isSupervisor)
        }
    }
    private fun checkifDirector(table : DatabaseReference, username: String, password: String, callback: (Boolean) -> Unit) {
        checkIfNameExiststoLOGIN(table, username, password) { isDirector ->
            callback(isDirector)
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////// switch to user layout
    ////////////////   If Supervisor
    private fun switchToSupervisorLayout(username: String) {
        setContentView(R.layout.supervisor_layout)
        // Set OnClickListener for the Logout button
        val logoutButton: Button = findViewById(R.id.supervisorlogoutbtn)
        logoutButton.setOnClickListener {
            // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Clear the back stack, so pressing back won't return to the current activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Start the MainActivity
            startActivity(intent)
        }
    }
    ////////////////   If Director
    private fun switchToDirectorLayout(username: String) {
        setContentView(R.layout.director_layout)
        // Set OnClickListener for the Logout button
        val logoutButton: Button = findViewById(R.id.directorlogoutbtn)
        logoutButton.setOnClickListener {
            // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Clear the back stack, so pressing back won't return to the current activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Start the MainActivity
            startActivity(intent)
        }

    }
    ////////////////   If ADMIN
    private fun switchToAdminLayout(username: String) {
        setContentView(R.layout.admin_layout)
        // Find the user type Spinner by ID : to add new user
        val typeSpinner: Spinner = findViewById(R.id.typeSpinner)
        showToast("Get the admin layout")

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
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                newStatus = handleTypeSelection(position)
                setVisibilityForEditTextFields(View.VISIBLE)
                changevisibilityEdittext(newStatus)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////// add and delete  users
        // Get entry Data
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val spinnerGender: Spinner = findViewById(R.id.spinnerGender)
        val GenderOptions = arrayOf("Male", "Female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, GenderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapter
        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = GenderOptions[position]
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
        // Add new user
        val addnewuserBtn: Button = findViewById(R.id.addnewuserBtn)
        addnewuserBtn.setOnClickListener {
            // Get values from EditText fields
            val newname = editTextName.text.toString()
            val newbirthday = editTextBirthday.text.toString()
            val newbranch = editTextBranch.text.toString()
            val newsection = editTextSection.text.toString()
            val newgender = selectedGender
            // check if newname already exist in the newstatus table
            checkIfNameExiststoADD(database, newname, newStatus) { nameExists ->
                if (nameExists) {
                    showToast("Name already exists!")
                } else {
                    showToast("Name does not exist. Proceed with registration.")
                    val usersRef = when (newStatus) {
                        "student" -> database.getReference("student")
                        "teacher" -> database.getReference("teacher")
                        "supervisor" -> database.getReference("supervisor")
                        "director" -> database.getReference("director")
                        "admin" -> database.getReference("admin")
                        else -> database.getReference("student")
                    }
                    // create new ID + add new user
                    generate_newID(usersRef) { nextId ->
                        if (nextId != -1) {
                            val id = nextId.toString()
                            val user = when (newStatus) {
                                "student" -> student_Users(newname, newbirthday, newgender, newbranch, newsection, newname, 5)
                                "teacher" -> teacher_Users(newname, newgender, newbranch, newsection, newname, 5)
                                "supervisor" -> supervisor_Users(newname, newbranch, newname, 5)
                                "director", "admin" -> director_Users(newname, newname, 5)
                                else -> throw IllegalArgumentException("Invalid newStatus: $newStatus")
                            }
                            usersRef.child(id).setValue(user)
                                .addOnSuccessListener {
                                    showToast("user added")
                                }
                                .addOnFailureListener { e ->
                                   showToast("Failed to add user: ${e.message}")
                           }
                        }
                    }
                }
            }
        }
        ///////////// Delete user btn
        val deleteuserBtn: Button = findViewById(R.id.deleteuserBtn)
        deleteuserBtn.setOnClickListener {
            val newname = editTextName.text.toString()
            val newbirthday = editTextBirthday.text.toString()
            val newbranch = editTextBranch.text.toString()
            val newsection = editTextSection.text.toString()
            val newgender = selectedGender
            // connnect to the database
            // check if newname already exist in the newstatus table
            val databaseRef = when (newStatus) {
                "student" -> database.getReference("student")
                "teacher" -> database.getReference("teacher")
                "supervisor" -> database.getReference("supervisor")
                "director" -> database.getReference("director")
                "admin" -> database.getReference("admin")
                else -> database.getReference("student")
            }
            checkIfNameExiststoADD(database, newname, newStatus) { nameExists ->
                if (nameExists) {
                    removeItemFromTable(databaseRef, newname)
                    showToast("Deleted")
                } else {
                    showToast("Name does not exist!")
                }
            }

        }

        ///////////// log out btn
        val adminlogoutButton: Button = findViewById(R.id.adminlogoutBtn)
        adminlogoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
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
    // go to grade_layout

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
    ///////////////////////////////////////////////////////////////////////////////////////////////   TOAST msg
    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }
    /////////////////////////////////////////////////////////////////////////////////////////////      Functions for Firebase
    fun generate_newID(usersRef: DatabaseReference, onComplete: (Int) -> Unit) {
        var maxId = 0
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val key = userSnapshot.key?.toInt()
                    if (key != null && key > maxId) {
                          maxId = key
                    }
                }
                val nextId = maxId + 1
                onComplete.invoke(nextId)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                onComplete.invoke(-1) // Pass -1 to indicate an error condition
            }
        }
        )
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////   Admin layout functions
    private fun setVisibilityForEditTextFields(visibility: Int) {
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val spinnerGender: Spinner = findViewById(R.id.spinnerGender)
        editTextName.visibility = visibility
        spinnerGender.visibility = visibility
        editTextBirthday.visibility = visibility
        editTextBranch.visibility = visibility
        editTextSection.visibility = visibility
    }
    private fun changevisibilityEdittext(newStatus: String) {
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val editTextBranch: EditText = findViewById(R.id.editTextBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val spinnerGender: Spinner = findViewById(R.id.spinnerGender)
        when (newStatus)
        {
            "student" -> {  // Student
            }
            "teacher" -> {  // Teacher
                editTextBirthday.visibility = View.GONE
            }
            "supervisor" -> {  // Supervisor
                editTextSection.visibility = View.GONE
                editTextBirthday.visibility = View.GONE
                spinnerGender.visibility = View.GONE
            }
            "director", "admin"-> {  // Director
                editTextSection.visibility = View.GONE
                editTextBranch.visibility = View.GONE
                editTextBirthday.visibility = View.GONE
                spinnerGender.visibility = View.GONE
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
    ///// add new user - check if already exists
    private fun checkIfNameExiststoADD(database: FirebaseDatabase, newname: String, newstatus: String, callback: (Boolean) -> Unit) {
        // Specify the table based on newstatus
        val tableRef = when (newstatus) {
            "teacher" -> database.getReference("teacher")
            "student" -> database.getReference("student")
            "director" -> database.getReference("director")
            "supervisor" -> database.getReference("supervisor")
            "admin" -> database.getReference("admin")
            else -> null // Handle unsupported newstatus
        }
        if (tableRef == null) {
            callback(false)
            return
        } else {
            val query = tableRef.orderByChild("name").equalTo(newname)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    callback(dataSnapshot.exists())
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    callback(false)
                }
            })
        }
    }
    private fun checkIfNameExiststoLOGIN(table: DatabaseReference, username: String, password: String, callback: (Boolean) -> Unit) {
        val query = table.orderByChild("name").equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the username exists
                if (dataSnapshot.exists()) {
                    // Username exists, now check if the password matches
                    val userSnapshot = dataSnapshot.children.first()
                    val storedPassword = userSnapshot.child("password").getValue(String::class.java)

                    if (storedPassword == password) {
                        // Username and password match
                        callback(true)
                    } else {
                        val errorText: TextView = findViewById(R.id.errortext)
                        errorText.text = "Wrong Password"
                        callback(false)
                    }
                } else {
                    // Username doesn't exist
                    val errorText: TextView = findViewById(R.id.errortext)
                    errorText.text = "Wrong username"
                    callback(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                callback(false)
            }
        })
    }
    fun removeItemFromTable(databaseRef: DatabaseReference, newname: String) {
        databaseRef.orderByChild("name").equalTo(newname)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }
}


