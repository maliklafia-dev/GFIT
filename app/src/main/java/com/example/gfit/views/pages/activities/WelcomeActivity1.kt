package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.example.gfit.R
import com.example.gfit.data.database.AppDatabase
import com.example.gfit.repositories.UserRepository
import com.example.gfit.viewmodel.UserViewModel
import com.example.gfit.viewmodel.factory.UserViewModelFactory

class WelcomeActivity1 : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom1_4)

        val nextBtn1: Button = findViewById(R.id.next_btn1)
        val pass: TextView = findViewById(R.id.pass_txtView)
        val database = AppDatabase.getDatabase(applicationContext)
        val userDao = database.userDao()
        val repository = UserRepository(userDao)
        val factory = UserViewModelFactory(repository)

        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        nextBtn1.setOnClickListener {
            val intent = Intent(this, WelcomeActivity2::class.java)
            startActivity(intent)
        }

        pass.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateUpToMain() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateUpToWelcomeActivity2() {
        Intent(this, WelcomeActivity2::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (userViewModel.currentUser() != null) {
            navigateUpToMain()
        }
    }

}
