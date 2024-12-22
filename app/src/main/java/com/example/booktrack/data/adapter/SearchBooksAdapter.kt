package com.example.booktrack.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.booktrack.R
import com.example.booktrack.data.model.BookItem


class SearchBooksAdapter (private val onAddClick: (BookItem) -> Unit, private val onItemClick: (BookItem) -> Unit) : RecyclerView.Adapter<SearchBookHolder>() {

    val bookslist = mutableListOf<BookItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBookHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        val viewHolder = SearchBookHolder(view)
        return viewHolder

    }

    override fun getItemCount(): Int {
        return bookslist.size
    }

    override fun onBindViewHolder(holder: SearchBookHolder, position: Int) {
        val book = bookslist[position]
        holder.textTitle.text = book.volumeInfo?.title ?: "Titre inconnu"
        holder.textAuthor.text = book.volumeInfo?.authors?.joinToString(", ") ?: "Auteur inconnu"

        // Gestion des clics
        holder.addButton.setOnClickListener {
            onAddClick(book)
        }
        holder.itemView.setOnClickListener {
            onItemClick(book)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBooks(newBooks: List<BookItem>) {
        bookslist.clear()
        bookslist.addAll(newBooks)
        notifyDataSetChanged()
    }


}

class SearchBookHolder(itemView: View) : ViewHolder(itemView){

    val textTitle : TextView = itemView.findViewById(R.id.title)
    val textAuthor : TextView = itemView.findViewById(R.id.author)
    val addButton: Button = itemView.findViewById(R.id.add_button)

}