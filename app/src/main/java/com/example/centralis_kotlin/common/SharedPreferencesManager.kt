package com.example.centralis_kotlin.common

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    
    private val sharedPrefs: SharedPreferences = 
        context.getSharedPreferences("centralis_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USER_ID_KEY = "user_id"
        private const val USERNAME_KEY = "username"
    }
    
    fun saveToken(token: String) {
        sharedPrefs.edit().putString(TOKEN_KEY, token).apply()
    }
    
    fun getToken(): String? {
        return sharedPrefs.getString(TOKEN_KEY, null)
    }
    
    fun saveUserId(userId: String) {
        sharedPrefs.edit().putString(USER_ID_KEY, userId).apply()
    }
    
    fun getUserId(): String? {
        return sharedPrefs.getString(USER_ID_KEY, null)
    }
    
    fun saveUsername(username: String) {
        sharedPrefs.edit().putString(USERNAME_KEY, username).apply()
    }
    
    fun getUsername(): String? {
        return sharedPrefs.getString(USERNAME_KEY, null)
    }
    
    fun clearAll() {
        sharedPrefs.edit().clear().apply()
    }
    
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}