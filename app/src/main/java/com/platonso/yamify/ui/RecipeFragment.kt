package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.identity.android.legacy.Utility
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.model.Document
import com.platonso.yamify.R
import com.platonso.yamify.data.Favourites
import com.platonso.yamify.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private lateinit var sharedViewModel: SharedViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.textRecipe.text = getString(R.string.recipe_will_be_here)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Отображение результата
        val textView: TextView = binding.textRecipe
        sharedViewModel.recipe.observe(viewLifecycleOwner) {
            textView.text = it
        }


        binding.buttonAddToFavourites.setOnClickListener {
            saveRecipe()
        }



    }

    private fun saveRecipe(){
        val textRecipe = binding.textRecipe.text
        if (textRecipe.toString() == getString(R.string.recipe_will_be_here) || textRecipe.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Сначала получите рецепт", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val favourites = Favourites()
        favourites.setTitle("Заголовок")
        favourites.setContent(textRecipe.toString())
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