package com.example.booktrack.data.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.repository.GoalRepository

class GoalViewModelFactory(private val goalRepository: GoalRepository, private val application: BooksApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            return GoalViewModel(goalRepository , application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
