package com.example.sftraining.repository

import com.example.sftraining.model.Exer
import com.example.sftraining.repository.Repository.Companion.EXER_PATH
import com.example.sftraining.repository.Repository.Companion.PRIVATE_EXERS_PATH
import com.example.sftraining.repository.Repository.Companion.USER_PATH
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExersRepository(
    private val imageRepository: ImageRepository
) : Repository {

    private val db = Firebase.firestore

    fun addExer(
        exer: Exer,
        onSuccess: () -> Unit,
        onFailure: (String?) -> Unit
    ) {
        //add images
        imageRepository.addExerImages(exer)

        if (!exer.isPrivate) {
            val doc = db.collection(EXER_PATH).document()
            doc.set(exer)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message)
                }

        } else {

            val doc = db.collection(USER_PATH).document(exer.userUid)
                .collection(PRIVATE_EXERS_PATH).document()
            doc.set(exer)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message)
                }
        }
    }
}
