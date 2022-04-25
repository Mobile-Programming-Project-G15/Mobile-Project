package com.example.project

import android.util.Log
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

        var foundIndex = -1

        reservedBooks.forEachIndexed { index, it ->
            if (book.name == it.name){
                foundIndex = index
                return
            }
        }

        if (foundIndex > -1) {
            reservedBooks.removeAt(foundIndex)
        } else {
            Log.d("Error", "Book not found")
        }
    }

    fun confirmReservation(book: Book) {
        isReserved.value = true
        reservedBooks.removeAll(listOf(book))
    }

    fun addBookByAdmin(book: Book) {
        val db = FirebaseFirestore.getInstance()

        db.collection("books")
            .add(book)
            .addOnSuccessListener {
                books.add(Book(book.name, book.author, book.image, book.price, book.genre, book.condition ,book.description))
            }
    }

    fun deleteBookByAdmin(book: Book) {
        val db = FirebaseFirestore.getInstance()

        books.forEachIndexed { index, it ->
            if(book.name == it.name) {

                db.collection("books").document()
                    .get()
                    .addOnSuccessListener { Log.d("tag", "deleted") }
                    .addOnFailureListener { Log.d("tag", "delete does not work") }
            }
        }
    }
}
