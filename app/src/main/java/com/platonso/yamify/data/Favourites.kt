package com.platonso.yamify.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

data class Favourites(
    var title: String? = null,
    var content: String? = null
) {
    companion object {
        fun getCollectionReferenceForRecipes(): CollectionReference {
            val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            return FirebaseFirestore.getInstance().collection("favourites")
                .document(currentUser?.uid ?: "").collection("my_favourites")
        }
    }
}
