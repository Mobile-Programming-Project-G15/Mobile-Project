package com.example.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookViewModel: ViewModel() {
    var books = mutableStateOf( listOf<String>() )

    init {
        Firebase.firestore
            .collection("books")
            .addSnapshotListener { value, error ->
                if(error != null) {
                    // Error info here
                } else if(value != null && !value.isEmpty) {
                    val bookList = mutableListOf<String>()
                    for(d in value.documents) {
                        bookList.add( d.get("name").toString() + ", " + d.get("authur").toString() )

                    }
                    books.value = bookList
                }
            }
    }
}