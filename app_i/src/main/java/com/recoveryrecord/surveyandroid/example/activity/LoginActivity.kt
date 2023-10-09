package com.recoveryrecord.surveyandroid.example.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID
import com.recoveryrecord.surveyandroid.example.NewsHybridActivity
import com.recoveryrecord.surveyandroid.example.R

class LoginActivity : AppCompatActivity() {
    private var useremail: EditText? = null
    private var password: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        mAuth?.currentUser?.let { user ->
            if (!checkLocalLoginAndCache(user)) return@let
        }
        val button = findViewById<Button>(R.id.bt_login)
        useremail = findViewById(R.id.et_username)
        password = findViewById(R.id.et_password)
        button.setOnClickListener { userLogin() }
    }

    @SuppressLint("HardwareIds")
    private fun checkLocalLoginAndCache(user: FirebaseUser): Boolean {
        user.apply {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val editor = sharedPrefs.edit()
            editor.putString(SHARE_PREFERENCE_USER_ID, email?.split("@")?.get(0))
            editor.apply()
            Toast.makeText(this@LoginActivity, "登入成功", Toast.LENGTH_LONG).show()
            startNewsHybridActivity()
            return true
        }
        return false
    }

    @SuppressLint("ApplySharedPref")
    private fun userLogin() {
        val stringUseremail = useremail?.text.toString().trim()
        val stringPassword = password?.text.toString().trim()
        if (stringUseremail.isEmpty()) {
            useremail?.error = "帳號還沒打阿"
            useremail?.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(stringUseremail).matches()) {
            useremail?.error = "帳號格式好像錯了阿"
            useremail?.requestFocus()
            return
        }
        if (stringPassword.isEmpty()) {
            password?.error = "密碼還沒打阿"
            password?.requestFocus()
            return
        }

        mAuth?.signInWithEmailAndPassword(stringUseremail, stringPassword)
            ?.addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    mAuth?.currentUser?.let { user ->
                        if (!checkLocalLoginAndCache(user)) {
                            Toast.makeText(
                                this@LoginActivity,
                                "登入失敗，需要幫忙請來信詢問",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "登入失敗，需要幫忙請來信詢問",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun startNewsHybridActivity() {
        startActivity(Intent(this@LoginActivity, NewsHybridActivity::class.java))
        finish()
    }
}