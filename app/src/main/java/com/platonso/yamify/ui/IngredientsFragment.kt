package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
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
        binding.getRecipeButton.setOnClickListener {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonIds = arrayOf(R.id.toggleButton_mushroom, R.id.toggleButton_avocado,
            R.id.toggleButton_beef, R.id.toggleButton_bread, R.id.toggleButton_cheese,
            R.id.toggleButton_chicken, R.id.toggleButton_crab, R.id.toggleButton_cucumber,
            R.id.toggleButton_egg, R.id.toggleButton_fish, R.id.toggleButton_flour,
            R.id.toggleButton_pepper, R.id.toggleButton_pasta, R.id.toggleButton_onion,
            R.id.toggleButton_potato, R.id.toggleButton_rice, R.id.toggleButton_salad,
            R.id.toggleButton_salmon, R.id.toggleButton_shrimp, R.id.toggleButton_tomato)


        binding.getRecipeButton.setOnClickListener {
            val selectedItems = mutableListOf<String>()
            for (id in buttonIds) {
                val toggleButton = view.findViewById<ToggleButton>(id)
                if (toggleButton.isChecked) {
                    selectedItems.add(toggleButton.contentDescription.toString())
                }
            }
            val selectedItemsString = selectedItems.joinToString(", ")
            sharedViewModel.setText(selectedItemsString)


            val activity = requireActivity() as? MainActivity
            activity?.sendRequest(selectedItemsString)


            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.nav_host_fragment_activity_main, RecipeFragment())
            fragmentTransaction?.commit()
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                ?.selectedItemId = R.id.navigation_recipe
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}