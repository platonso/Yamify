package com.platonso.yamify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        // Отображение прошлого результата
        val textView: TextView = binding.textRecipe
        sharedViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}