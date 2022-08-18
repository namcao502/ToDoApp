package com.example.todoapp.ui

import androidx.appcompat.widget.SearchView

inline fun SearchView.OnQueryTextListener(crossinline listener: (String) -> Unit){

    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }

    })
}