package com.example.booktrack.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.database.BookDao
import com.example.booktrack.data.model.Book
import com.example.booktrack.data.network.RetrofitClient


class BookRepository(val bookDao: BookDao ) {

    fun getAllBooks() : LiveData<List<Book>>{
        return bookDao.getAllBooks()
    }

    suspend fun searchBooks(query: String ) = RetrofitClient.api.searchBooks(query)

    suspend fun insertBook(book : Book) {
        bookDao.insert(book)
    }

    suspend fun deleteBook(book : Book) {
        bookDao.delete(book)
    }

    suspend fun updateBookStatus(bookId: Long , newStatus: String) {
        bookDao.updateBookStatus(bookId,newStatus)
    }

    fun getBooksByStatus(status : String) : LiveData<List<Book>> {
        return bookDao.getBooksByStatus(status)
    }

    fun getBooksCountByStatus(status: String): LiveData<Int> {
        return bookDao.getBooksCountByStatus(status)
    }
}

