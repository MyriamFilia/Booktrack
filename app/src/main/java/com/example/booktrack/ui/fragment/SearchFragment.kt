package com.example.booktrack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.R
import com.example.booktrack.data.adapter.SearchBooksAdapter
import com.example.booktrack.data.view.BookViewModel
import com.example.booktrack.data.view.BookViewModelFactory
import com.example.booktrack.data.database.BooksApplication
import com.example.booktrack.data.model.Book
import com.example.booktrack.wrapper.Resource

class SearchFragment : Fragment() {

    lateinit var viewModel: BookViewModel
    lateinit var adapter: SearchBooksAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var searchField: EditText
    lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.results_list)
        searchField = view.findViewById(R.id.search_field)
        searchButton = view.findViewById(R.id.search_button)

        //initialiser l'adaptateur et le Recyclerview
        adapter = SearchBooksAdapter(
            onAddClick = { bookItem ->
                // Ajouter le livre dans la base de données
                val book = Book(
                    title = bookItem.volumeInfo?.title ?: "Titre inconnu",
                    author = bookItem.volumeInfo?.authors?.joinToString(", ") ?: "Auteur inconnu",
                    isbn = null,
                    description = bookItem.volumeInfo?.description,
                    thumbnailUrl = bookItem.volumeInfo?.imageLinks?.thumbnail,
                    status = "À lire",
                    note = null,
                )
                viewModel.insertBook(book)
                Toast.makeText(requireContext(), "Livre ajouté !", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { bookItem ->
                // Naviguer vers la page de détails
                //val action = SearchFragmentDirections.actionSearchFragmentToBookDetailFragment(bookItem)
                //findNavController().navigate(action)
                val bundle = Bundle().apply {
                    putParcelable("bookItem", bookItem) // Assurez-vous que BookItem implémente Parcelable
                }
                findNavController().navigate(R.id.action_searchFragment_to_bookDetailFragment, bundle)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Initialiser le Repository et le ViewModel
        val application = requireActivity().application as BooksApplication
        val repository = application.bookRepository
        val factory = BookViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, factory).get(BookViewModel::class.java)

        // Observer les données des livres
        viewModel.books.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    //Log.d("SearchFragment", "Résultats de la recherche : ${resource.data}")
                    resource.data?.let { searchResult ->
                        adapter.updateBooks(searchResult.items ?: emptyList())
                    }
                }
                is Resource.Error -> {
                    // Gérer l'erreur
                    //Log.e("SearchFragment", "Erreur lors de la recherche : ${resource.message}")
                }
                is Resource.Loading -> {
                    // Afficher un indicateur de chargement
                    //Log.d("SearchFragment", "Chargement des résultats...")
                }
            }
        })

        //Definir l'action du bouton de recherche
        searchButton.setOnClickListener {
            val query = searchField.text.toString()
            if(query.isNotEmpty()) {
                //Log.d("SearchFragment", "Bouton Rechercher cliqué avec la requête : $query")
                viewModel.getSearchresult(query)
            } else {
                //Log.d("SearchFragment", "Champ de recherche vide.")
            }
        }
    }
}
