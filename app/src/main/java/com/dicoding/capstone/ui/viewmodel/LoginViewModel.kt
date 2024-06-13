package com.dicoding.capstone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.data.repository.ResultState
import com.dicoding.capstone.data.repository.UserRepository

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _loginResult = MutableLiveData<ResultState<String>>()
    val loginResult: LiveData<ResultState<String>> = _loginResult

    fun loginUser(username: String, password: String){
        _loginResult.value = ResultState.Loading
        userRepository.loginUser(username, password){success, message ->
            if (success){
                _loginResult.postValue(ResultState.Success(message ?: "Login successful"))
            } else {
                _loginResult.postValue(ResultState.Error(message ?: "Login Failed"))
            }
        }
    }
}