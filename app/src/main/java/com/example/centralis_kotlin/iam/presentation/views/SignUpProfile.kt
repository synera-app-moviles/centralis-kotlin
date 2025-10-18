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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import com.example.centralis_kotlin.profile.presentation.viewmodels.ProfileViewModel
import com.example.centralis_kotlin.common.SharedPreferencesManager
import com.example.centralis_kotlin.profile.models.Position
import com.example.centralis_kotlin.profile.models.Department
import com.example.centralis_kotlin.common.components.CustomDropDownMenu
/*
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
*/

@Composable
fun SignUpProfile(
    nav: NavHostController,
    userId: String = "", // Recibir userId como parámetro
    onSaveProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val profileViewModel = remember { ProfileViewModel(context) }
    val sharedPrefsManager = remember { SharedPreferencesManager(context) }
    
    // Obtener userId si no se pasa como parámetro
    val actualUserId = userId.ifEmpty { sharedPrefsManager.getUserId() ?: "" }
    
    val textFirstName = remember { mutableStateOf("") }
    val textLastName = remember { mutableStateOf("") }
    val textEmail= remember { mutableStateOf("") }
    var selectedPosition by remember { mutableStateOf<Position?>(Position.EMPLOYEE) }
    var selectedDepartment by remember { mutableStateOf<Department?>(Department.IT) }
    
    // Observar el resultado de crear perfil
    LaunchedEffect(profileViewModel.createProfileResult) {
        profileViewModel.createProfileResult?.let { result ->
            if (result.profileId.isNotEmpty()) {
                onSaveProfile()
                profileViewModel.clearResults()
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
                    
                    // Mostrar errores
                    profileViewModel.operationError?.let { error ->
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
                            // Validar que todos los campos estén llenos
                            if (textFirstName.value.isNotBlank() && 
                                textLastName.value.isNotBlank() && 
                                textEmail.value.isNotBlank() &&
                                selectedPosition != null &&
                                selectedDepartment != null &&
                                actualUserId.isNotEmpty()) {
                                
                                profileViewModel.createProfile(
                                    userId = actualUserId,
                                    firstName = textFirstName.value,
                                    lastName = textLastName.value,
                                    email = textEmail.value,
                                    avatarUrl = null, // Por ahora null
                                    position = selectedPosition!!,
                                    department = selectedDepartment!!
                                )
                            }
                        },
                        enabled = !profileViewModel.isOperationLoading,
                        border = BorderStroke(0.dp, Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .padding(vertical = 12.dp,horizontal = 16.dp,)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .background(
                                color = if (profileViewModel.isOperationLoading) Color(0xFF823DF9).copy(alpha = 0.6f) else Color(0xFF823DF9),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(vertical = 12.dp,)
                        ){
                            if (profileViewModel.isOperationLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
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
}