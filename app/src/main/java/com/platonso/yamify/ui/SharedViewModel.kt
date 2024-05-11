package com.platonso.yamify.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет рецепт"
    }

    val text: LiveData<String> = _text

    fun setText(newText: String) {
        _text.value = newText
    }
}