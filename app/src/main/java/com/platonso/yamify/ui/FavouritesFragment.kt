package com.platonso.yamify.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.platonso.yamify.activity.LoginActivity
import com.platonso.yamify.data.Favourites
import com.platonso.yamify.databinding.FragmentFavouritesBinding
import java.util.Locale

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipeAdapter: RecipeAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка почты текущего пользователя в TextView
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        binding.userEmailTw.text = currentUser?.email.toString()

        // Обработа нажатия выхода из аккаунта
        binding.exitBtn.setOnClickListener {
            logOutOfAccount()
        }

        // Отображение списка избранных рецептов в RecyclerView
        setupRecyclerView("")

        // Слушатель на изменения текста в SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(searchQuery: String): Boolean {
                // Обновляем запрос при изменении текста в поисковом поле
                if (searchQuery.isEmpty()) {
                    setupRecyclerView(searchQuery)
                } else {
                    val newSearchQuery = searchQuery.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString() }
                    setupRecyclerView(newSearchQuery)
                }
                return true
            }
        })
    }

    private fun logOutOfAccount(){
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Выход")
                setMessage("Выйти из аккаунта?")
                setPositiveButton("Выйти"){_,_ ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    activity?.finish()
                }
                setNeutralButton("Отмена", null)
            }.create().show()
        }
    }

    private fun setupRecyclerView(searchText: String) {
        val query: Query
        if (searchText.isEmpty()) {
            query = Favourites.getCollectionReferenceForRecipes().orderBy("title", Query.Direction.ASCENDING)
        } else {
            query = Favourites.getCollectionReferenceForRecipes()
                .orderBy("title")
                .startAt(searchText)
                .endAt("$searchText\uf8ff")
        }

        val options = FirestoreRecyclerOptions.Builder<Favourites>()
            .setQuery(query, Favourites::class.java)
            .build()

        recipeAdapter = RecipeAdapter(options, requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = recipeAdapter
        recipeAdapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        recipeAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        recipeAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        recipeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}