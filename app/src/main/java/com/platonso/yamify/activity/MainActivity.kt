package com.platonso.yamify.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.platonso.yamify.BuildConfig
import com.platonso.yamify.R
import com.platonso.yamify.databinding.ActivityMainBinding
import com.platonso.yamify.ui.FavouritesFragment
import com.platonso.yamify.ui.IngredientsFragment
import com.platonso.yamify.ui.RecipeFragment
import com.platonso.yamify.ui.RecipeViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        replaceFragment(IngredientsFragment())

        binding.bottomNavView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.navigation_ingredients -> replaceFragment(IngredientsFragment())
                R.id.navigation_recipe -> replaceFragment(RecipeFragment())
                R.id.navigation_favourites -> replaceFragment(FavouritesFragment())
            }
            true
        }
    }

     private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)

        // Проверка, не является ли текущий фрагмент тем же самым
        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return
        }

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        fragmentTransaction.commit()
    }

    fun sendRequest(promt: String, question: String) {
        val API_KEY = BuildConfig.API_KEY_YaGPT

        // Создание объекта и добавление значений в JSON
        val jsonBody = JSONObject().apply {
            put("modelUri", "gpt://b1gp1ia0bndrc90m55bm/yandexgpt-lite")

            val completionOptions = JSONObject().apply {
                put("stream", false)
                put("temperature", 0.6)
                put("maxTokens", 2000)
            }
            put("completionOptions", completionOptions)

            // Создание массива сообщений
            val messages = JSONArray().apply {
                // Создание объекта сообщения и добавление его в массив
                val message = JSONObject().apply {
                    put("role", "user")
                    put("text", "$promt $question")
                }
                put(message)
            }
            // Добавление массива сообщений в объект JSON
            put("messages", messages)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://llm.api.cloud.yandex.net/foundationModels/v1/completion")
            .header("Authorization", API_KEY)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.let { responseBody ->
                    if (response.isSuccessful) {
                        try {
                            val jsonResponse = JSONObject(responseBody.string())
                            val alternatives = jsonResponse
                                .getJSONObject("result")
                                .getJSONArray("alternatives")
                            var text: String = alternatives
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("text")
                            text = text.replace("*", "")
                                .replace("#", "")
                                .replace("Название блюда: ", "")
                                .replace("Блюдо: ", "")
                                .replaceFirst("Ингредиенты:\n\n", "Ингредиенты:\n")
                                .replaceFirst("Рецепт:\n\n", "Рецепт:\n")
                            text = text.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                else it.toString() }

                            // Обновление ViewModel
                            runOnUiThread {
                                recipeViewModel.setRecipe(text.trim())
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            responseBody.close()
                        }
                    } else {
                        Log.e("Response Error", "Code: ${response.code}")
                    }
                }
            }
        })
    }
}




