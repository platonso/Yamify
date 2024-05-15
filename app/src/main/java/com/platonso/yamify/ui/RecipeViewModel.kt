package com.platonso.yamify.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeViewModel: ViewModel() {

    private val _recipe = MutableLiveData<String>()
    private val _ingredients = MutableLiveData<String>()
    val toggleButtonStates = mutableMapOf<Int, Boolean>()
    val recipe: LiveData<String> = _recipe

    fun setRecipe(recipe: String) {
        _recipe.value = recipe
    }

    fun setIngredients(ingredients: String){
        _ingredients.value = ingredients
    }




}