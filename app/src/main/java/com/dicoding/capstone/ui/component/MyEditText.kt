package com.dicoding.capstone.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class UsernameEditText : AppCompatEditText {
    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attr: AttributeSet) : super(context, attr){
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyle: Int) : super(context, attr, defStyle){
        init()
    }

    private fun init(){
        addTextChangedListener(object  : TextWatcher{
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (!usernameMatch(s.toString())){
                    "Masukkan nama pengguna yang tepat!"
                } else{
                    null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun usernameMatch(username: String): Boolean{
        val usernamePattern = "^[a-zA-Z0-9]*$"
        return username.matches(Regex(usernamePattern))
    }
}

class MyEditText : AppCompatEditText{
    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr){
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyle: Int) : super (context, attr, defStyle){
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s.toString().length < 8){
                    "Panjang password harus lebih dari 8 huruf!"
                } else{
                    null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}