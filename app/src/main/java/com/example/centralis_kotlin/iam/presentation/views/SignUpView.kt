package com.example.centralis_kotlin.iam.presentation.views

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
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
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.NavHostController
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import com.example.centralis_kotlin.iam.presentation.viewmodels.IAMViewModel
import com.example.centralis_kotlin.common.components.CustomDropDownMenu
import com.example.centralis_kotlin.profile.models.Position
import com.example.centralis_kotlin.profile.models.Department
import com.example.centralis_kotlin.common.components.AvatarImageView

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
fun SignUpView(
    nav: NavHostController,
    onSignUpSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val iamViewModel = remember { IAMViewModel(context) }
    
    // Estados para todos los campos del registro completo
    val textUserName = remember { mutableStateOf("") }
    val textPassword = remember { mutableStateOf("") }
    val textPassword1 = remember { mutableStateOf("") }
    val textName = remember { mutableStateOf("") }
    val textLastName = remember { mutableStateOf("") }
    val textEmail = remember { mutableStateOf("") }
    var selectedPosition by remember { mutableStateOf<Position?>(null) }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    
    // Observar el resultado del registro
    LaunchedEffect(iamViewModel.signUpResult) {
        iamViewModel.signUpResult?.let { result ->
            if (result.id.isNotEmpty()) {
                onSignUpSuccess() // Ir directamente a la aplicación después del registro exitoso
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
                
                // Avatar selector
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarImageView(
                        imageUrl = avatarUrl,
                        onImageChange = { newUrl ->
                            avatarUrl = newUrl
                        },
                        onImageRemoved = {
                            avatarUrl = null
                        },
                        size = 100.dp
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
                
                // Campo Name
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
                        Text("Name",
                            color = Color(0xFFFFFFFF),
                            fontSize = 16.sp,
                        )
                    }
                    TextFieldView(
                        placeholder = "Enter your name",
                        value = textName.value,
                        onValueChange = { newText -> textName.value = newText },
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
                
                // Campo Last Name
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
                        placeholder = "Enter your last name",
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
                
                // Campo Email
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
                        placeholder = "Enter your email",
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
                
                // Position Dropdown
                CustomDropDownMenu(
                    label = "Position",
                    placeholder = "Select your position",
                    selectedOption = selectedPosition,
                    options = Position.values().toList(),
                    onOptionSelected = { selectedPosition = it },
                    getDisplayText = { it.displayName }
                )
                
                // Department Dropdown
                CustomDropDownMenu(
                    label = "Department",
                    placeholder = "Select your department",
                    selectedOption = selectedDepartment,
                    options = Department.values().toList(),
                    onOptionSelected = { selectedDepartment = it },
                    getDisplayText = { it.displayName }
                )
                
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
                
                // Validación visual de contraseñas
                if (textPassword.value.isNotEmpty() && textPassword1.value.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        if (textPassword.value != textPassword1.value) {
                            Text(
                                text = "Las contraseñas no coinciden",
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        } else {
                            Text(
                                text = "Las contraseñas coinciden",
                                color = Color.Green,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
            
            // Mensaje informativo sobre el avatar
            if (avatarUrl != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "✓ Avatar seleccionado. Podrás verlo en tu perfil después del registro.",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            
            // Mostrar errores del ViewModel
            iamViewModel.signUpError?.let { error ->
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
            
            // Validación de campos obligatorios
            if (textUserName.value.isNotBlank() || textPassword.value.isNotBlank() || 
                textName.value.isNotBlank() || textLastName.value.isNotBlank() || 
                textEmail.value.isNotBlank()) {
                
                val missingFields = mutableListOf<String>()
                if (textUserName.value.isBlank()) missingFields.add("Username")
                if (textName.value.isBlank()) missingFields.add("Name")
                if (textLastName.value.isBlank()) missingFields.add("Last Name")
                if (textEmail.value.isBlank()) missingFields.add("Email")
                if (selectedPosition == null) missingFields.add("Position")
                if (selectedDepartment == null) missingFields.add("Department")
                if (textPassword.value.isBlank()) missingFields.add("Password")
                if (textPassword1.value.isBlank()) missingFields.add("Confirm Password")
                
                if (missingFields.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Missing fields: ${missingFields.joinToString(", ")}",
                            color = Color(0xFFFFAA00),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }


            OutlinedButton(
                onClick = {
                    // Validar que todos los campos estén llenos y las contraseñas coincidan
                    if (textUserName.value.isNotBlank() && 
                        textPassword.value.isNotBlank() && 
                        textPassword1.value.isNotBlank() &&
                        textName.value.isNotBlank() &&
                        textLastName.value.isNotBlank() &&
                        textEmail.value.isNotBlank() &&
                        selectedPosition != null &&
                        selectedDepartment != null) {
                        
                        if (textPassword.value == textPassword1.value) {
                            iamViewModel.signUp(
                                username = textUserName.value,
                                password = textPassword.value,
                                name = textName.value,
                                lastname = textLastName.value,
                                email = textEmail.value
                            )
                        } else {
                            // Las contraseñas no coinciden - podrías mostrar un error aquí
                        }
                    }
                },
                enabled = !iamViewModel.isSignUpLoading && 
                         textUserName.value.isNotBlank() && 
                         textPassword.value.isNotBlank() && 
                         textPassword1.value.isNotBlank() &&
                         textName.value.isNotBlank() &&
                         textLastName.value.isNotBlank() &&
                         textEmail.value.isNotBlank() &&
                         selectedPosition != null &&
                         selectedDepartment != null &&
                         textPassword.value == textPassword1.value,
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .padding(vertical = 24.dp,horizontal = 16.dp,)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(
                        color = if (iamViewModel.isSignUpLoading) {
                            Color(0xFF823DF9).copy(alpha = 0.6f)
                        } else if (textUserName.value.isNotBlank() && 
                                  textPassword.value.isNotBlank() && 
                                  textPassword1.value.isNotBlank() &&
                                  textName.value.isNotBlank() &&
                                  textLastName.value.isNotBlank() &&
                                  textEmail.value.isNotBlank() &&
                                  selectedPosition != null &&
                                  selectedDepartment != null &&
                                  textPassword.value == textPassword1.value) {
                            Color(0xFF823DF9)
                        } else {
                            Color(0xFF823DF9).copy(alpha = 0.4f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 12.dp,)
                ){
                    if (iamViewModel.isSignUpLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
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