package com.platonso.yamify.ui.ingredients


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.platonso.yamify.R
import com.platonso.yamify.activity.MainActivity
import com.platonso.yamify.databinding.FragmentIngredientsBinding
import com.platonso.yamify.ui.recipe.RecipeFragment


class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ingredientsViewModel =
            ViewModelProvider(this).get(IngredientsViewModel::class.java)

        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)



        binding.button2.setOnClickListener {

            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.nav_host_fragment_activity_main,RecipeFragment())
            fragmentTransaction?.commit()
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                ?.selectedItemId = R.id.navigation_recipe

        }



        val textView: TextView = binding.textIngredients
        ingredientsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}