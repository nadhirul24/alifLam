package com.dicoding.capstone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.data.repository.ResultState
import com.dicoding.capstone.data.repository.User
import com.dicoding.capstone.data.repository.UserRepository

class RegisterViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _registResult = MutableLiveData<ResultState<String>>()
    val registResult: LiveData<ResultState<String>> = _registResult

    fun registUser(fullname: String, username: String, password: String){
        _registResult.value = ResultState.Loading
        val user = User(fullname = fullname, username = username, password = password)
        userRepository.registUser(user){success, message ->
            if (success){
                _registResult.postValue(ResultState.Success(message ?: "Registration successful"))
            } else {
                _registResult.postValue(ResultState.Error(message ?: "Registration failed"))
            }
        }
    }
}