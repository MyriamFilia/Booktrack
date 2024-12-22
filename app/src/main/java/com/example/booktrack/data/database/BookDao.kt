package com.example.booktrack.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.booktrack.data.model.Book

@Dao
interface BookDao {

    // Méthode pour ajouter un livre
    @Insert
    suspend fun insert(book: Book)

    // Méthode pour supprimer un livre
    @Delete
    suspend fun delete(book: Book)

    // Méthode pour récupérer tous les livres
    @Query("SELECT * FROM books")
    fun getAllBooks(): LiveData<List<Book>>

    // Méthode pour récupérer tous les livres avec un statut donné
    @Query("SELECT * FROM books WHERE status = :status")
    fun getBooksByStatus(status: String): LiveData<List<Book>>

    // Méthode pour modifier le statut d'un livre
    @Query("UPDATE books SET status = :newStatus WHERE id = :bookId")
    suspend fun updateBookStatus(bookId: Long, newStatus: String)


    //compter les livres par statut
    @Query("SELECT COUNT(*) FROM books WHERE status = :status")
    fun getBooksCountByStatus(status: String): LiveData<Int>
}