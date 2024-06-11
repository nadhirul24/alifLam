package com.dicoding.capstone.ui.page

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Divided Title Page Color
        val fullText = "AlifLam"
        val spannable = SpannableString(fullText)
        val colorAlif = ForegroundColorSpan(Color.BLACK)
        spannable.setSpan(colorAlif, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val colorLam = ForegroundColorSpan(Color.rgb(101, 221, 193))
        spannable.setSpan(colorLam, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.titlePage.text = spannable

        //Hover Intent Register
        val intentLogin = findViewById<TextView>(R.id.intentLogin)
        val spannableIntent = SpannableString(intentLogin.text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                registerActivity()
            }
        }
        spannableIntent.setSpan(
            clickableSpan,
            0,
            intentLogin.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        intentLogin.text = spannableIntent
        intentLogin.movementMethod = LinkMovementMethod.getInstance()
        intentLogin.highlightColor = Color.TRANSPARENT
    }

    private fun registerActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
