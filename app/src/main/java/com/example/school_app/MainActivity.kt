package com.example.school_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.school_app.ui.theme.School_appTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContent {
            School_appTheme {
                // Call your custom Composable function here
                MyAppLayout()
            }
        }
    }
}


@Composable
fun MyAppLayout() {
    var message by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color.White)
    ) {
        // 1. Logo on the top
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(shape = MaterialTheme.shapes.small),
            contentScale = ContentScale.Fit // Use ContentScale.Fit instead of ContentScale.Crop
        )

        // 2. Text boxes for login and password
        var username by remember { mutableStateOf("") }

        OutlinedTextField(
            value = username,
            onValueChange = { newValue ->
                username = newValue
            },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 8.dp, end = 8.dp)
        )

        var password by remember { mutableStateOf("") }
        OutlinedTextField(
            value = password,
            onValueChange =  { newValue ->
                password = newValue
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 8.dp, end = 8.dp)
        )

        // 3. Login button

        Button(
            onClick = {
                // Handle login click
                message = "Login button clicked! Username: $username, Password: $password"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 8.dp, end = 8.dp)
        ) {
            Text("Login")
        }
        //Text(text = message, modifier = Modifier.padding(16.dp))

        // activities figures
        Image(
            painter = painterResource(id = R.drawable.act_1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp) // Adjust the height as needed
                .padding(8.dp)
                .clip(shape = MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )


    }
}

@Composable
@Preview(showBackground = true)
fun MyAppLayoutPreview() {
    School_appTheme {
        MyAppLayout()
    }
}