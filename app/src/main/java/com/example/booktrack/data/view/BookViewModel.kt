@file:Suppress("DEPRECATION")
package com.example.booktrack.data.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Book
import com.example.booktrack.data.model.SearchResult
import com.example.booktrack.data.repository.BookRepository
import com.example.booktrack.wrapper.Resource
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class BookViewModel(val bookRepository: BookRepository, val application: BooksApplication): AndroidViewModel(application){

        //ajout book data
        val books : MutableLiveData<Resource<SearchResult>> = MutableLiveData()
        //recuperer les livres ajouter
        val getAllBook = bookRepository.getAllBooks()

        fun getSearchresult(query: String) = viewModelScope.launch {
            //Log.d("BookViewModel", "Recherche lancée pour : $query")
            checkInternetAndSearchResult(query)
        }


        private suspend fun checkInternetAndSearchResult(query: String){
            //Log.d("BookViewModel", "Vérification de la connexion Internet pour : $query")
            books.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val response = bookRepository.searchBooks(query)
                    //Log.d("BookViewModel", "Réponse obtenue pour la recherche : ${response.body()}")
                    books.postValue(handleSearchResponse(response))
                } else {
                    //Log.d("BookViewModel", "Pas de connexion Internet.")
                    books.postValue(Resource.Error("NO INTERNET CONNECTION"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> {
                        //Log.e("BookViewModel", "Erreur réseau : ${t.localizedMessage}")
                        books.postValue(Resource.Error("Erreur réseau. Vérifiez votre connexion Internet."))
                    }
                    is JsonSyntaxException -> {
                        //Log.e("BookViewModel", "Erreur de parsing JSON : ${t.localizedMessage}")
                        books.postValue(Resource.Error("Erreur de conversion des données."))
                    }
                    else -> {
                        //Log.e("BookViewModel", "Erreur inconnue : ${t.localizedMessage}")
                        books.postValue(Resource.Error("Erreur inconnue."))
                    }
                }
            }
        }

        private fun handleSearchResponse(response: Response<SearchResult>): Resource<SearchResult> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


        // verifie la reponse d'internet
        private fun hasInternetConnection(): Boolean {
            val connectivityManager = getApplication<BooksApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        fun insertBook(book : Book) {
            viewModelScope.launch(Dispatchers.IO) {
                bookRepository.insertBook(book)
            }
        }

        fun deleteBook(book : Book) {
            viewModelScope.launch (Dispatchers.IO) {
                bookRepository.deleteBook(book)
            }
        }

        fun getBooksByStatus(status: String) : LiveData<List<Book>> {
                return bookRepository.getBooksByStatus(status)
        }

        fun updateBookStatus(bookId: Long , status: String){
            viewModelScope.launch (Dispatchers.IO) {
                bookRepository.updateBookStatus(bookId, status)
            }
        }

        fun getBooksCountByStatus(status: String): LiveData<Int> {
            return bookRepository.getBooksCountByStatus(status)
        }

}