package com.platonso.yamify.ui

import android.os.Bundle
import android.util.Log
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
    private lateinit var recipeViewModel: RecipeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.textRecipe.text = getString(R.string.recipe_will_be_here)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Отображение результата
        val textView: TextView = binding.textRecipe
        recipeViewModel.recipe.observe(viewLifecycleOwner) {
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

        val title = textRecipe.toString().substringBefore("\n")
        val content = textRecipe.toString().replaceFirst("$title\n", "")

        val favourites = Favourites()
        favourites.setTitle(title)
        favourites.setContent(content)
        saveRecipeToFirebase(favourites)
    }

    private fun saveRecipeToFirebase(favourites: Favourites){
        val documentReference: DocumentReference
        documentReference = Favourites.getCollectionReferenceForRecipes().document()
        documentReference.set(favourites).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Рецепт добавлен в избранное
                Toast.makeText(requireContext(), "Recipe added successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Failed while adding recipe", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}