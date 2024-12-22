package com.example.booktrack.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,        // ID unique pour chaque objectif
    val year: Int,                                            // Année pour l'objectif (ex : 2024)
    val yearlyGoal: Int,                                      // Objectif annuel (nombre de livres à lire sur l'année)
    //val currentMonthlyProgress: Int = 0,                       // Progression mensuelle actuelle
    val currentYearlyProgress: Int = 0                         // Progression annuelle actuelle
)
