package com.example.centralis_kotlin.iam.presentation.views

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.foundation.text.BasicTextField
import androidx.navigation.NavHostController


@Composable
fun TextFieldView(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = textStyle.copy(color = textStyle.color.copy(alpha = 0.6f))
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpView(nav: NavHostController) {
    val textUserName = remember { mutableStateOf("") }
    val textPassword = remember { mutableStateOf("") }
    val textPassword1 = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFF160F23),
            )
    ){
        // Contenido principal que ocupa el espacio disponible
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
                            .padding(start = 24.dp,)
                    ){
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp,)
                ){
                    Text("Create your account",
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
                        value = textUserName.value,
                        onValueChange = { newText -> textUserName.value = newText },
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
                        Text("Confirm Password",
                            color = Color(0xFFFFFFFF),
                            fontSize = 16.sp,
                        )
                    }
                    TextFieldView(
                        placeholder = "Confirm  your password",
                        value = textPassword1.value,
                        onValueChange = { newText -> textPassword1.value = newText },
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
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp,horizontal = 16.dp,)
            ){
                Text("Already have an account?",
                    color = Color(0xFFA58ECC),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 2.dp,)
                )
                TextButton(
                    onClick = { 
                        // Navegar a SignInView
                        nav.navigate("SignInView")
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ){
                    Text("Sign In",
                        color = Color(0xFFD6BBFB),
                        fontSize = 14.sp,
                    )
                }
            }


            OutlinedButton(
                onClick = { nav.navigate("SignUpProfile") },
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .padding(vertical = 24.dp,horizontal = 16.dp,)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF823DF9),
                        shape = RoundedCornerShape(8.dp)
                    )
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 12.dp,)
                ){
                    Column(
                        modifier = Modifier
                            .padding(bottom = 1.dp,)
                    ){
                        Text("Sign Up",
                            color = Color(0xFFFFFFFF),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        
        // Botón fijo al final de la pantalla

    }
}