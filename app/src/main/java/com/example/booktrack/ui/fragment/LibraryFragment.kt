package com.example.booktrack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.R
import com.example.booktrack.data.adapter.BookAdapter
import com.example.booktrack.data.view.BookViewModel
import com.example.booktrack.data.view.BookViewModelFactory
import com.example.booktrack.data.database.BooksApplication

class LibraryFragment : Fragment() {

    private lateinit var viewModel: BookViewModel
    private lateinit var adapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.library_list)
        statusSpinner = view.findViewById(R.id.status_spinner)

        // Initialiser l'adaptateur et le RecyclerView
        adapter = BookAdapter(
            onItemClick = { book ->
                // Ajoutez une action pour afficher les détails du livre
                    val bundle = Bundle().apply {
                        putParcelable("book", book)
                    }
                    findNavController().navigate(R.id.action_libraryFragment_to_bookDetailFragment, bundle)
            }
        )
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        // Initialiser le ViewModel
        val application = requireActivity().application as BooksApplication
        val repository = application.bookRepository
        val factory = BookViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, factory).get(BookViewModel::class.java)

        // Observer les livres enregistrés
        viewModel.getAllBook.observe(viewLifecycleOwner, Observer { books ->
            books?.let {
                adapter.updateBooks(it)
            }
        })

        // Initialiser le Spinner pour filtrer par statut
        val statusOptions = arrayOf("Tous", "À lire", "En cours", "Terminé")
        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = statusAdapter

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedStatus = statusOptions[position]
                if (selectedStatus == "Tous") {
                    // Afficher tous les livres
                    viewModel.getAllBook.observe(viewLifecycleOwner, Observer { books ->
                        adapter.updateBooks(books)
                    })
                } else {
                    // Filtrer par statut
                    viewModel.getBooksByStatus(selectedStatus).observe(viewLifecycleOwner, Observer { books ->
                        adapter.updateBooks(books)
                    })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}


