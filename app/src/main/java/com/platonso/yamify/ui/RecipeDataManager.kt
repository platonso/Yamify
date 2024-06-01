package com.platonso.yamify.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeDataManager: ViewModel() {

    private val _recipe = MutableLiveData<String>()
    val recipe: LiveData<String> = _recipe

    private val _titleFavourites = MutableLiveData<String>()
    val titleFavourites: LiveData<String> get() = _titleFavourites

    private val _contentFavourites = MutableLiveData<String>()
    val contentFavourites: LiveData<String> get() = _contentFavourites

    private val _docID = MutableLiveData<String>()
    val docId: LiveData<String> get() = _docID

    val toggleButtonStates = mutableMapOf<Int, Boolean>()



    fun setRecipe(recipe: String) {
        _recipe.value = recipe
    }

    fun setTitleFavourites(titleFavourites: String) {
        _titleFavourites.value = titleFavourites
    }

    fun setContentFavourites(contentFavourites: String) {
        _contentFavourites.value = contentFavourites
    }

    fun setDocId(docId: String) {
        _docID.value = docId
    }

}