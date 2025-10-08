package com.example.centralis_kotlin.iam.presentation.views

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.layout.*
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


@Composable
fun SignUpProfile(
    nav: NavHostController,
    onSaveProfile: () -> Unit = {}
) {
    val textFirstName = remember { mutableStateOf("") }
    val textLastName = remember { mutableStateOf("") }
    val textEmail= remember { mutableStateOf("") }
    val textPosition = remember { mutableStateOf("") }
    val textDepartment = remember { mutableStateOf("") }
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
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF160F23),
                    )
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
                        Text("Complete your profile",
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
                            Text("First Name",
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                            )
                        }
                        TextFieldView(
                            placeholder = "Enter  your first name",
                            value = textFirstName.value,
                            onValueChange = { newText -> textFirstName.value = newText },
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
                            Text("Last Name",
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                            )
                        }
                        TextFieldView(
                            placeholder = "Enter  your last name",
                            value = textLastName.value,
                            onValueChange = { newText -> textLastName.value = newText },
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
                            Text("Email",
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                            )
                        }
                        TextFieldView(
                            placeholder = "Enter  your email",
                            value = textEmail.value,
                            onValueChange = { newText -> textEmail.value = newText },
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
                            Text("Position",
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFF302149),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(vertical = 9.dp,horizontal = 6.dp,)
                        ){
                            TextFieldView(
                                placeholder = "Select  your  position",
                                value = textPosition.value,
                                onValueChange = { newText -> textPosition.value = newText },
                                textStyle = TextStyle(
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 16.sp,
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 7.dp,)
                            )

                        }
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
                            Text("Department",
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFF302149),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(vertical = 9.dp,horizontal = 6.dp,)
                        ){
                            TextFieldView(
                                placeholder = "Select  your  department",
                                value = textDepartment.value,
                                onValueChange = { newText -> textDepartment.value = newText },
                                textStyle = TextStyle(
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 16.sp,
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 7.dp,)
                            )

                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp,)
                    ){
                        OutlinedButton(
                            onClick = { println("Pressed!") },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(start = 16.dp,)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = Color(0xFF302149),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ){
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 9.dp,horizontal = 16.dp,)
                            ){
                                Text("Upload Profile Photo",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = onSaveProfile,
                        border = BorderStroke(0.dp, Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .padding(vertical = 12.dp,horizontal = 16.dp,)
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
                            Text("Save Profile",
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