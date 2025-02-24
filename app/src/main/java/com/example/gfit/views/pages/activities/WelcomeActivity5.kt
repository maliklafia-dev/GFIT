package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gfit.R
import com.example.gfit.databinding.ActivityWelcom5Binding

class WelcomeActivity5: AppCompatActivity() {
    private lateinit var biding: ActivityWelcom5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom5)

        biding = ActivityWelcom5Binding.inflate(layoutInflater)
        setContentView(biding.root)

        biding.letsgoBtn.setOnClickListener {
            Intent(this, FormActivity1::class.java).also {
                startActivity(it)
                finish()
            }
        }

    }
}