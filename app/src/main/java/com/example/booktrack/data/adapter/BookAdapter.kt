package com.example.booktrack.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.R
import com.example.booktrack.data.model.Book
import com.squareup.picasso.Picasso

class BookAdapter(
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val booksList = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book2, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = booksList[position]
        holder.bind(book)
        holder.itemView.setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int = booksList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateBooks(newBooks: List<Book>) {
        booksList.clear()
        booksList.addAll(newBooks)
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.book_title)
        private val textAuthor: TextView = itemView.findViewById(R.id.book_status)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.book_thumbnail)


        fun bind(book: Book) {
            // Préparer l'URL de l'image
            val imageUrl = book.thumbnailUrl?.replace("http://", "https://") // Remplacer HTTP par HTTPS

            // Charger l'image depuis l'URL corrigée
            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl) // URL corrigée
                    .placeholder(R.drawable.ic_book_image_placeholder) // Image par défaut
                    .error(R.drawable.ic_error_image) // Image en cas d'erreur
                    .into(thumbnailImageView)
            } else {
                // Si l'URL est nulle ou vide, afficher une image par défaut
                thumbnailImageView.setImageResource(R.drawable.ic_book_image_placeholder)
            }

            textTitle.text = book.title.ifEmpty { "Titre inconnu" }
            textAuthor.text = book.author.ifEmpty { "Auteur inconnu" }
        }
    }


}
