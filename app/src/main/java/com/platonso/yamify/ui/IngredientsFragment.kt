package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.platonso.yamify.R
import com.platonso.yamify.activity.MainActivity
import com.platonso.yamify.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private lateinit var recipeViewModel: RecipeViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toggleButtonIds = arrayOf(R.id.toggleButton_mushroom, R.id.toggleButton_avocado,
            R.id.toggleButton_beef, R.id.toggleButton_bread, R.id.toggleButton_cheese,
            R.id.toggleButton_chicken, R.id.toggleButton_crab, R.id.toggleButton_cucumber,
            R.id.toggleButton_egg, R.id.toggleButton_fish, R.id.toggleButton_flour,
            R.id.toggleButton_pepper, R.id.toggleButton_pasta, R.id.toggleButton_onion,
            R.id.toggleButton_potato, R.id.toggleButton_rice, R.id.toggleButton_salad,
            R.id.toggleButton_salmon, R.id.toggleButton_shrimp, R.id.toggleButton_tomato)

        // Установка начальных состояний из ViewModel
        for (id in toggleButtonIds) {
            val toggleButton = view.findViewById<ToggleButton>(id)
            toggleButton.isChecked = recipeViewModel.toggleButtonStates[id] ?: false

            // Сохранение состояния при изменении
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                recipeViewModel.toggleButtonStates[id] = isChecked
            }
        }

        binding.getRecipeButton.setOnClickListener {

            // Добавление значений ингредиентов с кнопок в массив
            val selectedItems = mutableListOf<String>()
            for (id in toggleButtonIds) {
                val toggleButton = view.findViewById<ToggleButton>(id)
                if (toggleButton.isChecked) {
                    selectedItems.add(toggleButton.contentDescription.toString())
                }
            }

            if (selectedItems.isNotEmpty()) {
                // Очистка предыдущего полученного рецепта
                recipeViewModel.setRecipe(getString(R.string.recipe_is_being_generated))

                val selectedItemsString = selectedItems.joinToString(", ")

                val activity = requireActivity() as? MainActivity
                activity?.sendRequest(getString(R.string.promt), selectedItemsString)

                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.nav_host_fragment_activity_main, RecipeFragment())
                fragmentTransaction?.commit()
                (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                    ?.selectedItemId = R.id.navigation_recipe
            }else{
                Toast.makeText(requireContext(), "Выберите ингредиенты", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}