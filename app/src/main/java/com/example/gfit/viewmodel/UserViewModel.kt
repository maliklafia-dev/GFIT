package com.example.gfit.viewmodel


import androidx.lifecycle.ViewModel
import com.example.gfit.repositories.UserRepository


class UserViewModel(private val repository: UserRepository): ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
    fun signup(email: String, password: String) = repository.signup(email, password)
    fun logout() = repository.logout()
    fun currentUser() = repository.currentUser()
}