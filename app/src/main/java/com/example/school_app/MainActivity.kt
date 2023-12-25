package com.example.school_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.content.Intent
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import android.widget.TableRow
import android.graphics.Color
import android.widget.HorizontalScrollView
import android.text.TextUtils
import android.view.Gravity
import android.graphics.Typeface



class MainActivity : ComponentActivity() {
    //////////////////////////////////////////////////////////////////////////////////////////////// Variables to add new users
    data class student_Users(
        val name: String,
        val birthday: String,
        val gender: String,
        val branch: String,
        val section: String,
        val password: String,
        val phone: String,
    )
    data class teacher_Users(
        val name: String,
        val gender: String,
        val branch: String,
        val section: String,
        val password: String,
        val phone: String,
    )
    data class supervisor_Users(
        val name: String,
        val branch: String,
        val password: String,
        val phone: String,
    )
    data class director_Users(
        val name: String,
        val password: String,
        val phone: String,
    )
    private var newStatus: String = ""
    private var selectedGender: String = ""

    //////////////////////////////////////////////////////////////////////////////////////////////// Variables to send agenda
    data class AgendaItem(
        val homeworkMsg: String = "",
        val noteMsg: String = ""
    )
    private val database = FirebaseDatabase.getInstance("https://bdaass0-default-rtdb.europe-west1.firebasedatabase.app/")
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
                    switchToAdminShowLayout(username)
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
    //////////////////////////////////////////////////////////////////////////////////////////////// switch to users layout
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
    private fun switchToAdminShowLayout(username: String){
        setContentView(R.layout.admin_show_layout)
        // Add the radio br=tns
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        fun addRadioButton(text: String) {
            val radioButton = RadioButton(this)
            radioButton.text = text
            radioButton.layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            radioGroup.addView(radioButton)
        }
        addRadioButton("Students")
        addRadioButton("Teachers")
        addRadioButton("Supervisors")
        addRadioButton("Directors")
        addRadioButton("Admins")
        // Set up a listener for the RadioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val selectedUser = selectedRadioButton?.text.toString()
            val selectedBranch: Spinner = findViewById(R.id.spinnerBranch)
            val defaultBranchValue = selectedBranch.selectedItem.toString()
            updateAdminshowTableUSERS(selectedUser, defaultBranchValue)

            selectedBranch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                    val selectedBranchValue = selectedBranch.selectedItem.toString()
                    updateAdminshowTableUSERS(selectedUser, selectedBranchValue)
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // Do nothing if nothing is selected (optional)
                }
            }
        }






        ///////////// Modify btn
        val adminmodifyButton: Button = findViewById(R.id.adminmodifyBtn)
        adminmodifyButton.setOnClickListener {
            switchToAdminModifyLayout(username)
        }
        ///////////// log out btn
        val adminlogoutButton: Button = findViewById(R.id.adminlogoutBtn)
        adminlogoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    private fun switchToAdminModifyLayout(username: String) {
        setContentView(R.layout.admin_modify_layout)
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
        val spinnerBranch: Spinner = findViewById(R.id.spinnerBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val spinnerGender: Spinner = findViewById(R.id.spinnerGender)
        val editTextPhone: EditText = findViewById(R.id.editTextPhone)
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
            val newbranch = spinnerBranch.selectedItem.toString()
            val newsection = editTextSection.text.toString()
            val newphone = editTextPhone.text.toString()
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
                                "student" -> student_Users(newname, newbirthday, newgender, newbranch, newsection, newname, newphone)
                                "teacher" -> teacher_Users(newname, newgender, newbranch, newsection, newname, newphone)
                                "supervisor" -> supervisor_Users(newname, newbranch, newname, newphone)
                                "director", "admin" -> director_Users(newname, newname, newphone)
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
            val newbranch = spinnerBranch.selectedItem.toString()
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

        ///////////// back btn
        val adminlogoutButton: Button = findViewById(R.id.adminbackBtn)
        adminlogoutButton.setOnClickListener {
            switchToAdminShowLayout(username)
        }
    }
    ////////////////   If Student
    private fun switchToStudentLayout(username: String) {
        setContentView(R.layout.student_layout)
        // fetch new info from server
        val student_howmework_msg : TextView = findViewById(R.id.studenthowmeworkmsg)
        val student_note_msg : TextView = findViewById(R.id.studentnotemsg)
        val agendaRef: DatabaseReference = database.getReference("agenda")

        // Step 1: Get the ID of the student from the student table
        val studentTableRef = database.getReference("student")
        val query = studentTableRef.orderByChild("name").equalTo(username)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val studentID = snapshot.key

                    // Step 2: Get the info from the agenda table using the student ID
                    if (studentID != null) {
                        val agendaTableRef = database.getReference("agenda").child(studentID)

                        agendaTableRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(agendaSnapshot: DataSnapshot) {
                                // Check if the agenda data exists for the student
                                if (agendaSnapshot.exists()) {
                                    // Retrieve agenda data
                                    val agendaItem = agendaSnapshot.getValue(AgendaItem::class.java)

                                    // Update TextViews with agenda data
                                    student_howmework_msg.text = agendaItem?.homeworkMsg
                                    student_note_msg.text = agendaItem?.noteMsg
                                } else {
                                    // Handle the case where no agenda data is found for the student
                                    // You might want to set some default values or display a message
                                }
                            }

                            override fun onCancelled(agendaDatabaseError: DatabaseError) {
                                // Handle errors related to the agenda table
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors related to the student table
            }
        })

        // Set the username in the EditText
        val studentName: TextView = findViewById(R.id.student_name_topview)
        studentName.text =username
        // show grade button
        val buttonGoToGrade: Button = findViewById(R.id.student_showgrade)
        buttonGoToGrade.setOnClickListener {

            // Call a function to switch to the grade layout with the username
            switchToGradeLayout(username)
        }







        // Logout button
        val logoutButton: Button = findViewById(R.id.student_logoutButton)
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
    private fun switchToGradeLayout(username: String) {
        setContentView(R.layout.grade_layout)
        // fetch new info from server
        val gradeRef: DatabaseReference = database.getReference("grade")
        val section1textView: TextView = findViewById(R.id.section1textView)
        val section2textView: TextView = findViewById(R.id.section2textView)
        gradeRef.orderByChild("name").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val grade1: String? = userSnapshot.child("grade1").getValue(String::class.java)
                            showToast(grade1.toString())
                            // Split the string into a list of strings
                            val stdGrade: List<String> = grade1.toString().split(",")
                            // Convert the list of strings into a list of numbers
                            val studentGrade: List<Int> = stdGrade.map { it.toInt() }
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    showToast("Error: ${databaseError.message}")
                }
            })
        // Logout button
        val logoutButton: Button = findViewById(R.id.grade_logoutButton)
        logoutButton.setOnClickListener {
            switchToStudentLayout(username)
        }

        }
    ////////////////   If Teacher
    private fun switchToTeacherLayout(username: String) {
        setContentView(R.layout.teacher_layout)
        //////////////////////////////////////////////////////////////////////////////////////////// Fill top view
        // Find the RelativeLayout
        val topView: RelativeLayout = findViewById(R.id.topview)
        val teacherNameTopView: TextView = topView.findViewById(R.id.teachernametopview)
        teacherNameTopView.text = username
        val teacherBranchTopView: TextView = topView.findViewById(R.id.teacherbranchtopview)
        val teacherSectionTopView: TextView = topView.findViewById(R.id.teachersectiontopview)
        // get the teacher ID and fill the top view data
        val teacherRef: DatabaseReference = database.getReference("teacher")
        teacherRef.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val branch: String? = userSnapshot.child("branch").getValue(String::class.java)
                            teacherBranchTopView.text = branch
                            val section: String? = userSnapshot.child("section").getValue(String::class.java)
                            teacherSectionTopView.text = section
                            //////////////////////////////////////////////////////////////////////////////////////////// set the spinner values
                            // Get the list of students in the same section
                            val studentsRef: DatabaseReference = database.getReference("student")
                            studentsRef.orderByChild("section").equalTo(section).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(studentsSnapshot: DataSnapshot) {
                                        if (studentsSnapshot.exists()) {
                                            val studentList = mutableListOf<String>()
                                            for (studentSnapshot in studentsSnapshot.children) {
                                                val studentUsername =
                                                    studentSnapshot.child("name").getValue(String::class.java)
                                                if (studentUsername != null) {
                                                    studentList.add(studentUsername)
                                                }
                                            }
                                            // 2. Set the spinner list entries
                                            val spinnerStudents: Spinner = findViewById(R.id.teacherstudentlistSpinner)
                                            val adapter = ArrayAdapter(
                                                applicationContext,
                                                android.R.layout.simple_spinner_item,
                                                studentList
                                            )
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                            spinnerStudents.adapter = adapter
                                        } else {
                                            showToast("No students found in the same section.")
                                        }

                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        showToast("Error getting students: ${databaseError.message}")
                                    }
                                })

                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    showToast("Error: ${databaseError.message}")
                }
            })

        ///////////////////////////////////////////////////////////////////////////////////////////// send agenda
        val homeworkmsg: EditText = findViewById(R.id.teacherhomeworkmsg)
        val notemsg: EditText = findViewById(R.id.teachernotemsg)

        val sendButton: Button = findViewById(R.id.teachersendbtn)
        sendButton.setOnClickListener {
            val homework_msg = homeworkmsg.text
            val note_msg = notemsg.text
            // Write to database
            val spinnerStudents: Spinner = findViewById(R.id.teacherstudentlistSpinner)
            val studentname = spinnerStudents.selectedItem.toString()
            val studenttableRef = database.getReference("student")    // search its id
            val query = studenttableRef.orderByChild("name").equalTo(studentname)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val studentID = snapshot.key
                        // Now you have the student ID, and you can proceed with your agenda logic
                        val agendaItem = AgendaItem(homework_msg.toString(), note_msg.toString())
                        val tableRef = database.getReference("agenda").child(studentID!!)
                        tableRef.setValue(agendaItem)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                }
            })

        }
        ///////////////////////////////////////////////////////////////////////////////////////////// log out
        val logoutButton: Button = findViewById(R.id.teacher_logoutButton)
        logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        val spinnerBranch: Spinner = findViewById(R.id.spinnerBranch)
        val editTextSection: EditText = findViewById(R.id.editTextSection)
        val spinnerGender: Spinner = findViewById(R.id.spinnerGender)
        editTextName.visibility = visibility
        spinnerGender.visibility = visibility
        editTextBirthday.visibility = visibility
        spinnerBranch.visibility = visibility
        editTextSection.visibility = visibility
    }
    private fun changevisibilityEdittext(newStatus: String) {
        val editTextBirthday: EditText = findViewById(R.id.editTextBirthday)
        val spinnerBranch: Spinner = findViewById(R.id.spinnerBranch)

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
                spinnerBranch.visibility = View.GONE
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
    ////////////////////////////////////////////////////////////////////////////////////////////////   Admin show the user data
    private fun updateAdminshowTableUSERS(selectedUser: String, selectedBranch : String) {
        val scroll: ScrollView = findViewById(R.id.scrollView)
        val linearLayout: LinearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        val tabletype = when (selectedUser) {
            "Students" -> "student"
            "Teachers" -> "teacher"
            "Supervisors" -> "supervisor"
            "Directors" -> "director"
            "Admins" -> "admin"
            else -> selectedUser
        }
        val horizontalScrollView = HorizontalScrollView(this@MainActivity)
        horizontalScrollView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val tableLayout = TableLayout(this@MainActivity)
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        val studentTable = database.getReference(tabletype)
        studentTable.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tableLayout.removeAllViews() // Clear existing views
                //////////////////////////////////////////////////////////////////////////////////// Add titles
                val row = TableRow(this@MainActivity)
                val cardView = CardView(this@MainActivity)
                val innerLayout = LinearLayout(this@MainActivity)
                var titles: Array<String>
                var titlessizes: IntArray
                val ID_size  = 100
                val name_size = 400
                val Branch_size = 300
                val section_size = 300
                val Phone_size = 300
                val Gender_size = 200
                val Birth_size = 250

                when (tabletype) {
                    "teacher" -> {
                        titles = arrayOf("ID", "Name", "Branch", "Section", "Phone", "Gender")
                        titlessizes = intArrayOf(ID_size, name_size, Branch_size, section_size, Phone_size, Gender_size)
                    }
                    "supervisor" -> {
                        titles = arrayOf("ID", "Name", "Branch", "Phone")
                        titlessizes = intArrayOf(ID_size, name_size, Branch_size, Phone_size)
                    }
                    "director", "admin" -> {
                        titles = arrayOf("ID", "Name","Phone")
                        titlessizes = intArrayOf(ID_size, name_size,  Phone_size )
                    }
                    else -> {
                        titles = arrayOf("ID", "Name", "Branch", "Section", "Phone", "Gender", "Birthday")
                        titlessizes = intArrayOf(ID_size, name_size, Branch_size, section_size, Phone_size, Gender_size, Birth_size)
                    }
                }

                for (i in titles.indices) {
                    val textView = TextView(this@MainActivity)
                    innerLayout.gravity = Gravity.END
                    textView.text = titles[i]
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.ellipsize = TextUtils.TruncateAt.END
                    textView.layoutParams = LinearLayout.LayoutParams(
                        titlessizes[i],
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    textView.textDirection = View.TEXT_DIRECTION_RTL
                    innerLayout.addView(textView, 0)
                }

                cardView.addView(innerLayout)
                row.addView(cardView)
                tableLayout.addView(row)

                // Add underline after each row
                val rowUnderline = View(this@MainActivity)
                rowUnderline.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    2
                )
                rowUnderline.setBackgroundColor(Color.BLACK)
                tableLayout.addView(rowUnderline)

                ///////////////////////////////////////////////////////////////////////////////////// Add data rows
                for (userSnapshot in snapshot.children) {
                    val Id = userSnapshot.key
                    val Data = userSnapshot.getValue() as? Map<*, *>?
                    if (Data != null) {
                        val branch = Data["branch"]
                        if (branch == selectedBranch || selectedBranch == "all branches" || tabletype == "director" || tabletype == "admin") {
                                val row = TableRow(this@MainActivity)
                                val cardView = CardView(this@MainActivity)
                                val innerLayout = LinearLayout(this@MainActivity)
                                // ID
                                val idTextView = TextView(this@MainActivity)
                                idTextView.text = "$Id"
                                idTextView.layoutParams = LinearLayout.LayoutParams(
                                    ID_size,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                            idTextView.textDirection = View.TEXT_DIRECTION_RTL // arabic direction

                            innerLayout.addView(idTextView, 0)

                                var columnsinformation = arrayOf(Data["name"],Data["branch"],Data["section"],Data["phone"],Data["gender"], Data["birth"])
                                when (tabletype) {
                                    "teacher" -> {
                                        columnsinformation = arrayOf(Data["name"],Data["branch"],Data["section"],Data["phone"],Data["gender"])
                                    }
                                    "supervisor" -> {
                                        columnsinformation = arrayOf(Data["name"], Data["branch"], Data["phone"])
                                    }
                                    "director", "admin" -> {
                                        columnsinformation = arrayOf(Data["name"], Data["phone"])
                                    }
                                    else -> {
                                        columnsinformation = arrayOf(Data["name"],Data["branch"],Data["section"],Data["phone"],Data["gender"], Data["birthday"])
                                    }
                                }
                                for (i in columnsinformation.indices) {
                                    val textView = TextView(this@MainActivity)
                                    textView.text = columnsinformation[i].toString()
                                    textView.ellipsize = TextUtils.TruncateAt.END
                                    textView.layoutParams = LinearLayout.LayoutParams(
                                        titlessizes[i + 1],
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    textView.textDirection = View.TEXT_DIRECTION_RTL // arabic direction
                                    innerLayout.addView(textView, 0)
                                }

                                cardView.addView(innerLayout)
                                row.addView(cardView)
                                tableLayout.addView(row)

                                // Add underline after each row
                                val rowUnderline = View(this@MainActivity)
                                rowUnderline.layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    2
                                )
                                rowUnderline.setBackgroundColor(Color.BLACK)
                                tableLayout.addView(rowUnderline)
                            }
                        }

                }
                // to arabic direction
                horizontalScrollView.addView(tableLayout)
                horizontalScrollView.post {
                    horizontalScrollView.scrollTo(2000, 0)
                }


                // Update the ScrollView with the new content
                scroll.removeAllViews()
                scroll.addView(horizontalScrollView)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })



    }
}


