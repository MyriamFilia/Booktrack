package com.example.booktrack.data.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // ID unique pour chaque livre
    val title: String,
    val author: String,
    val isbn: String?,
    val description: String?,
    val thumbnailUrl: String?, // URL de la couverture du livre
    var status: String,            // Statut du livre (ex : "À lire", "En cours", "Lu")
    val note: Float?
) : Parcelable

