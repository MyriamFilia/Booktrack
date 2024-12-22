package com.example.booktrack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.R
import com.example.booktrack.data.view.BookViewModel
import com.example.booktrack.data.view.BookViewModelFactory
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Book
import com.example.booktrack.data.model.BookItem
import com.squareup.picasso.Picasso

class DetailApiFragment : Fragment() {
    private lateinit var viewModel: BookViewModel
    private lateinit var bookItem: BookItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_detail_api, container, false)
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
                bookItem = it.getParcelable("bookItem") ?: throw IllegalArgumentException("BookItem is missing!")
            }
        }

        // Utilisez bookItem pour afficher les détails dans l'UI

        // Afficher les informations du livre
        view.findViewById<TextView>(R.id.detail_title).text = bookItem.volumeInfo?.title
        view.findViewById<TextView>(R.id.detail_author).text = bookItem.volumeInfo?.authors?.joinToString(", ")
        view.findViewById<TextView>(R.id.detail_description).text = bookItem.volumeInfo?.description
        view.findViewById<TextView>(R.id.detail_publish_date).text = bookItem.volumeInfo?.publishedDate ?: "Date inconnue"

        // Charger l'image du livre avec Picasso
        //val imageUrl = bookItem.volumeInfo?.imageLinks?.thumbnail
        val imageUrl = bookItem.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")
        val imageView = view.findViewById<ImageView>(R.id.detail_book_image)
        //Log.d("BookImageURL", "URL de l'image: ${bookItem.volumeInfo?.imageLinks?.thumbnail}")

        if (imageUrl != null) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_book_image_placeholder) // Image par défaut pendant le chargement
                .error(R.drawable.ic_error_image) // Image en cas d'erreur de chargement
                .into(imageView)
        }


        // Ajouter un bouton pour enregistrer le livre
        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            val book = Book(
                title = bookItem.volumeInfo?.title ?: "Titre inconnu",
                author = bookItem.volumeInfo?.authors?.joinToString(", ") ?: "Auteur inconnu",
                isbn = null,
                description = bookItem.volumeInfo?.description,
                thumbnailUrl = bookItem.volumeInfo?.imageLinks?.thumbnail,
                status = "À lire",
                note = null
            )
            viewModel.insertBook(book)
            Toast.makeText(requireContext(), "Livre ajouté !", Toast.LENGTH_SHORT).show()
        }
    }

}