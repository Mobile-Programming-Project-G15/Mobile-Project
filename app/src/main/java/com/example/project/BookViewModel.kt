package com.example.project

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookViewModel: ViewModel() {
    var books = mutableStateListOf(Book)
    var bookName = mutableStateOf("")
    var bookAuthor = mutableStateOf("")
    var bookImage = mutableStateOf("")


    init {
        Firebase.firestore
            .collection("books")
            .get()
            .addOnSuccessListener {
                val bookList = mutableListOf<String>()
                it.documents.forEach { d ->
                    bookImage.value = d.get("image").toString()
                    bookName.value = d.get("name").toString()
                    bookAuthor.value = d.get("author").toString()
                }
                books.value = bookList
            }
    }

    /*     init {
        Firebase.firestore
            .collection("books")
            .addSnapshotListener { value, error ->
                if(error != null) {
                    // Error
                } else if(value != null && !value.isEmpty) {
                    val bookList = mutableListOf<String>()
                    for(d in value.documents) {
                        bookList.add(

                            d.get("name").toString() + ", " +
                            d.get("author").toString() )

                    }
                    books.value = bookList
                }
            }
    } */
}
