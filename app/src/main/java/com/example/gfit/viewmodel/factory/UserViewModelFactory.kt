package com.example.gfit.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gfit.repositories.UserRepository
import com.example.gfit.viewmodel.UserViewModel

class UserViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
    }
        throw IllegalArgumentException("Class viewModel not reconized")
    }
}
