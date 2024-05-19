package com.platonso.yamify.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.platonso.yamify.R
import com.platonso.yamify.activity.LoginActivity
import com.platonso.yamify.activity.MainActivity
import com.platonso.yamify.data.Favourites
import com.platonso.yamify.databinding.FragmentFavouritesBinding

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

        // Отображение списка избранных рецептов в RecyclerView
        setupRecyclerView()

        // Обработа нажатия выхода из аккаунта
        binding.exitBtn.setOnClickListener {
            logOutOfAccount()
        }
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

    private fun setupRecyclerView(){
        val query: Query = Favourites.getCollectionReferenceForRecipes().orderBy("title", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<Favourites> = FirestoreRecyclerOptions.Builder<Favourites>()
            .setQuery(query, Favourites::class.java).build()

        binding.recylerView.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = RecipeAdapter(options, com.google.api.Context.getDefaultInstance())
        binding.recylerView.adapter = recipeAdapter
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