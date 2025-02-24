package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.example.gfit.R

class WelcomeActivity2 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom2_4)

        val nextBtn2: Button = findViewById(R.id.next_btn2)
        nextBtn2.setOnClickListener {
            val intent = Intent(this, WelcomeActivity3::class.java)
            startActivity(intent)
        }
    }

}
