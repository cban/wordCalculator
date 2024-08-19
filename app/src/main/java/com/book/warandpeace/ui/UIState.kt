package com.book.warandpeace.ui

sealed class UIState {
    data object Init : UIState()
    data object Loading : UIState()
    data class Success(val bookText: String) : UIState()
    data class Error( var errorMessage: String) : UIState()
}