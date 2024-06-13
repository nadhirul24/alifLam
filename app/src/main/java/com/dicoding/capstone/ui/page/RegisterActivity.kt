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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.R
import com.dicoding.capstone.data.repository.ResultState
import com.dicoding.capstone.databinding.ActivityRegisterBinding
import com.dicoding.capstone.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Divided Title Page Color
        val fullText = "AlifLam"
        val spannable = SpannableString(fullText)
        val colorAlif = ForegroundColorSpan(Color.BLACK)
        spannable.setSpan(colorAlif, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val colorLam = ForegroundColorSpan(Color.rgb(101,221,193))
        spannable.setSpan(colorLam, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.titlePage.text = spannable

        //Hover Intent Login
        val intentLogin = findViewById<TextView>(R.id.intentLogin)
        val spannableIntent = SpannableString(intentLogin.text)
        val clickableSpan: ClickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                loginActivity()
            }
        }
        spannableIntent.setSpan(clickableSpan, 0, intentLogin.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        intentLogin.text = spannableIntent
        intentLogin.movementMethod = LinkMovementMethod.getInstance()
        intentLogin.highlightColor = Color.TRANSPARENT

        //Register Button
        binding.registerButton.setOnClickListener {
            registUser()
        }

        //Observe regist result
        viewModel.registResult.observe(this) { result ->
            when(result){
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                   binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()
                    loginActivity()
                }
                is ResultState.Error ->{
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun loginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registUser(){
        binding.apply {
            val fullname = fullNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmpassword = confirmPassEditText.text.toString()

            if(fullname.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()){
                if (confirmpassword == password){
                viewModel.registUser(fullname, username, password)
                } else {
                    Toast.makeText(this@RegisterActivity, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@RegisterActivity, "Mohon isi semua kolom pendaftaran", Toast.LENGTH_SHORT).show()
            }
        }
    }

}