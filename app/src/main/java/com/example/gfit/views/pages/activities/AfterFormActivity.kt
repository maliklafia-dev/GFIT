package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gfit.databinding.ActivityAfterFormBinding

class AfterFormActivity: AppCompatActivity() {
    private lateinit var biding: ActivityAfterFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biding = ActivityAfterFormBinding.inflate(layoutInflater)
        setContentView(biding.root)

        biding.letsgoBtn.setOnClickListener {
            navigateToMain()
        }

    }

    private fun navigateToMain(){
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}