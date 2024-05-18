package com.platonso.yamify.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.Query
import com.platonso.yamify.R
import com.platonso.yamify.data.Favourites
import com.platonso.yamify.databinding.FragmentFavouritesBinding
import com.platonso.yamify.databinding.FragmentFavouritesDetailsBinding

class FavouritesDetailsFragment : Fragment() {
    private var _binding: FragmentFavouritesDetailsBinding? = null
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        _binding = FragmentFavouritesDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Скрыть BottomNavigationView
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.visibility = View.GONE

        recipeViewModel.selectedTitleFavourites.observe(viewLifecycleOwner) {
            binding.titleTv.text = it
        }
        recipeViewModel.selectedContentFavourites.observe(viewLifecycleOwner) {
            binding.contentTv.text = it
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()

        // Показать BottomNavigationView
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view            )
        bottomNavigationView?.visibility = View.VISIBLE
    }

}