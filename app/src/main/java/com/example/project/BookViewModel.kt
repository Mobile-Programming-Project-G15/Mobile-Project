package com.example.project

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
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
                    val bookPrice = d.get("price").toString()
                    val bookGenre = d.get("genre").toString()
                    val bookCondition = d.get("condition").toString()

                    books.add(Book(bookName, bookAuthor, bookImage, bookPrice, bookGenre, bookCondition,bookDescription))
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

    fun confirmReservation(book: Book) {
        //delete all items from the reservation list
    }

    fun addBookByAdmin(book: Book) {
        val db = FirebaseFirestore.getInstance()

        db.collection("books")
            .add(book)
            .addOnSuccessListener {
                books.add(Book(book.name, book.author, book.image, book.price, book.genre, book.condition ,book.description))
            }
    }
}
