package com.example.booktrack.data.repository

import androidx.lifecycle.LiveData
import com.example.booktrack.data.database.GoalDao
import com.example.booktrack.data.model.Goal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalRepository(val goalDao : GoalDao) {

    // Utiliser LiveData pour obtenir un objectif de l'année
    suspend fun getGoalByYear(year: Int): Goal? {
        return withContext(Dispatchers.IO) { // Force l'exécution dans le thread IO
            goalDao.getGoalByYear(year)
        }
    }

    // Sauvegarder ou mettre à jour l'objectif
    suspend fun saveOrUpdateGoal(goal: Goal) {
        withContext(Dispatchers.IO) {
            val existingGoal = goalDao.getGoalByYear(goal.year)
            if (existingGoal == null) {
                goalDao.insert(goal)
            } else {
                goalDao.update(goal)
            }
        }
    }

}