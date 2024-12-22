package com.example.booktrack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.R
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Goal
import com.example.booktrack.data.view.GoalViewModel
import com.example.booktrack.data.view.GoalViewModelFactory

class SettingsFragment : Fragment() {

    private lateinit var goalViewModel: GoalViewModel

    // Variables pour les champs de saisie
    private lateinit var yearlyGoalInput: EditText
    private lateinit var saveButton: Button

    override fun onCreateView   (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des vues ici
        yearlyGoalInput = view.findViewById(R.id.yearly_goal_input)
        saveButton = view.findViewById(R.id.save_button)

        // Initialisation du ViewModel avec le GoalViewModelFactory
        val application = requireActivity().application as BooksApplication
        val goalRepository = application.goalRepository
        val factory = GoalViewModelFactory(goalRepository, application)
        goalViewModel = ViewModelProvider(this, factory)[GoalViewModel::class.java]

        // Observer pour récupérer l'objectif actuel
        goalViewModel.currentGoal.observe(viewLifecycleOwner) { goal ->
            goal?.let {
                // Mettre à jour l'UI avec les objectifs récupérés
                updateUI(it)
            }
        }

        // Sauvegarder les objectifs lors du clic sur le bouton
        saveButton.setOnClickListener {
            val yearlyGoal = yearlyGoalInput.text.toString().toIntOrNull() ?: 0
            goalViewModel.saveGoal2( yearlyGoal) { goal -> // Mettre à jour l'UI avec le nouvel objectif
             updateUI(goal) }
        }


    }

    private fun updateUI(goal: Goal) {
        // Remplir les champs avec les objectifs existants
        yearlyGoalInput.setText(goal.yearlyGoal.toString())
    }
}