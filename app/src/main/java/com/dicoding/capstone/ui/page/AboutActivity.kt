package com.dicoding.capstone.ui.page.fragment

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }
    }
}
