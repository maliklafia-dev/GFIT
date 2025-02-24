package com.example.gfit.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gfit.repositories.UserRepository
import kotlinx.coroutines.launch


class UserViewModel(private val repository: UserRepository): ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
    fun signup(email: String, password: String) = repository.signup(email, password)
    fun logout() = viewModelScope.launch {
        repository.logout()
    }
    fun currentUser() = repository.currentUser()
}