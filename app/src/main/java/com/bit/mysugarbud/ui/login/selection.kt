package com.bit.mysugarbud.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bit.mysugarbud.R

class selection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selection_activity)

        val signup = findViewById<Button>(R.id.buttonChooseSignUp)
        val signin = findViewById<Button>(R.id.buttonChooseLogin)

        signup.setOnClickListener {
            val intent = Intent (this, com.bit.mysugarbud.ui.login.signup::class.java)
            startActivity(intent)
        }
        signin.setOnClickListener {
            val intent = Intent(this, com.bit.mysugarbud.ui.login.signin::class.java)
            startActivity(intent)
        }
    }
}