package com.example.booktrack.data.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.repository.BookRepository

class BookViewModelFactory(private val bookRepository: BookRepository, private val application: BooksApplication) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(
                BookViewModel
            ::class.java)) {
            return BookViewModel(bookRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}