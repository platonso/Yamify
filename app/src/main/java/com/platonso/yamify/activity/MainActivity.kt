package com.platonso.yamify.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.ai.client.generativeai.GenerativeModel
import com.platonso.yamify.BuildConfig
import com.platonso.yamify.R
import com.platonso.yamify.databinding.ActivityMainBinding
import com.platonso.yamify.ui.FavouritesFragment
import com.platonso.yamify.ui.IngredientsFragment
import com.platonso.yamify.ui.RecipeFragment
import com.platonso.yamify.ui.RecipeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeViewModel: RecipeViewModel

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

     fun replaceFragment(fragment: Fragment) {
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
/*
    suspend fun sendRequest(promt: String, question: String){
        val API_KEY = BuildConfig.API_KEY

        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = API_KEY
        )

        val prompt = "$promt $question"
        var response = generativeModel.generateContent(prompt).text.toString()

        response = response.replace("*", "")
        response = response.replace("#", "")

        recipeViewModel.setRecipe(response)
    }

 */


}




