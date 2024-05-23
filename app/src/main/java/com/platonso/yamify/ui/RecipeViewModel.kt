package com.platonso.yamify.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.platonso.yamify.BuildConfig
import kotlinx.coroutines.launch

class RecipeViewModel: ViewModel() {

    private val _recipe = MutableLiveData<String>()
    val recipe: LiveData<String> = _recipe

    private val _selectedTitleFavourites = MutableLiveData<String>()
    val selectedTitleFavourites: LiveData<String> get() = _selectedTitleFavourites

    private val _selectedContentFavourites = MutableLiveData<String>()
    val selectedContentFavourites: LiveData<String> get() = _selectedContentFavourites

    private val _selectedDocID = MutableLiveData<String>()
    val selectedDocId: LiveData<String> get() = _selectedDocID

    val toggleButtonStates = mutableMapOf<Int, Boolean>()



    fun setRecipe(recipe: String) {
        _recipe.value = recipe
    }

    fun setTitleFavourites(titleFavourites: String) {
        _selectedTitleFavourites.value = titleFavourites
    }

    fun setContentFavourites(contentFavourites: String) {
        _selectedContentFavourites.value = contentFavourites
    }

    fun setDocId(docId: String) {
        _selectedDocID.value = docId
    }



    fun sendRequest(prompt: String, question: String) {
        val API_KEY = BuildConfig.API_KEY
        viewModelScope.launch {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = API_KEY
            )

            val fullPrompt = "$prompt $question"
            var response = generativeModel.generateContent(fullPrompt).text.toString()

            response = response.replace("*", "")
            response = response.replace("#", "")
            response = response.replace("Название блюда: ", "")
            response = response.replace("Блюдо: ", "")

            setRecipe(response)
        }
    }
}