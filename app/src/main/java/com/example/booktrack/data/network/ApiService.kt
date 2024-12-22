package com.example.booktrack.data.network

import com.example.booktrack.data.model.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    // Méthode pour rechercher des livres par titre, auteur, ou ISBN
    @Headers("Platform: Android")
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,    // Paramètre de recherche, pourrait être un titre, un auteur ou un ISBN
        @Query("langRestrict") langRestrict: String = "fr", //seulement les livre en francais
        @Query("orderBy") orderBy: String = "newest",
        @Query("maxResults") maxResults: Int = 20    // Limite du nombre de résultats
    ): Response<SearchResult>
}

