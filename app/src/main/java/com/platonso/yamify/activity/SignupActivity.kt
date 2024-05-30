package com.platonso.yamify.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.platonso.yamify.BuildConfig
import com.platonso.yamify.R

class SignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var createAccountBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginBtnTextView: TextView
    private lateinit var developerLogo: ImageView
    private lateinit var version: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text)
        createAccountBtn = findViewById(R.id.create_account_btn)
        progressBar = findViewById(R.id.progress_bar)
        loginBtnTextView = findViewById(R.id.login_text_view_btn)
        developerLogo = findViewById(R.id.platonso)
        version = findViewById(R.id.version_tv)
        createAccountBtn.setOnClickListener { createAccount() }
        loginBtnTextView.setOnClickListener { finish() }

        developerLogo.setOnClickListener {
            val rotateAnim = ObjectAnimator.ofFloat(developerLogo, "rotation", 0f, 360f)
            rotateAnim.duration = 1000
            rotateAnim.start()
        }

        val versionName = BuildConfig.VERSION_NAME
        version.text = "Версия: $versionName"
    }

    private fun createAccount() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        val isValidated = validateData(email, password, confirmPassword)
        if (!isValidated) {
            return
        }
        createAccountFirebase(email, password)
    }

    private fun createAccountFirebase(email: String, password: String) {
        changeInProgress(true)

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@SignupActivity, OnCompleteListener<AuthResult> { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    Toast.makeText(
                        this@SignupActivity,
                        "Аккаунт успешно создан",
                        Toast.LENGTH_SHORT
                    ).show()
                    finishAffinity()

                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.visibility = View.VISIBLE
            createAccountBtn.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            createAccountBtn.visibility = View.VISIBLE
        }
    }

    private fun validateData(email: String, password: String, confirmPassword: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Некорректый адрес почты"
            return false
        }
        if (password.length < 6) {
            passwordEditText.error = "Пароль должен быть больше 5 символов"
            return false
        }
        if (password != confirmPassword) {
            passwordEditText.error = "Пароли не совпадают"
            return false
        }
        return true
    }
}
