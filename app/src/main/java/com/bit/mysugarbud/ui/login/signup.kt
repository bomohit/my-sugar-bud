package com.bit.mysugarbud.ui.login

import android.os.Bundle
import android.text.BoringLayout.make
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bit.mysugarbud.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class signup : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup_activity)

        val username = findViewById<TextView>(R.id.inputSignupUsername).text
        val password = findViewById<TextView>(R.id.inputSignupPassword).text
        val confirmPassword = findViewById<TextView>(R.id.inputSignupConfirmPassword).text
        val buttonSignUp = findViewById<Button>(R.id.buttonConfirmSignUp)


        buttonSignUp.setOnClickListener {
            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.toString() == confirmPassword.toString()) {
                    registerUser(username.toString(), password.toString())
                } else {
                    Toast.makeText(this, "enter same password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all the Form", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(uid: String, pass: String) {
        val data = hashMapOf(
                "username" to uid,
                "password" to pass
        )
        db.collection("user")
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
    }
}