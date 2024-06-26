package com.platonso.yamify.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import com.google.firebase.auth.FirebaseAuth
import com.platonso.yamify.BuildConfig
import com.platonso.yamify.R

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var createAccountBtnTextView: TextView
    private lateinit var developerLogo: ImageView
    private lateinit var version: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginBtn = findViewById(R.id.login_btn)
        progressBar = findViewById(R.id.progress_bar)
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn)
        developerLogo = findViewById(R.id.platonso)
        version = findViewById(R.id.version_tv)

        loginBtn.setOnClickListener { loginUser() }
        createAccountBtnTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

        developerLogo.setOnClickListener {
            val rotateAnim = ObjectAnimator.ofFloat(developerLogo, "rotation", 0f, 360f)
            rotateAnim.duration = 1000
            rotateAnim.start()
        }

        val versionName = BuildConfig.VERSION_NAME
        version.text = "Версия: $versionName"

    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val isValidated = validateData(email, password)
        if (!isValidated) {
            return
        }
        loginAccountFirebase(email, password)
    }

    private fun loginAccountFirebase(email: String?, password: String?) {
        val firebaseAuth = FirebaseAuth.getInstance()
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Успешный вход в аккаунт
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                // Ошибка входа в аккаунт
                Toast.makeText(
                    this@LoginActivity, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                ).show()
            }
            changeInProgress(false)
        }
    }

    private fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.visibility = View.VISIBLE
            loginBtn.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            loginBtn.visibility = View.VISIBLE
        }
    }


    private fun validateData(email: String?, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Некорректный адрес почты"
            return false
        }
        if (password.length < 6) {
            passwordEditText.error = "Пароль должен быть больше 5 символов"
            return false
        }
        return true
    }
}