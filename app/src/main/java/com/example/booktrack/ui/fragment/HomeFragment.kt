package com.example.booktrack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.booktrack.R
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Goal
import com.example.booktrack.data.view.BookViewModel
import com.example.booktrack.data.view.BookViewModelFactory
import com.example.booktrack.data.view.GoalViewModel
import com.example.booktrack.data.view.GoalViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeFragment : Fragment() {

    private lateinit var bookView: BookViewModel
    private lateinit var goalView: GoalViewModel
    private lateinit var barChart: BarChart
    private lateinit var statisticsText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser les boutons
        val buttonAddBook: Button = view.findViewById(R.id.button_add_book)
        val buttonLibrary: Button = view.findViewById(R.id.button_library)
        progressBar = view.findViewById(R.id.progress_bar)

        buttonAddBook.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addBookFragment)
        }

        buttonLibrary.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_libraryFragment)
        }

        // Initialiser les vues
        barChart = view.findViewById(R.id.chart)
        statisticsText = view.findViewById(R.id.statistics)

        // Initialiser les ViewModels
        val application = requireActivity().application as BooksApplication
        val bookRepository = application.bookRepository
        val goalRepository = application.goalRepository

        val bookViewModelFactory = BookViewModelFactory(bookRepository, application)
        val goalViewModelFactory = GoalViewModelFactory(goalRepository, application)

        bookView = ViewModelProvider(this, bookViewModelFactory)[BookViewModel::class.java]
        goalView = ViewModelProvider(this, goalViewModelFactory)[GoalViewModel::class.java]

        // Observer les données des livres et mettre à jour le graphique
        bookView.getAllBook.observe(viewLifecycleOwner) { books ->
            val toReadCount = books.count { it.status == "À lire" }
            val inProgressCount = books.count { it.status == "En cours" }
            val completedCount = books.count { it.status == "Terminé" }

            updateBarChart(toReadCount, inProgressCount, completedCount)
        }

        goalView.currentGoal.observe(viewLifecycleOwner) { goal ->
            goal?.let {
                updateStatistics(it)
            }
        }
    }


    private fun updateStatistics(goal: Goal) {
        val yearlyGoal = goal.yearlyGoal

        // Observer les livres terminés
        bookView.getBooksCountByStatus("Terminé").observe(viewLifecycleOwner) { completedCount ->
            bookView.getBooksCountByStatus("En cours").observe(viewLifecycleOwner) { inProgressCount ->
                // Mettre à jour les statistiques et la progression une fois que les deux valeurs sont disponibles
                goalView.getGoalProgress(
                    bookView.getBooksCountByStatus("Terminé"),
                    bookView.getBooksCountByStatus("En cours"),
                    goal.yearlyGoal
                ).observe(viewLifecycleOwner) { progress ->
                    updateGoalProgress(progress)
                }

                // Mettre à jour le texte des statistiques
                updateStatisticsText(goal, completedCount, inProgressCount)
            }
        }
    }

    private fun updateGoalProgress(progress: Int) {
        progressBar.max = 100
        progressBar.progress = progress

        val progressTextView: TextView = view?.findViewById(R.id.progress_text) ?: return
        progressTextView.text = "$progress% atteint"
    }

    private fun updateStatisticsText(goal: Goal, completedCount: Int, inProgressCount: Int) {
        val statsText = "Objectif annuel : ${goal.yearlyGoal} livres\n" +
                "Livres lus : $completedCount/${goal.yearlyGoal}\n" +
                "Livres en cours : $inProgressCount\n"
        statisticsText.text = statsText
    }



    private fun updateBarChart(toReadCount: Int, inProgressCount: Int, completedCount: Int) {
        val entries = listOf(
            BarEntry(0f, toReadCount.toFloat()), // "À lire"
            BarEntry(1f, inProgressCount.toFloat()), // "En cours"
            BarEntry(2f, completedCount.toFloat()) // "Terminé"
        )

        val dataSet = BarDataSet(entries, "Statistiques des livres")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_200)
        dataSet.valueTextSize = 14f

        val data = BarData(dataSet)
        barChart.data = data
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = true  // Activer la légende

        // Configuration de l'axe X
        class XAxisFormatter : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value) {
                    0f -> "À lire"
                    1f -> "En cours"
                    2f -> "Terminé"
                    else -> ""
                }
            }
        }

        // Appliquer le formatteur personnalisé
        barChart.xAxis.valueFormatter = XAxisFormatter()
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Configuration de l'axe Y
        val yAxis = barChart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.axisMinimum = 0f // Valeur minimale de l'axe Y

        barChart.animateY(1000)
        barChart.invalidate()  // Rafraîchir le graphique
    }

}

