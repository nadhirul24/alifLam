package com.dicoding.capstone.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.capstone.data.repository.ResultState
import com.dicoding.capstone.data.repository.User
import com.dicoding.capstone.data.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()

    private val _loginResult = MutableLiveData<ResultState<String>>()
    val loginResult: LiveData<ResultState<String>> = _loginResult

    private val sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun loginUser(username: String, password: String){
        _loginResult.value = ResultState.Loading
        userRepository.loginUser(username, password){success, message, user ->
            if (success){
                user?.let {
                    saveUserToPreferences(it)
                }
                _loginResult.postValue(ResultState.Success(message ?: "Login successful"))
            } else {
                _loginResult.postValue(ResultState.Error(message ?: "Login Failed"))
            }
        }
    }

    private fun saveUserToPreferences(user: User) {
        with(sharedPreferences.edit()) {
            putString("fullname", user.fullname)
            putString("username", user.username)
            putString("profile_image_url", user.profileImageUrl) // Simpan URL gambar profil
            apply()
        }
    }
}
