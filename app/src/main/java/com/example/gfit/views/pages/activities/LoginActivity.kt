package com.example.gfit.views.pages.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gfit.data.database.AppDatabase
import com.example.gfit.databinding.ActivityLoginBinding
import com.example.gfit.repositories.UserRepository
import com.example.gfit.utilis.States
import com.example.gfit.viewmodel.UserViewModel
import com.example.gfit.viewmodel.factory.UserViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var biding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(biding.root)

        val database = AppDatabase.getDatabase(applicationContext)
        val userDao = database.userDao()
        val repository = UserRepository(userDao)
        val factory = UserViewModelFactory(repository)
        val loginBtn: Button = biding.loginBtn
        val signupTxt: TextView = biding.signupTxtView

        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        loginBtn.setOnClickListener {
            val email = biding.emailEdtView.text.toString()
            val password = biding.passwordEdtView.text.toString()
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Email or Password can't be empty", Toast.LENGTH_SHORT).show()
            } else if(!isValidEmail(email)) {
                Toast.makeText(this@LoginActivity, "Email is not valid", Toast.LENGTH_SHORT).show()
            } else if(!isValidPassword(password)) {
                Toast.makeText(this@LoginActivity, "Password is not valid", Toast.LENGTH_SHORT).show()
            } else {
                hideKeyboard(biding.root)
                lifecycleScope.launch {
                    login(email, password)
                }
            }
        }

        biding.signupTxtView.setOnClickListener {
             navigateToSignup()
         }

    }

    private fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private suspend fun login(email: String, password: String) {
        userViewModel.login(email, password).collect {
            when(it) {
                is States.Loading -> {
                    Toast.makeText(this@LoginActivity, it.text, Toast.LENGTH_SHORT).show()
                }
                is States.Success -> {
                    if(userViewModel.currentUser()?.uid == null) {
                        navigateToWelcomeActivity5()
                    } else {
                        navigateToMain()
                    }

                }
                is States.Failed -> {
                    Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToWelcomeActivity5() {
        Intent(this, WelcomeActivity5::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToMain() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToSignup() {
        Intent(this, SignupActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!?.]).{8,}$".toRegex()
        return password.matches(passwordRegex)
    }

    override fun onStart() {
        super.onStart()
        if(userViewModel.currentUser() != null){
            navigateToMain()
        }
    }

}
