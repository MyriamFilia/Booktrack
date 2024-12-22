package com.example.booktrack.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult(
    val items: List<BookItem>? // Liste des livres
) : Parcelable

@Parcelize
data class BookItem(
    val id: String?, // Identifiant unique du livre
    val volumeInfo: VolumeInfo?, // Détails du livre
) : Parcelable

@Parcelize
data class VolumeInfo(
    val title: String?, // Titre du livre
    val authors: List<String>?, // Liste des auteurs
    val publishedDate: String?, // Date de publication
    val description: String?, // Description du livre
    val categories: List<String>?, // Catégories du livre
    val imageLinks: ImageLinks?, // Liens des images
    val language: String?, // Langue
) : Parcelable

@Parcelize
data class ImageLinks(
    val smallThumbnail: String?, // URL de l'image petite
    val thumbnail: String? // URL de l'image grande
) : Parcelable

// Modèle de données simplifié
@Parcelize
data class BookInfo(
    val title: String,
    val authors: List<String>,
    val thumbnail: String
) : Parcelable

