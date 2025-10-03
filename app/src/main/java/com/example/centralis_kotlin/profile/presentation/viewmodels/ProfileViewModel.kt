package com.example.centralis_kotlin.profile.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.profile.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {
    
    private val webService = RetrofitClient.profileWebService
    
    // Estados para perfil individual
    var currentProfile: ProfileResponse? by mutableStateOf(null)
    var isProfileLoading by mutableStateOf(false)
    var profileError: String? by mutableStateOf(null)
    
    // Estados para lista de perfiles
    var profilesList: List<ProfileResponse> by mutableStateOf(emptyList())
    var isProfilesListLoading by mutableStateOf(false)
    var profilesListError: String? by mutableStateOf(null)
    
    // Estados para operaciones
    var createProfileResult: ProfileResponse? by mutableStateOf(null)
    var updateProfileResult: ProfileResponse? by mutableStateOf(null)
    var operationError: String? by mutableStateOf(null)
    var isOperationLoading by mutableStateOf(false)
    
    fun createProfile(
        userId: String,
        firstName: String,
        lastName: String,
        email: String,
        avatarUrl: String?,
        position: String,
        department: String
    ) {
        viewModelScope.launch {
            isOperationLoading = true
            operationError = null
            
            try {
                val request = ProfileRequest(userId, firstName, lastName, email, avatarUrl, position, department)
                val response = webService.createProfile(request)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        if (profileResponse != null) {
                            createProfileResult = profileResponse
                            currentProfile = profileResponse
                        } else {
                            operationError = "Respuesta vacía del servidor"
                        }
                    } else {
                        operationError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    operationError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isOperationLoading = false
                }
            }
        }
    }
    
    fun getProfileByUserId(userId: String) {
        viewModelScope.launch {
            isProfileLoading = true
            profileError = null
            
            try {
                val response = webService.getProfileByUserId(userId)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        currentProfile = response.body()
                        if (currentProfile == null) {
                            profileError = "Perfil no encontrado"
                        }
                    } else {
                        profileError = "Error ${response.code()}: ${response.message()}"
                        currentProfile = null
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    profileError = "Error de conexión: ${e.message}"
                    currentProfile = null
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isProfileLoading = false
                }
            }
        }
    }
    
    fun getAllProfiles() {
        viewModelScope.launch {
            isProfilesListLoading = true
            profilesListError = null
            
            try {
                val response = webService.getAllProfiles()
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        profilesList = response.body() ?: emptyList()
                    } else {
                        profilesListError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    profilesListError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isProfilesListLoading = false
                }
            }
        }
    }
    
    fun updateProfile(
        profileId: String,
        firstName: String,
        lastName: String,
        email: String,
        avatarUrl: String?,
        position: String,
        department: String
    ) {
        viewModelScope.launch {
            isOperationLoading = true
            operationError = null
            
            try {
                val request = UpdateProfileRequest(firstName, lastName, email, avatarUrl, position, department)
                val response = webService.updateProfile(profileId, request)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        if (profileResponse != null) {
                            updateProfileResult = profileResponse
                            currentProfile = profileResponse
                        } else {
                            operationError = "Respuesta vacía del servidor"
                        }
                    } else {
                        operationError = "Error ${response.code()}: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    operationError = "Error de conexión: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isOperationLoading = false
                }
            }
        }
    }
    
    fun clearResults() {
        createProfileResult = null
        updateProfileResult = null
        operationError = null
        profileError = null
        profilesListError = null
    }
}