package com.bit.mysugarbud.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bit.mysugarbud.MainActivity
import com.bit.mysugarbud.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class signin : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signin_activity)
        val username = findViewById<TextView>(R.id.inputLoginUsername).text
        val password = findViewById<TextView>(R.id.inputLoginPassword).text
        val login = findViewById<Button>(R.id.buttonLogin)

        login.setOnClickListener {
            if (username.isNotEmpty() && password.isNotEmpty()) {
                checkLogin(username.toString(), password.toString())
            }
        }

    }

    private fun checkLogin(uid: String, pass: String) {
        db.collection("user")
                .get()
                .addOnSuccessListener {
                    var valid = false
                    for (result in it) {
                        val Username = result.getField<String>("username").toString()
                        val Password = result.getField<String>("password").toString()
                        val doc_id = result.id

                        if (Username == uid && Password == pass) {
                            // pass doc_id
                            valid = true
                            Toast.makeText(applicationContext, "Log in Success", Toast.LENGTH_SHORT).show()
                            //open the main activity
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("uid", doc_id)
                            intent.putExtra("username", Username)
                            startActivity(intent)
                            finish()

                        }

                    }
                    if (!valid) {
                        // no pass
                        Toast.makeText(applicationContext, "Password or Username is not the same", Toast.LENGTH_LONG).show()
                    }

                }
    }
}