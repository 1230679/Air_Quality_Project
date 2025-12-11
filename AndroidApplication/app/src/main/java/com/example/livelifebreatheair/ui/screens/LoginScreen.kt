package com.example.livelifebreatheair.ui.screens

// Firebase imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livelifebreatheair.ui.theme.LiveLifeBreatheAirTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    // Gebruik dit veld als e-mail voor Firebase
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Extra state voor auth
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = Firebase.auth

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFCCECFF),
                        Color(0xFFA6D3FF),
                        Color(0xFF88ACFF)
                    )
                )
            )
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(40.dp))

            CloudLogo()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "air quality",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C2433)
            )

            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null
                },
                // Dit is in feite het e-mail veld
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), clip = false)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xE6F3F7FF)),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    cursorColor = Color.DarkGray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), clip = false)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xE6F3F7FF)),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    cursorColor = Color.DarkGray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    fontSize = 11.sp,
                    color = Color(0xCCFFFFFF),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Foutmelding onder de velden
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFFFE0E0),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    // Eenvoudige validatie
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in both email and password."
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    // Firebase email/password login
                    auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                // Succes → navigeer verder
                                onLogin()
                            } else {
                                errorMessage = task.exception?.message
                                    ?: "Login failed. Please check your credentials."
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(10.dp, RoundedCornerShape(26.dp), clip = false)
                    .clip(RoundedCornerShape(26.dp)),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1C2433)
                )
            ) {
                Text(
                    text = if (isLoading) "Logging in..." else "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0x66FFFFFF)
                )
                Text(
                    text = "  or  ",
                    fontSize = 12.sp,
                    color = Color.White
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0x66FFFFFF)
                )
            }

            Spacer(Modifier.height(14.dp))

            // Google knop nog als UI (Google Sign-In implementatie is meer werk)
            Surface(
                onClick = {
                    // TODO: Google Sign-In koppelen als je wilt
                    // Voor nu gewoon dezelfde onLogin aanroepen na een succesvolle Google login.
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(10.dp, RoundedCornerShape(26.dp), clip = false),
                shape = RoundedCornerShape(26.dp),
                color = Color(0xE6F3F7FF)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "G",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4285F4)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Log in with Google",
                        fontSize = 14.sp,
                        color = Color(0xFF1C2433)
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CloudLogo() {
    Box(
        modifier = Modifier
            .size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 8.dp, y = (-12).dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD94D))
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .size(70.dp)
                .offset(x = (-30).dp, y = 10.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LiveLifeBreatheAirTheme {
        LoginScreen(onLogin = {})
    }
}
