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
                    // Error
                } else if(value != null && !value.isEmpty) {
                    val bookList = mutableListOf<String>()
                    for(d in value.documents) {
                        bookList.add(
                            /* d.get("image").toString() + "  " + */
                            d.get("name").toString() + ", " +
                            d.get("author").toString() )

                    }
                    books.value = bookList
                }
            }
    }
}
