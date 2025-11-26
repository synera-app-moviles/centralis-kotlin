package com.example.centralis_kotlin.iam.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.iam.models.*
import com.example.centralis_kotlin.common.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IAMViewModel(context: Context) : ViewModel() {
    
    private val webService = RetrofitClient.iamWebService
    private val sharedPrefsManager = SharedPreferencesManager(context)
    
    // Estados para login
    var loginResult: AuthResponse? by mutableStateOf(null)
    var loginError: String? by mutableStateOf(null)
    var isLoginLoading by mutableStateOf(false)
    
    // Estados para registro completo (siguiendo la API)
    var signUpResult: AuthResponse? by mutableStateOf(null)
    var signUpError: String? by mutableStateOf(null)
    var isSignUpLoading by mutableStateOf(false)
    
    // Estados para usuarios
    var usersList: List<User> by mutableStateOf(emptyList())
    var isUsersLoading by mutableStateOf(false)
    var usersError: String? by mutableStateOf(null)
    
    // Estados para usuario individual
    var currentUser: User? by mutableStateOf(null)
    var isCurrentUserLoading by mutableStateOf(false)
    var currentUserError: String? by mutableStateOf(null)
    
    // Estados para roles
    var rolesList: List<Role> by mutableStateOf(emptyList())
    var isRolesLoading by mutableStateOf(false)
    var rolesError: String? by mutableStateOf(null)
    
    // Estado de sesión
    var isLoggedIn by mutableStateOf(sharedPrefsManager.isLoggedIn())
        private set
    
    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            isLoginLoading = true
            loginError = null
            
            try {
                val request = SignInRequest(username, password)
                val response = webService.signIn(request)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        if (authResponse != null) {
                            loginResult = authResponse
                            // Guardar datos de sesión
                            authResponse.token?.let { sharedPrefsManager.saveToken(it) }
                            sharedPrefsManager.saveUserId(authResponse.id)
                            sharedPrefsManager.saveUsername(authResponse.username)
                            isLoggedIn = true
                        } else {
                            loginError = "Respuesta vacía del servidor"
                        }
                    } else {
                        loginError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loginError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isLoginLoading = false
                }
            }
        }
    }
    
    // SignUp completo siguiendo la API exacta (Opción A)
    fun signUp(
        username: String,
        password: String,
        name: String,
        lastname: String,
        email: String,
        roles: List<String> = listOf("ROLE_USER")
    ) {
        viewModelScope.launch {
            isSignUpLoading = true
            signUpError = null
            
            try {
                val request = SignUpRequest(username, password, name, lastname, email, roles)
                val response = webService.signUp(request)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        if (authResponse != null) {
                            signUpResult = authResponse
                            // Auto-login después del registro exitoso
                            authResponse.token?.let { sharedPrefsManager.saveToken(it) }
                            sharedPrefsManager.saveUserId(authResponse.id)
                            sharedPrefsManager.saveUsername(authResponse.username)
                            isLoggedIn = true
                        } else {
                            signUpError = "Respuesta vacía del servidor"
                        }
                    } else {
                        signUpError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    signUpError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isSignUpLoading = false
                }
            }
        }
    }
    
    fun getAllUsers() {
        viewModelScope.launch {
            isUsersLoading = true
            usersError = null
            
            try {
                val token = "Bearer ${sharedPrefsManager.getToken()}"
                val response = webService.getAllUsers(token)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        usersList = response.body() ?: emptyList()
                    } else {
                        usersError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    usersError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isUsersLoading = false
                }
            }
        }
    }
    
    fun getUserById(userId: String) {
        viewModelScope.launch {
            isCurrentUserLoading = true
            currentUserError = null
            currentUser = null
            
            try {
                val token = "Bearer ${sharedPrefsManager.getToken()}"
                val response = webService.getUserById(userId, token)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        currentUser = response.body()
                    } else {
                        currentUserError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    currentUserError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isCurrentUserLoading = false
                }
            }
        }
    }
    
    fun getAllRoles() {
        viewModelScope.launch {
            isRolesLoading = true
            rolesError = null
            
            try {
                val token = "Bearer ${sharedPrefsManager.getToken()}"
                val response = webService.getAllRoles(token)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        rolesList = response.body() ?: emptyList()
                    } else {
                        rolesError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    rolesError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isRolesLoading = false
                }
            }
        }
    }
    
    fun logout() {
        sharedPrefsManager.clearAll()
        isLoggedIn = false
        loginResult = null
        signUpResult = null
        usersList = emptyList()
        rolesList = emptyList()
        clearErrors()
    }
    
    fun clearResults() {
        loginResult = null
        signUpResult = null
        clearErrors()
    }
    
    fun clearErrors() {
        loginError = null
        signUpError = null
        usersError = null
        rolesError = null
    }
}