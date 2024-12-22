package com.example.booktrack.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.booktrack.data.model.Goal

@Dao
interface GoalDao {

    // Méthode pour ajouter un objectif
    @Insert
    suspend fun insert(goal: Goal)

    // Méthode pour mettre à jour un objectif
    @Update
    suspend fun update(goal: Goal)

    // Méthode pour supprimer un objectif
    @Delete
    suspend fun delete(goal: Goal)

    // Méthode pour supprimer tous les objectifs
    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    // Récupérer un objectif par année
    @Query("SELECT * FROM goals WHERE year = :year LIMIT 1")
    fun getGoalByYear(year: Int): Goal?

    // Récupérer tous les objectifs (année et mois)
    @Query("SELECT * FROM goals")
    suspend fun getAllGoals(): List<Goal>


}
