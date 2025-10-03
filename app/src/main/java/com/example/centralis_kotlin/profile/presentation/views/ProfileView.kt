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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileView(
    nav: NavHostController,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val profileViewModel = remember { ProfileViewModel() }
    val sharedPrefsManager = remember { SharedPreferencesManager(context) }
    
    // Obtener userId y cargar perfil
    val userId = sharedPrefsManager.getUserId() ?: ""
    
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            profileViewModel.getProfileByUserId(userId)
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
                        .padding(bottom = 331.dp,)
                        .fillMaxWidth()
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF160F23),
                            )
                            .padding(vertical = 16.dp,)
                    ){
                        /*
                        Column(
                            modifier = Modifier
                                .padding(top = 12.dp,bottom = 12.dp,end = 24.dp,)
                        ){
                            IconButton(
                                //Regresa a la pantalla anterior
                                onClick = { nav.popBackStack() },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFFFFFFFF),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }

                        }*/
                        Text("Profile",
                            color = Color(0xFFFFFFFF),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Column(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                        ){
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        Column(
                        ){
                            GlideImage(
                                model = "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg",
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 16.dp)
                                    .size(128.dp)
                                    .clip(CircleShape)
                            )
                            Column(
                            ){
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 31.dp,)
                                        .padding(bottom = 1.dp,)
                                ){
                                    Text("Teruko Asakura",
                                        color = Color(0xFFFFFFFF),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(bottom = 1.dp,)
                                        .align(Alignment.CenterHorizontally)
                                ){
                                    Text("teruko.asakura@centralis.com",
                                        color = Color(0xFFA88ECC),
                                        fontSize = 16.sp,
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp,)
                                        .padding(bottom = 1.dp,)
                                        .align(Alignment.CenterHorizontally)
                                ){
                                    Text("Ing. Software",
                                        color = Color(0xFFA88ECC),
                                        fontSize = 16.sp,
                                    )
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
                            onClick = { println("Pressed!") },
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
                                    Text("Edit profile",
                                        color = Color(0xFFFFFFFF),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                        OutlinedButton(
                            onClick = onLogout,
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
                                    Text("Sign out",
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
}