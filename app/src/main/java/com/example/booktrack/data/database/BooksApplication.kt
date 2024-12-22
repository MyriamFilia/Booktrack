package com.example.booktrack.data.database

import android.app.Application
import com.example.booktrack.data.repository.BookRepository
import com.example.booktrack.data.repository.GoalRepository

class BooksApplication () : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val bookRepository by lazy { BookRepository(database.bookDao()) }
    val goalRepository by lazy { GoalRepository(database.goalDao()) }
}