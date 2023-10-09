package com.recoveryrecord.surveyandroid.example.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID
import com.recoveryrecord.surveyandroid.example.NewsHybridActivity
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.util.showToast

class LoginActivity : AppCompatActivity() {
    private var useremail: EditText? = null
    private var password: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance().apply { checkLocalLoginAndCache(currentUser) }
        val button = findViewById<Button>(R.id.bt_login)
        useremail = findViewById(R.id.et_username)
        password = findViewById(R.id.et_password)
        button.setOnClickListener { userLogin() }
    }

    @SuppressLint("HardwareIds")
    private fun checkLocalLoginAndCache(user: FirebaseUser?): Boolean {
        user?.apply {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val editor = sharedPrefs.edit()
            editor.putString(SHARE_PREFERENCE_USER_ID, email?.split("@")?.get(0))
            editor.apply()
            showToast(this@LoginActivity, getString(R.string.login_success))
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
            useremail?.error = getString(R.string.account_is_empty)
            useremail?.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(stringUseremail).matches()) {
            useremail?.error = getString(R.string.account_wrong_format)
            useremail?.requestFocus()
            return
        }
        if (stringPassword.isEmpty()) {
            password?.error = getString(R.string.password_is_empty)
            password?.requestFocus()
            return
        }

        mAuth?.signInWithEmailAndPassword(stringUseremail, stringPassword)
            ?.addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    checkLocalLoginAndCache(mAuth?.currentUser).takeIf { !it }.run {
                        showToast(this@LoginActivity, getString(R.string.login_failed))
                    }
                } else {
                    showToast(this@LoginActivity, getString(R.string.login_failed))
                }
            }
    }

    private fun startNewsHybridActivity() {
        startActivity(Intent(this@LoginActivity, NewsHybridActivity::class.java))
        finish()
    }
}