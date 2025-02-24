package com.example.gfit.data.network.service

import com.example.gfit.utilis.States
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

object FirebaseAuthService {
    private val firebaseAuth = Firebase.auth

    fun login(email: String, password: String) = flow {
        emit(States.loading<String>("Login..."))

        val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        emit(States.success(loginResult))
    }.catch {
        emit(States.failed(it.message!!))
    }.flowOn(Dispatchers.IO)

    fun signup(email: String, password: String) = flow {
        emit(States.loading<String>("Sign in up..."))

        val signupResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        emit(States.success(signupResult))
    }.catch {
        emit(States.failed(it.message!!))
    }.flowOn(Dispatchers.IO)

    fun logOut() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}