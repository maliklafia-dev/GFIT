package com.example.gfit.repositories

import com.example.gfit.data.network.service.FirebaseAuthService

class UserRepository {
    fun login(email: String, password: String) = FirebaseAuthService.login(email, password)
    fun signup(email: String, password: String) = FirebaseAuthService.signup(email, password)
    fun logout() = FirebaseAuthService.logOut()
    fun currentUser() = FirebaseAuthService.currentUser()
}