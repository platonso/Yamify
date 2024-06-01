package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference
import com.platonso.yamify.R
import com.platonso.yamify.data.Favourites
import com.platonso.yamify.databinding.FragmentFavouritesDetailsBinding

class FavouritesDetailsFragment : Fragment() {
    private var _binding: FragmentFavouritesDetailsBinding? = null
    private lateinit var recipeDataManager: RecipeDataManager
    private lateinit var docId: String
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recipeDataManager = ViewModelProvider(requireActivity()).get(RecipeDataManager::class.java)
        _binding = FragmentFavouritesDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Скрыть BottomNavigationView
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.visibility = View.GONE

        recipeDataManager.titleFavourites.observe(viewLifecycleOwner) {
            binding.titleTv.text = it
        }
        recipeDataManager.contentFavourites.observe(viewLifecycleOwner) {
            binding.contentTv.text = it
        }
        recipeDataManager.docId.observe(viewLifecycleOwner) {
            docId = it
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.deleteBtn.setOnClickListener{
            deleteRecipe()
        }
    }

    private fun deleteRecipe(){
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Удаление")
                setMessage("Удалить рецепт из избранного?")
                setPositiveButton("Удалить"){_,_ ->
                    deleteRecipeFromFirebase()
                }
                setNeutralButton("Отмена", null)
            }.create().show()
        }
    }

    private fun deleteRecipeFromFirebase(){
        val documentReference: DocumentReference
        documentReference = Favourites.getCollectionReferenceForRecipes().document(docId)
        documentReference.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Рецепт удалён
                Toast.makeText(requireContext(), "Рецепт удалён", Toast.LENGTH_SHORT)
                    .show()
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Ошибка удаления", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        // Вернуть BottomNavigationView
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }
}