package com.platonso.yamify.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeViewModel: ViewModel() {

    private val _recipe = MutableLiveData<String>()

    private val _selectedTitleFavourites = MutableLiveData<String>()
    val selectedTitleFavourites: LiveData<String> get() = _selectedTitleFavourites

    private val _selectedContentFavourites = MutableLiveData<String>()
    val selectedContentFavourites: LiveData<String> get() = _selectedContentFavourites

    val toggleButtonStates = mutableMapOf<Int, Boolean>()
    val recipe: LiveData<String> = _recipe

    fun setRecipe(recipe: String) {
        _recipe.value = recipe
    }




    fun setTitleFavourites(titleFavourites: String) {
        _selectedTitleFavourites.value = titleFavourites
    }


    fun setContentFavourites(contentFavourites: String) {
        _selectedContentFavourites.value = contentFavourites
    }


}