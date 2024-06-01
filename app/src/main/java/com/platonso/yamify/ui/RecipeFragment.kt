package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.DocumentReference
import com.platonso.yamify.R
import com.platonso.yamify.data.Favourites
import com.platonso.yamify.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private lateinit var recipeDataManager: RecipeDataManager

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recipeDataManager = ViewModelProvider(requireActivity()).get(RecipeDataManager::class.java)
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.textRecipe.text = getString(R.string.recipe_will_be_here)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Отображение результата
        val textView: TextView = binding.textRecipe
        recipeDataManager.recipe.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.buttonAddToFavourites.setOnClickListener {
            saveRecipe()
        }
    }

    private fun saveRecipe(){
        val textRecipe = binding.textRecipe.text.toString()
        if (textRecipe == getString(R.string.recipe_will_be_here) ||
            textRecipe == getString(R.string.recipe_is_being_generated)) {
            Toast.makeText(requireContext(), "Сначала получите рецепт", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val title = textRecipe.substringBefore("\n")
        val content = textRecipe.replaceFirst("$title\n", "")

        val favourites = Favourites()
        favourites.title = title
        favourites.content = content
        saveRecipeToFirebase(favourites)
    }

    private fun saveRecipeToFirebase(favourites: Favourites){
        val documentReference: DocumentReference
        documentReference = Favourites.getCollectionReferenceForRecipes().document()
        documentReference.set(favourites).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Рецепт добавлен в избранное
                Toast.makeText(requireContext(), "Рецепт добавлен в избранное", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Ошибка добавления", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}