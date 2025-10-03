package com.example.centralis_kotlin.iam.presentation.views

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import com.example.centralis_kotlin.iam.presentation.viewmodels.IAMViewModel


@Composable
fun SignInView(
    nav: NavHostController,
    onLoginSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val iamViewModel = remember { IAMViewModel(context) }
    
    val textUsername= remember { mutableStateOf("") }
    val textPassword = remember { mutableStateOf("") }
    
    // Observar el resultado del login
    LaunchedEffect(iamViewModel.loginResult) {
        iamViewModel.loginResult?.let { result ->
            if (result.token != null) {
                onLoginSuccess()
                iamViewModel.clearResults()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFF302149),
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = Color(0xFF160F23),
                )
                .verticalScroll(rememberScrollState())
        ){
            Column(
                modifier = Modifier
                    .padding(bottom = 32.dp,)
                    .fillMaxWidth()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF160F23),
                        )
                        .padding(top = 30.dp,bottom = 16.dp,start = 64.dp,end = 16.dp,)
                ){
                    Text("Centralis",
                        color = Color(0xFFFFFFFF),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Column(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .padding(start = 24.dp)
                    ){
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp,)
                ){
                    Text("Sign in to your account",
                        color = Color(0xFFFFFFFF),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp,)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp,horizontal = 16.dp,)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp,)
                    ){
                        Text("Username",
                            color = Color(0xFFFFFFFF),
                            fontSize = 16.sp,
                        )
                    }
                    TextFieldView(
                        placeholder = "Enter  your username",
                        value = textUsername.value,
                        onValueChange = { newText -> textUsername.value = newText },
                        textStyle = TextStyle(
                            color = Color(0xFFA58ECC),
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF302149),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 16.dp,bottom = 16.dp,start = 16.dp,end = 32.dp,)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp,horizontal = 16.dp,)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp,)
                    ){
                        Text("Password",
                            color = Color(0xFFFFFFFF),
                            fontSize = 16.sp,
                        )
                    }
                    TextFieldView(
                        placeholder = "Enter  your password",
                        value = textPassword.value,
                        onValueChange = { newText -> textPassword.value = newText },
                        textStyle = TextStyle(
                            color = Color(0xFFA58ECC),
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF302149),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 16.dp,bottom = 16.dp,start = 16.dp,end = 32.dp,)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp,horizontal = 16.dp,)
                ){
                    Text("New to Centralis?",
                        color = Color(0xFFA58ECC),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(end = 3.dp,)
                    )
                    TextButton(
                        onClick = { 
                            // Navegar a SignUpView
                            nav.navigate("SignUpView")
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ){
                        Text("Sign Up",
                            color = Color(0xFFD6BBFB),
                            fontSize = 14.sp,
                        )
                    }
                }
                
                // Mostrar errores
                iamViewModel.loginError?.let { error ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                
                OutlinedButton(
                    onClick = {
                        if (textUsername.value.isNotBlank() && textPassword.value.isNotBlank()) {
                            iamViewModel.signIn(textUsername.value, textPassword.value)
                        }
                    },
                    enabled = !iamViewModel.isLoginLoading,
                    border = BorderStroke(0.dp, Color.Transparent),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .padding(vertical = 12.dp,horizontal = 16.dp,)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .background(
                            color = if (iamViewModel.isLoginLoading) Color(0xFF823DF9).copy(alpha = 0.6f) else Color(0xFF823DF9),
                            shape = RoundedCornerShape(8.dp)
                        )
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = 12.dp,)
                    ){
                        if (iamViewModel.isLoginLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.width(20.dp)
                                    .height(20.dp)
                            )
                        } else {
                            Text("Sign In",
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

        }
    }
}