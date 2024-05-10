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
import com.platonso.yamify.R
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