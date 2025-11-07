package com.example.centralis_kotlin.profile.presentation.views

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.layout.*
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import com.example.centralis_kotlin.profile.presentation.viewmodels.ProfileViewModel
import com.example.centralis_kotlin.common.SharedPreferencesManager
import com.example.centralis_kotlin.profile.models.Position
import com.example.centralis_kotlin.profile.models.Department
import com.example.centralis_kotlin.common.components.CustomDropDownMenu
import com.example.centralis_kotlin.common.components.AvatarImageView
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import android.util.Log
import androidx.compose.material.icons.filled.Notifications
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun SimpleTextFieldView(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = TextStyle(color = Color(0xFFFFFFFF).copy(alpha = 0.6f), fontSize = 16.sp)
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color(0xFFFFFFFF), fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileView(
    nav: NavHostController,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val profileViewModel = remember { ProfileViewModel(context) }
    val sharedPrefsManager = remember { SharedPreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // Estados para el formulario de edición
    var isEditingProfile by remember { mutableStateOf(false) }
    var editFirstName by remember { mutableStateOf("") }
    var editLastName by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editAvatarUrl by remember { mutableStateOf<String?>(null) }
    var editPosition by remember { mutableStateOf<Position?>(Position.EMPLOYEE) }
    var editDepartment by remember { mutableStateOf<Department?>(Department.IT) }
    
    // Obtener userId y cargar perfil
    val userId = sharedPrefsManager.getUserId() ?: ""
    
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            profileViewModel.getProfileByUserId(userId)
        }
    }
    
    // Llenar campos de edición cuando se carga el perfil
    LaunchedEffect(profileViewModel.currentProfile) {
        profileViewModel.currentProfile?.let { profile ->
            editFirstName = profile.firstName
            editLastName = profile.lastName
            editEmail = profile.email
            editAvatarUrl = profile.avatarUrl
            editPosition = profile.position
            editDepartment = profile.department
        }
    }
    
    // Observar cuando se completa la creación/actualización de perfil
    LaunchedEffect(profileViewModel.createProfileResult, profileViewModel.updateProfileResult) {
        if (profileViewModel.createProfileResult != null || profileViewModel.updateProfileResult != null) {
            // Recargar el perfil para mostrar los datos actualizados
            profileViewModel.getProfileByUserId(userId)
            profileViewModel.clearResults()
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
                   Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFF160F23))
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            "Profile",
                            color = Color(0xFFFFFFFF),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { nav.navigate(com.example.centralis_kotlin.common.navigation.NavigationRoutes.NOTIFICATIONS) },
                            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFFFFFFFF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        // DATOS DIRECTOS SIN VALIDACIONES - PARA DEBUG
                        val profile = profileViewModel.currentProfile
                        val username = sharedPrefsManager.getUsername() ?: "Usuario"
                        val userId = sharedPrefsManager.getUserId() ?: ""
                        val authToken = sharedPrefsManager.getToken() ?: "NO_TOKEN"
                        var fcmToken by remember { mutableStateOf<String?>(null) }
                        var tokenRegistered by remember { mutableStateOf<Boolean?>(null) }

                        // Obtener el FCM token al cargar la vista
                        LaunchedEffect(Unit) {
                            try {
                                val tokenManager = com.example.centralis_kotlin.common.di.DependencyFactory.getDeviceTokenManager(context)
                                fcmToken = tokenManager.getStoredToken()
                            } catch (e: Exception) {
                                fcmToken = "ERROR_GETTING_TOKEN"
                            }
                        }

        fun checkTokenRegistration() {
            val uid = sharedPrefsManager.getUserId() ?: ""
            val currentFcmToken = fcmToken
            
            if (uid.isEmpty() || currentFcmToken.isNullOrEmpty()) {
                tokenRegistered = null
                return
            }

            coroutineScope.launch {
                try {
                    val authToken = sharedPrefsManager.getToken() ?: ""
                    val resp = com.example.centralis_kotlin.common.RetrofitClient.fcmApiService.getFCMTokens(uid, "Bearer $authToken")
                    
                    if (resp.isSuccessful) {
                        val tokens = resp.body() ?: emptyList()
                        val isRegistered = tokens.any { it.fcmToken == currentFcmToken }
                        tokenRegistered = isRegistered
                    } else {
                        tokenRegistered = null
                    }
                } catch (e: Exception) {
                    tokenRegistered = null
                }
            }
        }                        // URL de avatar - usar la del perfil o por defecto si está vacía/null
                        val avatarUrl = if (profile?.avatarUrl.isNullOrEmpty()) {
                            "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg"
                        } else {
                            profile?.avatarUrl
                        }
                        

                        
                        // MOSTRAR DATOS SIEMPRE
                        Column {
                            GlideImage(
                                model = avatarUrl,
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 16.dp)
                                    .size(128.dp)
                                    .clip(CircleShape)
                            )
                            
                            Column {
                                    // Nombre completo
                                    Column(
                                        modifier = Modifier
                                            .padding(horizontal = 31.dp,)
                                            .padding(bottom = 1.dp,)
                                            .align(Alignment.CenterHorizontally)
                                    ){
                                        Text(
                                            text = profile?.fullName ?: "${profile?.firstName ?: username} ${profile?.lastName ?: ""}".trim(),
                                            color = Color(0xFFFFFFFF),
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                    
                                    // Email
                                    Column(
                                        modifier = Modifier
                                            .padding(bottom = 1.dp,)
                                            .align(Alignment.CenterHorizontally)
                                    ){
                                        Text(
                                            text = profile?.email ?: "email@centralis.com",
                                            color = Color(0xFFA88ECC),
                                            fontSize = 16.sp,
                                        )
                                    }

                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp,)
                    ){
                        OutlinedButton(
                            onClick = { 
                                isEditingProfile = !isEditingProfile
                            },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(bottom = 12.dp,start = 16.dp,end = 16.dp,)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFF8E3DF9),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(vertical = 9.dp,)
                            ){
                                Column(
                                ){
                                    Text(if (isEditingProfile) "Cancel" else "Edit Profile",
                                        color = Color(0xFFFFFFFF),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                        
                        // Formulario de edición (solo se muestra cuando isEditingProfile es true)
                        if (isEditingProfile) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                // First Name
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text("First Name", color = Color(0xFFFFFFFF), fontSize = 14.sp)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = Color(0xFF302149),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        SimpleTextFieldView(
                                            placeholder = "Enter your first name",
                                            value = editFirstName,
                                            onValueChange = { editFirstName = it }
                                        )
                                    }
                                }
                                
                                // Last Name
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text("Last Name", color = Color(0xFFFFFFFF), fontSize = 14.sp)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = Color(0xFF302149),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        SimpleTextFieldView(
                                            placeholder = "Enter your last name",
                                            value = editLastName,
                                            onValueChange = { editLastName = it }
                                        )
                                    }
                                }
                                
                                // Email
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text("Email", color = Color(0xFFFFFFFF), fontSize = 14.sp)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = Color(0xFF302149),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        SimpleTextFieldView(
                                            placeholder = "Enter your email",
                                            value = editEmail,
                                            onValueChange = { editEmail = it }
                                        )
                                    }
                                }
                                
                                // Avatar Image Selector
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Foto de Perfil", 
                                        color = Color(0xFFFFFFFF), 
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    AvatarImageView(
                                        imageUrl = editAvatarUrl,
                                        onImageChange = { newUrl ->
                                            editAvatarUrl = newUrl
                                        },
                                        onImageRemoved = {
                                            editAvatarUrl = null
                                        },
                                        size = 80.dp
                                    )
                                }
                                
                                // Position Dropdown
                                CustomDropDownMenu(
                                    label = "Position",
                                    placeholder = "Select your position",
                                    selectedOption = editPosition,
                                    options = Position.values().toList(),
                                    onOptionSelected = { editPosition = it },
                                    getDisplayText = { it.displayName }
                                )
                                
                                // Department Dropdown  
                                CustomDropDownMenu(
                                    label = "Department",
                                    placeholder = "Select your department",
                                    selectedOption = editDepartment,
                                    options = Department.values().toList(),
                                    onOptionSelected = { editDepartment = it },
                                    getDisplayText = { it.displayName }
                                )
                                
                                // Save Button
                                OutlinedButton(
                                    onClick = {
                                        if (editFirstName.isNotBlank() && 
                                            editLastName.isNotBlank() && 
                                            editEmail.isNotBlank() &&
                                            editPosition != null &&
                                            editDepartment != null) {
                                            
                                            if (profileViewModel.currentProfile != null) {
                                                // Actualizar perfil existente
                                                profileViewModel.updateProfile(
                                                    profileId = profileViewModel.currentProfile!!.profileId,
                                                    firstName = editFirstName,
                                                    lastName = editLastName,
                                                    email = editEmail,
                                                    avatarUrl = editAvatarUrl,
                                                    position = editPosition!!,
                                                    department = editDepartment!!
                                                )
                                            } else {
                                                // Crear nuevo perfil
                                                profileViewModel.createProfile(
                                                    userId = userId,
                                                    firstName = editFirstName,
                                                    lastName = editLastName,
                                                    email = editEmail,
                                                    avatarUrl = editAvatarUrl,
                                                    position = editPosition!!,
                                                    department = editDepartment!!
                                                )
                                            }
                                            isEditingProfile = false
                                        }
                                    },
                                    enabled = !profileViewModel.isOperationLoading,
                                    border = BorderStroke(0.dp, Color.Transparent),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                                    contentPadding = PaddingValues(),
                                    modifier = Modifier
                                        .padding(vertical = 12.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .fillMaxWidth()
                                        .background(
                                            color = if (profileViewModel.isOperationLoading) Color(0xFF823DF9).copy(alpha = 0.6f) else Color(0xFF823DF9),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    ) {
                                        if (profileViewModel.isOperationLoading) {
                                            CircularProgressIndicator(
                                                color = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Text(
                                                text = if (profileViewModel.currentProfile != null) "Update Profile" else "Create Profile",
                                                color = Color(0xFFFFFFFF),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }
                                
                                // Mostrar errores
                                profileViewModel.operationError?.let { error ->
                                    Text(
                                        text = error,
                                        color = Color.Red,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }

                        // Botón para ver anuncios guardados
                        OutlinedButton(
                            onClick = {
                                nav.navigate(com.example.centralis_kotlin.common.navigation.NavigationRoutes.SAVED_ANNOUNCEMENTS)
                            },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFF8E3DF9),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 9.dp)
                            ) {
                                Text(
                                    "Ver anuncios guardados",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }


                        OutlinedButton(
                            onClick = {
                                // Limpiar datos del perfil y logout
                                profileViewModel.clearResults()
                                
                                // Limpiar token FCM del dispositivo
                                coroutineScope.launch {
                                    try {
                                        val deviceTokenManager = com.example.centralis_kotlin.common.di.DependencyFactory.getDeviceTokenManager(context)
                                        deviceTokenManager.clearToken()
                                    } catch (e: Exception) {
                                        // Error silencioso
                                    }
                                }
                                
                                sharedPrefsManager.clearAll()
                                onLogout()
                            },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(horizontal = 16.dp,)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFF332149),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(vertical = 9.dp,)
                            ){
                                Column(
                                ){
                                    Text("Sign Out",
                                        color = Color(0xFFFFFFFF),
                                        fontSize = 14.sp,
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
