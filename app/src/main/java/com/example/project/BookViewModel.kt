package com.example.project

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookViewModel: ViewModel() {
    var books = mutableStateListOf<Book>()
    var reservedBooks = mutableStateListOf<Book>()
    var isReserved = mutableStateOf<Boolean>(false)

    init {
        Firebase.firestore
            .collection("books")
            .get()
            .addOnSuccessListener {
                it.documents.forEach { d ->
                    val bookImage = d.get("image").toString()
                    val bookName = d.get("name").toString()
                    val bookAuthor = d.get("author").toString()
                    val bookDescription = d.get("description").toString()
                    books.add(Book(bookName, bookAuthor, bookImage, bookDescription))
                }
            }
    }

    fun addReservation(book: Book) {
      isReserved.value = true
      reservedBooks.add(book)
    }

    fun deleteReservation(book: Book) {
    isReserved.value = false
    reservedBooks.remove(book)

    }

    fun confirmReservation(book:Book) {
        //delete all items from the reservation list
    }
}
