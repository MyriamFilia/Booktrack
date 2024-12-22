package com.example.booktrack.data.view

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Goal
import com.example.booktrack.data.repository.GoalRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class GoalViewModel(private val goalRepository: GoalRepository, val application: BooksApplication) : AndroidViewModel(application) {

    private val _currentGoal = MutableLiveData<Goal?>()
    val currentGoal: LiveData<Goal?> get() = _currentGoal

    init { // Initialiser avec l'objectif de l'année en cours
        viewModelScope.launch { _currentGoal.value = goalRepository.getGoalByYear(Calendar.getInstance().get(Calendar.YEAR)) }
    }

    // Sauvegarder ou mettre à jour les objectifs
    fun saveGoal2(yearlyGoal: Int, callback: (Goal) -> Unit) {
        viewModelScope.launch {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val existingGoal = goalRepository.getGoalByYear(currentYear)
            val goal = // Mettre à jour l'objectif existant
                existingGoal?.copy( yearlyGoal = yearlyGoal)
                    ?: // Créer un nouvel objectif si aucun n'existe pour l'année en cours
                    Goal(year = currentYear, yearlyGoal = yearlyGoal)
            goalRepository.saveOrUpdateGoal(goal) // Insérer ou mettre à jour l'objectif
            callback(goal)
        }
    }

    fun getGoalProgress(booksCompleted: LiveData<Int>, booksInProgress: LiveData<Int>, yearlyGoal: Int): LiveData<Int> {
        val progressLiveData = MediatorLiveData<Int>()

        val updateProgress: () -> Unit = {
            val completedCount = booksCompleted.value ?: 0
            val inProgressCount = booksInProgress.value ?: 0
            val totalBooksRead = completedCount + inProgressCount
            val progress = if (yearlyGoal > 0) {
                ((totalBooksRead.toFloat() / yearlyGoal.toFloat()) * 100).toInt()
            } else {
                0
            }
            progressLiveData.value = progress
        }

        // Ajouter les sources pour observer booksCompleted et booksInProgress
        progressLiveData.addSource(booksCompleted) { updateProgress() }
        progressLiveData.addSource(booksInProgress) { updateProgress() }

        return progressLiveData
    }


}
