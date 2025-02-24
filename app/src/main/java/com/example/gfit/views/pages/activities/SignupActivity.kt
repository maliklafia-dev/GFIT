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
import com.example.gfit.databinding.ActivitySignupBinding
import com.example.gfit.repositories.UserRepository
import com.example.gfit.utilis.States
import com.example.gfit.viewmodel.UserViewModel
import com.example.gfit.viewmodel.factory.UserViewModelFactory
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var biding: ActivitySignupBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(biding.root)

        val signupBtn: Button = biding.signupBtn
        val loginTxt: TextView = biding.alreadyHaveAccountTxtView
        val repository = UserRepository()
        val factory = UserViewModelFactory(repository)

        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        signupBtn.setOnClickListener {
            val email = biding.emailEdtView.text.toString()
            val password = biding.passwordEdtView.text.toString()
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or Password can't be empty", Toast.LENGTH_SHORT).show()
            } else if(!isValidEmail(email)) {
                Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show()
            } else if(!isValidPassword(password)) {
                Toast.makeText(this, "Password is not valid", Toast.LENGTH_SHORT).show()
            } else {
                hideKeyboard(biding.root)
                lifecycleScope.launch {
                    signup(email, password)
                }
            }
        }

        loginTxt.setOnClickListener {
            navigateToLogin()
        }

    }

    private fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private suspend fun signup(email: String, password: String) {
        userViewModel.signup(email, password).collect {
            when(it) {
                is States.Loading -> {
                    Toast.makeText(this@SignupActivity, it.text, Toast.LENGTH_SHORT).show()
                }
                is States.Success -> {
                    navigateToLogin()
                }
                is States.Failed -> {
                    Toast.makeText(this@SignupActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun navigateToForm() {
        Intent(this, FormActivity1::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).also {
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

}
