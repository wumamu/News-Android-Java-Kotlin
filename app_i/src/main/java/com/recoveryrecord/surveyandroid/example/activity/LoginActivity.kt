package com.recoveryrecord.surveyandroid.example.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.recoveryrecord.surveyandroid.example.NewsHybridActivity
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_USER_ID
import com.recoveryrecord.surveyandroid.example.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint

class LoginActivity : AppCompatActivity() {
    private lateinit var useremail: EditText
    private lateinit var password: EditText
    private lateinit var localLoginButton: Button
//    private lateinit var googleLoginButton: com.google.android.gms.common.SignInButton

    @Inject
    lateinit var auth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        localLoginButton = findViewById(R.id.bt_login)
//        googleLoginButton = findViewById(R.id.sign_in_button)
        useremail = findViewById(R.id.et_username)
        password = findViewById(R.id.et_password)
        localLoginButton.setOnClickListener { localLoginCheck() }
//        googleLoginButton.setOnClickListener { googleSignIn() }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUIIfUserExist(auth.currentUser)
    }

    @SuppressLint("HardwareIds")
    private fun updateUIIfUserExist(user: FirebaseUser?): Boolean {
        user?.apply {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val editor = sharedPrefs.edit()
            editor.putString(SHARE_PREFERENCE_USER_ID, email?.split("@")?.get(0))
            editor.apply()
            startActivity(Intent(this@LoginActivity, NewsHybridActivity::class.java))
            finish()
            return true
        }
        return false
    }

    @SuppressLint("ApplySharedPref")
    private fun localLoginCheck() {
        val stringUseremail = useremail.text.toString().trim()
        val stringPassword = password.text.toString().trim()
        if (stringUseremail.isEmpty()) {
            useremail.error = getString(R.string.account_is_empty)
            useremail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(stringUseremail).matches()) {
            useremail.error = getString(R.string.account_wrong_format)
            useremail.requestFocus()
            return
        }
        if (stringPassword.isEmpty()) {
            password.error = getString(R.string.password_is_empty)
            password.requestFocus()
            return
        }
        firebaseAuthWithLocalEmailAndPassword(stringUseremail, stringPassword)
    }

    private fun firebaseAuthWithLocalEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val res = updateUIIfUserExist(auth.currentUser)
                    if (res) {
                        showToast(this@LoginActivity, getString(R.string.login_success))
                    } else {
                        showToast(this@LoginActivity, getString(R.string.login_failed))
                    }
                } else {
                    updateUIIfUserExist(null)
                    showToast(this@LoginActivity, getString(R.string.login_failed))
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.apply {
                    Timber.d("firebaseAuthWithGoogle:$id")
                    firebaseAuthWithGoogle(idToken!!)
                }

            } catch (e: ApiException) {
                showToast(this@LoginActivity, getString(R.string.google_sign_in_failed))
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    showToast(this@LoginActivity, getString(R.string.login_success))
                    updateUIIfUserExist(auth.currentUser)
                } else {
                    showToast(this@LoginActivity, getString(R.string.google_sign_in_failed))
                }
            }
    }

//    private fun googleSignIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}