package com.example.project

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookViewModel: ViewModel() {
    var books = mutableStateListOf<Book>()

    init {
        Firebase.firestore
            .collection("books")
            .get()
            .addOnSuccessListener {
                it.documents.forEach { d ->
                    val bookImage = d.get("image").toString()
                    val bookName = d.get("name").toString()
                    val bookAuthor = d.get("author").toString()
                    books.add(Book(bookName, bookAuthor, bookImage))
                }
            }
    }

  fun addReservation(book: Book) {
      /*var newNotes = notes.value.toMutableList()
      newNotes.add(note)
      notes.value = newNotes*/
    }

}
