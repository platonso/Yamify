package com.platonso.yamify.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
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

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
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
}




