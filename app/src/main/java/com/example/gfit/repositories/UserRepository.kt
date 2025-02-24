package com.example.gfit.repositories

import com.example.gfit.data.model.user.UserEntity
import com.example.gfit.data.network.dao.UserDao
import com.example.gfit.data.network.service.FirebaseAuthService
import com.example.gfit.utilis.States
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(private val userDao: UserDao) {
    fun login(email: String, password: String) = flow {
        emit(States.loading("Login..."))
        try {
            FirebaseAuthService.login(email, password).collect { state ->
                when (state) {
                    is States.Success -> {
                        val user = FirebaseAuthService.currentUser()
                        user?.let {
                            userDao.insertUser(
                                UserEntity(
                                    email = it.email!!,
                                    uid = it.uid
                                )
                            )
                        }
                        emit(state)
                    }
                    else -> emit(state)
                }
            }
        } catch (e: Exception) {
            emit(States.failed(e.message ?: "Login failed"))
        }
    }.flowOn(Dispatchers.IO)

    fun signup(email: String, password: String) = FirebaseAuthService.signup(email, password)

    suspend fun logout() {
        FirebaseAuthService.logOut()
        userDao.deleteAllUsers()
    }

    fun currentUser() = FirebaseAuthService.currentUser()

}