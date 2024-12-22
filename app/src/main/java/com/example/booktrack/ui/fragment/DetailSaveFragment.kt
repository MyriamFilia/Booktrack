package com.example.booktrack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.R
import com.example.booktrack.data.view.BookViewModel
import com.example.booktrack.data.view.BookViewModelFactory
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Book
import com.squareup.picasso.Picasso

class DetailSaveFragment : Fragment() {
    private lateinit var viewModel: BookViewModel
    private lateinit var book: Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_detail_save, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser le ViewModel
        val application = requireActivity().application as BooksApplication
        val repository = application.bookRepository
        val factory = BookViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, factory).get(BookViewModel::class.java)

        // Récupérer les arguments
        arguments?.let {
            arguments?.let {
                book = it.getParcelable("book") ?: throw IllegalArgumentException("Book is missing!")
            }
        }

        // Afficher les informations du livre
        view.findViewById<TextView>(R.id.detail_title).text = book.title
        view.findViewById<TextView>(R.id.detail_author).text = book.author
        view.findViewById<TextView>(R.id.detail_description).text = book.description
        view.findViewById<TextView>(R.id.detail_statut).text = book.status

        // Charger l'image du livre avec Picasso
        val imageUrl = book.thumbnailUrl?.replace("http://", "https://")
        val imageView = view.findViewById<ImageView>(R.id.detail_book_image)
        //Log.d("BookImageURL", "URL de l'image: ${bookItem.volumeInfo?.imageLinks?.thumbnail}")

        if (imageUrl != null) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_book_image_placeholder) // Image par défaut pendant le chargement
                .error(R.drawable.ic_error_image) // Image en cas d'erreur de chargement
                .into(imageView)
        }

        // Bouton Modifier le statut
        val updateStatusButton = view.findViewById<Button>(R.id.update_status_button)
        updateStatusButton.setOnClickListener {
            updateBookStatus(view)
        }

        // Bouton Supprimer le livre
        val deleteBookButton = view.findViewById<Button>(R.id.delete_book_button)
        deleteBookButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun updateBookStatus(view: View) {
        // Liste des statuts possibles
        val statusList = arrayOf("À lire", "En cours", "Terminé")

        // Création d'un AlertDialog avec une liste
        AlertDialog.Builder(requireContext())
            .setTitle("Choisir le statut")
            .setItems(statusList) { _, which ->
                val selectedStatus = statusList[which]

                // Mise à jour du statut dans la base de données
                viewModel.updateBookStatus(book.id, selectedStatus)

                // Mise à jour de l'affichage dans l'UI
                book.status = selectedStatus
                view.findViewById<TextView>(R.id.detail_statut).text = selectedStatus

                // Affichage d'un Toast pour confirmer le changement
                Toast.makeText(requireContext(), "Statut modifié en : $selectedStatus", Toast.LENGTH_SHORT).show()
            }
            .show()
    }



    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Supprimer le livre")
            .setMessage("Êtes-vous sûr de vouloir supprimer ce livre ?")
            .setPositiveButton("Oui") { _, _ ->
                deleteBook()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun deleteBook() {
        viewModel.deleteBook(book) // Suppression dans la base de données
        Toast.makeText(requireContext(), "Livre supprimé avec succès", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Retourner à la bibliothèque
    }

}