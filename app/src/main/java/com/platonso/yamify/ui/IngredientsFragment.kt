package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.platonso.yamify.R
import com.platonso.yamify.activity.MainActivity
import com.platonso.yamify.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
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

        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        /*
        // Отправка нового запроса и переход на фрагмент Recipe
        binding.getRecipe.setOnClickListener {
            val question = binding.textEdit.text.toString()
            val activity = requireActivity() as? MainActivity
            activity?.sendRequest(question)


            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.nav_host_fragment_activity_main, RecipeFragment())
            fragmentTransaction?.commit()
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                ?.selectedItemId = R.id.navigation_recipe
        }


         */
        return binding.root

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}