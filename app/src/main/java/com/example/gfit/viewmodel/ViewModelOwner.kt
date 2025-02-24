package com.example.gfit.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

// ViewModelOwner.kt
object ViewModelOwner : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}