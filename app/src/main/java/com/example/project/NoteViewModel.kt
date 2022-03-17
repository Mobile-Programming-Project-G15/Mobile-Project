package com.example.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NoteViewModel: ViewModel() {
    var notes = mutableStateOf(listOf<Note>())

    fun addNote(note: Note) {
        var newNotes = notes.value.toMutableList()
        newNotes.add(note)
        notes.value = newNotes
    }

/*    init{
        Firebase.firestore
            .collection("lists")
            .document(Firebase.auth.currentUser!!.uid)
            .addSnapshotListener { value, error ->
                if(error != null) {
                    //error message
                } else if(value != null && !value.isEmpty) {
                    val listName = mutableListOf<String>()
                    for(d in value) {
                        listName.add(d.get("name").toString())
                    }
                    notes.value = listName
                }

            }
    }*/


    //Firebase.firestore.currentUser!!.uid
    /*
    fun getFromFirestore(){
    Firebase.auth
        .signInWithEmailAndPassword(emailLogin, pwLogin)
        .addOnSuccessListener {
            Firebase.firestore
                .collection("lists")
                .document(it.user!!.uid)
                .get()
                .addOnSuccessListener { doc ->
                    notes = doc.get("name").toString()
                }
        }

}
     */
}